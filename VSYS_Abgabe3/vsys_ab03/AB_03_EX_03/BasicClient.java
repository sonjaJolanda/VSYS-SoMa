package AB_03_EX_03;

import java.util.concurrent.ExecutionException;

interface BasicClient {
    void connect(int port, String hostname);

    Boolean receivePrimeRequestResult(boolean blocking);

	void sendPrimeRequest(long number) throws ExecutionException, InterruptedException;

    void disconnect();
}
