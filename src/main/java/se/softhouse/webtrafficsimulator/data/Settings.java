package se.softhouse.webtrafficsimulator.data;

public class Settings {

	public static Settings settings() {
		return new Settings();
	}

	private String browser;
	private Integer threads;
	private String url;

	private Settings() {
	}

	public String getBrowser() {
		return browser;
	}

	public Integer getThreads() {
		return threads;
	}

	public String getUrl() {
		return url;
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

	public Settings withThreads(Integer threads) {
		this.threads = threads;
		return this;
	}

	public Settings withUrl(String urlValue) {
		this.url = urlValue;
		return this;
	}
}
