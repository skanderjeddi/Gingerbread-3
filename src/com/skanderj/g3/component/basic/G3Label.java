package com.skanderj.g3.component.basic;

import java.awt.Color;
import java.awt.Graphics2D;

import com.skanderj.g3.component.ComponentManager;
import com.skanderj.g3.component.Label;
import com.skanderj.g3.display.Window;
import com.skanderj.g3.input.Keyboard;
import com.skanderj.g3.input.Mouse;
import com.skanderj.g3.util.VisualString;

/**
 * Represents a simple label centered inside a rectangle.
 *
 * @author Skander
 *
 */
public final class G3Label extends Label {
	private int x, y, width, height;

	public G3Label(int x, int y, int width, int height, VisualString graphicString) {
		super(graphicString);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * No need for logic.
	 */
	@Override
	public void update(double delta, Keyboard keyboard, Mouse mouse, Object... args) {
		return;
	}

	/**
	 * Just draw the string.
	 */
	@Override
	public void render(Window window, Graphics2D graphics, Object... args) {
		this.graphicString.drawCentered(graphics, this.x, this.y, this.width, this.height);
		if (ComponentManager.GRAPHICAL_DEBUG) {
			graphics.setColor(Color.RED);
			graphics.drawRect(this.x, this.y, this.width, this.height);
		}
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
	@Override
	public boolean containsMouse(int x, int y) {
		return false;
	}
}