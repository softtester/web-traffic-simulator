package se.softhouse.webtrafficsimulator;

import static com.google.common.collect.Maps.uniqueIndex;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.Iterator;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.softhouse.webtrafficsimulator.tools.RecordingServer;
import se.softhouse.webtrafficsimulator.tools.Recordings;

import com.google.common.base.Function;

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
	public void testThatThreadsCanBeSpecifiedOnCommandLine() throws Exception {
		final String url = "http://localhost:" + recordingServer.getPort() + "/testsite";
		final int threads = 2;
		final String[] args = { "-url", url, "-testMode", "true", "-threads", threads + "" };
		WebTrafficSimulator.main(args);
		assertThat(recordingServer.getRecordings()).isNotNull().hasSize(2);
		final Iterator<Recordings> itr = recordingServer.getRecordings().values().iterator();
		final Recordings firstRecording = itr.next();
		assertThat(firstRecording.getTargets()).isEqualTo(newArrayList("/testsite"));
		final Recordings secondRecording = itr.next();
		assertThat(secondRecording.getTargets()).isEqualTo(newArrayList("/testsite"));
		final Map<String, Recordings> browsers = uniqueIndex(recordingServer.getRecordings().values(),
				(Function<Recordings, String>) input -> input.getBrowserInstance());
		assertThat(browsers).as(
				"Expecting " + threads + " browsers to have been used since there are " + threads + " threads")
				.hasSize(threads);
	}

	@Test
	public void testThatURLAndBrowserCanBeSpecifiedOnCommandLine() throws Exception {
		final String url = "http://localhost:" + recordingServer.getPort() + "/testsite";
		final String[] args = { "-url", url, "-browser", "HtmlUnit", "-testMode", "true" };
		WebTrafficSimulator.main(args);
		assertThat(recordingServer.getRecordings()).isNotNull().hasSize(1);
		final Recordings firstRecording = recordingServer.getRecordings().values().iterator().next();
		assertThat(firstRecording.getTargets()).isEqualTo(newArrayList("/testsite"));
	}

	@Test
	public void testThatURLCanBeSpecifiedOnCommandLine() throws Exception {
		final String url = "http://localhost:" + recordingServer.getPort() + "/testsite";
		final String[] args = { "-url", url, "-testMode", "true" };
		WebTrafficSimulator.main(args);
		assertThat(recordingServer.getRecordings()).isNotNull().hasSize(1);
		final Recordings firstRecording = recordingServer.getRecordings().values().iterator().next();
		assertThat(firstRecording.getTargets()).isEqualTo(newArrayList("/testsite"));
	}
}
