import rm.requestResponse.Component;

import java.io.IOException;

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
        synchronized (MyThread.class) {
            try {
                //System.out.println("listening to " + sendPort);
                this.isPrime = (Boolean) communication.receive(sendPort, true, true).getContent();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
