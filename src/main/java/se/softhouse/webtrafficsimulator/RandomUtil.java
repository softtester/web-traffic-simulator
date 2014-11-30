package se.softhouse.webtrafficsimulator;

import static java.lang.System.currentTimeMillis;
import static se.softhouse.webtrafficsimulator.WebTrafficSimulator.shutdown;

import java.util.List;
import java.util.Random;

public class RandomUtil {
	private static Random random;

	static {
		resetRandom();
	}

	public static Random createFakeRandom(List<Integer> fakeRandomValues) {
		return new Random() {
			private static final long serialVersionUID = 3990179688518818972L;
			int invokations = 0;

			@Override
			public int nextInt(int bound) {
				invokations++;
				if (invokations <= fakeRandomValues.size()) {
					return fakeRandomValues.get(invokations - 1);
				}
				shutdown();
				return super.nextInt(bound);
			}
		};
	}

	public static Random getRandom() {
		return random;
	}

	public static int random(int max) {
		return random.nextInt(max - 1);
	}

	public static void resetRandom() {
		random = new Random(currentTimeMillis());
	}

	public static void setRandom(Random random) {
		RandomUtil.random = random;
	}

	private RandomUtil() {
	}
}
