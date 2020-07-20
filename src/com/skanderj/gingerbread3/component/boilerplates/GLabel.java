package com.skanderj.gingerbread3.component.boilerplates;

import java.awt.Color;
import java.util.Map;

import com.skanderj.gingerbread3.component.Components;
import com.skanderj.gingerbread3.component.Label;
import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.util.Text;

/**
 * Represents a simple text centered inside a rectangle.
 *
 * @author Skander
 *
 */
public final class GLabel extends Label {
	private double x, y;
	private int width, height;
	private boolean centered;

	public GLabel(final Application application, final double x, final double y, final int width, final int height, final Text text) {
		super(application, text);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.centered = true;
	}

	/**
	 * No need for logic.
	 */
	@Override
	public synchronized void update() {
		return;
	}

	/**
	 * Just draw the string.
	 */
	@Override
	public synchronized void render(final Screen screen) {
		final String identifier = Engine.identifier(this);
		final Map<String, Object> parameters = Engine.parameters(identifier);
		if (parameters != null) {
			final Object[] args = parameters.values().toArray(new Object[parameters.size()]);
			if (this.centered) {
				this.text.drawCentered(screen, (int) this.x, (int) this.y, this.width, this.height, args);
			} else {
				this.text.draw(screen, (int) this.x, (int) this.y, args);
			}
		} else {
			if (this.centered) {
				this.text.drawCentered(screen, (int) this.x, (int) this.y, this.width, this.height);
			} else {
				this.text.draw(screen, (int) this.x, (int) this.y);
			}
		}
		if (Components.GRAPHICAL_DEBUG) {
			screen.rectangle(Color.RED, this.x, this.y, this.width, this.height, false, 0, 0);
		}
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

	public void setCentered(final boolean state) {
		this.centered = state;
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
		return Priority.REGULAR;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public String description() {
		return Engine.identifier(this) + " -> GLabel.class(" + this.x + ", " + this.y + ", " + this.text.toString() + ")";
	}
}
