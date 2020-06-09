package com.skanderj.gingerbead3.component.basic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.skanderj.gingerbead3.component.Button;
import com.skanderj.gingerbead3.component.ComponentManager;
import com.skanderj.gingerbead3.display.Window;
import com.skanderj.gingerbead3.util.VisualString;

/**
 * A round edges version of the button. Still very basic.
 *
 * @author Skander
 *
 */
public final class G3REButton extends Button {
	private int x, y, width, height;
	private VisualString label;
	private Color backgroundColor, borderColor;
	// Border incline = how many pixels will be shaved off at each edge
	private int borderIncline;

	public G3REButton(int x, int y, int width, int height, VisualString label, Color backgroundColor, Color borderColor, int borderIncline) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.label = label;
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
		this.borderIncline = borderIncline;
	}

	/**
	 * Draws a simple round rectangle for the background, draws the border and the
	 * label.
	 */
	@Override
	public void render(Window window, Graphics2D graphics, Object... args) {
		graphics.setColor(this.backgroundColor);
		graphics.fillRoundRect(this.x, this.y, this.width, this.height, this.borderIncline, this.borderIncline);
		this.label.drawCentered(graphics, this.x, this.y, this.width, this.height);
		graphics.setColor(this.borderColor);
		graphics.drawRoundRect(this.x, this.y, this.width, this.height, this.borderIncline, this.borderIncline);
		if (ComponentManager.GRAPHICAL_DEBUG) {
			graphics.setColor(Color.RED);
			graphics.drawRoundRect(this.x, this.y, this.width, this.height, this.borderIncline, this.borderIncline);
		}
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public boolean containsMouse(int x, int y) {
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
	public int getBorderIncline() {
		return this.borderIncline;
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
	public void setLabel(VisualString label) {
		this.label = label;
	}

	/**
	 * Self explanatory.
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Self explanatory.
	 */
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	/**
	 * Self explanatory.
	 */
	public void setBorderIncline(int borderIncline) {
		this.borderIncline = borderIncline;
	}
}