package Lsg;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

/**
 * Counts the number of currently active {@link Thread}s. The current number is
 * logged every second.
 */
public class ThreadCounter extends Thread {
	private static final Logger LOGGER = Logger.getLogger("ThreadCounter");
	private final ExecutorService executorService;

	public ThreadCounter(ExecutorService executorService) {
		this.executorService = executorService;
	}

	private static int nActiveThreads(ExecutorService executorService) {
		if (executorService instanceof ThreadPoolExecutor threadPoolExecutor) {
			return threadPoolExecutor.getActiveCount();
		} else if (executorService instanceof MockExecutorService) {
			return 0;
		} else {
			// SingleThreadExecutor
			return 1;
		}
	}

	@Override
	public void run() {
		int lastNActiveThreads = -1;
		while (!this.isInterrupted()) {
			int nActiveThreads = nActiveThreads(executorService);
			if (nActiveThreads != lastNActiveThreads) {
				lastNActiveThreads = nActiveThreads;
				LOGGER.info("Currently running with %d threads.".formatted(nActiveThreads));
			}
		}
	}
}
