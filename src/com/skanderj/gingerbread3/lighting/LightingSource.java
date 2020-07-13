package com.skanderj.gingerbread3.lighting;

import com.skanderj.gingerbread3.core.G3Application;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.object.G3Object;
import com.skanderj.gingerbread3.particle.Moveable;

/**
 * Represents a simple lighting source.
 *
 * @author Skander
 *
 */
public abstract class LightingSource extends G3Object implements Moveable {
	protected int x, y;
	protected Priority priority;

	public LightingSource(final G3Application g3Application, final int x, final int y, Priority priority) {
		super(g3Application);
		this.x = x;
		this.y = y;
		this.priority = priority;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setX(final int x) {
		this.x = x;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setY(final int y) {
		this.y = y;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public int x() {
		return this.x;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public int y() {
		return this.y;
	}

	@Override
	public Priority priority() {
		return this.priority;
	}
}