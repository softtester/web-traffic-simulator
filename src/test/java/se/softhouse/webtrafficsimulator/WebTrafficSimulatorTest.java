package se.softhouse.webtrafficsimulator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.softhouse.webtrafficsimulator.tools.RecordingServer;

public class WebTrafficSimulatorTest {
	private RecordingServer recordingServer;

	@After
	public void after() throws Exception {
		recordingServer.stop();
	}

	@Before
	public void before() throws Exception {
		recordingServer = new RecordingServer();
	}

	@Test
	public void testThatURLAndBrowserCanBeSpecifiedOnCommandLine() throws Exception {
		final String url = "http://localhost:" + recordingServer.getPort() + "/testsite";
		final String[] args = { "-url", url, "-browser", "HtmlUnit" };
		WebTrafficSimulator.main(args);
		assertThat(recordingServer.getRequestedUrls()).isNotNull().isEqualTo(newArrayList("/testsite"));
	}

	@Test
	public void testThatURLCanBeSpecifiedOnCommandLine() throws Exception {
		final String url = "http://localhost:" + recordingServer.getPort() + "/testsite";
		final String[] args = { "-url", url };
		WebTrafficSimulator.main(args);
		assertThat(recordingServer.getRequestedUrls()).isNotNull().isEqualTo(newArrayList("/testsite"));
	}
}
