package se.softhouse.webtrafficsimulator.data;

import static com.google.common.base.MoreObjects.toStringHelper;

public class Settings {

	public static Settings settings() {
		return new Settings();
	}

	private String browser;
	private String url;

	private Settings() {
	}

	public String getBrowser() {
		return browser;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return toStringHelper(this).omitNullValues().toString();
	}

	public Settings withBrowser(String browser) {
		this.browser = browser;
		return this;
	}

	public Settings withUrl(String urlValue) {
		this.url = urlValue;
		return this;
	}
}
