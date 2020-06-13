package com.skanderj.gingerbread3.util;

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
	 * this???? --- BUGGY!
	 */
	public static int randomInteger(final int a, final int b) {
		if (Math.min(a, b) < 0) {
			return -Utilities.random.nextInt(Math.abs(Math.min(a, b)) + 1) + Utilities.random.nextInt(Math.abs(Math.max(a, b)) + 1);
		} else {
			return Math.min(a, b) + Utilities.random.nextInt(Math.max(a, b));
		}
	}

	/**
	 * Returns a random color. If useAlpha, color will have a random transparency.
	 */
	public static Color randomColor(final boolean useAlpha) {
		return new Color(Utilities.random.nextInt(255), Utilities.random.nextInt(255), Utilities.random.nextInt(255), useAlpha ? Utilities.random.nextInt(255) : 255);
	}

	/**
	 * I invite you to read the p5js documentation for this beautiful function.
	 */
	public static float map(final float value, final float valueMin, final float valueMax, final float targetMin, final float targetMax, final boolean withinBounds) {
		final float newval = (((value - valueMin) / (valueMax - valueMin)) * (targetMax - targetMin)) + targetMin;
		if (!withinBounds) {
			return newval;
		}
		if (targetMin < targetMax) {
			return Utilities.constraint(newval, targetMin, targetMax);
		} else {
			return Utilities.constraint(newval, targetMax, targetMin);
		}
	}

	/**
	 * Used for map()
	 */
	private static float constraint(final float value, final float minimum, final float maximum) {
		return Math.max(Math.min(value, maximum), minimum);
	}
}
