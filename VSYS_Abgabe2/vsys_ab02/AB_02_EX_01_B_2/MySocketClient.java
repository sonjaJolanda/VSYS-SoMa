package AB_02_EX_01_B_2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MySocketClient {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private String clientIdentifier = null; // B2
    private int counter = 0; // B2

    MySocketClient(String hostname, int port, String clientIdentifier) throws IOException {
        socket = new Socket();
        this.clientIdentifier = clientIdentifier; // B2
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
