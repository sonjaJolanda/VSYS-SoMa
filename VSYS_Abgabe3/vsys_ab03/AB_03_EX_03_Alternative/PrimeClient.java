package AB_03_EX_03_Alternative;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("DuplicatedCode")
class PrimeClient {
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 1234;
	private static final RequestMode REQUEST_MODE = RequestMode.SYNC;
	private static final long INITIAL_VALUE = (long) 1e17;
	private static final long COUNT = 20;
	private static final String CLIENT_NAME = PrimeClient.class.getName();
	private final String hostname;
	private final int port;
	private final RequestMode requestMode;
	private final long initialValue;
	private final long count;
	private RMIClient rmiClient;

	public PrimeClient(String hostname, int port, RequestMode requestMode, long initialValue, long count) {
		this.hostname = hostname;
		this.port = port;
		this.requestMode = requestMode;
		this.initialValue = initialValue;
		this.count = count;
	}

	public void run() {
		rmiClient = new RMIClient();
		for (long i = initialValue; i < initialValue + count; i++) {
			rmiClient.connect(port, hostname);
			processNumber(i);
			rmiClient.disconnect();
		}
	}

	public void processNumber(long value) {
		rmiClient.sendMessage(String.valueOf(value));

		switch (requestMode) {
			case SYNC -> {
				boolean isPrime = Boolean.parseBoolean(rmiClient.receiveMessage(true));
				synchronized (PrimeClient.class) {
					System.out.printf("%d:", value);
					System.out.println(isPrimeMessage(isPrime));
				}
			}
			case POLLING -> {
				System.out.printf("%d:", value);

				String message = rmiClient.receiveMessage(false);
				while (message == null) {
					System.out.print(".");
					try {
						TimeUnit.MILLISECONDS.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					message = rmiClient.receiveMessage(false);
				}

				boolean isPrime = Boolean.parseBoolean(message);
				System.out.println(isPrimeMessage(isPrime));
			}
			case ASYNC -> {
				AtomicReference<Boolean> result = new AtomicReference<>(null);
				Thread thread = new Thread(() -> result.set(Boolean.parseBoolean(rmiClient.receiveMessage(true))));

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

	public static void main(String[] args) throws IOException {
		String hostname = HOSTNAME;
		int port = PORT;
		RequestMode requestMode = REQUEST_MODE;
		long initialValue = INITIAL_VALUE;
		long count = COUNT;

		boolean doExit = false;

		String input;
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Welcome to " + CLIENT_NAME + "\n");

		while (!doExit) {
			System.out.print("Server hostname [" + hostname + "] > ");
			input = reader.readLine();
			if (!input.equals(""))
				hostname = input;

			System.out.print("Server port [" + port + "] > ");
			input = reader.readLine();
			if (!input.equals(""))
				port = Integer.parseInt(input);

			System.out.print("Request mode [" + requestMode + "] > ");
			input = reader.readLine();
			if (!input.equals(""))
				requestMode = RequestMode.parse(input);

			System.out.print("Prime search initial value [" + initialValue + "] > ");
			input = reader.readLine();
			if (!input.equals(""))
				initialValue = Long.parseLong(input);

			System.out.print("Prime search count [" + count + "] > ");
			input = reader.readLine();
			if (!input.equals(""))
				count = Integer.parseInt(input);

			new PrimeClient(hostname, port, requestMode, initialValue, count).run();

			System.out.println("Exit [n]> ");
			input = reader.readLine();
			if (input.equals("y") || input.equals("j"))
				doExit = true;
		}
	}

	private enum RequestMode {
		SYNC, ASYNC, POLLING;

		public static RequestMode parse(String string) {
			return RequestMode.valueOf(string.replace(" ", "_").toUpperCase(Locale.ROOT));
		}

		@Override
		public String toString() {
			return super.toString().toLowerCase(Locale.ROOT);
		}
	}
}
	
