package B_1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MySocketServer {
    private ServerSocket socket;
    private int port;

    public MySocketServer(int port) throws IOException {
        this.port = port;
        socket = new ServerSocket(port);
    }

    public void listen() {
        while (true) {
            try {
                System.out.println("Server: listening on port " + port);
                Socket incomingConnection = socket.accept();
                MySocketServerConnection connection = new MySocketServerConnection(incomingConnection);
                connection.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
