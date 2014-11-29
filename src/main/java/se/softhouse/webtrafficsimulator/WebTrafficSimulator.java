package se.softhouse.webtrafficsimulator;

import static se.softhouse.jargo.Arguments.stringArgument;
import static se.softhouse.jargo.CommandLineParser.withArguments;
import static se.softhouse.webtrafficsimulator.browser.BrowserState.browserState;
import static se.softhouse.webtrafficsimulator.data.Settings.settings;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import se.softhouse.jargo.Argument;
import se.softhouse.jargo.ParsedArguments;
import se.softhouse.webtrafficsimulator.browser.BrowserThread;
import se.softhouse.webtrafficsimulator.browser.BrowserThreadPool;
import se.softhouse.webtrafficsimulator.data.Settings;

public class WebTrafficSimulator {
	static Settings loadSettings(String[] args) {
		final Argument<String> url = stringArgument("-url").build();
		final ParsedArguments parsed = withArguments(url).parse(args);
		final String urlValue = parsed.get(url);
		return settings().withUrl(urlValue);
	}

	public static void main(String args[]) throws InterruptedException {
		final Settings settings = loadSettings(args);
		System.out.println("Starting crawler with settings:\n" + settings);
		final WebDriver webDriver = new PhantomJSDriver();
		final BrowserThreadPool browserThreadPool = new BrowserThreadPool();
		browserThreadPool.addBrowser(new BrowserThread(webDriver).withState(browserState().withUrl(settings.getUrl())));
		browserThreadPool.startAll();
		browserThreadPool.stopAll();
	}
}
