package B_2.B_1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MySocketServerConnection extends Thread {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public MySocketServerConnection(Socket socket) throws IOException {
        this.socket = socket;
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        System.out.println("Server: incoming connection accepted. (Port: " + socket.getPort() + ")");
    }

    public void run() {
        System.out.println("Server: waiting for message ... (Port: " + socket.getPort() + ")");
        try {
            String inputString;
            try {
                while ((inputString = (String) objectInputStream.readObject()) != null) {
                    System.out.println("Server: received '" + inputString + "' (Port: " + socket.getPort() + ")");
                    objectOutputStream.writeObject("server received " + inputString + " (Port: " + socket.getPort() + ")");
                }
            } catch (IOException ioe) {
                // objectInputStream.close();
                // objectOutputStream.close();
                System.out.print("Close socket (Port: " + socket.getPort() + ") ...");
                socket.close();
                System.out.println("done.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
