package AB_03_EX_03;

import java.rmi.Remote;
import java.rmi.RemoteException;

interface BasicClientConnection extends Remote {
	/**
	 * Sends a message to the server.
	 *
	 * @param text The message to be sent.
	 * @return The message, that has been sent.
	 */
	String message(String text) throws RemoteException;

	/**
	 * Disconnects the connection between server and client.
	 */
	void disconnect() throws RemoteException;
}
