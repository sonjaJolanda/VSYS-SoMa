package B;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MySocketServerConnection extends Thread {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public MySocketServerConnection(Socket socket)
            throws IOException {
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
                //inputClientStr = (String) objectInputStream.readObject();
                System.out.println("Server: received '" + inputClientStr + "'");
                objectOutputStream.writeObject("server received " + inputClientStr + "\n");
            }
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
