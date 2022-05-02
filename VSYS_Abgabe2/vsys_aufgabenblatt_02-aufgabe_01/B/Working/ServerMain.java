package B.Working;

import java.io.IOException;

public class ServerMain {
    private static final int portOne = 1234;
    private static final int portTwo = 5678;
    private static MySocketServer serverOne;
    private static MySocketServer serverTwo;

    public static void main(String args[]) {
        try {
            Thread serveOne = new Thread(() -> {
                try {
                    serverOne = new MySocketServer(portOne, "socketOne");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                serverOne.listen();
            });

            Thread serveTwo = new Thread(() -> {
                try {
                    serverTwo = new MySocketServer(portTwo, "socketTwo");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                serverTwo.listen();
            });

            serveOne.start();
            serveTwo.start();

            serveOne.join();
            serveTwo.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
