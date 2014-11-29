package se.softhouse.webtrafficsimulator.tools;

import static org.assertj.core.util.Lists.newArrayList;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class RecordingHandler extends AbstractHandler {
	private final List<String> requestedUrls = newArrayList();

	public List<String> getRequestedUrls() {
		return requestedUrls;
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		requestedUrls.add(target.trim());
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		response.getWriter().println("Recorded");
		System.out.println(this.getClass().getName() + " " + target);
	}
}
