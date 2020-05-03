package com.skanderj.g3.util;

import java.awt.Color;
import java.util.Random;

public final class Utilities {
	private static final Random random = new Random();

	private Utilities() {
		return;
	}

	public static final int randomInteger(int a, int b) {
		if (Math.min(a, b) < 0) {
			return -random.nextInt(Math.abs(Math.min(a, b)) + 1) + random.nextInt(Math.max(a, b) + 1);
		} else {
			return Math.min(a, b) + Utilities.random.nextInt(Math.max(a, b));
		}
	}

	public static final Color randomColor(boolean useAlpha) {
		return new Color(Utilities.random.nextInt(255), Utilities.random.nextInt(255), Utilities.random.nextInt(255), useAlpha ? Utilities.random.nextInt(255) : 255);
	}

	public static final float map(float value, float valueMin, float valueMax, float targetMin, float targetMax, boolean withinBounds) {
		float newval = (((value - valueMin) / (valueMax - valueMin)) * (targetMax - targetMin)) + targetMin;
		if (!withinBounds) {
			return newval;
		}
		if (targetMin < targetMax) {
			return Utilities.constrain(newval, targetMin, targetMax);
		} else {
			return Utilities.constrain(newval, targetMax, targetMin);
		}
	}

	private static float constrain(float value, float minimum, float maximum) {
		return Math.max(Math.min(value, maximum), minimum);
	}
}
