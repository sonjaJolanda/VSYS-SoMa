package B.Working;

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

    MySocketClient(String hostname, int port, String identifier) throws IOException {
        socket = new Socket();
        clientIdentifier = identifier;

        System.out.print("Client: connecting '" + hostname + "' on " + port + " ... ");
        socket.connect(new InetSocketAddress(hostname, port));
        System.out.println("done.");

        objectInputStream = new ObjectInputStream(socket.getInputStream());
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public String sendAndReceive(int requestId) throws Exception {
        objectOutputStream.writeObject(clientIdentifier + "-" + requestId);
        System.out.println(clientIdentifier + ": sent ------ id: " + requestId);
        return clientIdentifier + ": received --'" + objectInputStream.readObject() + "'";
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
