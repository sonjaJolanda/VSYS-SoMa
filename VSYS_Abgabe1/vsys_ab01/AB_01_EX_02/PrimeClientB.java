package AB_01_EX_02;

import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PrimeClientB extends Thread {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 1234;
    private static final long INITIAL_VALUE = (long) 1e17;
    private static final Long COUNT = Long.valueOf(20);
    private static final String CLIENT_NAME = PrimeClientB.class.getName();
    private static final String DEFAULT_REQUEST_MODE = "DEFAULT";

    private static long startTime = 0; //for sout only
    private static Long i = null; // for run() and processNumber()

    private Component communication;
    String hostname;
    int port;
    long initialValue, count;
    String requestMode;

    //is called only once
    public PrimeClientB(String hostname, int port, long initialValue, long count, String requestMode) {
        this.hostname = hostname;
        this.port = port;
        this.initialValue = initialValue;
        this.count = count;
        this.requestMode = requestMode;
    }

    //is called only once but calls the methode processNumber() multiple times
    public void run() {
        System.out.println("mode: " + requestMode + " --> run starting" + timeAndThread());
        communication = new Component();
        try {
            if (i == null) i = initialValue;
            System.out.println("startWhile() (init" + initialValue + ")" + timeAndThread());
            while (i < initialValue + count) {
                processNumber();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("run through" + timeAndThread());
    }

    public void processNumber() throws IOException, ClassNotFoundException {
        System.out.println("--> started processNumber, " + requestMode + ", " + i + ", " + Thread.currentThread().getName());
        synchronized (COUNT) {
            if (i >= initialValue + count) return;
            communication.send(new Message(hostname, port, new Long(i)), false);
            Boolean isPrime = (Boolean) communication.receive(port, true, true).getContent();
            System.out.println("-------> " + i + ": " + (isPrime.booleanValue() ? "!!!!!! PRIME" : "not prime") + timeAndThread());
            i++;
        }
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        String hostname = HOSTNAME;
        int port = PORT;
        long initialValue = INITIAL_VALUE;
        long count = COUNT;
        String requestMode = DEFAULT_REQUEST_MODE;

        boolean doExit = false;

        String input;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Welcome to " + CLIENT_NAME + "\n");

        while (!doExit) {
            i = null;
            System.out.print("Server hostname [" + hostname + "] > ");
            input = reader.readLine();
            if (!input.equals("")) hostname = input;

            System.out.print("Server port [" + port + "] > ");
            input = reader.readLine();
            if (!input.equals("")) port = Integer.parseInt(input);

            System.out.print("Prime request mode [" + requestMode + "] > ");
            input = reader.readLine();
            if (!input.equals("") && (input.equals("SYNC") || input.equals("DEFAULT"))) requestMode = input;

            System.out.print("Prime search initial value [" + initialValue + "] > ");
            input = reader.readLine();
            if (!input.equals("")) initialValue = Integer.parseInt(input);

            System.out.print("Prime search count [" + count + "] > ");
            input = reader.readLine();
            if (!input.equals("")) count = Integer.parseInt(input);

            //new
            if (requestMode.equals("DEFAULT")) {
                new PrimeClientB(hostname, port, initialValue, count, requestMode).run();
            } else if (requestMode.equals("SYNC")) {
                for (int i = 0; i < 4; i++) {
                    new PrimeClientB(hostname, port, initialValue, count, requestMode).start();
                }
            }

            System.out.println("Exit [n]> ");
            input = reader.readLine();
            if (input.equals("y") || input.equals("j")) doExit = true;
        }
    }

    private String timeAndThread() {
        if (startTime == 0) startTime = System.currentTimeMillis();
        long milliSecs = System.currentTimeMillis() - startTime;
        return " --> t" + milliSecs + " - " + Thread.currentThread().getName();
    }
}
	
