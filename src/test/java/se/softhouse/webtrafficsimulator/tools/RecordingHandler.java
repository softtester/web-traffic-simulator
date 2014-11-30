package se.softhouse.webtrafficsimulator.tools;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
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

	private final Map<String, RecordingServerWebPage> webPages = newHashMap();

	public Map<String, Recordings> getRecordings() {
		return recordings;
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String threadName = request.getParameter(THREAD_NAME);
		final String browserInstance = request.getParameter(BROWSER_INSTANCE);
		if (threadName == null) {
			threadName = "Unknown";
		}
		if (!recordings.containsKey(threadName)) {
			recordings.put(threadName, new Recordings());
		}
		recordings.get(threadName).recordRequest(target);
		recordings.get(threadName).setBrowserInstance(browserInstance);
		response.setContentType("text/html;charset=utf-8");
		if (webPages.containsKey(target)) {
			response.setStatus(SC_OK);
			baseRequest.setHandled(true);
			final String renderedContent = renderPage(webPages.get(target));
			logger.info("Responding to 123 " + target + " with:");
			logger.info(renderedContent);
			response.getWriter().println(renderedContent);
		} else {
			response.setStatus(SC_NOT_FOUND);
			baseRequest.setHandled(true);
		}
		logger.info("Responded to " + target);
	}

	private String renderPage(RecordingServerWebPage recordingServerWebPage) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<html>\n");
		sb.append("<head>\n");
		sb.append("<title>" + recordingServerWebPage.getPath() + "</title>\n");
		sb.append("<body>\n");
		for (final String linkTo : recordingServerWebPage.getLinks()) {
			sb.append("<a href=\"" + linkTo + "\">" + linkTo + "</a><br>\n");
		}
		sb.append("</body>\n");
		sb.append("<html>\n");
		return sb.toString();
	}

	public RecordingServerWebPage withPage(String path) {
		final RecordingServerWebPage webPage = new RecordingServerWebPage(path);
		webPages.put(path, webPage);
		return webPage;
	}
}
