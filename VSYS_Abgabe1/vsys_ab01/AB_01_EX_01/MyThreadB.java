package AB_01_EX_01;

/**
 * Lösung von der B
 */
public class MyThreadB extends Thread {
    private Long firstTime;
    private static final int threadMax = 10;
    private static Integer runCount = 0;

    public static void main(String args[]) {
        for (int i = 0; i < threadMax; i++) {
            new MyThreadB().start();
        }
    }

    public void run() {
        while (runCount++ < 100) {
            synchronizedMethod();
        }
    }

    //hier wurde das synchronized keyword im methoden-kopf benutzt dh das sperrobjekt ist this
    public synchronized void synchronizedMethod() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Long getTimeSinceFirst() {
        if (firstTime == null)
            firstTime = System.currentTimeMillis();
        return System.currentTimeMillis() - firstTime;
    }
}
