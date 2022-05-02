package B_2.B_1;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ClientMain2 {
    private static final int port = 5678;
    private static final String hostname = "localhost";
    private static MySocketClient client;
    private static BufferedReader reader;
    //new
    private static boolean isKeyPressed = false;

    public static void main(String args[]) {
        try {
            client = new MySocketClient(hostname, port, "MyClient2");

            //neuer Thread der dann die Anfragen an den Server stellt im Hintergrund
            Thread sendRequestsHandler = new Thread(() -> {
                while (!isKeyPressed) {
                    try {
                        System.out.println(client.sendAndReceive());
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        System.out.println("!!!!!!!!!!! ERROR: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
            sendRequestsHandler.start();

            //prüft ab ob eine Taste gedrückt wurde
            reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Press any key and submit to stop the communication");
            if (reader.readLine() != null) isKeyPressed = true;
            sendRequestsHandler.join();
            System.out.print("Disconnect client ...");
            client.disconnect();
            System.out.println("done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
