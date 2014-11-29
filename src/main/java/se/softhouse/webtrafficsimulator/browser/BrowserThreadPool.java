package se.softhouse.webtrafficsimulator.browser;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.Thread.sleep;
import static java.util.concurrent.Executors.newFixedThreadPool;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class BrowserThreadPool {
	private ExecutorService executor;

	private final List<BrowserThread> threads = newArrayList();

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
		System.out.println();
		while (true) {
			int executing = 0;
			for (final BrowserThread thread : threads) {
				if (thread.getState().isExecuting()) {
					executing++;
				}
			}
			System.out.println(executing + " of " + threads.size() + " executing");
			if (executing == threads.size()) {
				return;
			}
			try {
				sleep(100);
			} catch (final InterruptedException e) {
			}
		}
	}

	public BrowserThreadPool withThreads(Integer threads) {
		executor = newFixedThreadPool(threads);
		return this;
	}
}
