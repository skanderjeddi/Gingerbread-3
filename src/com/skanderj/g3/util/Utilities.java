package com.skanderj.g3.util;

public final class Utilities {
	private Utilities() {
		return;
	}

	public static final float map(float value, float valueMin, float valueMax, float targetMin, float targetMax, boolean withinBounds) {
		float newval = (value - valueMin) / (valueMax - valueMin) * (targetMax - targetMin) + targetMin;
		if (!withinBounds) {
			return newval;
		}
		if (targetMin < targetMax) {
			return constrain(newval, targetMin, targetMax);
		} else {
			return constrain(newval, targetMax, targetMin);
		}
	}

	private static float constrain(float value, float minimum, float maximum) {
		return Math.max(Math.min(value, maximum), minimum);
	}
}
