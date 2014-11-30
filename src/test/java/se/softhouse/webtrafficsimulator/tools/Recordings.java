package se.softhouse.webtrafficsimulator.tools;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

public class Recordings {
	private String browserInstance;
	private final List<String> targets = newArrayList();

	public String getBrowserInstance() {
		return browserInstance;
	}

	public List<String> getTargets() {
		return targets;
	}

	public void recordRequest(String target) {
		targets.add(target);
	}

	public void setBrowserInstance(String browserInstance) {
		this.browserInstance = browserInstance;
	}
}
