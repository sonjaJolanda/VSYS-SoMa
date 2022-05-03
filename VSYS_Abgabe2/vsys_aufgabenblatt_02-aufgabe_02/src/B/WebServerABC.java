package B;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * http://localhost:8080/website.html
 */
public class WebServerABC implements Runnable {
    static final File WEB_ROOT = new File(".");
    static final String DEFAULT_FILE = "website.html";
    static final String FILE_NOT_FOUND = "404.html";
    static final int PORT = 8080;
    private Socket socket;

    public WebServerABC(Socket socket) {
        this.socket = socket;
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverConnect = new ServerSocket(PORT);
            System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");
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
        String fileRequested;

        try {
            // Setting up input and output streams
            inFromClientViaSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outForClientViaSocket = new PrintWriter(socket.getOutputStream());
            requestedBinaryOutForClient = new BufferedOutputStream(socket.getOutputStream());

            // Getting input from client via socket and process given data
            inputClientViaSocket = inFromClientViaSocket.readLine(); // ToDo manchmal geht er da nicht rein???
            stringTokenizer = new StringTokenizer(inputClientViaSocket);
            methodRequested = stringTokenizer.nextToken().toUpperCase(); // we get the HTTP method of the client
            fileRequested = stringTokenizer.nextToken().toLowerCase(); // we get file requested

            File file = new File(WEB_ROOT, fileRequested.endsWith("/") ? (fileRequested + DEFAULT_FILE) : fileRequested);
            int fileLength = (int) file.length();
            System.out.println(file);

            if (methodRequested.equals("GET")) {
                byte[] fileData = readFileData(file, fileLength);

                outForClientViaSocket.println("HTTP/1.1 200 OK\r\n"); // send header
                outForClientViaSocket.flush();
                requestedBinaryOutForClient.write(fileData, 0, fileLength); // send content
                requestedBinaryOutForClient.flush();

                inFromClientViaSocket.close();
                outForClientViaSocket.close();
                requestedBinaryOutForClient.close();
            }
        } catch (FileNotFoundException fnfe) {
            fileNotFound(outForClientViaSocket, requestedBinaryOutForClient);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            }
        }
    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
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

    private void fileNotFound(PrintWriter outForClientViaSocket, BufferedOutputStream requestedBinaryOutForClient) {
        try {
            File file = new File(WEB_ROOT, FILE_NOT_FOUND);
            int fileLength = (int) file.length();
            byte[] fileData = readFileData(file, fileLength);

            outForClientViaSocket.println("HTTP/1.1 200 OK\r\n"); // send header
            outForClientViaSocket.flush();
            requestedBinaryOutForClient.write(fileData, 0, fileLength);
            requestedBinaryOutForClient.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}