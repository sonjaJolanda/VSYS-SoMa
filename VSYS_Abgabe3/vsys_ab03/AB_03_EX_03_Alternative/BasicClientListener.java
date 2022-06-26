package AB_03_EX_03_Alternative;

import java.rmi.Remote;
import java.rmi.RemoteException;

interface BasicClientListener extends Remote {
	/**
	 * Waits for a connection. After a connection has been made, a
	 * {@link BasicClientConnection} is created, exported, registered and
	 * returned.
	 *
	 * @return A connection to the connecting client.
	 */
	BasicClientConnection connect() throws RemoteException;
}
