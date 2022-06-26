package AB_03_EX_03;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

class RMIClientListener implements BasicClientListener {
	private final BasicListener listener;
	private BasicClientConnection clientConnectionStub;

	public RMIClientListener(BasicListener listener) {
		this.listener = listener;
	}

	@Override
	public BasicClientConnection connect() {
		try {
			RMIClientConnection clientConnection = new RMIClientConnection(listener);
			clientConnection.start();

			clientConnectionStub = (BasicClientConnection) UnicastRemoteObject.exportObject(clientConnection, 0);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return clientConnectionStub;
	}
}
