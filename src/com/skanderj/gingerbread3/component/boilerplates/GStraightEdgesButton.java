package com.skanderj.gingerbread3.component.boilerplates;

import java.awt.Color;
import java.awt.Rectangle;

import com.skanderj.gingerbread3.component.Button;
import com.skanderj.gingerbread3.component.Components;
import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.util.VisualString;

/**
 * A straight edges version of the button. Very basic.
 *
 * @author Skander
 *
 */
public final class GStraightEdgesButton extends Button {
	private int x, y, width, height;
	private VisualString label;
	private Color backgroundColor, borderColor;

	public GStraightEdgesButton(final Game game, final int x, final int y, final int width, final int height, final VisualString label, final Color backgroundColor, final Color borderColor) {
		super(game);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.label = label;
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
	}

	/**
	 * Draws a simple rectangle for the background, draws the border and the label.
	 */
	@Override
	public synchronized void render(final Screen screen) {
		screen.rectangle(this.backgroundColor, this.x, this.y, this.width, this.height, true, 0, 0);
		this.label.drawCentered(screen, this.x, this.y, this.width, this.height);
		screen.rectangle(this.borderColor, this.x, this.y, this.width, this.height, false, 0, 0);
		if (Components.GRAPHICAL_DEBUG) {
			screen.rectangle(Color.RED, this.x, this.y, this.width, this.height, false, 0, 0);
		}
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public boolean containsMouse(final int x, final int y) {
		return new Rectangle(this.x, this.y, this.width, this.height).contains(x, y);
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
	public VisualString getLabel() {
		return this.label;
	}

	/**
	 * Self explanatory.
	 */
	public Color getBackgroundColor() {
		return this.backgroundColor;
	}

	/**
	 * Self explanatory.
	 */
	public Color getBorderColor() {
		return this.borderColor;
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
	public void setLabel(final VisualString label) {
		this.label = label;
	}

	/**
	 * Self explanatory.
	 */
	public void setBackgroundColor(final Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Self explanatory.
	 */
	public void setBorderColor(final Color borderColor) {
		this.borderColor = borderColor;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public Priority priority() {
		return Priority.LOW;
	}
}