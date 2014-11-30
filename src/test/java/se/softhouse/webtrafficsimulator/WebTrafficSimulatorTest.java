package se.softhouse.webtrafficsimulator;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.uniqueIndex;
import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Sets.newHashSet;
import static se.softhouse.webtrafficsimulator.RandomUtil.createFakeRandom;
import static se.softhouse.webtrafficsimulator.RandomUtil.resetRandom;
import static se.softhouse.webtrafficsimulator.RandomUtil.setRandom;
import static se.softhouse.webtrafficsimulator.WebTrafficSimulator.BROWSER_HTMLUNIT;
import static se.softhouse.webtrafficsimulator.WebTrafficSimulator.getBrowserThreadPool;
import static se.softhouse.webtrafficsimulator.WebTrafficSimulator.main;
import static se.softhouse.webtrafficsimulator.WebTrafficSimulator.shutdown;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
		shutdown();
		recordingServer.stop();
	}

	@Before
	public void before() throws Exception {
		resetRandom();
		recordingServer = new RecordingServer();
		recordingServer.withPage("/");
	}

	private Iterator<String> getAllTargets(Map<String, Recordings> recordings) {
		final Set<String> allTargets = newHashSet();
		for (final Recordings r : recordings.values()) {
			for (final String target : r.getTargets().keySet()) {
				allTargets.add(target);
			}
		}
		return allTargets.iterator();
	}

	@Test
	public void testThatALinkCanBeRandomlyClicked() throws Exception {
		recordingServer.withPage("/").havingLinksTo("/target1", "/target2");
		recordingServer.withPage("/target1").havingLinksTo("/", "/target2");
		recordingServer.withPage("/target2").havingLinksTo("/", "/target1");
		final String[] args = { "-url", recordingServer.getBaseUrl(), "-testMode", "true" };
		setRandom(createFakeRandom(newArrayList(0, 1)));
		main(args);
		// TODO: get rid of this!
		sleep(1000);
		assertThat(getAllTargets(recordingServer.getRecordings())).containsOnly("/", "/target1", "/target2");
	}

	@Test
	public void testThatThreadsCanBeSpecifiedOnCommandLine() throws Exception {
		final int threads = 10;
		final String[] args = { "-url", recordingServer.getBaseUrl(), "-testMode", "true", "-threads", threads + "" };
		main(args);
		getBrowserThreadPool().waitForThreadsToStart();
		shutdown();
		assertThat(recordingServer.getRecordings()).isNotNull().hasSize(threads);
		final Iterator<Recordings> itr = recordingServer.getRecordings().values().iterator();
		while (itr.hasNext()) {
			final Recordings recording = itr.next();
			assertThat(recording.getTargets()).containsOnlyKeys("/");
		}
		final Map<String, Recordings> browsers = uniqueIndex(recordingServer.getRecordings().values(),
				(Function<Recordings, String>) input -> input.getBrowserInstance());
		assertThat(browsers).as(
				"Expecting " + threads + " browsers to have been used since there are " + threads + " threads")
				.hasSize(threads);
	}

	@Test
	public void testThatURLAndBrowserCanBeSpecifiedOnCommandLine() throws Exception {
		final String[] args = { "-url", recordingServer.getBaseUrl(), "-browser", BROWSER_HTMLUNIT, "-testMode", "true" };
		main(args);
		getBrowserThreadPool().waitForThreadsToStart();
		shutdown();
		assertThat(getAllTargets(recordingServer.getRecordings())).containsOnly("/");
	}

	@Test
	public void testThatURLCanBeSpecifiedOnCommandLine() throws Exception {
		final String[] args = { "-url", recordingServer.getBaseUrl(), "-testMode", "true" };
		main(args);
		getBrowserThreadPool().waitForThreadsToStart();
		shutdown();
		assertThat(getAllTargets(recordingServer.getRecordings())).containsOnly("/");
	}
}
