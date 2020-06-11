package com.skanderj.gingerbead3.component.basic;

import java.awt.Color;
import java.awt.Graphics2D;

import com.skanderj.gingerbead3.component.Background;
import com.skanderj.gingerbead3.component.ComponentPriority;
import com.skanderj.gingerbead3.display.Window;
import com.skanderj.gingerbead3.input.Keyboard;
import com.skanderj.gingerbead3.input.Mouse;

/**
 * Solid color background. Very basic.
 * 
 * @author Skander
 *
 */
public class G3SolidBackground extends Background {
	private int x, y, width, height;
	private Color color;

	public G3SolidBackground(int x, int y, int width, int height, Color color) {
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
	public void update(double delta, Keyboard keyboard, Mouse mouse, Object... args) {
		return;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void render(Window window, Graphics2D graphics, Object... args) {
		graphics.setColor(this.color);
		graphics.fillRect(this.x, this.y, this.width, this.height);
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public boolean containsMouse(int x, int y) {
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
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setY(int y) {
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
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setHeight(int height) {
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
	public void setColor(Color color) {
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
