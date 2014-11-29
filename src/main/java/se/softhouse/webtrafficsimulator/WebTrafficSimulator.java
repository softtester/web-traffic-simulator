package se.softhouse.webtrafficsimulator;

import static se.softhouse.jargo.Arguments.integerArgument;
import static se.softhouse.jargo.Arguments.stringArgument;
import static se.softhouse.jargo.CommandLineParser.withArguments;
import static se.softhouse.webtrafficsimulator.browser.BrowserState.browserState;
import static se.softhouse.webtrafficsimulator.browser.BrowserThread.browserThread;
import static se.softhouse.webtrafficsimulator.data.Settings.settings;

import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import se.softhouse.jargo.Argument;
import se.softhouse.jargo.ParsedArguments;
import se.softhouse.webtrafficsimulator.browser.BrowserThreadPool;
import se.softhouse.webtrafficsimulator.data.Settings;

public class WebTrafficSimulator {
	static Settings loadSettings(String[] args) {
		final Argument<String> url = stringArgument("-url").build();
		final Argument<String> browser = stringArgument("-browser").defaultValue("HtmlUnit").build();
		final Argument<Integer> threads = integerArgument("-threads").defaultValue(1).build();
		final ParsedArguments parsed = withArguments(url, browser, threads).parse(args);
		return settings().withUrl(parsed.get(url)).withBrowser(parsed.get(browser)).withThreads(parsed.get(threads));
	}

	public static void main(String args[]) throws InterruptedException, MalformedURLException {
		final Settings settings = loadSettings(args);
		System.out.println("Starting crawler with settings:\n" + settings);
		WebDriver webDriver = null;
		if (settings.getBrowser().equals("HtmlUnit")) {
			webDriver = new HtmlUnitDriver();
		}
		if (webDriver == null) {
			throw new RuntimeException("No browser specified! Use -browser parameter.");
		}
		final BrowserThreadPool browserThreadPool = new BrowserThreadPool();
		browserThreadPool.withThreads(settings.getThreads()).addBrowser(
				browserThread().withWebDriver(webDriver).withState(browserState().withUrl(settings.getUrl())));
		browserThreadPool.startAll();
		browserThreadPool.waitForThreadsToStart();
		browserThreadPool.stopAll();
	}
}
