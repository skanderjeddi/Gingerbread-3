package com.skanderj.gingerbread3.lighting;

import java.awt.Color;

import com.skanderj.gingerbread3.core.G3Application;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.core.object.G3Object;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.particle.Moveable;
import com.skanderj.gingerbread3.util.Utilities;

public class OmnidirectionLightSource extends G3Object implements Moveable {
	private final Color color;
	private int x, y;
	private final int radius;

	public OmnidirectionLightSource(final G3Application g3Application, final Color color, final int x, final int y, final int radius) {
		super(g3Application);
		this.color = color;
		this.x = x;
		this.y = y;
		this.radius = radius;
	}

	@Override
	public void update() {
		// TODO
	}

	@Override
	public void render(final Screen screen) {
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
		for (int c = 0; c < this.radius; c += 1) {
			final int alpha = (int) Utilities.map(c, 0, this.radius - 1, this.color.getAlpha(), 0, true);
			screen.oval(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), alpha), this.x, this.y, c, c, false);
		}
	}

	@Override
	public Priority priority() {
		return Priority.EXTREMELY_LOW;
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
	public Moveable copy() {
		return new OmnidirectionLightSource(this.g3Application, this.color, this.x, this.y, this.radius);
	}

	@Override
	public String description() {
		return Registry.identifier(this) + " -> OmnidirectionalLightSource.class(" + this.x + ", " + this.y + ", " + this.radius + ", " + this.color + ")";
	}
}
