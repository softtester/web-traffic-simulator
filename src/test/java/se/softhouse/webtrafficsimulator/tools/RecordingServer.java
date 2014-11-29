package se.softhouse.webtrafficsimulator.tools;

import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.eclipse.jetty.server.Server;

public class RecordingServer {
	private final List<String> getRequestedUrls = newArrayList();
	private final Server server;

	public RecordingServer() throws Exception {
		this.server = new Server();
		server.start();
	}

	public Integer getPort() {
		return server.getURI().getPort();
	}

	public List<String> getRequestedUrls() {
		return getRequestedUrls;
	}

	public void stop() throws Exception {
		server.stop();
	}
}
