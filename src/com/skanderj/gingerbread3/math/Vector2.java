package com.skanderj.gingerbread3.math;

import com.skanderj.gingerbread3.util.Utilities;

/**
 *
 * @author Skander
 *
 */
public class Vector2 {
	private int x, y;

	public static final Vector2[] randomVectors(final int count, final int xMin, final int xMax, final int yMin, final int yMax) {
		final Vector2[] vects = new Vector2[count];
		for (int i = 0; i < count; i += 1) {
			final int randomX = Utilities.randomInteger(xMin, xMax);
			final int randomY = Utilities.randomInteger(yMin, yMax);
			vects[i] = new Vector2(randomX, randomY);
		}
		return vects;
	}

	public Vector2(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public void setY(final int y) {
		this.y = y;
	}
}
