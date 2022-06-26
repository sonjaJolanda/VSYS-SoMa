package AB_03_EX_03_Alternative;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

class RMIServer implements BasicServer {
	private BasicListener primeServerBasicListener;

	@Override
	@SuppressWarnings({"InfiniteLoopStatement", "BusyWait"})
	public void waitForConnection(int port) {
		try {

			/* register the rmiClientListener */
			RMIClientListener rmiClientListener = new RMIClientListener(primeServerBasicListener);
			Remote clientListenerStub = UnicastRemoteObject.exportObject(rmiClientListener, 0);
			Registry registry = LocateRegistry.createRegistry(port);
			registry.rebind("clientListener", clientListenerStub);


			while (true) {  // ToDo "The thread does not lose ownership of any monitors."
				Thread.sleep(1000);
			}
		} catch (RemoteException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setPrimeServerBasicListener(BasicListener primeServerBasicListener) {
		this.primeServerBasicListener = primeServerBasicListener;
	}
}
