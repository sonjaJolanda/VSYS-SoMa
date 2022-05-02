package B.Working;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientMain {
    private static final int portOne = 1234;
    private static final int portTwo = 5678;
    private static final String hostname = "localhost";
    private static MySocketClient clientOne;
    private static MySocketClient clientTwo;
    private static BufferedReader reader;

    public static void main(String args[]) {
        try {
            clientOne = new MySocketClient(hostname, portOne, "The1AndOnlyClient");
            clientTwo = new MySocketClient(hostname, portTwo, "The2AndNotSoOnlyClient");

            Thread sendRequestsOne = new Thread(() -> clientOne.sendAndReceiveUntilPressedKey());
            Thread sendRequestsTwo = new Thread(() -> clientOne.sendAndReceiveUntilPressedKey());
            sendRequestsOne.start();
            sendRequestsTwo.start();

            System.out.println("There are multiple clients sending requests to the server: \n" + //
                    "- " + clientOne.getClientIdentifier() + "(1)\n" + //
                    "- " + clientTwo.getClientIdentifier() + "(2)\n" + //
                    "To stop them, just type in the clients number");

            stopRequestsThroughInput();
            sendRequestsOne.join();
            sendRequestsTwo.join();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void stopRequestsThroughInput() throws IOException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        if (reader.readLine().equals("1")) clientOne.stopRequests();
        else if (reader.readLine().equals("2")) clientTwo.stopRequests();

        if (!clientOne.isStopped() || !clientTwo.isStopped())
            stopRequestsThroughInput();
    }

}
