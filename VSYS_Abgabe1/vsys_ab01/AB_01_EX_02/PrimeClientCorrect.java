package AB_01_EX_02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import rm.requestResponse.*;

public class PrimeClientCorrect {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 1234;
    private static final String INITIAL_REQUEST_MODE = "SYNCHRONIZED";
    private static final long INITIAL_VALUE = (long) 1e17;
    private static final long COUNT = 20;
    private static final String CLIENT_NAME = PrimeClientCorrect.class.getName();

    private Component communication;
    String hostname, requestMode;
    int port;
    long initialValue, count;
    //new
    Boolean isPrime = null;

    public PrimeClientCorrect(String hostname, int port, String requestMode, long initialValue, long count) {
        this.hostname = hostname;
        this.port = port;
        this.requestMode = requestMode;
        this.initialValue = initialValue;
        this.count = count;
    }

    public void run() throws ClassNotFoundException, IOException {
        communication = new Component();
        if (requestMode.equals("POLLING")) {
            for (long i = initialValue; i < initialValue + count; i++) processNumberPolling(i);
        } else if (requestMode.equals("SYNCHRONIZED")) {
            for (long i = initialValue; i < initialValue + count; i++) processNumber(i);
        } else {
            for (long i = initialValue; i < initialValue + count; i++) processNumberThreaded(i);
        }
    }

    public void processNumber(long value) throws IOException, ClassNotFoundException {
        communication.send(new Message(hostname, port, new Long(value)), false);
        Boolean isPrime = (Boolean) communication.receive(port, true, true).getContent();
        System.out.println(value + ": " + (isPrime.booleanValue() ? "prime" : "not prime"));
    }

    //Aufgabe c
/*    public void processNumberPolling(long value) throws IOException, ClassNotFoundException {
        communication.send(new Message(hostname, port, new Long(value)), false);
        Message receiver = null;
        int receiverCounter = 1;
        while (receiver == null) {
            receiver = communication.receive(port, false, true);
            if (receiver != null) {
                Boolean isPrime = (Boolean) receiver.getContent();
                System.out.println(value + ": " + (isPrime.booleanValue() ? "prime" : "not prime"));
            } else {
                try {
                    System.out.println(value + ": " + ".".repeat(receiverCounter));
                    receiverCounter += 1;
                    Thread.sleep(500);
                } catch (Exception e) {
                    System.out.println("ERROR");
                }
            }
        }
    }*/

    //Aufgabe c edited
    public void processNumberPolling(long value) throws IOException, ClassNotFoundException {
        communication.send(new Message(hostname, port, new Long(value)), false);
        System.out.println(value + ": ");
        System.out.println((processNumberPollingReceive(value)) ? "prime" : "not prime");
    }

    public Boolean processNumberPollingReceive(long value) throws IOException, ClassNotFoundException {
        Message receiver = communication.receive(port, false, true);
        if (receiver != null)
            return (Boolean) receiver.getContent();

        try {
            System.out.print(".");
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("!!!!!!!!!!! ERROR: " + e.getMessage());
        }
        return processNumberPollingReceive(value);
    }

    public void processNumberThreaded(long value) {
        isPrime = null;

        Thread userInterface = new Thread(() -> {
            outputHandler(value);
        });

        Thread serverCommunication = new Thread(() -> {
            messageHandler(value);
        });

        userInterface.start();
        serverCommunication.start();

        try {
            userInterface.join();
            serverCommunication.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void outputHandler(long value) {
        System.out.print(value + ":");
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //while die serverCommunication kein Wert auf isPrime gesetzt hat
        while (isPrime == null) {
            System.out.print(".");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println((isPrime.booleanValue() ? "prime" : "not prime"));
    }

    public void messageHandler(long value) {
        try {
            communication.send(new Message(hostname, port, new Long(value)), false);
            isPrime = (Boolean) communication.receive(port, true, true).getContent();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        String hostname = HOSTNAME;
        int port = PORT;
        String requestMode = INITIAL_REQUEST_MODE;
        long initialValue = INITIAL_VALUE;
        long count = COUNT;

        boolean doExit = false;

        String input;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Welcome to " + CLIENT_NAME + "\n");

        while (!doExit) {
            System.out.print("Server hostname [" + hostname + "] > ");
            input = reader.readLine();
            if (!input.equals("")) hostname = input;

            System.out.print("Server port [" + port + "] > ");
            input = reader.readLine();
            if (!input.equals("")) port = Integer.parseInt(input);

            System.out.println("Request mode [" + requestMode + "] >");
            while (true) {
                System.out.println("[1] SYNCHRONIZED\n[2] POLLING\n[3] THREADED");
                input = reader.readLine();
                input = input.toUpperCase();
                if (input.equals("") || input.equals("1")) {
                    break;
                } else if (input.equals("2")) {
                    requestMode = "POLLING";
                    break;
                } else if (input.equals("3")) {
                    requestMode = "THREADED";
                    break;
                }
            }
            System.out.println("Request mode [" + requestMode + "]");

            System.out.print("Prime search initial value [" + initialValue + "] > ");
            input = reader.readLine();
            if (!input.equals("")) initialValue = Integer.parseInt(input);

            System.out.print("Prime search count [" + count + "] > ");
            input = reader.readLine();
            if (!input.equals("")) count = Integer.parseInt(input);

            new PrimeClientCorrect(hostname, port, requestMode, initialValue, count).run();

            System.out.println("Exit [n]> ");
            input = reader.readLine();
            if (input.equals("y") || input.equals("j")) doExit = true;
        }
    }
}