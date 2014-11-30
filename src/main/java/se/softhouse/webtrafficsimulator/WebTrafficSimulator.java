package se.softhouse.webtrafficsimulator;

import static java.lang.Boolean.FALSE;
import static se.softhouse.jargo.Arguments.booleanArgument;
import static se.softhouse.jargo.Arguments.integerArgument;
import static se.softhouse.jargo.Arguments.stringArgument;
import static se.softhouse.jargo.CommandLineParser.withArguments;
import static se.softhouse.webtrafficsimulator.browser.BrowserState.browserState;
import static se.softhouse.webtrafficsimulator.browser.BrowserThread.browserThread;
import static se.softhouse.webtrafficsimulator.browser.BrowserThreadPool.browserThreadPool;
import static se.softhouse.webtrafficsimulator.data.Settings.settings;

import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.softhouse.jargo.Argument;
import se.softhouse.jargo.ParsedArguments;
import se.softhouse.webtrafficsimulator.browser.BrowserThreadPool;
import se.softhouse.webtrafficsimulator.data.Settings;

public class WebTrafficSimulator {
	private static final String BROWSER_CHROME = "Chrome";
	private static final String BROWSER_HTMLUNIT = "HtmlUnit";
	private static Logger logger = LoggerFactory.getLogger(WebTrafficSimulator.class);

	private static WebDriver getBrowser(final Settings settings) {
		WebDriver webDriver = null;
		switch (settings.getBrowser()) {
		case BROWSER_HTMLUNIT:
			webDriver = new HtmlUnitDriver();
			break;
		case BROWSER_CHROME:
			webDriver = new ChromeDriver();
			break;
		}
		if (webDriver == null) {
			throw new RuntimeException("No browser specified! Use -browser parameter.");
		}
		return webDriver;
	}

	static Settings loadSettings(String[] args) {
		final Argument<String> url = stringArgument("-url").required().build();
		final Argument<String> browser = stringArgument("-browser").defaultValue(BROWSER_HTMLUNIT).build();
		final Argument<Integer> threads = integerArgument("-threads").defaultValue(1).build();
		final Argument<Integer> sleepBetweenPages = integerArgument("-sleepBetweenPages").defaultValue(500).build();
		final Argument<Boolean> testMode = booleanArgument("-testMode").defaultValue(FALSE).hideFromUsage().build();
		final ParsedArguments parsed = withArguments(url, browser, threads, sleepBetweenPages, testMode).parse(args);
		return settings() //
				.withUrl(parsed.get(url)) //
				.withBrowser(parsed.get(browser)) //
				.withThreads(parsed.get(threads)) //
				.withTestMode(parsed.get(testMode)) //
				.withSleepBetweenPages(parsed.get(sleepBetweenPages));
	}

	public static void main(String args[]) throws InterruptedException, MalformedURLException {
		final Settings settings = loadSettings(args);
		logger.info("Starting crawler with settings:\n" + settings);
		BrowserThreadPool browserThreadPool = browserThreadPool() //
				.withThreads(settings.getThreads());
		for (int i = 0; i < settings.getThreads(); i++) {
			browserThreadPool = browserThreadPool.addBrowser(browserThread() //
					.withWebDriver(getBrowser(settings)) //
					.withSettings(settings) //
					.withState(browserState() //
							.withUrl(settings.getUrl())));
		}
		browserThreadPool.startAll();
		browserThreadPool.waitForThreadsToStart();
		browserThreadPool.stopAll();
	}
}
