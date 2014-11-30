package se.softhouse.webtrafficsimulator.tools;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

public class Recordings {
	private String browserInstance;
	private final Map<String, Integer> targets = newHashMap();

	public String getBrowserInstance() {
		return browserInstance;
	}

	public Map<String, Integer> getTargets() {
		return targets;
	}

	public void recordRequest(String target) {
		if (!targets.containsKey(target)) {
			targets.put(target, 1);
		} else {
			targets.put(target, targets.get(target) + 1);
		}
	}

	public void setBrowserInstance(String browserInstance) {
		this.browserInstance = browserInstance;
	}
}
