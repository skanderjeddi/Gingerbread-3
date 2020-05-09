package com.skanderj.g3.util;

import java.awt.Color;
import java.util.Random;

/**
 * Helper class for basically anything that doesn't fit somewhere else.
 * 
 * @author Skander
 *
 */
public final class Utilities {
	private static final Random random = new Random();

	private Utilities() {
		return;
	}

	/**
	 * Returns a random integer between a and b - included. Why doesn't Java have
	 * this????
	 */
	public static final int randomInteger(int a, int b) {
		if (Math.min(a, b) < 0) {
			return -Utilities.random.nextInt(Math.abs(Math.min(a, b)) + 1) + Utilities.random.nextInt(Math.max(a, b) + 1);
		} else {
			return Math.min(a, b) + Utilities.random.nextInt(Math.max(a, b));
		}
	}

	/**
	 * Returns a random color. If useAlpha, color will have a random transparency.
	 */
	public static final Color randomColor(boolean useAlpha) {
		return new Color(Utilities.random.nextInt(255), Utilities.random.nextInt(255), Utilities.random.nextInt(255), useAlpha ? Utilities.random.nextInt(255) : 255);
	}

	/**
	 * I invite you to read the p5js documentation for this beautiful function.
	 */
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

	/**
	 * Used for map()
	 */
	private static float constrain(float value, float minimum, float maximum) {
		return Math.max(Math.min(value, maximum), minimum);
	}
}
