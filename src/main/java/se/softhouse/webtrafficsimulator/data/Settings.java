package se.softhouse.webtrafficsimulator.data;

public class Settings {

	public static Settings settings() {
		return new Settings();
	}

	private String browser;
	private Integer sleepBetweenPages;
	private Boolean testMode;
	private Integer threads;
	private String url;

	private Settings() {
	}

	public String getBrowser() {
		return browser;
	}

	public Integer getSleepBetweenPages() {
		return sleepBetweenPages;
	}

	public Integer getThreads() {
		return threads;
	}

	public String getUrl() {
		return url;
	}

	public Boolean isTestMode() {
		return testMode;
	}

	@Override
	public String toString() {
		return "Browser: " + browser + "\n" //
				+ "Url: " + url + "\n";
	}

	public Settings withBrowser(String browser) {
		this.browser = browser;
		return this;
	}

	public Settings withSleepBetweenPages(Integer ms) {
		this.sleepBetweenPages = ms;
		return this;
	}

	public Settings withTestMode(Boolean testMode) {
		this.testMode = testMode;
		return this;
	}

	public Settings withThreads(Integer threads) {
		this.threads = threads;
		return this;
	}

	public Settings withUrl(String url) {
		this.url = url;
		return this;
	}
}
