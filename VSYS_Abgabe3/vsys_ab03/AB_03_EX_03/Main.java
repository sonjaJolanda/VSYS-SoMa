package AB_03_EX_03;

import java.io.IOException;

public class Main {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 1234;
    private static final String REQUEST_TYPE = "ASYNC";
    private static final long COUNT = 20;

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            int sendPort = 1235 + i;
            long initialValue = (long) 1e16 * (i + 1);
            Thread thread = new Thread(() -> {
                try {
                    new PrimeClientCorrect(HOSTNAME, PORT, sendPort, REQUEST_TYPE, initialValue, COUNT).run();
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }
    }
}
