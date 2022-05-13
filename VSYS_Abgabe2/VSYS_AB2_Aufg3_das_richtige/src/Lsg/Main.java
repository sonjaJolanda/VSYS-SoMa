package Lsg;

import java.io.IOException;

public class Main {
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 1234;
	private static final RequestMode REQUEST_MODE = RequestMode.SYNC;
	private static final long COUNT = 20;

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			int sendPort = 1235 + i;
			long initialValue = (long) 1e16 * (i + 1);
			Thread thread = new Thread(() -> {
				try {
					new PrimeClient(HOSTNAME, PORT, sendPort, REQUEST_MODE, initialValue, COUNT).run();
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			});
			thread.start();
		}
	}
}
