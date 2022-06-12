package AB_02_EX_03;

import rm.requestResponse.*;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class PrimeServer_2_3_B {
    private final static int PORT = 1234;
    private final static Logger LOGGER = Logger.getLogger(PrimeServer_2_3_B.class.getName());

    private Component communication;
    private int port = PORT;

    PrimeServer_2_3_B(int port) {
        communication = new Component();
        if (port > 0) this.port = port;
    }

    private boolean primeService(long number) {
        for (long i = 2; i < Math.sqrt(number) + 1; i++) {
            if (number % i == 0)
                return false;
        }
        return true;
    }

    void listen() {
        Queue<RequestPair> requestPairs = new LinkedList<>();

        Thread requestReceiver = new Thread(() -> {
            while (true) {
                try {
                    Message message = communication.receive(port, true, false);
                    Long newIncomingRequest = (Long) message.getContent();
                    Integer sendPort = message.getPort();
                    requestPairs.add(new RequestPair(sendPort, newIncomingRequest));
                    System.out.println(">> received " + newIncomingRequest + " (p: " + sendPort + ")" + printQueue(requestPairs));
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        requestReceiver.start();

        while (true) {
            try {
                RequestPair requestPairInWork = requestPairs.poll();
                if (requestPairInWork == null) {
                    //System.out.println(">>> waiting to calculate " + printQueue(requestPairs));
                    Thread.sleep(1000);
                    continue;
                }
                Long requestInWork = requestPairInWork.requestValue;
                Integer sendPort = requestPairInWork.sendPort;
                // System.out.println(">>> in work " + requestInWork + " (p:" + sendPort + ")" + printQueue(requestPairs));
                communication.send(new Message("localhost", this.port, primeService(requestInWork)), sendPort, true);
                System.out.println(">>> sent " + requestInWork + " (p:" + sendPort + ")" + printQueue(requestPairs));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        int port = 0;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-port":
                    try {
                        port = Integer.parseInt(args[++i]);
                    } catch (NumberFormatException e) {
                        LOGGER.severe("port must be an integer, not " + args[i]);
                        System.exit(1);
                    }
                    break;
                default:
                    LOGGER.warning("Wrong parameter passed ... '" + args[i] + "'");
            }
        }
        new PrimeServer_2_3_B(port).listen();
    }

    private String printQueue(Queue<RequestPair> queue) {
        if (queue.isEmpty()) return " . . . []";

        String returnValue = " . . .  [";
        for (RequestPair pair : queue) {
            returnValue += pair.requestValue + " (p:" + pair.sendPort + "), ";
        }
        return returnValue.substring(0, returnValue.length() - 2) + "]";
    }
}