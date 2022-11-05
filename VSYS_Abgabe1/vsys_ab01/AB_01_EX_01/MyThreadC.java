package AB_01_EX_01;

/**
 * Lösung von der C
 */
public class MyThreadC extends Thread {
    private static final int threadMax = 10;
    private static int runCounter = 0;

    public void run() {
        while (runCounter++ < 100) {
            while_synchronizer();
        }
    }


    public synchronized static void while_synchronizer() {
        try {
            System.out.println(runCounter + ": " + Thread.currentThread().getName());
            sleep(1000);
        } catch (InterruptedException ignore) {
            System.out.println("InterruptedException raised");
        }
    }


    public static void main(String[] args) {
        for (int i = 0; i < threadMax; i++) {
            new MyThreadC().start();
        }
    }
}
