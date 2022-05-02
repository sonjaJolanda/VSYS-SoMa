package B_2.B_1;

import java.io.IOException;

public class ServerMain {
    private static final int port1 = 1234;
    private static final int port2 = 5678;
    private static MySocketServer server;

    public static void main(String args[]) {
        try {
            server = new MySocketServer(port1, port2);
            server.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
