package se.softhouse.webtrafficsimulator.tools;

import java.util.List;

import org.eclipse.jetty.server.Server;

public class RecordingServer {
	private static final Integer PORT = 8888;
	private final RecordingHandler recordingHandler;
	private final Server server;

	public RecordingServer() throws Exception {
		this.server = new Server(PORT);
		recordingHandler = new RecordingHandler();
		server.setHandler(recordingHandler);
		server.start();
	}

	public Integer getPort() {
		return PORT;
	}

	public List<String> getRequestedUrls() {
		return recordingHandler.getRequestedUrls();
	}

	public void stop() throws Exception {
		server.stop();
	}
}
