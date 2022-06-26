package AB_03_EX_03;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

class RMIServer implements BasicServer {
	private BasicListener listener;

	@Override
	@SuppressWarnings({"InfiniteLoopStatement", "BusyWait"})
	public void waitForConnection(int port) {
		try {
			Registry registry = LocateRegistry.createRegistry(port);

			RMIClientListener clientListener = new RMIClientListener(listener);
			Remote clientListenerStub = UnicastRemoteObject.exportObject(clientListener, 0);

			registry.rebind("clientListener", clientListenerStub);
			while (true) {
				Thread.sleep(1000);
			}
		} catch (RemoteException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setListener(BasicListener listener) {
		this.listener = listener;
	}
}
