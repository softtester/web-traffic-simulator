package se.softhouse.webtrafficsimulator;

import static se.softhouse.jargo.Arguments.stringArgument;
import static se.softhouse.jargo.CommandLineParser.withArguments;
import static se.softhouse.webtrafficsimulator.data.Settings.settings;
import se.softhouse.jargo.Argument;
import se.softhouse.jargo.ParsedArguments;
import se.softhouse.webtrafficsimulator.data.Settings;

public class WebTrafficSimulator {
	static Settings loadSettings(String[] args) {
		final Argument<String> url = stringArgument("-url").build();
		final ParsedArguments parsed = withArguments(url).parse(args);
		final String urlValue = parsed.get(url);
		return settings().withUrl(urlValue);
	}

	public static void main(String args[]) {
		loadSettings(args);
	}
}
