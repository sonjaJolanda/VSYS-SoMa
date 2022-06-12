package AB_01_EX_01;

/**
 * Lösung von der D
 */
public class MyThreadD extends AB_01_EX_01.MyAccount implements Runnable {
    private static final int threadMax = 10;

    public static void main(String args[]) {
        MyThreadD account = new MyThreadD();
        for (int i = 0; i < threadMax; i++) {
            new Thread(account).start();
        }
    }

    public synchronized void run() {
        int var = this.getVar();

        System.out.print(var);
        if (Math.random() > 0.5) {
            System.out.print(" + 1 ");
            this.setVar(var + 1);
            System.out.print(" = " + (var + 1)); //oder ein getVar()
        } else {
            System.out.print(" - 1 ");
            this.setVar(var - 1);
            System.out.print(" = " + (var - 1));
        }
    }
}