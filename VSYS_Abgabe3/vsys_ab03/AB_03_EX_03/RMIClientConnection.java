package AB_03_EX_03;

import java.util.concurrent.ExecutionException;

class RMIClientConnection extends Thread implements BasicClientConnection, BasicConnection {
    private final BasicListener primeServerBasicListener;

    public RMIClientConnection(BasicListener primeServerBasicListener) {
        this.primeServerBasicListener = primeServerBasicListener;
    }

    @Override
    public Boolean primeService(long number) {
        try {
            return primeServerBasicListener.primeService(number).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
