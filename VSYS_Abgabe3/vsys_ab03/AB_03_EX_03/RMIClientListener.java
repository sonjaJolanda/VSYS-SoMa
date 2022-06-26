package AB_03_EX_03;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

class RMIClientListener implements BasicClientListener {
	private final BasicListener primeServerBasicListener;
	private BasicClientConnection clientConnectionStub;

	public RMIClientListener(BasicListener primeServerBasicListener) {
		this.primeServerBasicListener = primeServerBasicListener;
	}

	@Override
	public BasicClientConnection connect() {
		try {

			/*  */
			RMIClientConnection rmiClientConnection = new RMIClientConnection(primeServerBasicListener);
			rmiClientConnection.start();
			clientConnectionStub = (BasicClientConnection) UnicastRemoteObject.exportObject(rmiClientConnection, 0);

		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return clientConnectionStub;
	}
}
