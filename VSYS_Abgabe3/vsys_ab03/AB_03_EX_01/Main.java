package AB_03_EX_01;

import java.io.IOException;

public class Main {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 5656;
    private static final String REQUEST_TYPE = "POLLING"; //[1] SYNCHRONIZED [2] POLLING [3] THREADED
    private static final long COUNT = 5;

    /**
     * 8 Clients aber 5 Zahlen
     * ----------------------------------------------------------------------------------------------------
     * SYNCHRONIZED - SINGLE THREADED
     *  p - überall 0 bis auf bei der prime (Durchschn. schwankt zwischen 167 und 179)
     *  c - zwischen 2 und 202 (Durchschn. Ende 61)
     *      die erste Connection pro Client ist immer am höchsten (großer Unterschied)
     *  w - zwischen 0 und 3458 (Durchschn. schwankt zwischen 1107 und 1127)
     *      besonders hoch ist sie nach einer prime (aber von allen Clients ist sie dann besonders hoch)
     *      (immer nur einmal bei jedem Client)
     * POLLING - SINGLE THREADED
     *  p - überall 0 bis auf bei den prime (Durchschn. schwankt zwischen 162 und 183)
     *  c - zwischen 258 und 744 (Durchschn. schwankt zwischen 503 und 712)
     *      die erste Connection pro Client ist immer am höchsten (mittlerer Unterschied)
     *  w - zwischen 0 und 7839 (Durchschn. schwankt zwischen 1087 und 1149)
     *      besonders hoch ist sie nach einer prime (aber von allen Clients ist sie dann besonders hoch)
     *      (immer nur einmal bei jedem Client)
     * THREADED - SINGLE THREADED
     *  p - überall 0 bis auf bei den prime (Durchschn. schwankt zwischen 166 und 177)
     *  c - zwischen 2 und 130 (Durchschn. Ende 36)
     *      die erste Connection pro Client ist immer am höchsten (großer Unterschied)
     *  w - zwischen 0 und 7839 (Durchschn. schwankt zwischen 1087 und 1149)
     *      besonders hoch ist sie nach einer prime (aber von allen Clients ist sie dann besonders hoch)
     *      (immer nur einmal bei jedem Client)
     * ----------------------------------------------------------------------------------------------------
     * SYNCHRONIZED - DYNAMIC THREADPOOL
     *  p - überall 0 (auch der Durschnitt), bis dann eine Primzahlberechnung kommt (dann aber auch nur für diese Berechnung)
     *  c - zwischen 1 und 355 (Durchschn. Ende 94)
     *      die erste Connection pro Client ist immer am höchsten (großer Unterschied)
     *  w - zwischen 5 und 155 (Durschn. zwischen 19 und 58)
     *      nach einer Primzahl berechnung ist es bei den anderen Clients nicht höher
     * POLLING - DYNAMIC THREADPOOL
     *  p - überall 0 (auch der Durschnitt), bis dann eine Primzahlberechnung kommt (dann aber auch nur für diese Berechnung)
     *  c - zwischen 658 und 76 (Durschn. zwischen 646 und 517)
     *      bei den beiden Primes ist es deutlich geringer (weil p so hoch ist??)
     *  w - zwischen 0 (am Schluss) und 45
     *      nach Primzahlberechnung -> 0
     * THREADED - DYNAMIC THREADPOOL
     *  p - überall 0 (nicht der Durschnitt), bis dann eine Primzahlberechnung kommt (dann aber auch nur für diese Berechnung)
     *  c - zwischen 5 und 191 (Durschn. zwischen 44 und 169)
     *      die erste Connection pro Client ist immer am höchsten (großer Unterschied)
     *  w - zwischen 0 und 36 (Durschn. zwischen 24 und 30)
     *      am Anfang am höchsten und dann schnell geringer
     *      nach Primzahlberechnung nicht höher eher gegen 0
     * ----------------------------------------------------------------------------------------------------
     * SYNCHRONIZED - CONSTANT THREADPOOL -> 2
     *  p - überall 0 (auch der Durschnitt), bis dann eine Primzahlberechnung kommt (dann aber auch nur für diese Berechnung)
     *  c - zwischen 6 und 439 (Durchschn. Ende 106)
     *      die erste Connection pro Client ist immer am höchsten (großer Unterschied)
     *  w - zwischen 0 und 3256 (Durschn. bis zur Primzahl eher bei 28 und danach starker Anstieg -> Ende 349)
     *      Nicht alle aber ein paar Clients (4/8) haben ein hohes w nach der Primzahl
     * POLLING - CONSTANT THREADPOOL -> 2
     *  p - p ist fast immer 0, auch bei nichtPrimzahlberechnung kann es aber zu 1 kommen
     *  c - schwankt zwischen 143 und 637 (Durschn. Ende 473)
     *      nicht abhängig von der Zahl
     *  w - zwischen 0 und 2368 (Durschn. bis zur Primzahl eher bei 28 und danach starker Anstieg -> Ende 383)
     *      Nicht alle aber ein paar Clients (4/8) haben ein hohes w nach der Primzahl
     * THREADED - CONSTANT THREADPOOL -> 2
     *  p - überall 0 (auch der Durschnitt), bis dann eine Primzahlberechnung kommt (dann aber auch nur für diese Berechnung)
     *  c - Anfangs bei 155 im Durschnitt (Bei allen Clients 1x) dann nicht höher als 50 (Durschn. Ende 44)
     *      Außer nach der ersten Primzahl ist es bei einem 95
     *  w - zwischen 0 und 3256 (Durschn. bis zur Primzahl eher bei 28 und danach starker Anstieg -> Ende 349)
     *      Alle außer den Primzahlen haben ein hohes w nach der Primzahl
     */

    public static void main(String[] args) {
        for (int i = 0; i < 8; i++) {
            int answerPort = 1235 + i;
            long initialValue = (long) 1e16 * (i + 1);
            System.out.println("-> initalValue: " + initialValue + ", answerPort: " + answerPort);
            Thread thread = new Thread(() -> {
                try {
                    new PrimeClientCorrect(HOSTNAME, PORT, answerPort, REQUEST_TYPE, initialValue, COUNT).run();
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }
    }
}
