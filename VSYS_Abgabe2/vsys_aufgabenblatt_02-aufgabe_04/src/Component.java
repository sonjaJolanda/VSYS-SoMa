import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Component {

    // ToDo: complete, blocking

    public Component() {

    }

    public Message send(Message msg, int port, boolean complete) throws IOException {

        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept();
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(msg);
        return msg;
    }

    public Message receive(int port, boolean blocking, boolean complete) throws IOException, ClassNotFoundException {

        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept();
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        Message msg = (Message) inputStream.readObject();

        return msg;
    }

    public void cleanup() {

    }
}
	
