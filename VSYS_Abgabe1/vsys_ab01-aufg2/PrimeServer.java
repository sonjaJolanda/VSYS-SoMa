import java.io.IOException;
import java.util.logging.*;

import rm.requestResponse.*;

public class PrimeServer {
    private final static int PORT = 1234;
    private final static Logger LOGGER = Logger.getLogger(PrimeServer.class.getName());

    private Component communication;
    private int port = PORT;

    private static long startTime = 0;

    PrimeServer(int port) {
        communication = new Component();
        if (port > 0) this.port = port;

//    	setLogLevel(Level.FINER);
    }

    private boolean primeService(long number) {
        System.out.println("Calc start (" + number + "): " + time());
        for (long i = 2; i < Math.sqrt(number) + 1; i++) {
            if (number % i == 0) {
                System.out.println("Calc false (" + number + "): " + time());
                return false;
            }
        }
        System.out.println("Calc true (" + number + "): " + time());
        return true;
    }

    void setLogLevel(Level level) {
        for (Handler h : LOGGER.getLogger("").getHandlers()) h.setLevel(level);
        LOGGER.setLevel(level);
        LOGGER.info("Log level set to " + level);
    }

    void listen() {
        LOGGER.info("Listening on port " + port);

        while (true) {
            Long request = null;

            LOGGER.finer("Receiving ...");
            System.out.println("Receiving ...");
            try {
                request = (Long) communication.receive(port, true, false).getContent();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
            LOGGER.fine(request.toString() + " received.");
            System.out.println(request.toString() + " received.");

            LOGGER.finer("Sending ...");
            System.out.println("Sending ...");
            try {
                communication.send(new Message("localhost", port,
                        new Boolean(primeService(request.longValue()))), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            LOGGER.fine("message sent.");
            System.out.println("message sent.\n-----------------------------------------------------------------");
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
        new PrimeServer(port).listen();
    }


    private String time() {
        if (startTime == 0) startTime = System.currentTimeMillis();
        long milliSecs = System.currentTimeMillis() - startTime;
        return " --> t: " + milliSecs;
    }
}
