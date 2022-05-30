
import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PrimeClient_2_3_E {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 1234;
    private static final int SEND_PORT = 1235;
    private static final String REQUEST_TYPE = "SYNC";
    private static final long INITIAL_VALUE = (long) 1e17;
    private static final long COUNT = 20;
    private static final String CLIENT_NAME = PrimeClient_2_3_E.class.getName();

    private Component communication;
    String hostname;
    int port;
    int sendPort;
    String requestType;
    long initialValue, count;

    public PrimeClient_2_3_E(String hostname, int port, int sendPort, String requestType, long initialValue, long count) {
        this.hostname = hostname;
        this.port = port;
        this.sendPort = sendPort;
        this.requestType = requestType;
        this.initialValue = initialValue;
        this.count = count;
    }

    public void run() throws ClassNotFoundException, IOException, InterruptedException {
        communication = new Component();
        for (long i = initialValue; i < initialValue + count; i++) {
            processNumber(i);
        }
        communication.cleanup();
    }

    public void processNumber(long value) throws IOException, ClassNotFoundException, InterruptedException {
        Boolean isPrime = false;
        RequestPair requestPair = new RequestPair(sendPort, value);
        requestPair.setcStart();
        communication.send(new Message(hostname, sendPort, requestPair), port, false);

        if (this.requestType.equals("SYNC")) {
            requestPair = (RequestPair) communication.receive(sendPort, true, true).getContent();
            requestPair.setcEnd();
            isPrime = requestPair.answer;
        } else if (this.requestType.equals("POLLING")) {
            Message m = communication.receive(sendPort, false, true);
            while (m == null) {
                Thread.sleep(100);
                System.out.print(".");

                m = communication.receive(sendPort, false, true);
            }
            requestPair = (RequestPair) communication.receive(sendPort, true, true).getContent();
            requestPair.setcEnd();
            isPrime = requestPair.answer;
        } else if (this.requestType.equals("ASYNC")) {
            MyThread t = new MyThread(communication, port, sendPort);
            t.start();
            while (t.isAlive()) {
                System.out.print(".");
                Thread.sleep(100);
            }
            isPrime = t.getPrime();
            requestPair = t.getRequestPair();
        }

        System.out.println("port " + sendPort + " " + value + ": " + (isPrime.booleanValue() ? "prime" : "not prime" +
                " | p: " + requestPair.getProcessingTime() + " ms" +
                " | w: " + requestPair.getWaitingTime() + " ms" +
                " | c: " + requestPair.getCommunicationTime() + " ms"));

//		communication.cleanup();
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException {
        String hostname = HOSTNAME;
        int port = PORT;
        int sendPort = SEND_PORT;
        String requestType = REQUEST_TYPE;
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

            System.out.print("Send port [" + sendPort + "] > ");
            input = reader.readLine();
            if (!input.equals("")) sendPort = Integer.parseInt(input);

            System.out.print("Request mode [(s)ync, (a)sync, (p)olling)] > ");
            input = reader.readLine();
            switch (input) {
//				case "s":
//					requestType = "SYNC";
//					break;
                case "a":
                    requestType = "ASYNC";
                    break;
                case "p":
                    requestType = "POLLING";
                    break;
                default:
                    requestType = "SYNC";
                    break;
            }

            System.out.print("Prime search initial value [" + initialValue + "] > ");
            input = reader.readLine();
            if (!input.equals("")) initialValue = Integer.parseInt(input);

            System.out.print("Prime search count [" + count + "] > ");
            input = reader.readLine();
            if (!input.equals("")) count = Integer.parseInt(input);

//			if(isSynchronized) System.out.println("Anfrage wird ohne Polling durchgeführt.");
            System.out.println("/////" + requestType + "/////");
            new PrimeClient_2_3_E(hostname, port, sendPort, requestType, initialValue, count).run();

            System.out.println("Exit [n]> ");
            input = reader.readLine();
            if (input.equals("y") || input.equals("j")) doExit = true;
        }
    }
}
