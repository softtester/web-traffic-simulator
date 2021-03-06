package se.softhouse.webtrafficsimulator;

import static java.lang.Boolean.FALSE;
import static java.lang.Runtime.getRuntime;
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
	static final String BROWSER_CHROME = "Chrome";
	static final String BROWSER_HTMLUNIT = "HtmlUnit";
	private static BrowserThreadPool browserThreadPool;
	private static Logger logger = LoggerFactory.getLogger(WebTrafficSimulator.class);
	private static final String PARAM_BROWSER = "-browser";

	private static WebDriver getBrowser(final Settings settings) {
		switch (settings.getBrowser()) {
		case BROWSER_HTMLUNIT:
			return new HtmlUnitDriver();
		case BROWSER_CHROME:
			return new ChromeDriver();
		}
		throw new RuntimeException("No browser specified! Use " + PARAM_BROWSER + " parameter.");
	}

	static BrowserThreadPool getBrowserThreadPool() {
		return browserThreadPool;
	}

	static Settings loadSettings(String[] args) {
		final Argument<String> url = stringArgument("-url").required().build();
		final Argument<String> browser = stringArgument(PARAM_BROWSER).defaultValue(BROWSER_HTMLUNIT).build();
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
		browserThreadPool = browserThreadPool() //
				.withThreads(settings.getThreads());
		for (int i = 0; i < settings.getThreads(); i++) {
			browserThreadPool = browserThreadPool.addBrowser(browserThread() //
					.withWebDriver(getBrowser(settings)) //
					.withSettings(settings) //
					.withState(browserState() //
							.withUrl(settings.getUrl())));
		}
		setupShutDownHook();
		browserThreadPool.startAll();
	}

	private static void setupShutDownHook() {
		getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				shutdown();
			}
		});
	}

	static void shutdown() {
		browserThreadPool.stopAll();
	}
}
