package com.skanderj.gingerbread3.component.boilerplates;

import java.awt.Color;

import com.skanderj.gingerbread3.component.Background;
import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.display.Screen;

/**
 * Solid color background. Very basic.
 *
 * @author Skander
 *
 */
public class GBackgroundColor extends Background {
	private double x, y;
	private int width, height;
	private Color color;

	public GBackgroundColor(final Application application, final double x, final double y, final int width, final int height, final Color color) {
		super(application);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}

	/**
	 * No need for logic.
	 */
	@Override
	public synchronized void update() {
		return;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public synchronized void render(final Screen screen) {
		screen.rectangle(this.color, this.x, this.y, this.width, this.height, true, 0, 0);
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public boolean containsMouse(final int x, final int y) {
		return false;
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
	public int width() {
		return this.width;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public int height() {
		return this.height;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setWidth(final int width) {
		this.width = width;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setHeight(final int height) {
		this.height = height;
	}

	/**
	 * Self explanatory.
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * Self explanatory.
	 */
	public void setColor(final Color color) {
		this.color = color;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public Priority priority() {
		return Priority.HIGH;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public String description() {
		return Engine.identifier(this) + " -> GBackgroundColor.class(" + this.x + ", " + this.y + ", " + this.width + ", " + this.height + ")";
	}
}
