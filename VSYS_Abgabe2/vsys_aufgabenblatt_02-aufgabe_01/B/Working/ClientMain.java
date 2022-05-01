package B.Working;

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
            client = new MySocketClient(hostname, port, "The1AndOnlyClient");

            Thread sendRequestsHandler = new Thread(() -> {
                while (!isInput) {
                    try {
                        client.sendAndReceive(counter);
                        //System.out.println(client.sendAndReceive(counter));
                        long randomTimeToWait = (long) (Math.random() * 2000);
                        //System.out.println("--> t: " + randomTimeToWait);
                        Thread.sleep(randomTimeToWait);
                    } catch (Exception e) {
                        System.out.println("!!!!!!!!!!! ERROR: " + e.getMessage());
                        e.printStackTrace();
                    }
                    counter++;
                }
            });

            sendRequestsHandler.start();
            reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Press any key to stop the communication");
            if (reader.readLine() != null) isInput = true;
            sendRequestsHandler.join();

            System.out.print("Disconnect ... ");
            client.disconnect();
            System.out.println("done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
