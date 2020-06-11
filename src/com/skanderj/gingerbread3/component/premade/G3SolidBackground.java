package com.skanderj.gingerbread3.component.premade;

import java.awt.Color;
import java.awt.Graphics2D;

import com.skanderj.gingerbread3.component.ComponentPriority;
import com.skanderj.gingerbread3.component.unit.Background;
import com.skanderj.gingerbread3.display.Window;
import com.skanderj.gingerbread3.input.Keyboard;
import com.skanderj.gingerbread3.input.Mouse;

/**
 * Solid color background. Very basic.
 *
 * @author Skander
 *
 */
public class G3SolidBackground extends Background {
	private int x, y, width, height;
	private Color color;

	public G3SolidBackground(final int x, final int y, final int width, final int height, final Color color) {
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
	public void update(final double delta, final Keyboard keyboard, final Mouse mouse, final Object... args) {
		return;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void render(final Window window, final Graphics2D graphics, final Object... args) {
		graphics.setColor(this.color);
		graphics.fillRect(this.x, this.y, this.width, this.height);
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
	public int getX() {
		return this.x;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public int getY() {
		return this.y;
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
	public int getWidth() {
		return this.width;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public int getHeight() {
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
	public ComponentPriority priority() {
		return ComponentPriority.HIGH;
	}
}