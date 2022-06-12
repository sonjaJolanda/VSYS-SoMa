package AB_01_EX_01;

/**
 * Lösung von der A
 */
public class MyThreadA extends Thread {
    private static final int threadMax = 10;
    private static int runCount = 0;

    public static void main(String args[]) {
        for (int i = 0; i < threadMax; i++) {
            new MyThreadA().start();
        }
    }

    public void run() {
        System.out.println("---------------- Thread start " + Thread.currentThread().getName() + " Time: " + System.currentTimeMillis());
        while (runCount++ < 100) {
            System.out.println("count " + runCount + " : " + Thread.currentThread().getName() + " Time: " + System.currentTimeMillis());
        }
        System.out.println("---------------- Thread end " + Thread.currentThread().getName() + " Time: " + System.currentTimeMillis());
    }
}