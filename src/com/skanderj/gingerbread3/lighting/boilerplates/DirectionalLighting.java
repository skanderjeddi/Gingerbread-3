package com.skanderj.gingerbread3.lighting.boilerplates;

import java.awt.Color;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.geom.Arc2D;

import com.skanderj.gingerbread3.core.G3Application;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.lighting.LightingSource;
import com.skanderj.gingerbread3.particle.Moveable;

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

	public DirectionalLighting(final G3Application g3Application, final int x, final int y, final Color color, final int radius, final int startingAngle, final int angleEnd) {
		super(g3Application, x, y);
		this.color = color;
		this.radius = radius;
		this.startingAngle = startingAngle;
		this.arcAngle = angleEnd;
	}

	@Override
	public Moveable copy() {
		return new DirectionalLighting(this.g3Application, this.x, this.y, this.color, this.radius, this.startingAngle, this.arcAngle);
	}

	@Override
	public final void update() {
		// TODO: potential flickering
		return;
	}

	@Override
	public void render(final Screen screen) {
		final RadialGradientPaint paint = new RadialGradientPaint(new Point(this.x + (this.radius / 2), this.y + (this.radius / 2)), this.radius, new float[] { 0f, 1f }, new Color[] { this.color, new Color(1f, 1f, 1f, 0.1f) });
		screen.drawGraphics().setPaint(paint);
		screen.drawGraphics().fill(new Arc2D.Float(this.x, this.y, this.radius, this.radius, this.startingAngle, this.arcAngle, Arc2D.PIE));
	}

	public final Color color() {
		return this.color;
	}

	public final int startingAngle() {
		return this.startingAngle;
	}

	public final int arcAngle() {
		return this.arcAngle;
	}

	public void setColor(final Color color) {
		this.color = color;
	}

	public void setStartingAngle(final int startingAngle) {
		this.startingAngle = startingAngle;
	}

	public void setArcAngle(final int arcAngle) {
		this.arcAngle = arcAngle;
	}

	@Override
	public String description() {
		return Registry.identifier(this) + " -> DirectionalLighting.class(" + this.x + ", " + this.y + ", " + this.startingAngle + ", " + this.arcAngle + ", " + this.color + ")";
	}
}
