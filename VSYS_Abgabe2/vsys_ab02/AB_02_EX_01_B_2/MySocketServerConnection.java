package AB_02_EX_01_B_2;

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
    }

    public void run() {
        System.out.println("Server: waiting for message ...");
        try {
            String inputString;
            try {
                while ((inputString = (String) objectInputStream.readObject()) != null) {
                    objectOutputStream.writeObject("server received " + inputString + " (Port: " + socket.getPort() + ")");
                }
            } catch (IOException ioe) {
                socket.close();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
