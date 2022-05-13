import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrimeServer_2_3_D {
    private final static int PORT = 1234;
    private final static Logger LOGGER = Logger.getLogger(PrimeServer_2_3_D.class.getName());

    private Component communication;
    private int port = PORT;

    private ThreadPoolExecutor executorService = null;
    private LinkedList<RequestPair> requestPairs = new LinkedList<>();

    private static long startTime = 0;

    PrimeServer_2_3_D(int port) {
        communication = new Component();
        if (port > 0) this.port = port;
        setLogLevel(Level.FINER);
    }

    private boolean primeService(long number) {
        for (long i = 2; i < Math.sqrt(number) + 1; i++) {
            if (number % i == 0)
                return false;
        }
        return true;
    }

    void setLogLevel(Level level) {
        for (Handler h : LOGGER.getLogger("").getHandlers()) h.setLevel(level);
        LOGGER.setLevel(level);
        LOGGER.info("Log level set to " + level);
    }

    void listen() {
        LOGGER.info("Listening on port " + port);

        Thread receiver = new Thread(() -> {
            while (true) {
                try {
                    Message message = communication.receive(port, true, false);
                    Long newIncomingRequest = (Long) message.getContent();
                    Integer sendPort = message.getPort();
                    final RequestPair pair = new RequestPair(sendPort, newIncomingRequest);
                    requestPairs.add(pair);
                    LOGGER.info(">> received " + newIncomingRequest + " (p: " + sendPort + ")" + printQueue());
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread threadCounter = new Thread(() -> {
            while (true) {
                LOGGER.info("> Threadcount: ? ...");
                if (this.executorService == null) {
                    LOGGER.info("> Threadcount: " + 0);
                    System.out.println("> Threadcount: " + 0);
                } else {
                    LOGGER.info("> Threadcount: " + this.executorService.getActiveCount());
                    System.out.println("> Threadcount: " + this.executorService.getActiveCount());
                }
            }
        });

        executorService = new ThreadPoolExecutor(1, Integer.MAX_VALUE, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1));
        executorService.submit(receiver);
        executorService.submit(threadCounter);

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

        System.out.println("\n\nnew PrimeServer");
        new PrimeServer_2_3_D(port).listen();
    }

    private String time() {
        if (startTime == 0) startTime = System.currentTimeMillis();
        long milliSecs = System.currentTimeMillis() - startTime;
        return " --> t: " + milliSecs;
    }

    private String printQueue() {
        if (requestPairs.isEmpty()) return " . . . []";

        String returnValue = " . . . [";
        for (RequestPair pair : requestPairs) {
            returnValue += pair.requestValue + " (p:" + pair.sendPort + "), ";
        }
        return returnValue.substring(0, returnValue.length() - 2) + "]";
    }

    private class Calculate implements Runnable {
        RequestPair requestPair;

        Calculate(RequestPair requestPair) {
            this.requestPair = requestPair;
        }

        @Override
        public void run() {
            try {
                LOGGER.info(">>> in work " + requestPair.requestValue + " (p:" + requestPair.sendPort + ")" + printQueue());
                communication.send(new Message("localhost", requestPair.sendPort, primeService(requestPair.requestValue)), requestPair.sendPort, true);
                LOGGER.info(">>> sent " + requestPair.requestValue + " (p:" + requestPair.sendPort + ")" + printQueue());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}