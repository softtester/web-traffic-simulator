package se.softhouse.webtrafficsimulator.browser;

import org.openqa.selenium.WebDriver;

public class BrowserThread {

	private BrowserState state;
	private final WebDriver webDriver;

	public BrowserThread(WebDriver webDriver) {
		this.webDriver = webDriver;
	}

	public BrowserState getState() {
		return state;
	}

	public WebDriver getWebDriver() {
		return webDriver;
	}

	public BrowserThread start() {
		webDriver.get(state.getUrl());
		return this;
	}

	public BrowserThread stop() {
		webDriver.quit();
		return this;
	}

	public BrowserThread withState(BrowserState state) {
		this.state = state;
		return this;
	}
}
