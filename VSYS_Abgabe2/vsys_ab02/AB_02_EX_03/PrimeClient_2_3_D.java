package AB_02_EX_03;

import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PrimeClient_2_3_D {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 1234;
    private static final long INITIAL_VALUE = (long) 1e17;
    private static final long COUNT = 5;
    private static final String CLIENT_NAME = PrimeClient_2_3_D.class.getName();

    private static long startTime = 0;

    private Component communication;
    String hostname;
    int requestPort;
    int answerPort;
    long initialValue, count;

    public PrimeClient_2_3_D(String hostname, int requestPort, int answerPort, long initialValue, long count) {
        this.hostname = hostname;
        this.requestPort = requestPort;
        this.answerPort = answerPort;
        this.initialValue = initialValue;
        this.count = count;
    }

    public void run() throws ClassNotFoundException, IOException {
        System.out.println("\nrun starting (initial value: " + initialValue);
        communication = new Component();
        System.out.println("for loop starting (initial value: " + initialValue + "\n");
        for (long i = initialValue; i < initialValue + count; i++) processNumber(i);
        System.out.println("run through (initial value: " + initialValue);
        communication.cleanup(); // ToDo does that make sense?
    }

    public void processNumber(long value) throws IOException, ClassNotFoundException {
        System.out.println("pN start " + value);
        communication.send(new Message(hostname, answerPort, (Long) value), requestPort, false);
        Boolean isPrime = (Boolean) communication.receive(answerPort, true, true).getContent();
        System.out.println(value + ": " + (isPrime.booleanValue() ? "!!!!!!prime" : "not prime") + "\n\n");
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        String hostname = HOSTNAME;
        int requestPort = PORT;
        int answerport = PORT;
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

            System.out.print("Server requestPort [" + requestPort + "] > ");
            input = reader.readLine();
            if (!input.equals("")) requestPort = Integer.parseInt(input);

            System.out.print("Server answerPort [" + answerport + "] > ");
            input = reader.readLine();
            if (!input.equals("")) answerport = Integer.parseInt(input);

            System.out.print("Prime search initial value [" + initialValue + "] > ");
            input = reader.readLine();
            if (!input.equals("")) initialValue = Long.parseLong(input);

            System.out.print("Prime search count [" + count + "] > ");
            input = reader.readLine();
            if (!input.equals("")) count = Integer.parseInt(input);

            new PrimeClient_2_3_D(hostname, requestPort, answerport, initialValue, count).run();

            System.out.println("Exit [n]> ");
            input = reader.readLine();
            if (input.equals("y") || input.equals("j")) doExit = true;
        }
    }

    private String time() {
        if (startTime == 0) startTime = System.currentTimeMillis();
        long milliSecs = System.currentTimeMillis() - startTime;
        return " --> t: " + milliSecs;
    }
}
	
