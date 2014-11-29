package se.softhouse.webtrafficsimulator.browser;

import java.net.MalformedURLException;
import java.net.URL;

public class BrowserState {
	public static BrowserState browserState() {
		return new BrowserState();
	}

	private boolean executing;
	private boolean started;
	private String url;

	private BrowserState() {
	}

	public boolean isStarted() {
		return started;
	}

	public String getUrl() {
		return url;
	}

	public boolean isExecuting() {
		return executing;
	}

	public BrowserState setExecuting(boolean executing) {
		this.executing = executing;
		return this;
	}

	public BrowserState setStarted(boolean started) {
		this.started = started;
		return this;
	}

	public BrowserState withUrl(String url) throws MalformedURLException {
		new URL(url); // Check if its valid
		this.url = url;
		return this;
	}
}
