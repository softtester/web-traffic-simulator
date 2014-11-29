package se.softhouse.webtrafficsimulator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

import org.junit.Test;

import se.softhouse.webtrafficsimulator.tools.RecordingServer;

public class WebTrafficSimulatorTest {
	@Test
	public void testThatURLAndBrowserCanBeSpecifiedOnCommandLine() throws Exception {
		final RecordingServer recordingServer = new RecordingServer();
		final String[] args = { "-url", "http://localhost:" + recordingServer.getPort() + "/", "--browser", "PhantomJS" };
		WebTrafficSimulator.main(args);
		assertThat(recordingServer.getRequestedUrls()).isNotNull().isEqualTo(newArrayList("http://github.com/"));
		recordingServer.stop();
	}
}
