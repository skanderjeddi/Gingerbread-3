package com.skanderj.gingerbread3.component.boilerplates;

import java.awt.Color;
import java.util.Map;

import com.skanderj.gingerbread3.component.Components;
import com.skanderj.gingerbread3.component.Text;
import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.util.Label;

/**
 * Represents a simple label centered inside a rectangle.
 *
 * @author Skander
 *
 */
public final class GText extends Text {
	private int x, y, width, height;

	public GText(final Application application, final int x, final int y, final int width, final int height, final Label label) {
		super(application, label);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
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
			this.label.drawCentered(screen, this.x, this.y, this.width, this.height, args);
		} else {
			this.label.drawCentered(screen, this.x, this.y, this.width, this.height);
		}
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

	@Override
	public String description() {
		return Engine.identifier(this) + " -> GText.class(" + this.x + ", " + this.y + ", " + this.label.toString() + ")";
	}
}