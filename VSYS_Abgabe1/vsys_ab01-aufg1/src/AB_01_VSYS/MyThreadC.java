package AB_01_VSYS;

/**
 * Lösung von der C
 */
public class MyThreadC extends Thread {
    private static final int threadMax = 10;
    private static Integer runCount = 0;

    public static void main(String args[]) {
        for (int i = 0; i < threadMax; i++) {
            new MyThreadC().start();
        }
    }

    public void run() {
        System.out.println("---------------- Thread start " + Thread.currentThread().getName() + " Time: " + System.currentTimeMillis());
        while (runCount++ < 100) {
            synchronizedMethod();
        }
        System.out.println("---------------- Thread end " + Thread.currentThread().getName() + " Time: " + System.currentTimeMillis());
    }

    public void synchronizedMethod() {
        synchronized (runCount) {
            System.out.println(runCount + " : " + Thread.currentThread().getName() + " Time: " + System.currentTimeMillis());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //das ist auch eine Alternative
    /*public void synchronizedMethod() {
        synchronized (MyThread2.class) {
            System.out.println(runCount + " : " + Thread.currentThread().getName() + " Time: " + System.currentTimeMillis());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }*/

    //das ist eine alternative
 /*   public static synchronized void synchronizedMethod() {
        System.out.println(runCount + " : " + Thread.currentThread().getName() + " Time: " + System.currentTimeMillis());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/
}
