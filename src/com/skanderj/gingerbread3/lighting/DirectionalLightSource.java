package com.skanderj.gingerbread3.lighting;

import java.awt.Color;

import com.skanderj.gingerbread3.core.G3Application;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.particle.Moveable;
import com.skanderj.gingerbread3.util.Utilities;

public class DirectionalLightSource extends LightSource {
	private Color color;
	private final int radius;
	private int startingAngle;
	private int arcAngle;

	public DirectionalLightSource(final G3Application g3Application, final int x, final int y, final Color color, final int radius, final int angleStart, final int angleEnd) {
		super(g3Application, x, y);
		this.color = color;
		this.radius = radius;
		this.startingAngle = angleStart;
		this.arcAngle = angleEnd;
	}

	@Override
	public Moveable copy() {
		return new DirectionalLightSource(this.g3Application, this.x, this.y, this.color, this.radius, this.startingAngle, this.arcAngle);
	}

	@Override
	public final void update() {
		// TODO: potential flickering
		return;
	}

	@Override
	public void render(final Screen screen) {
		for (int c = 0; c < this.radius; c += 1) {
			final int alpha = (int) Utilities.map(c, 0, this.radius - 1, this.color.getAlpha(), 0, true);
			screen.arc(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), alpha), this.x, this.y, c, c, this.startingAngle, this.arcAngle, false);
		}
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
		return Registry.identifier(this) + " -> DirectionalLightSource.class(" + this.x + ", " + this.y + ", " + this.startingAngle + ", " + this.arcAngle + ", " + this.color + ")";
	}
}
