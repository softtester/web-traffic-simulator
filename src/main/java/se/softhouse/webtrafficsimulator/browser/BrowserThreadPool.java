package se.softhouse.webtrafficsimulator.browser;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Thread.sleep;
import static java.util.concurrent.Executors.newFixedThreadPool;

import java.util.List;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowserThreadPool {

	public static BrowserThreadPool browserThreadPool() {
		return new BrowserThreadPool();
	}

	private ExecutorService executor;
	private final Logger logger = LoggerFactory.getLogger(BrowserThreadPool.class);

	private final List<BrowserThread> threads = newArrayList();

	private BrowserThreadPool() {
	}

	public BrowserThreadPool addBrowser(BrowserThread state) {
		threads.add(state);
		return this;
	}

	public List<BrowserThread> getThreads() {
		return threads;
	}

	public void startAll() {
		for (final BrowserThread thread : threads) {
			thread.start();
			executor.submit(thread);
		}
	}

	public void stopAll() {
		for (final BrowserThread thread : threads) {
			thread.stop();
		}
		executor.shutdown();
	}

	public void waitForThreadsToStart() {
		while (true) {
			int executing = 0;
			for (final BrowserThread thread : threads) {
				if (thread.getState().isExecuting()) {
					executing++;
				}
			}
			logger.info(executing + " of " + threads.size() + " threads are executing");
			if (executing == threads.size()) {
				return;
			}
			try {
				sleep(500);
			} catch (final InterruptedException e) {
			}
		}
	}

	public void waitForThreadsToStop() {
		while (true) {
			Boolean isExecuting = FALSE;
			for (final BrowserThread thread : threads) {
				if (thread.getState().isExecuting()) {
					isExecuting = TRUE;
				}
			}
			if (isExecuting) {
				try {
					sleep(500);
				} catch (final InterruptedException e) {
				}
			} else {
				return;
			}
		}
	}

	public BrowserThreadPool withThreads(Integer threads) {
		executor = newFixedThreadPool(threads);
		return this;
	}
}
