package se.softhouse.webtrafficsimulator;

import static com.google.common.collect.Maps.toMap;
import static com.google.common.collect.Maps.uniqueIndex;
import static org.assertj.core.api.Assertions.assertThat;

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
		final int threads = 10;
		final String[] args = { "-url", recordingServer.getBaseUrl(), "-testMode", "true", "-threads", threads + "" };
		WebTrafficSimulator.main(args);
		assertThat(recordingServer.getRecordings()).isNotNull().hasSize(threads);
		final Iterator<Recordings> itr = recordingServer.getRecordings().values().iterator();
		final Recordings firstRecording = itr.next();
		assertThat(toMap(firstRecording.getTargets(), (Function<String, String>) it -> it)).containsOnlyKeys("/");
		final Recordings secondRecording = itr.next();
		assertThat(toMap(secondRecording.getTargets(), (Function<String, String>) it -> it)).containsOnlyKeys("/");
		final Map<String, Recordings> browsers = uniqueIndex(recordingServer.getRecordings().values(),
				(Function<Recordings, String>) input -> input.getBrowserInstance());
		assertThat(browsers).as(
				"Expecting " + threads + " browsers to have been used since there are " + threads + " threads")
				.hasSize(threads);
	}

	@Test
	public void testThatURLAndBrowserCanBeSpecifiedOnCommandLine() throws Exception {
		final String[] args = { "-url", recordingServer.getBaseUrl(), "-browser", "HtmlUnit", "-testMode", "true" };
		WebTrafficSimulator.main(args);
		assertThat(recordingServer.getRecordings()).isNotNull().hasSize(1);
		final Recordings firstRecording = recordingServer.getRecordings().values().iterator().next();
		assertThat(toMap(firstRecording.getTargets(), (Function<String, String>) it -> it)).containsOnlyKeys("/");
	}

	@Test
	public void testThatURLCanBeSpecifiedOnCommandLine() throws Exception {
		final String[] args = { "-url", recordingServer.getBaseUrl(), "-testMode", "true" };
		WebTrafficSimulator.main(args);
		assertThat(recordingServer.getRecordings()).isNotNull().hasSize(1);
		final Recordings firstRecording = recordingServer.getRecordings().values().iterator().next();
		assertThat(toMap(firstRecording.getTargets(), (Function<String, String>) it -> it)).containsOnlyKeys("/");
	}
}
