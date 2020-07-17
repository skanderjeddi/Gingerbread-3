package com.skanderj.gingerbread3.lighting.boilerplates;

import java.awt.Color;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.geom.Arc2D;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.lighting.LightingSource;
import com.skanderj.gingerbread3.core.Moveable;

/**
 * Represents a directed (in a cone) lighting source.
 *
 * @author Skander
 *
 */
public class DirectionalLighting extends LightingSource {
	private Color color;
	private final int radius;
	private int startingAngle;
	private int arcAngle;

	public DirectionalLighting(final Application application, final double x, final double y, final Color color, final int radius, final int startingAngle, final int angleEnd, final Priority priority) {
		super(application, x, y, priority);
		this.color = color;
		this.radius = radius;
		this.startingAngle = startingAngle;
		this.arcAngle = angleEnd;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public Moveable copy() {
		return new DirectionalLighting(this.application, this.x, this.y, this.color, this.radius, this.startingAngle, this.arcAngle, this.priority);
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public final void update() {
		// TODO: potential flickering
		return;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void render(final Screen screen) {
		final RadialGradientPaint paint = new RadialGradientPaint(new Point((int) this.x + (this.radius / 2), (int) this.y + (this.radius / 2)), this.radius, new float[] { 0f, 1f }, new Color[] { this.color, new Color(1f, 1f, 1f, 0.1f) });
		screen.drawGraphics().setPaint(paint);
		screen.drawGraphics().fill(new Arc2D.Float((int) this.x, (int) this.y, this.radius, this.radius, this.startingAngle, this.arcAngle, Arc2D.PIE));
	}

	/**
	 * Self explanatory.
	 */
	public final Color color() {
		return this.color;
	}

	/**
	 * Self explanatory.
	 */
	public final int startingAngle() {
		return this.startingAngle;
	}

	/**
	 * Self explanatory.
	 */
	public final int arcAngle() {
		return this.arcAngle;
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
	public void setStartingAngle(final int startingAngle) {
		this.startingAngle = startingAngle;
	}

	/**
	 * Self explanatory.
	 */
	public void setArcAngle(final int arcAngle) {
		this.arcAngle = arcAngle;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public String description() {
		return Engine.identifier(this) + " -> DirectionalLighting.class(" + this.x + ", " + this.y + ", " + this.startingAngle + ", " + this.arcAngle + ", " + this.color + ")";
	}
}
