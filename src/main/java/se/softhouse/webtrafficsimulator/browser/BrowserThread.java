package se.softhouse.webtrafficsimulator.browser;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class BrowserThread implements Callable<Boolean> {

	private BrowserState state;
	private final WebDriver webDriver;
	
	private final ExecutorService executor = Executors.newFixedThreadPool(1);
	private boolean executing = false;

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
		executor.submit(this);
		System.out.println("Started thread");
		return this;
	}

	public BrowserThread stop() {
		executing = false;
		return this;
	}

	public BrowserThread withState(BrowserState state) {
		this.state = state;
		return this;
	}

	public Boolean call() throws Exception {
		webDriver.get(state.getUrl());
		executing = true;
		System.out.println("asdasdasd");
		try {
			while (executing) {
				try {
					System.out.println("asdasd");
					System.out.println(webDriver.getTitle());
					List<WebElement> aElements = webDriver.findElements(By.tagName("a"));
					for (WebElement aElement : aElements) {
						System.out.println(aElement.getText());
					}
					String source = webDriver.getPageSource();
					//System.out.println(source);
					Thread.sleep(1000);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		} finally {
			System.out.println("QUITING now");
			webDriver.quit();
			executor.shutdown();
		}
		return true;
	}
}
