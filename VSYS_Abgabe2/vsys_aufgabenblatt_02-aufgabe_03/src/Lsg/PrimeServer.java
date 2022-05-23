package Lsg;

import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

class PrimeServer {
	private final static int PORT=1234;
	private final static Logger LOGGER=Logger.getLogger(PrimeServer.class.getName());

	private Component communication;
	private int port=PORT;
	private final ExecutorService executorService;
	private final ThreadCounter threadCounter;

    PrimeServer(int port, ExecutorService executorService) {
    	this.communication=new Component();
    	this.executorService=executorService;
    	this.threadCounter=new ThreadCounter(executorService);

    	if(port>0) this.port=port;

    	setLogLevel(Level.FINER);
    }

    private boolean primeService(long number) {
		for (long i = 2; i < Math.sqrt(number)+1; i++) {
		    if (number % i == 0) return false;
		}
		return true;
    }

	void setLogLevel(Level level) {
    	for(Handler h : LOGGER.getLogger("").getHandlers())	h.setLevel(level);
    	LOGGER.setLevel(level);
    	LOGGER.info("Log level set to "+level);
    }

    void listen() {
	    LOGGER.info("Listening on port " + port);

	    this.threadCounter.start();
	    while (true) {
		    Long request = null;
		    Integer sendPort = null;

		    LOGGER.finer("Receiving ...");
		    try {
			    Message message = communication.receive(port, true, true);
			    request = (Long) message.getContent();
			    sendPort = message.getPort();
		    } catch (ClassNotFoundException | IOException e) {
			    e.printStackTrace();
		    }
		    LOGGER.fine(request.toString() + " received.");

		    LOGGER.finer("Sending ...");
		    // ugly but works
		    final Long finalRequest = request;
		    final Integer finalSendPort = sendPort;
		    executorService.submit(() -> {
			    try {
				    communication.send(new Message("localhost", port, primeService(finalRequest)), finalSendPort, true);
			    	LOGGER.fine("message sent.");
			    } catch (IOException e) {
				    e.printStackTrace();
			    }
		    });
	    }
    }

    public static void main(String[] args) {
    	int port=0;
    	ExecutorType executorType = ExecutorType.SINGLE;

    	for (int i = 0; i<args.length; i++)  {
			switch(args[i]) {
				case "-port":
					try {
				        port = Integer.parseInt(args[++i]);
				    } catch (NumberFormatException e) {
				    	LOGGER.severe("port must be an integer, not "+args[i]);
				        System.exit(1);
				    }
					break;
				case "-executor":
					executorType = ExecutorType.valueOf(args[++i]);
					break;
				default:
					LOGGER.warning("Wrong parameter passed ... '"+args[i]+"'");
			}
        }

    	LOGGER.info("using %s executor".formatted(executorType));
    	ExecutorService executorService = switch (executorType) {
    		case SINGLE -> Executors.newSingleThreadExecutor();
		    case FIXED -> Executors.newFixedThreadPool(4);
		    case DYNAMIC -> new ThreadPoolExecutor(1, Integer.MAX_VALUE, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1));
		    case MOCK -> new MockExecutorService();
	    };
	    new PrimeServer(port, executorService).listen();
    }

    private enum ExecutorType {
    	SINGLE,
	    DYNAMIC,
	    FIXED,
	    MOCK
    }
}
