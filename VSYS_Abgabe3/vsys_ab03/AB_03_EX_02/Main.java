package AB_03_EX_02;

import java.io.IOException;

public class Main {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 1234;
    private static final String REQUEST_TYPE = "THREADED"; //[1] SYNCHRONIZED [2] POLLING [3] THREADED
    private static final long COUNT = 5;

    public static void main(String[] args) {
        for (int i = 0; i < 4; i++) {
            int answerPort = 1235 + i;
            long initialValue = (long) 1e16 * (i + 1);
            System.out.println("-> initalValue: " + initialValue + ", answerPort: " + answerPort);
            Thread thread = new Thread(() -> {
                try {
                    new PrimeClientCorrect(HOSTNAME, PORT, answerPort, REQUEST_TYPE, initialValue, COUNT).run();
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }
    }
}
