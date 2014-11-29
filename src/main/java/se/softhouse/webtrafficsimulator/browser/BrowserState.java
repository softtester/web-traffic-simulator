package se.softhouse.webtrafficsimulator.browser;

import java.net.MalformedURLException;
import java.net.URL;

public class BrowserState {
	public static BrowserState browserState() {
		return new BrowserState();
	}

	private String url;

	private BrowserState() {
	}

	public String getUrl() {
		return url;
	}

	public BrowserState withUrl(String url) throws MalformedURLException {
		new URL(url); // Check if its valid
		this.url = url;
		return this;
	}
}
