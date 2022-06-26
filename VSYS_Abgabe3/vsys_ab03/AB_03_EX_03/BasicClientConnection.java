package AB_03_EX_03;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

interface BasicClientConnection extends Remote {

	Boolean primeService(long number) throws RemoteException;
}
