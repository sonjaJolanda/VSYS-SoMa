package AB_03_EX_03;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class RMIClient implements BasicClient {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private BasicClientConnection baseClientConnection;
    private Future<Boolean> futureIncomingMessage;

    @Override
    public void connect(int port, String hostname) {
        try {
            /* get the rmiClientListener from the registry */
            Registry registry = LocateRegistry.getRegistry(hostname, port);
            BasicClientListener rmiClientListener = (BasicClientListener) registry.lookup("clientListener");
            /* in RMIClientListener.class */
            baseClientConnection = rmiClientListener.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Boolean receivePrimeRequestResult(boolean blocking) {
        if (!blocking && !futureIncomingMessage.isDone())
            return null;

        try {
            return futureIncomingMessage.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void sendPrimeRequest(long number) {
        futureIncomingMessage = executorService.submit(() -> baseClientConnection.primeService(number));
    }

    @Override
    public void disconnect() {
        // do nothing
    }
}
