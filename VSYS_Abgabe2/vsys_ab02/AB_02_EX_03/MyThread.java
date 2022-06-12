package AB_02_EX_03;

import rm.requestResponse.Component;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class MyThread extends Thread {
    private Component communication;
    private int port;
    private int sendPort;
    private Boolean isPrime = false;

    public MyThread(Component communication, int port, int sendPort) {
        this.communication = communication;
        this.port = port;
        this.sendPort = sendPort;
    }

    public Boolean getPrime() {
        return isPrime;
    }

    public void run() {
        AtomicReference<Boolean> result = new AtomicReference<>(null);
        try {
            //System.out.println("listening to " + sendPort);
            result.set((Boolean) communication.receive(sendPort, true, true).getContent());
            isPrime = result.get();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
