package se.softhouse.webtrafficsimulator.tools;

import java.util.Map;

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

	public String getBaseUrl() {
		return "http://localhost:" + getPort() + "/";
	}

	public Integer getPort() {
		return PORT;
	}

	public Map<String, Recordings> getRecordingClients() {
		return recordingHandler.getRecordedClients();
	}

	public void stop() throws Exception {
		server.stop();
	}

	public RecordingServerWebPage withPage(String path) {
		return recordingHandler.withPage(path);
	}
}
