package B.Working;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MySocketServer {
    private ServerSocket socket;
    private int port;
    private String name;

    public MySocketServer(int port, String name) throws IOException {
        this.port = port;
        socket = new ServerSocket(port);
        this.name = name;
    }

    public void listen() {
        while (true) {
            try {
                System.out.println(name + ": listening on port " + port);
                Socket incomingConnection = socket.accept();
                MySocketServerConnection connection = new MySocketServerConnection(incomingConnection);
                connection.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
