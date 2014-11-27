package se.softhouse.webtrafficsimulator.data;

public class Settings {

	public static Settings settings() {
		return new Settings();
	}

	private String url;

	private Settings() {
	}

	public String getUrl() {
		return url;
	}

	public Settings withUrl(String urlValue) {
		this.url = urlValue;
		return this;
	}
}
