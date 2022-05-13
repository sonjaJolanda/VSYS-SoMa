package Lsg;

import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

class PrimeClient {
	private static final String HOSTNAME="localhost";
	private static final int PORT=1234;
	private static final int SEND_PORT=1235;
	private static final RequestMode REQUEST_MODE= RequestMode.SYNC;
	private static final long INITIAL_VALUE=(long)1e17;
	private static final long COUNT=20;
	private static final String CLIENT_NAME=PrimeClient.class.getName();

	private Component communication;
	String hostname;
	int port;
	int sendPort;
	RequestMode requestMode;
	long initialValue,count;

	public PrimeClient(String hostname, int port, int sendPort, RequestMode requestMode, long initialValue, long count) {
		this.hostname=hostname;
		this.port=port;
		this.sendPort=sendPort;
		this.requestMode=requestMode;
		this.initialValue=initialValue;
		this.count=count;
	}

	public void run() throws ClassNotFoundException, IOException {
		communication=new Component();
		for (long i=initialValue;i<initialValue+count;i++) processNumber(i);
		communication.cleanup();
    }

    public void processNumber(long value) throws IOException, ClassNotFoundException {
		communication.send(new Message(hostname,sendPort,value),port,false);

		switch (requestMode) {
			case SYNC -> {
				Boolean	isPrime = (Boolean) communication.receive(sendPort, true, true).getContent();
				synchronized (PrimeClient.class) {
					System.out.printf("%d:", value);
					System.out.println(isPrimeMessage(isPrime));
				}
			}
			case POLLING -> {
				System.out.printf("%d:", value);

				Message message = communication.receive(sendPort,false,true);
				while (message == null) {
					System.out.print(".");
					try {
						TimeUnit.MILLISECONDS.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					message = communication.receive(sendPort,false,true);
				}

				Boolean isPrime = (Boolean) message.getContent();
				System.out.println(isPrimeMessage(isPrime));
			}
			case ASYNC -> {
				AtomicReference<Boolean> result = new AtomicReference<>(null);
				Thread thread = new Thread(() -> {
					try {
						result.set((Boolean) communication.receive(sendPort, true, true).getContent());
					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				});

				System.out.printf("%d:", value);

				thread.start();
				while (thread.isAlive()) {
					System.out.print(".");
					try {
						TimeUnit.MILLISECONDS.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				Boolean isPrime = result.get();
				System.out.println(isPrimeMessage(isPrime));
			}
		}
    }

	private String isPrimeMessage(boolean isPrime) {
		return isPrime ? "prime" : "not prime";
	}

	public static void main(String args[]) throws IOException, ClassNotFoundException {
		String hostname=HOSTNAME;
		int port=PORT;
		int sendPort=SEND_PORT;
		RequestMode requestMode=REQUEST_MODE;
		long initialValue=INITIAL_VALUE;
		long count=COUNT;

		boolean doExit=false;

		String input;
		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in ));

		System.out.println("Welcome to "+CLIENT_NAME+"\n");

		while(!doExit) {
			System.out.print("Server hostname ["+hostname+"] > ");
			input=reader.readLine();
			if(!input.equals("")) hostname=input;

			System.out.print("Server port ["+port+"] > ");
			input=reader.readLine();
			if(!input.equals("")) port=Integer.parseInt(input);

			System.out.print("Send port ["+sendPort+"] > ");
			input=reader.readLine();
			if(!input.equals("")) sendPort=Integer.parseInt(input);

			System.out.print("Request mode ["+requestMode+"] > ");
			input=reader.readLine();
			if(!input.equals("")) requestMode= RequestMode.parse(input);

			System.out.print("Prime search initial value ["+initialValue+"] > ");
			input=reader.readLine();
			if(!input.equals("")) initialValue=Long.parseLong(input);

			System.out.print("Prime search count ["+count+"] > ");
			input=reader.readLine();
			if(!input.equals("")) count=Integer.parseInt(input);

			new PrimeClient(hostname,port,sendPort,requestMode,initialValue,count).run();

			System.out.println("Exit [n]> ");
			input=reader.readLine();
			if(input.equals("y") || input.equals("j")) doExit=true;
		}
	}
}
	
