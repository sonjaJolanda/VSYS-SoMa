package AB_01_EX_01;

/**
 * Lösung von der A
 */
public class MyThreadA extends Thread {
    private Long firstTime;
    private static final int threadMax = 10;
    private static int runCount = 0;

    public static void main(String args[]) {
        for (int i = 0; i < threadMax; i++) {
            new MyThreadA().start();
        }
    }

    public void run() {
        while (runCount++ < 100) {
            System.out.println("count " + runCount + " : " + Thread.currentThread().getName() + " Time: " + getTimeSinceFirst());
        }
    }

    public Long getTimeSinceFirst() {
        if (firstTime == null)
            firstTime = System.currentTimeMillis();
        return System.currentTimeMillis() - firstTime;
    }
}