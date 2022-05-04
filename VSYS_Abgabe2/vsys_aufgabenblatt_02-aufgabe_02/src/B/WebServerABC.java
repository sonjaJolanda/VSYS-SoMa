package B;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * http://localhost:8080/website.html
 */
public class WebServerABC implements Runnable {
    static final File WEB_ROOT = new File(".");
    static final String DEFAULT_FILE = "website.html";
    static final String FILE_NOT_FOUND = "404.html";
    static final int PORT = 8080;
    private Socket socket;

    //Logging
    private static Logger logger = Logger.getLogger("MyLog");
    private static FileHandler fileHandler;

    public WebServerABC(Socket socket) {
        this.socket = socket;
    }

    public static void main(String[] args) {
        //------------- LOGGING -------------------
        try {
            fileHandler = new FileHandler("./MyLogFile.log");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (IOException ioe) {
            System.err.println("Error creating logger : " + ioe.getMessage());
        }
        //------------ CONNECTION USW. ----------------
        try {
            ServerSocket serverConnect = new ServerSocket(PORT);
            logger.info("Server started.\nListening for connections on port : " + PORT + " ...\n");
            while (true) {
                WebServerABC myServer = new WebServerABC(serverConnect.accept());
                Thread threadClientConnection = new Thread(myServer);
                threadClientConnection.start();
            }
        } catch (IOException e) {
            System.err.println("Server Connection error : " + e.getMessage());
        }
    }

    public void run() {
        BufferedReader inFromClientViaSocket;
        PrintWriter outForClientViaSocket = null;
        BufferedOutputStream requestedBinaryOutForClient = null;

        String inputClientViaSocket;
        StringTokenizer stringTokenizer;
        String methodRequested;
        String fileRequested = null;

        try {

            logger.info("Setting up input and output streams\n");
            inFromClientViaSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outForClientViaSocket = new PrintWriter(socket.getOutputStream());
            requestedBinaryOutForClient = new BufferedOutputStream(socket.getOutputStream());

            logger.info("Getting input from client via socket and process given data\n");
            inputClientViaSocket = inFromClientViaSocket.readLine();

            if (inputClientViaSocket == null) {
                socket.close();
                logger.info("the end of the stream has been reached without reading any characters -> connection closed\n");
                return;
            }

            stringTokenizer = new StringTokenizer(inputClientViaSocket);
            methodRequested = stringTokenizer.nextToken().toUpperCase(); // we get the HTTP method of the client
            fileRequested = stringTokenizer.nextToken().toLowerCase(); // we get file requested

            logger.info("search for file requested: " + fileRequested + "\n");
            File file = new File(WEB_ROOT, fileRequested.endsWith("/") ? (fileRequested + DEFAULT_FILE) : fileRequested);
            int fileLength = (int) file.length();
            System.out.println(file);

            if (methodRequested.equals("GET")) {
                byte[] fileData = readFileData(file, fileLength);

                logger.info("send header back \n");
                outForClientViaSocket.println("HTTP/1.1 200 OK\r\n"); // send header
                outForClientViaSocket.flush();
                logger.info("send html back \n");
                requestedBinaryOutForClient.write(fileData, 0, fileLength); // send content
                requestedBinaryOutForClient.flush();

                logger.info("close all streams \n");
                inFromClientViaSocket.close();
                outForClientViaSocket.close();
                requestedBinaryOutForClient.close();
            }
        } catch (FileNotFoundException fnfe) {
            fileNotFound(outForClientViaSocket, requestedBinaryOutForClient, fileRequested);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                logger.info("connection closed\n");
                socket.close();
            } catch (Exception e) {
                logger.warning("Error closing stream : " + e.getMessage() + "\n");
                System.err.println("Error closing stream : " + e.getMessage());
            }
        }
    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
        logger.info("read File Data " + file.getAbsolutePath() + " \n");
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }
        return fileData;
    }

    private void fileNotFound(PrintWriter outForClientViaSocket, BufferedOutputStream requestedBinaryOutForClient, String fileName) {
        try {
            logger.warning("the file requested (" + fileName + ") wasn't found. Return html with info 'file not found'!\n");
            File file = new File(WEB_ROOT, FILE_NOT_FOUND);
            int fileLength = (int) file.length();
            byte[] fileData = readFileData(file, fileLength);

            outForClientViaSocket.println("HTTP/1.1 200 OK\r\n"); // send header
            outForClientViaSocket.flush();
            requestedBinaryOutForClient.write(fileData, 0, fileLength);
            requestedBinaryOutForClient.flush();
        } catch (IOException ioe) {
            logger.warning("the file with info 'file not found' wasn't found!!!!!!!\n");
            ioe.printStackTrace();
        }
    }
}