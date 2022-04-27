package B;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ClientMain {
    private static final int port = 1234;
    private static final String hostname = "localhost";
    private static MySocketClient client;
    private static BufferedReader reader;

    private static boolean isInput = false;
    private static int counter = 0;

    public static void main(String args[]) {
        try {
            client = new MySocketClient(hostname, port);

            Thread sendRequestsHandler = new Thread(() -> {
                while (isInput) {
                    try {
                        sendRequest(counter);
                        Thread.sleep(500);
                    } catch (Exception e) {
                        System.out.println("!!!!!!!!!!! ERROR: " + e.getMessage());
                    }
                    counter++;
                }
            });

            sendRequestsHandler.start();
            reader = new BufferedReader(new InputStreamReader(System.in));
            isInput = true; // reader.is...; //ToDo schauen ob ein Key gepressed ist
            sendRequestsHandler.join();
            client.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendRequest(int requestId) throws Exception {
        System.out.println(client.sendAndReceive(requestId));
    }
}
