package gambler.tools.cli.util;

import java.util.ArrayList;
import java.util.List;

public enum Ratio {

	ONE_EIGHTH(0, "1/8", 1.0f / 8), ONE_FOURTH(10, "1/4", 1.0f / 4), ONE_THIRD(20, "1/3", 1.0f / 3), THREE_EIGHTH(0,
			"3/8", 3.0f / 8), ZERO_382(30, "0.382", 0.382f), HALF(20, "1/2", 1.0f / 2), ZERO_618(30, "0.618",
					0.618f), FIVE_EIGHTH(0, "5/8", 5.0F / 8), TWO_THIRD(20, "2/3", 2.0f / 3), THREE_FOURTH(10, "3/4",
							3.0f / 4), SEVEN_EIGHTH(0, "7/8", 7.0f / 8), ONE(20, "1", 1.0f), GLODEN_ONE(30, "1", 1.0f);
	/**
	 * 0: LOW 10: NORMAL 20: HIGH, 30: GLODEN
	 */
	private int level = 0;

	private String name;

	private float ratio;

	public static final int GLODEN_LEVEL = 30;

	private Ratio(int level, String name, float ratio) {
		this.level = level;
		this.name = name;
		this.ratio = ratio;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getRatio() {
		return ratio;
	}

	public void setRatio(float ratio) {
		this.ratio = ratio;
	}

	public static Ratio[] allRatios() {
		return Ratio.values();
	}

	public static Ratio[] allGlodenRatios() {
		List<Ratio> ratios = new ArrayList<Ratio>();
		for (Ratio ratio : Ratio.values()) {
			if (ratio.getLevel() == GLODEN_LEVEL) {
				ratios.add(ratio);
			}
		}
		return ratios.toArray(new Ratio[0]);
	}

	public static Ratio[] equalsLevel(int level) {
		List<Ratio> ratios = new ArrayList<Ratio>();
		for (Ratio ratio : Ratio.values()) {
			if (ratio.getLevel() == level) {
				ratios.add(ratio);
			}
		}
		return ratios.toArray(new Ratio[0]);
	}

	public static Ratio[] greaterThanLevel(int level) {
		List<Ratio> ratios = new ArrayList<Ratio>();
		for (Ratio ratio : Ratio.values()) {
			if (ratio.getLevel() > level) {
				ratios.add(ratio);
			}
		}
		return ratios.toArray(new Ratio[0]);
	}

	public static Ratio[] greaterEqualsLevel(int level) {
		List<Ratio> ratios = new ArrayList<Ratio>();
		for (Ratio ratio : Ratio.values()) {
			if (ratio.getLevel() >= level) {
				ratios.add(ratio);
			}
		}
		return ratios.toArray(new Ratio[0]);
	}

	public static Ratio[] lessThanLevel(int level) {
		List<Ratio> ratios = new ArrayList<Ratio>();
		for (Ratio ratio : Ratio.values()) {
			if (ratio.getLevel() < level) {
				ratios.add(ratio);
			}
		}
		return ratios.toArray(new Ratio[0]);
	}

	public static Ratio[] lessEqualsLevel(int level) {
		List<Ratio> ratios = new ArrayList<Ratio>();
		for (Ratio ratio : Ratio.values()) {
			if (ratio.getLevel() <= level) {
				ratios.add(ratio);
			}
		}
		return ratios.toArray(new Ratio[0]);
	}

}
