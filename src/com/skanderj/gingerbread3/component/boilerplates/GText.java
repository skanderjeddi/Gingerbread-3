package com.skanderj.gingerbread3.component.boilerplates;

import java.awt.Color;
import java.util.Map;

import com.skanderj.gingerbread3.component.Components;
import com.skanderj.gingerbread3.component.Text;
import com.skanderj.gingerbread3.core.G3Application;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.util.Label;
import com.skanderj.gingerbread3.util.LabelProperties;

/**
 * Represents a simple label centered inside a rectangle.
 *
 * @author Skander
 *
 */
public final class GText extends Text {
	private int x, y, width, height;
	private final String format;

	public GText(final G3Application g3Application, final int x, final int y, final int width, final int height, final Label label) {
		super(g3Application, label);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.format = label.getContent();
	}

	/**
	 * No need for logic.
	 */
	@Override
	public synchronized void update() {
		final String identifier = Registry.identifier(this);
		final Map<String, Object> parameters = Registry.parameters(identifier);
		if (parameters != null) {
			final Object[] args = parameters.values().toArray(new Object[parameters.size()]);
			this.label = new Label(String.format(this.format, args), new LabelProperties(this.label.getFont(), this.label.getColor()));
		} else {
			this.label = new Label(String.format(this.format), new LabelProperties(this.label.getFont(), this.label.getColor()));
		}
	}

	/**
	 * Just draw the string.
	 */
	@Override
	public synchronized void render(final Screen screen) {
		this.label.drawCentered(screen, this.x, this.y, this.width, this.height);
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