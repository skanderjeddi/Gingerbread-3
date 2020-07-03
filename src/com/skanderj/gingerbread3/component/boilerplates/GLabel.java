package com.skanderj.gingerbread3.component.boilerplates;

import java.awt.Color;

import com.skanderj.gingerbread3.component.Components;
import com.skanderj.gingerbread3.component.Label;
import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.util.VisualString;
import com.skanderj.gingerbread3.util.VisualStringProperties;

/**
 * Represents a simple label centered inside a rectangle.
 *
 * @author Skander
 *
 */
public final class GLabel extends Label {
	private int x, y, width, height;
	private final String format;

	public GLabel(final Game game, final int x, final int y, final int width, final int height, final VisualString graphicString) {
		super(game, graphicString);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.format = graphicString.getContent();
	}

	/**
	 * No need for logic.
	 */
	@Override
	public synchronized void update(final double delta, final Object... args) {
		this.graphicString = new VisualString(String.format(this.format, args), new VisualStringProperties(this.graphicString.getFont(), this.graphicString.getColor()));
	}

	/**
	 * Just draw the string.
	 */
	@Override
	public synchronized void render(final Screen screen) {
		this.graphicString.drawCentered(screen, this.x, this.y, this.width, this.height);
		if (Components.GRAPHICAL_DEBUG) {
			screen.rectangle(Color.RED, this.x, this.y, this.width, this.height, false, 0, 0);
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
	public Priority priority() {
		return Priority.LOW;
	}
}