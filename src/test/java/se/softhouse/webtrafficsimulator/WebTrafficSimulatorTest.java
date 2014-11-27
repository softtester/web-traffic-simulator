package se.softhouse.webtrafficsimulator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import se.softhouse.webtrafficsimulator.data.Settings;

public class WebTrafficSimulatorTest {
	@Test
	public void testThatURLCanBeSpecifiedOnCommandLine() {
		final String[] args = { "-url", "http://github.com/" };
		final Settings settings = WebTrafficSimulator.loadSettings(args);
		assertThat(settings.getUrl()).isNotNull().isEqualTo("http://github.com/");
	}
}
