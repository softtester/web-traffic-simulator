package se.softhouse.webtrafficsimulator.browser;

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

	public BrowserState withUrl(String url) {
		this.url = url;
		return this;
	}
}
