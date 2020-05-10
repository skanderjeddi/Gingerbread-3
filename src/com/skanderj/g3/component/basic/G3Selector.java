package com.skanderj.g3.component.basic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.skanderj.g3.component.ComponentManager;
import com.skanderj.g3.component.Selector;
import com.skanderj.g3.io.FontManager;
import com.skanderj.g3.util.GraphicString;
import com.skanderj.g3.util.TextProperties;
import com.skanderj.g3.window.Window;

/**
 * A basic selector with rectangle arrows.
 *
 * @author Skander
 *
 */
public final class G3Selector extends Selector {
	private int x, y, width, height;
	private TextProperties properties;
	private int arrowSize;

	/**
	 * Self explanatory.
	 */
	public G3Selector(int x, int y, int width, int height, int arrowSize, TextProperties properties, String[] optionsArray) {
		this(x, y, width, height, arrowSize, properties, optionsArray, optionsArray[0]);
	}

	/**
	 * Self explanatory.
	 */
	public G3Selector(int x, int y, int width, int height, int arrowSize, TextProperties properties, String[] optionsArray, String defaultOption) {
		super(optionsArray, defaultOption);
		this.x = x;
		this.y = y;
		this.properties = properties;
		this.arrowSize = arrowSize;
		this.width = width;
		this.height = height;
	}

	@Override
	/**
	 * Very basic rendering, 2 rectangles for the arrows.
	 */
	public void render(Window window, Graphics2D graphics, Object... args) {
		graphics.setColor(Color.WHITE);
		graphics.fillRect(this.x + 10 + this.arrowSize, this.y, this.width, this.height);
		new GraphicString(this.currentOption, this.properties, this.properties.getColor().darker().darker()).drawCentered(graphics, this.x + 10 + this.arrowSize, this.y, this.width, this.height);
		if (ComponentManager.GRAPHICAL_DEBUG) {
			graphics.setColor(Color.RED);
			graphics.drawRect(this.x + 10 + this.arrowSize, this.y, this.width, this.height);
		}
		graphics.setColor(Color.WHITE);
		graphics.fillRect(this.x, this.y, this.arrowSize, this.height);
		new GraphicString("<", Color.BLACK, FontManager.getFont("lunchtime", 24)).drawCentered(graphics, this.x, this.y, this.arrowSize, this.height);
		if (ComponentManager.GRAPHICAL_DEBUG) {
			graphics.setColor(Color.RED);
			graphics.drawRect(this.x, this.y, this.arrowSize, this.height);
		}
		graphics.setColor(Color.WHITE);
		graphics.fillRect(this.x + this.width + 20 + this.arrowSize, this.y, this.arrowSize, this.height);
		new GraphicString(">", Color.BLACK, FontManager.getFont("lunchtime", 24)).drawCentered(graphics, this.x + this.width + 20 + this.arrowSize, this.y, this.arrowSize, this.height);
		if (ComponentManager.GRAPHICAL_DEBUG) {
			graphics.setColor(Color.RED);
			graphics.drawRect(this.x + this.width + 20 + this.arrowSize, this.y, this.arrowSize, this.height);
		}
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public boolean leftArrowContainsMouse(int x, int y) {
		return new Rectangle(this.x, this.y, this.arrowSize, this.height).contains(x, y);
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public boolean rightArrowContainsMouse(int x, int y) {
		return new Rectangle(this.x + this.width + 20 + this.arrowSize, this.y, this.arrowSize, this.height).contains(x, y);
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public final int getX() {
		return this.x;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public final int getY() {
		return this.y;
	}

	/**
	 * Self explanatory.
	 */
	public final TextProperties getProperties() {
		return this.properties;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public final int getWidth() {
		return this.width;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public final int getHeight() {
		return this.height;
	}

	/**
	 * Self explanatory.
	 */
	public final int getArrowSize() {
		return this.arrowSize;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public final void setX(int x) {
		this.x = x;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public final void setY(int y) {
		this.y = y;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public final void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public final void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Self explanatory.
	 */
	public final void setProperties(TextProperties properties) {
		this.properties = properties;
	}

	/**
	 * Self explanatory.
	 */
	public final void setArrowSize(int arrowSize) {
		this.arrowSize = arrowSize;
	}
}