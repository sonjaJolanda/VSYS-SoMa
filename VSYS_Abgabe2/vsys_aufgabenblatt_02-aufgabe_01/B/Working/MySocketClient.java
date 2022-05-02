package B.Working;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MySocketClient {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private String clientIdentifier = null;
    private boolean isStopped = false;
    private int counter = 0;

    MySocketClient(String hostname, int port, String identifier) throws IOException {
        socket = new Socket();
        clientIdentifier = identifier;

        System.out.print("Client: connecting '" + hostname + "' on " + port + " ... ");
        socket.connect(new InetSocketAddress(hostname, port));
        System.out.println("done.");

        objectInputStream = new ObjectInputStream(socket.getInputStream());
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public void sendAndReceiveUntilPressedKey() {

        Thread sendRequests = new Thread(() -> {
            while (!isStopped) {
                try {
                    // sendAndReceive(counter);
                    System.out.println(sendAndReceive(counter));
                    long randomTimeToWait = (long) (Math.random() * 2000);
                    System.out.println("--> t: " + randomTimeToWait);
                    Thread.sleep(randomTimeToWait);
                } catch (Exception e) {
                    System.out.println("!!!!!!!!!!! ERROR: " + e.getMessage());
                    e.printStackTrace();
                }
                counter++;
            }
        });

        sendRequests.start();
        try {
            sendRequests.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.print("Disconnect" + clientIdentifier + " ... ");
        disconnect();
        System.out.println("done.");
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

    public String getClientIdentifier() {
        return clientIdentifier;
    }

    public void stopRequests() {
        this.isStopped = true;
    }

    public boolean isStopped() {
        return isStopped;
    }
}
