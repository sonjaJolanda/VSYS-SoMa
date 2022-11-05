package AB_02_EX_01_B_2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MySocketServer {
    private ServerSocket socket1;
    private ServerSocket socket2; // B2

    public MySocketServer(int port1, int port2) throws IOException {
        socket1 = new ServerSocket(port1);
        socket2 = new ServerSocket(port2); // B2
    }

    public void listen() {
        while (true) {
            try {
                Thread listenToSocket2 = new Thread(() -> {
                    try {
                        Socket incomingConnection2 = socket2.accept();
                        MySocketServerConnection connection2 = new MySocketServerConnection(incomingConnection2);
                        connection2.start();
                    } catch (IOException e) {  e.printStackTrace();   }
                });

                listenToSocket2.start();
                Socket incomingConnection1 = socket1.accept();
                MySocketServerConnection connection1 = new MySocketServerConnection(incomingConnection1);
                connection1.start();
                listenToSocket2.join();
            } catch (IOException | InterruptedException e) { e.printStackTrace();  }
        }
    }
}
