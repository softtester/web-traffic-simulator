package se.softhouse.webtrafficsimulator.browser;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

public class BrowserThreadPool {
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
		}
	}

	public void stopAll() {
		for (final BrowserThread thread : threads) {
			thread.stop();
		}
	}
}
