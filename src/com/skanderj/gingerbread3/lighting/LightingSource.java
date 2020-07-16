package com.skanderj.gingerbread3.lighting;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.object.ApplicationObject;
import com.skanderj.gingerbread3.particle.Moveable;

/**
 * Represents a simple lighting source.
 *
 * @author Skander
 *
 */
public abstract class LightingSource extends ApplicationObject implements Moveable {
	protected double x, y;
	protected Priority priority;

	public LightingSource(final Application application, final double x, final double y, final Priority priority) {
		super(application);
		this.x = x;
		this.y = y;
		this.priority = priority;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setX(final double x) {
		this.x = x;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setY(final double y) {
		this.y = y;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public double x() {
		return this.x;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public double y() {
		return this.y;
	}

	@Override
	public Priority priority() {
		return this.priority;
	}
}
