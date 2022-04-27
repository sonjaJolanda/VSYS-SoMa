package B;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MySocketClient {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private static final String clientIdentifier = "MyFirstClient";

    MySocketClient(String hostname, int port)
            throws IOException {
        socket = new Socket();
        System.out.print("Client: connecting '" + hostname +
                "' on " + port + " ... ");
        socket.connect(new InetSocketAddress(hostname, port));
        System.out.println("done.");
        objectInputStream =
                new ObjectInputStream(socket.getInputStream());
        objectOutputStream =
                new ObjectOutputStream(socket.getOutputStream());
    }

    public String sendAndReceive(int requestId)
            throws Exception {
        objectOutputStream.writeObject(clientIdentifier + " " + requestId);
        System.out.println("Client: send " + clientIdentifier + " " + requestId);
        return "Client: received '"
                + (String) objectInputStream.readObject() + "'";
    }

    public void disconnect()
            throws IOException {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
