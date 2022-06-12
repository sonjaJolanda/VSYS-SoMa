import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class PrimeServer_2_3_E_final {
    private static int PORT = 1234;
    private final static Logger LOGGER = Logger.getLogger(PrimeServer_2_3_E_final.class.getName());

    private Component communication;
    private int port = PORT;

    private ExecutorService executorService = null;

    private static long startTime = 0;

    PrimeServer_2_3_E_final(int port) {
        communication = new Component();
        if (port > 0) this.port = port;
    }

    private boolean primeService(RequestPair requestPair) {
        requestPair.setpStart();
        for (long i = 2; i < Math.sqrt(requestPair.requestValue) + 1; i++) {
            if (requestPair.requestValue % i == 0) {
                requestPair.setpEnd();
                return false;
            }
        }
        requestPair.setpEnd();
        return true;
    }

    void listen() {
        String inputExecType = null;
        int inputPort = PORT;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.print("Server port > ");
            inputPort = Integer.valueOf(reader.readLine());
            if (inputPort > 65535)
                throw new IllegalArgumentException();

            System.out.print("Server executor type: Choose between '(s)INGLE', '(c)ONSTANT' and '(d)YNAMIC' > ");
            inputExecType = reader.readLine();
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("Wrong input.");
            listen();
        }

        this.PORT = inputPort;
        this.port = inputPort;
        System.out.println("port: " + PORT);

        switch (inputExecType) {
            case "c": {
                System.out.print("#threads > ");
                try {
                    int nrThreads = Integer.parseInt(reader.readLine());
                    System.out.println("> Threadcount (fixed): " + nrThreads);
                    executorService = Executors.newFixedThreadPool(nrThreads);
                    handleRequestsConcurrently();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Wrong input.");
                    listen();
                }
                break;
            }
            case "s": {
                System.out.println("> Threadcount (fixed): " + 1);
                executorService = Executors.newSingleThreadExecutor();
                handleRequestsSingleThreaded();
                break;
            }
            case "d": {
                executorService = new ThreadPoolExecutor(1, Integer.MAX_VALUE, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1));
                handleRequestsConcurrently();
                break;
            }
            default: {
                System.out.println("Wrong input, try again.");
                listen();
            }
        }
    }

    private void countThreads() {
        Thread threadCounter = new Thread(() -> {
            if (!(this.executorService instanceof ThreadPoolExecutor executor))
                return;

            int threadCountBefore = 0;
            while (true) {
                int threadCountNow = executor.getActiveCount();
                boolean isExecService = this.executorService != null;
                boolean isDifferent = isExecService ? threadCountNow != threadCountBefore : threadCountBefore != 0;
                if (!isExecService && isDifferent) {
                    threadCountBefore = 0;
                    System.out.println("> threads: " + 0);
                } else if (isExecService && isDifferent) {
                    threadCountBefore = threadCountNow;
                    System.out.println("> ACTIVE threads: " + threadCountNow);
                }
            }
        });

        threadCounter.start();
    }

    private void handleRequestsConcurrently() {
        LinkedList<RequestPair> requestPairs = new LinkedList<>();
        Thread requestReceiver = new Thread(() -> {
            while (true) {
                requestPairs.add(receive());
            }
        });
        countThreads();
        requestReceiver.start();

        while (true) {
            try {
                if (requestPairs.isEmpty()) {
                    Thread.sleep(0);
                } else {
                    System.out.println("submitting ");
                    executorService.submit(new Calculate(requestPairs.pollFirst()));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleRequestsSingleThreaded() {
        while (true) {
            executorService.submit(new Calculate(receive()));
        }
    }

    public RequestPair receive() {
        try {
            Message message = communication.receive(port, true, false);
            final RequestPair pair = (RequestPair) message.getContent();
            pair.setwStart();
            System.out.println(">> received " + pair.requestValue + " (port: " + pair.sendPort + ")");
            return pair;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
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
        new PrimeServer_2_3_E_final(port).listen();
    }

    private class Calculate implements Runnable {
        RequestPair requestPair;

        Calculate(RequestPair requestPair) {
            this.requestPair = requestPair;
        }

        @Override
        public void run() {
            try {
                System.out.println("run " + requestPair.sendPort);
                //Thread.sleep(5000); //ToDo remove later
                //System.out.println(">>> in work " + requestPair.requestValue + " (p:" + requestPair.sendPort + ")" + printQueue());
                requestPair.setwEnd();
                requestPair.answer = primeService(requestPair);
                communication.send(new Message("localhost", requestPair.sendPort, requestPair), requestPair.sendPort, true);
                System.out.println(">>> sent " + requestPair.requestValue + " (p:" + requestPair.sendPort + ")");
            } catch (IOException e) {
                System.out.println(requestPair.requestValue + " (p:" + requestPair.sendPort + ")");
                e.printStackTrace();
            }
        }

        private String time() {
            if (startTime == 0) startTime = System.currentTimeMillis();
            long milliSecs = System.currentTimeMillis() - startTime;
            return " --> t: " + milliSecs;
        }
    }
}