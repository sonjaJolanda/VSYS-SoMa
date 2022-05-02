package B_1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MySocketClient {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private String clientIdentifier = null;
    private int counter = 0;

    MySocketClient(String hostname, int port, String clientIdentifier) throws IOException {
        socket = new Socket();
        this.clientIdentifier = clientIdentifier;
        System.out.print("Client: connecting '" + hostname + "' on " + port + " ... ");
        socket.connect(new InetSocketAddress(hostname, port));
        System.out.println("done.");
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public String sendAndReceive() throws Exception {
        objectOutputStream.writeObject(clientIdentifier + " with request " + counter++);
        System.out.println("Client: send " + clientIdentifier + " with request " + counter);
        return "Client: received '" + (String) objectInputStream.readObject() + "'";
    }

    public void disconnect() throws IOException {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
