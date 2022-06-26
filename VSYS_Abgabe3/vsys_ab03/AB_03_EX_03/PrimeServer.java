package AB_03_EX_03;

import java.util.concurrent.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("DuplicatedCode")
class PrimeServer implements BasicListener {
	private static final Logger LOGGER = Logger.getLogger(PrimeServer.class.getName());
	private final ExecutorService executorService;
	private final BasicServer basicServer;

	PrimeServer(BasicServer basicServer, ExecutorService executorService) {
		this.executorService = executorService;
		this.basicServer = basicServer;
		this.basicServer.setPrimeServerBasicListener(this);

		new ThreadCounter(executorService).start();
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
		for (Handler h : Logger.getLogger("").getHandlers()) {
			h.setLevel(level);
		}
		LOGGER.setLevel(level);
		LOGGER.info("Log level set to " + level);
	}

	@Override
	public void connectionAccepted(BasicConnection rmiClientConnection) {
		LOGGER.finer("Receiving ...");
		long request = Long.parseLong(rmiClientConnection.receiveMessage());
		LOGGER.fine("%d received.".formatted(request));

		executorService.submit(() -> {
			rmiClientConnection.sendMessage(String.valueOf(primeService(request)));
			LOGGER.fine("message sent.");
		});
	}

	public static void main(String[] args) {
		int port = 1234;
		ExecutorType executorType = ExecutorType.SINGLE;

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
				case "-executor":
					executorType = ExecutorType.valueOf(args[++i]);
					break;
				default:
					LOGGER.warning("Wrong parameter passed ... '" + args[i] + "'");
			}
		}

		LOGGER.info("using %s executor".formatted(executorType));
		ExecutorService executorService = switch (executorType) {
			case SINGLE -> Executors.newSingleThreadExecutor();
			case FIXED -> Executors.newFixedThreadPool(4);
			case DYNAMIC -> new ThreadPoolExecutor(1, Integer.MAX_VALUE, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1));
		};

		BasicServer basicServer = new RMIServer();
		basicServer.setPrimeServerBasicListener(new PrimeServer(basicServer, executorService));
		basicServer.waitForConnection(port);
	}

	private enum ExecutorType {
		SINGLE,
		DYNAMIC,
		FIXED,
	}
}
