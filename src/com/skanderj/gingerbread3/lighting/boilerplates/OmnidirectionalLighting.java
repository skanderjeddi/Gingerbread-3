package com.skanderj.gingerbread3.lighting.boilerplates;

import java.awt.Color;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.geom.Ellipse2D;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.lighting.LightingSource;
import com.skanderj.gingerbread3.core.Moveable;

/**
 * Represents an omnidirectional (in a circle) fading lighting source.
 *
 * @author Skander
 *
 */
public class OmnidirectionalLighting extends LightingSource {
	private final Color color;
	private final int radius;

	public OmnidirectionalLighting(final Application application, final Color color, final double x, final double y, final int radius, final Priority priority) {
		super(application, x, y, priority);
		this.color = color;
		this.radius = radius;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public final void update() {
		// TODO: here flickering and stuff
		return;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public final void render(final Screen screen) {
//		for (int c = 0; c < this.radius; c += 1) {
//			float[] hsb = Color.RGBtoHSB(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), null);
//			System.out.println(hsb[0] + ", " + hsb[1] + ", " + hsb[2]);
//			float b = Utilities.map(c, 0, radius - 1, 1f, 0.8f, false);
//			System.out.println(b);
//			int rgb = Color.HSBtoRGB(hsb[0], hsb[1], b);
//			Color sColor = new Color(rgb);
//			System.out.println(sColor.getRed() + ", " + sColor.getGreen() + ", " + sColor.getBlue());
//			Color nsColor = new Color(sColor.getRed(), sColor.getGreen(), sColor.getBlue(), this.color.getAlpha());
//			screen.oval(nsColor, this.x, this.y, c, c, false);
//		}
//		for (int c = 0; c < this.radius; c += 1) {
//			final int alpha = (int) Utilities.map(c, 0, this.radius - 1, this.color.getAlpha(), 0, true);
//			screen.oval(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), alpha), this.x, this.y, c, c, false);
//		}

		final RadialGradientPaint paint = new RadialGradientPaint(new Point((int) this.x, (int) this.y), this.radius, new float[] { 0f, 1f }, new Color[] { this.color, new Color(1f, 1f, 1f, 1f) });
		screen.drawGraphics().translate(0, 0);
		screen.drawGraphics().setPaint(paint);
		screen.drawGraphics().fill(new Ellipse2D.Double(this.x - (this.radius / 2), this.y - (this.radius / 2), this.radius, this.radius));
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
	public final int radius() {
		return this.radius;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public Moveable copy() {
		return new OmnidirectionalLighting(this.application, this.color, this.x, this.y, this.radius, this.priority);
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public String description() {
		return Engine.identifier(this) + " -> OmnidirectionalLighting.class(" + this.x + ", " + this.y + ", " + this.radius + ", " + this.color + ")";
	}
}
