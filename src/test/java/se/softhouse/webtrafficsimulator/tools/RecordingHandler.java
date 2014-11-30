package se.softhouse.webtrafficsimulator.tools;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.assertj.core.util.Maps.newHashMap;
import static se.softhouse.webtrafficsimulator.browser.BrowserThread.BROWSER_INSTANCE;
import static se.softhouse.webtrafficsimulator.browser.BrowserThread.THREAD_NAME;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecordingHandler extends AbstractHandler {
	private final Logger logger = LoggerFactory.getLogger(RecordingHandler.class);

	private final Map<String, Recordings> recordings = newHashMap();

	public Map<String, Recordings> getRecordings() {
		return recordings;
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		final String threadName = request.getParameter(THREAD_NAME);
		final String browserInstance = request.getParameter(BROWSER_INSTANCE);
		if (threadName == null) {
			throw new RuntimeException(THREAD_NAME + " was null, use -testMode true in test cases!");
		}
		if (!recordings.containsKey(threadName)) {
			recordings.put(threadName, new Recordings());
		}
		recordings.get(threadName).recordRequest(target);
		recordings.get(threadName).setBrowserInstance(browserInstance);
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(SC_OK);
		baseRequest.setHandled(true);
		response.getWriter().println("Recorded");
		logger.info("Responded to " + target);
	}
}
