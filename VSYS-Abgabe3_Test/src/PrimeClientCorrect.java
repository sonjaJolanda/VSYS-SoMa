import rm.requestResponse.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PrimeClientCorrect {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 1234;
    private static final String INITIAL_REQUEST_MODE = "SYNCHRONIZED";
    private static final long INITIAL_VALUE = (long) 1e17;
    private static final long COUNT = 20;
    private static final String CLIENT_NAME = PrimeClientCorrect.class.getName();

    private Component communication;
    String hostname, requestMode;
    int sendPort;
    int answerPort;
    long initialValue, count;
    //new
    Boolean isPrime = null;
    RequestPair requestPair = null;

    public PrimeClientCorrect(String hostname, int sendPort, int answerPort, String requestMode, long initialValue, long count) {
        this.hostname = hostname;
        this.sendPort = sendPort;
        this.answerPort = answerPort;
        this.requestMode = requestMode;
        this.count = count;
        this.initialValue = initialValue;
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
        RequestPair requestPair = new RequestPair(answerPort, value);
        requestPair.setcStart();
        communication.send(new Message(hostname, answerPort, requestPair), sendPort, false);
        requestPair = (RequestPair) communication.receive(answerPort, true, true).getContent();
        requestPair.setcEnd();
        printResult(requestPair);
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

    public void processNumberPolling(long value) throws IOException, ClassNotFoundException {
        RequestPair requestPair = new RequestPair(answerPort, value);
        requestPair.setcStart();
        communication.send(new Message(hostname, answerPort, requestPair), sendPort, false);
        //System.out.println(value + ": ");
        printResult(processNumberPollingReceive(requestPair));
    }

    public RequestPair processNumberPollingReceive(RequestPair requestPair) throws IOException, ClassNotFoundException {
        //System.out.println("receive " + requestPair.sendPort);
        Message receiver = communication.receive(requestPair.sendPort, false, true);
        if (receiver != null) {
            //System.out.println("receiver != null");
            requestPair = (RequestPair) receiver.getContent();
            //System.out.println("new request pair added");
            requestPair.setcEnd();
            return requestPair;
        }

        try {
            System.out.print(".");
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("!!!!!!!!!!! ERROR: " + e.getMessage());
        }

        return processNumberPollingReceive(requestPair);
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
        //System.out.print(value + ":");
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

        this.requestPair.answer = isPrime;
        printResult(this.requestPair);
    }

    public void messageHandler(long value) {
        try {
            this.requestPair = new RequestPair(answerPort, value);
            this.requestPair.setcStart();
            communication.send(new Message(hostname, answerPort, this.requestPair), sendPort, false);
            this.requestPair = (RequestPair) communication.receive(answerPort, true, true).getContent();
            this.requestPair.setcEnd();
            isPrime = this.requestPair.answer;
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

            //new PrimeClientCorrect(hostname, port, requestMode, initialValue, count).run();

            System.out.println("Exit [n]> ");
            input = reader.readLine();
            if (input.equals("y") || input.equals("j")) doExit = true;
        }
    }

    private void printResult(RequestPair requestPair) {
        System.out.println("port " + requestPair.sendPort + " " +
                requestPair.requestValue + ": " + (requestPair.answer.booleanValue() ? "prime" : "not prime") +
                " | p: " + requestPair.getProcessingTime() + " (" + requestPair.getProcessingTimeAverage() + ") " + " ms" +
                " | w: " + requestPair.getWaitingTime() + " (" + requestPair.getWaitingTimeAverage() + ") " +" ms" +
                " | c: " + requestPair.getCommunicationTime() + " (" + requestPair.getCommunicationTimeAverage() + ") " +" ms");
    }
}