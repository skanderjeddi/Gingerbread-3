package com.skanderj.gingerbread3.component.boilerplates;

import java.awt.Color;
import java.awt.Rectangle;

import com.skanderj.gingerbread3.component.Button;
import com.skanderj.gingerbread3.component.Components;
import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.util.Text;

/**
 * A round edges version of the button. Still very basic.
 *
 * @author Skander
 *
 */
public final class GButton extends Button {
	private double x, y;
	private int width, height;
	private Text text;
	private Color backgroundColor, borderColor;
	// Border incline = how many pixels will be shaved off at each edge
	private int borderIncline;

	public GButton(final Application application, final double x, final double y, final int width, final int height, final Text text, final Color backgroundColor, final Color borderColor, final int borderIncline) {
		super(application);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
		this.borderIncline = borderIncline;
	}

	/**
	 * Draws a simple round rectangle for the background, draws the border and the
	 * text.
	 */
	@Override
	public synchronized void render(final Screen screen) {
		screen.rectangle(this.backgroundColor, this.x, this.y, this.width, this.height, true, this.borderIncline, this.borderIncline);
		this.text.drawCentered(screen, (int) this.x, (int) this.y, this.width, this.height);
		screen.rectangle(this.borderColor, this.x, this.y, this.width, this.height, false, this.borderIncline, this.borderIncline);
		if (Components.GRAPHICAL_DEBUG) {
			screen.rectangle(Color.RED, this.x, this.y, this.width, this.height, false, this.borderIncline, this.borderIncline);
		}
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public boolean containsMouse(final int x, final int y) {
		return new Rectangle((int) this.x, (int) this.y, this.width, this.height).contains(x, y);
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public double getX() {
		return this.x;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public double getY() {
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
	public Text text() {
		return this.text;
	}

	/**
	 * Self explanatory.
	 */
	public Color backgroundColor() {
		return this.backgroundColor;
	}

	/**
	 * Self explanatory.
	 */
	public Color borderColor() {
		return this.borderColor;
	}

	/**
	 * Self explanatory.
	 */
	public int borderIncline() {
		return this.borderIncline;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setX(final double x) {
		this.x = x;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setY(final double y) {
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
	public void setText(final Text text) {
		this.text = text;
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
	public void setBorderIncline(final int borderIncline) {
		this.borderIncline = borderIncline;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public Priority priority() {
		return Priority.REGULAR;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public String description() {
		return Engine.identifier(this) + " -> GButton.class(" + this.x + ", " + this.y + ", " + this.width + ", " + this.height + ", " + this.text.toString() + ")";
	}
}