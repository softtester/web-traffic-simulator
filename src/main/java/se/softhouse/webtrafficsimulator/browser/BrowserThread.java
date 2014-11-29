package se.softhouse.webtrafficsimulator.browser;

import static java.lang.Thread.currentThread;
import static org.openqa.selenium.By.tagName;

import java.util.List;
import java.util.concurrent.Callable;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class BrowserThread implements Callable<Boolean> {

	public static BrowserThread browserThread() {
		return new BrowserThread();
	}

	private BrowserState state;
	private WebDriver webDriver;

	private BrowserThread() {
	}

	@Override
	public Boolean call() throws Exception {
		System.out.println(currentThread().getName() + " " + this + " Started");
		webDriver.get(state.getUrl());
		try {
			while (state.isStarted()) {
				state.setExecuting(true);
				try {
					System.out.println(currentThread().getName() + " " + this + " is at " + webDriver.getCurrentUrl()
							+ " " + webDriver.getTitle());
					final List<WebElement> aElements = webDriver.findElements(tagName("a"));
					for (final WebElement aElement : aElements) {
						aElement.getAttribute("href");
					}
				} catch (final Exception e) {
					state.setStarted(false);
					e.printStackTrace();
				}
			}
		} finally {
			webDriver.quit();
			System.out.println(currentThread().getName() + " " + this + " Stopped");
		}
		state.setExecuting(false);
		return true;
	}

	public BrowserState getState() {
		return state;
	}

	public WebDriver getWebDriver() {
		return webDriver;
	}

	public BrowserThread start() {
		System.out.println(this + " Starting");
		state.setStarted(true);
		return this;
	}

	public BrowserThread stop() {
		System.out.println(this + " Stopping");
		state.setStarted(false);
		return this;
	}

	@Override
	public String toString() {
		return this.webDriver.getClass().getSimpleName() + " " + state.getUrl();
	}

	public BrowserThread withState(BrowserState state) {
		this.state = state;
		return this;
	}

	public BrowserThread withWebDriver(WebDriver webDriver) {
		this.webDriver = webDriver;
		return this;
	}
}
