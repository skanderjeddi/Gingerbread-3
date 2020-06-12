package com.skanderj.gingerbread3.component.premade;

import java.awt.Color;
import java.awt.Graphics2D;

import com.skanderj.gingerbread3.component.ComponentManager;
import com.skanderj.gingerbread3.component.ComponentPriority;
import com.skanderj.gingerbread3.component.unit.Label;
import com.skanderj.gingerbread3.display.Window;
import com.skanderj.gingerbread3.input.Keyboard;
import com.skanderj.gingerbread3.input.Mouse;
import com.skanderj.gingerbread3.util.VisualString;

/**
 * Represents a simple label centered inside a rectangle.
 *
 * @author Skander
 *
 */
public final class G3Label extends Label {
	private int x, y, width, height;
	private final String formatString;

	public G3Label(final int x, final int y, final int width, final int height, final VisualString graphicString) {
		super(graphicString);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.formatString = graphicString.getContent();
	}

	/**
	 * No need for logic.
	 */
	@Override
	public void update(final double delta, final Keyboard keyboard, final Mouse mouse, final Object... args) {
		this.graphicString.setContent(String.format(this.formatString, args));
	}

	/**
	 * Just draw the string.
	 */
	@Override
	public void render(final Window window, final Graphics2D graphics, final Object... args) {
		this.graphicString.drawCentered(graphics, this.x, this.y, this.width, this.height, args);
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
	@Override
	public boolean containsMouse(final int x, final int y) {
		return false;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public ComponentPriority priority() {
		return ComponentPriority.LOW;
	}
}