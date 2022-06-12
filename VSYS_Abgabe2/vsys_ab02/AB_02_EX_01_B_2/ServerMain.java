package AB_02_EX_01_B_2;

import java.io.IOException;

public class ServerMain {
    private static final int port1 = 1234;
    private static final int port2 = 5678; // B2
    private static MySocketServer server;

    public static void main(String args[]) {
        try {
            server = new MySocketServer(port1, port2);  // B2 port2 added
            server.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
