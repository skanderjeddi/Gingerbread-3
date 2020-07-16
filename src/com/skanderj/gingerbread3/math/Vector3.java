package com.skanderj.gingerbread3.math;

import com.skanderj.gingerbread3.util.Utilities;

/**
 *
 * @author Skander
 *
 */
public class Vector3 {
	public double x, y, z;

	/**
	 * Self explanatory.
	 */
	public static final Vector3[] randomVectors(final int count, final int xMin, final int xMax, final int yMin, final int yMax, final int zMin, final int zMax) {
		final Vector3[] vects = new Vector3[count];
		for (int i = 0; i < count; i += 1) {
			final int randomX = Utilities.randomInteger(xMin, xMax);
			final int randomY = Utilities.randomInteger(yMin, yMax);
			final int randomZ = Utilities.randomInteger(zMin, zMax);
			vects[i] = new Vector3(randomX, randomY, randomZ);
		}
		return vects;
	}

	/**
	 * Self explanatory.
	 */
	public Vector3(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
