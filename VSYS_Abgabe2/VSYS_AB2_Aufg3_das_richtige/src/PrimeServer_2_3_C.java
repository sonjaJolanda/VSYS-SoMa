import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PrimeServer_2_3_C {
    private final static int PORT = 1234;
    private final static Logger LOGGER = Logger.getLogger(PrimeServer_2_3_C.class.getName());

    private Component communication;
    private int port = PORT;

    private LinkedList<RequestPair> requestPairs = new LinkedList<>();

    PrimeServer_2_3_C(int port) {
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
        Thread requestReceiver = new Thread(() -> {
            while (true) {
                try {
                    Message message = communication.receive(port, true, false);
                    Long newIncomingRequest = (Long) message.getContent();
                    Integer sendPort = message.getPort();
                    final RequestPair pair = new RequestPair(sendPort, newIncomingRequest);
                    requestPairs.add(pair);
                    System.out.println(">> received " + newIncomingRequest + " (p: " + sendPort + ")" + printQueue());
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        requestReceiver.start();

        // ToDo THIS IS NEW
        ExecutorService executorService = new ThreadPoolExecutor(1, Integer.MAX_VALUE, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1));

        // ToDo THIS IS NEW
        while (true) {
            try {
                if (requestPairs.isEmpty())
                    Thread.sleep(1000);
                else
                    executorService.submit(new Calculate(requestPairs.pollFirst()));

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
        new PrimeServer_2_3_C(port).listen();
    }
    
    private String printQueue() {
        if (requestPairs.isEmpty()) return " . . . []";

        String returnValue = " . . . [";
        for (RequestPair pair : requestPairs) {
            returnValue += pair.requestValue + " (p:" + pair.sendPort + "), ";
        }
        return returnValue.substring(0, returnValue.length() - 2) + "]";
    }

    // ToDo THIS IS NEW
    private class Calculate implements Runnable {
        RequestPair requestPair;

        Calculate(RequestPair requestPair) {
            this.requestPair = requestPair;
        }

        @Override
        public void run() {
            try {
                // System.out.println(">>> in work " + requestPair.requestValue + " (p:" + requestPair.sendPort + ")" + printQueue());
                communication.send(new Message("localhost", port, primeService(requestPair.requestValue)), requestPair.sendPort, true);
                System.out.println(">>> sent " + requestPair.requestValue + " (p:" + requestPair.sendPort + ")" + printQueue());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}