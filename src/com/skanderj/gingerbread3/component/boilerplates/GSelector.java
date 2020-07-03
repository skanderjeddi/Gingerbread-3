package com.skanderj.gingerbread3.component.boilerplates;

import java.awt.Color;
import java.awt.Rectangle;

import com.skanderj.gingerbread3.component.ComponentManager;
import com.skanderj.gingerbread3.component.Selector;
import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.io.Fonts;
import com.skanderj.gingerbread3.util.VisualString;
import com.skanderj.gingerbread3.util.VisualStringProperties;

/**
 * A basic selector with rectangle arrows.
 *
 * @author Skander
 *
 */
public final class GSelector extends Selector {
	private int x, y, width, height;
	private VisualStringProperties properties;
	private int arrowSize;

	/**
	 * Self explanatory.
	 */
	public GSelector(final Game game, final int x, final int y, final int width, final int height, final int arrowSize, final VisualStringProperties properties, final String[] optionsArray) {
		this(game, x, y, width, height, arrowSize, properties, optionsArray, optionsArray[0]);
	}

	/**
	 * Self explanatory.
	 */
	public GSelector(final Game game, final int x, final int y, final int width, final int height, final int arrowSize, final VisualStringProperties properties, final String[] optionsArray, final String defaultOption) {
		super(game, optionsArray, defaultOption);
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
	public synchronized void render(final Screen screen) {
		screen.rectangle(Color.WHITE, this.x + 10 + this.arrowSize, this.y, this.width, this.height, true, 0, 0);
		new VisualString(this.currentOption, this.properties, this.properties.getColor().darker().darker()).drawCentered(screen, this.x + 10 + this.arrowSize, this.y, this.width, this.height);
		if (ComponentManager.GRAPHICAL_DEBUG) {
			screen.rectangle(Color.RED, this.x + 10 + this.arrowSize, this.y, this.width, this.height, false, 0, 0);
		}
		screen.rectangle(Color.WHITE, this.x, this.y, this.arrowSize, this.height, true, 0, 0);
		new VisualString("<", Color.BLACK, Fonts.get("lunchtime", 24)).drawCentered(screen, this.x, this.y, this.arrowSize, this.height);
		if (ComponentManager.GRAPHICAL_DEBUG) {
			screen.rectangle(Color.RED, this.x, this.y, this.arrowSize, this.height, false, 0, 0);
		}
		screen.rectangle(Color.WHITE, this.x + this.width + 20 + this.arrowSize, this.y, this.arrowSize, this.height, true, 0, 0);
		new VisualString(">", Color.BLACK, Fonts.get("lunchtime", 24)).drawCentered(screen, this.x + this.width + 20 + this.arrowSize, this.y, this.arrowSize, this.height);
		if (ComponentManager.GRAPHICAL_DEBUG) {
			screen.rectangle(Color.RED, this.x + this.width + 20 + this.arrowSize, this.y, this.arrowSize, this.height, false, 0, 0);
		}
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public boolean leftArrowContainsMouse(final int x, final int y) {
		return new Rectangle(this.x, this.y, this.arrowSize, this.height).contains(x, y);
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public boolean rightArrowContainsMouse(final int x, final int y) {
		return new Rectangle(this.x + this.width + 20 + this.arrowSize, this.y, this.arrowSize, this.height).contains(x, y);
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
	public VisualStringProperties getProperties() {
		return this.properties;
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
	public int getArrowSize() {
		return this.arrowSize;
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
	public void setProperties(final VisualStringProperties properties) {
		this.properties = properties;
	}

	/**
	 * Self explanatory.
	 */
	public void setArrowSize(final int arrowSize) {
		this.arrowSize = arrowSize;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public Priority priority() {
		return Priority.LOW;
	}
}