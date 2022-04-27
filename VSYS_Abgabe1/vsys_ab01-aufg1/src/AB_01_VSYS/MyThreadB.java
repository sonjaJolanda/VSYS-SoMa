package AB_01_VSYS;

/**
 * Lösung von der B
 */
public class MyThreadB extends Thread {
    private static final int threadMax = 10;
    private static Integer runCount = 0;

    public static void main(String args[]) {
        for (int i = 0; i < threadMax; i++) {
            new MyThreadB().start();
        }
    }

    public void run() {
        System.out.println("---------------- Thread start " + Thread.currentThread().getName() + " Time: " + System.currentTimeMillis());
        while (runCount++ < 100) {
            synchronizedMethod();
        }
        System.out.println("---------------- Thread end " + Thread.currentThread().getName() + " Time: " + System.currentTimeMillis());
    }

    public synchronized void synchronizedMethod() { //hier wurde das synchronized keyword im methoden-kopf benutzt dh das sperrobjekt ist this
        System.out.println(runCount + " : " + Thread.currentThread().getName() + " Time: " + System.currentTimeMillis());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
