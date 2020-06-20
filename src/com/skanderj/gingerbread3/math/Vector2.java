package com.skanderj.gingerbread3.math;

import com.skanderj.gingerbread3.util.Utilities;

/**
 *
 * @author Skander
 *
 */
public class Vector2 {
	public float x, y;

	public static final Vector2[] randomVectors(final int count, final int xMin, final int xMax, final int yMin, final int yMax) {
		final Vector2[] vects = new Vector2[count];
		for (int i = 0; i < count; i += 1) {
			final float randomX = Utilities.randomInteger(xMin, xMax);
			final float randomY = Utilities.randomInteger(yMin, yMax);
			vects[i] = new Vector2(randomX, randomY);
		}
		return vects;
	}

	/**
	 * Self explanatory.
	 */
	public Vector2(final float x, final float y) {
		this.x = x;
		this.y = y;
	}
}
