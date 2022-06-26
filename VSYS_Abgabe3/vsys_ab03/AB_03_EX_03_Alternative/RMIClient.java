package AB_03_EX_03_Alternative;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class RMIClient implements BasicClient {
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private BasicClientConnection baseClientConnection;
	private Future<String> futureIncomingMessage;
	private String incomingMessage;
	private String outgoingMessage;

	@Override
	public void connect(int port, String hostname) {
		try {

			/* get the rmiClientListener from the registry */
			Registry registry = LocateRegistry.getRegistry(hostname, port);
			BasicClientListener basicClientListener = (BasicClientListener) registry.lookup("clientListener");
			/* in RMIClientListener.class */
			baseClientConnection = basicClientListener.connect();


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String receiveMessage(boolean blocking) {
		if (!blocking && !futureIncomingMessage.isDone())
			return null;

		try {
			incomingMessage = futureIncomingMessage.get();
			if (incomingMessage != "") {
				outgoingMessage = incomingMessage;
				incomingMessage = "";
			}
			return outgoingMessage;
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void sendMessage(String text) {
		futureIncomingMessage = executorService.submit(() -> baseClientConnection.message(text));
	}

	@Override
	public void disconnect() {
		// do nothing
	}
}
