import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Component {

    private static final String HOSTNAME = "localhost";

    private ReceiveNotBlocked receiveNotBlocked;
    private ServerSocket servSockReceive;
    private Socket socketReceive;
    private ObjectInputStream inpStrReceive;


    // ToDo: complete
    List<ServerSocket> serverSockets = new ArrayList<>();
    List<Socket> sockets = new ArrayList<>();

    public Component() {

    }

    public Message send(Message msg, int port, boolean complete) throws IOException {
        System.out.println("send port: " + port + " . . .");
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(this.HOSTNAME, port));
        sockets.add(socket);
        System.out.println(" send connected.");

        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(msg);

        outputStream.close();
        socket.close();
        sockets.remove(socket);
        return msg;
    }

    public Message receive(int port, boolean blocking, boolean complete) throws IOException, ClassNotFoundException {
        System.out.println("receive port: " + port + " . . .");
        Message msg = null;

        // create connection and InputStream
        if ((!blocking && (receiveNotBlocked == null)) || (blocking)) {
            servSockReceive = new ServerSocket(port);
            serverSockets.add(servSockReceive);
            socketReceive = servSockReceive.accept();
            System.out.println(" receive accepted.");
            inpStrReceive = new ObjectInputStream(socketReceive.getInputStream());
        }

        if (!blocking && (receiveNotBlocked == null)) {
            receiveNotBlocked = new ReceiveNotBlocked(inpStrReceive);
            receiveNotBlocked.run();
        }

        if (!blocking)
            msg = receiveNotBlocked.getMessage();
        if (blocking)
            msg = (Message) inpStrReceive.readObject();

        //close the connection and InputStream
        if (blocking || (!blocking && (msg != null))) {
            inpStrReceive.close();
            socketReceive.close();
            servSockReceive.close();
            serverSockets.remove(servSockReceive);
        }

        return msg;
    }

    public void cleanup() {

    }

    private String printServerSockets() {
        if (serverSockets.isEmpty()) return " no serverSockets";

        String string = "";
        for (ServerSocket ss : this.serverSockets) {
            string += " ssport:" + ss.getLocalPort();
        }
        return string;
    }

    private String printSockets() {
        if (sockets.isEmpty()) return " no sockets";

        String string = "";
        for (Socket ss : this.sockets) {
            string += " sport:" + ss.getLocalPort();
        }
        return string;
    }

    private class ReceiveNotBlocked implements Runnable {

        private Message msg;
        ObjectInputStream inputStream;

        public ReceiveNotBlocked(ObjectInputStream inputStream) {
            this.inputStream = inputStream;
        }

        public void run() {
            try {
                msg = (Message) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        public Message getMessage() {
            return this.msg;
        }
    }

}
	
