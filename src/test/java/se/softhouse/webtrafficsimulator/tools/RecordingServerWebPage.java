package se.softhouse.webtrafficsimulator.tools;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

public class RecordingServerWebPage {

	private final List<String> links = newArrayList();
	private final String path;

	public RecordingServerWebPage(String path) {
		this.path = path;
	}

	public List<String> getLinks() {
		return links;
	}

	public String getPath() {
		return path;
	}

	public void havingLinksTo(String... linksToPath) {
		this.links.addAll(newArrayList(linksToPath));
	}
}
