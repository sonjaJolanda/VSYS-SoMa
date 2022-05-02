package B;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
                while (!isInput) {
                    try {
                        System.out.println(client.sendAndReceive(counter));
                        System.out.println("---------------> Sent request (" + counter + ")");
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        System.out.println("!!!!!!!!!!! ERROR: " + e.getMessage());
                        e.printStackTrace();
                    }
                    counter++;
                }
            });

            sendRequestsHandler.start();
            reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Press enter to stop the communication");
            if (reader.readLine() != null) isInput = true;
            sendRequestsHandler.join();
            client.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}