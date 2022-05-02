package B.Working;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MySocketServerConnection extends Thread {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private String name;

    public MySocketServerConnection(Socket socket) throws IOException {
        this.socket = socket;
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        System.out.println("Server: incoming connection accepted.");
    }

    public void run() {
        System.out.println("Server: waiting for message ...");

        try {
            String inputClientStr;
            while ((inputClientStr = (String) objectInputStream.readObject()) != null) {
                System.out.println("Server: received --'" + inputClientStr + "'");
                objectOutputStream.writeObject("RECEIVED-" + inputClientStr.split("-")[1]);
                //System.out.println("Server: sent ------'" + "RECEIVED-" + inputClientStr.split("-")[1]);
            }
        } catch (EOFException eof) {
            try {
                System.out.print("End of inputStream is reached, closing connection ...");
                socket.close();
                System.out.println("done.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
