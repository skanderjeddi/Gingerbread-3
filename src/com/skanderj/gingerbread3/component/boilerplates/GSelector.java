package com.skanderj.gingerbread3.component.boilerplates;

import java.awt.Color;
import java.awt.Rectangle;

import com.skanderj.gingerbread3.component.Components;
import com.skanderj.gingerbread3.component.Selector;
import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.resources.Fonts;
import com.skanderj.gingerbread3.util.Text;
import com.skanderj.gingerbread3.util.TextProperties;

/**
 * A basic selector with rectangle arrows.
 *
 * @author Skander
 *
 */
public final class GSelector extends Selector {
	private double x, y;
	private int width, height;
	private TextProperties properties;
	private int arrowSize;

	/**
	 * Self explanatory.
	 */
	public GSelector(final Application application, final double x, final double y, final int width, final int height, final int arrowSize, final TextProperties properties, final String[] optionsArray) {
		this(application, x, y, width, height, arrowSize, properties, optionsArray, optionsArray[0]);
	}

	/**
	 * Self explanatory.
	 */
	public GSelector(final Application application, final double x, final double y, final int width, final int height, final int arrowSize, final TextProperties properties, final String[] optionsArray, final String defaultOption) {
		super(application, optionsArray, defaultOption);
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
		new Text(this.currentOption, this.properties, this.properties.color.darker().darker()).drawCentered(screen, (int) this.x + 10 + this.arrowSize, (int) this.y, this.width, this.height);
		if (Components.GRAPHICAL_DEBUG) {
			screen.rectangle(Color.RED, this.x + 10 + this.arrowSize, this.y, this.width, this.height, false, 0, 0);
		}
		screen.rectangle(Color.WHITE, this.x, this.y, this.arrowSize, this.height, true, 0, 0);
		new Text("<", Color.BLACK, Fonts.get("lunchtime", 24)).drawCentered(screen, (int) this.x, (int) this.y, this.arrowSize, this.height);
		if (Components.GRAPHICAL_DEBUG) {
			screen.rectangle(Color.RED, this.x, this.y, this.arrowSize, this.height, false, 0, 0);
		}
		screen.rectangle(Color.WHITE, this.x + this.width + 20 + this.arrowSize, this.y, this.arrowSize, this.height, true, 0, 0);
		new Text(">", Color.BLACK, Fonts.get("lunchtime", 24)).drawCentered(screen, (int) this.x + this.width + 20 + this.arrowSize, (int) this.y, this.arrowSize, this.height);
		if (Components.GRAPHICAL_DEBUG) {
			screen.rectangle(Color.RED, this.x + this.width + 20 + this.arrowSize, this.y, this.arrowSize, this.height, false, 0, 0);
		}
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public boolean leftArrowContainsMouse(final int x, final int y) {
		return new Rectangle((int) this.x, (int) this.y, this.arrowSize, this.height).contains(x, y);
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public boolean rightArrowContainsMouse(final int x, final int y) {
		return new Rectangle((int) this.x + this.width + 20 + this.arrowSize, (int) this.y, this.arrowSize, this.height).contains(x, y);
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
	public TextProperties textProperties() {
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
	public int arrowSize() {
		return this.arrowSize;
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
	public void setProperties(final TextProperties properties) {
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
		return Priority.REGULAR;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public String description() {
		return Engine.identifier(this) + " -> GSelector.class(" + this.x + ", " + this.y + ", " + this.width + ", " + this.height + ", " + this.properties.toString() + ")";
	}
}