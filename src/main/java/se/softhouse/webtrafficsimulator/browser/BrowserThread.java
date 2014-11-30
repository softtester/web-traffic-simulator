package se.softhouse.webtrafficsimulator.browser;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static org.openqa.selenium.By.tagName;

import java.util.List;
import java.util.concurrent.Callable;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.softhouse.webtrafficsimulator.data.Settings;

public class BrowserThread implements Callable<Boolean> {
	public static final String BROWSER_INSTANCE = "browser_instance";

	public static final String THREAD_NAME = "thread_name";

	public static BrowserThread browserThread() {
		return new BrowserThread();
	}

	private final Logger logger = LoggerFactory.getLogger(BrowserThread.class);

	private Settings settings;
	private BrowserState state;
	private WebDriver webDriver;

	private BrowserThread() {
	}

	private String appendExtras(String url) {
		if (!settings.isTestMode()) {
			return url;
		}
		url = appendParam(url, THREAD_NAME, Thread.currentThread().getName());
		url = appendParam(url, BROWSER_INSTANCE, webDriver.hashCode() + "");
		return url;
	}

	private String appendParam(String url, String param, String value) {
		if (url.contains("?")) {
			url = url + "&";
		} else {
			url = url + "?";
		}
		return url += param + "=" + value;
	}

	@Override
	public Boolean call() throws Exception {
		logger.info(currentThread().getName() + " " + this + " Started");
		webDriver.get(appendExtras(state.getUrl()));
		try {
			while (state.isStarted()) {
				state.setExecuting(true);
				try {
					logger.info(currentThread().getName() + " " + this + " is at " + webDriver.getCurrentUrl() + " "
							+ webDriver.getTitle());
					final List<WebElement> aElements = webDriver.findElements(tagName("a"));
					for (final WebElement aElement : aElements) {
						aElement.getAttribute("href");
					}
				} catch (final Exception e) {
					state.setStarted(false);
					e.printStackTrace();
				}
				sleep(settings.getSleepBetweenPages());
			}
		} finally {
			webDriver.quit();
			logger.info(currentThread().getName() + " " + this + " Stopped");
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
		logger.info(this + " Starting");
		state.setStarted(true);
		return this;
	}

	public BrowserThread stop() {
		logger.info(this + " Stopping");
		state.setStarted(false);
		return this;
	}

	@Override
	public String toString() {
		return this.webDriver.getClass().getSimpleName() + " " + state.getUrl();
	}

	public BrowserThread withSettings(Settings settings) {
		this.settings = settings;
		return this;
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
