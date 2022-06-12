package AB_01_EX_01;

/**
 * Lösung von der D
 */
public class MyAccount {
    private static int var = 1;

    public synchronized static void setVar(int var) {
        MyAccount.var = var;
    }

    public synchronized static int getVar() {
        return var;
    }
}
