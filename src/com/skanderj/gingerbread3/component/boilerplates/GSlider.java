package com.skanderj.gingerbread3.component.boilerplates;

import java.awt.Color;
import java.awt.Rectangle;

import com.skanderj.gingerbread3.component.ComponentLabelPosition;
import com.skanderj.gingerbread3.component.Components;
import com.skanderj.gingerbread3.component.Slider;
import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.util.Label;
import com.skanderj.gingerbread3.util.Utilities;

/**
 * Represents a very basic slider with a label on on side.
 *
 * @author Skander
 *
 */
public final class GSlider extends Slider {
	private double x, y;
	private int width, height;
	private int sliderX, sliderWidth, sliderHeight;
	private Label label;
	private ComponentLabelPosition labelPosition;
	private Color color;

	public GSlider(final Application application, final double x, final double y, final int width, final int height, final int sliderWidth, final int sliderHeight, final float min, final float max, final float defaultValue, final Color color, final Label label, final ComponentLabelPosition position) {
		super(application, min, max, defaultValue);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.label = label;
		this.sliderX = (int) (x + (int) Utilities.map(defaultValue, this.minimumValue, this.maximumValue, 0, width, true));
		this.sliderWidth = sliderWidth;
		this.sliderHeight = sliderHeight;
		this.labelPosition = position;
		this.color = color;
	}

	@Override
	public synchronized void update() {
		super.update();
		if (this.hasFocus) {
			this.sliderX = (int) Utilities.map(this.application.mouse().getX(), this.x, this.x + this.width(), this.x, this.x + this.width(), true);
		}
	}

	/**
	 * Draws a simple rectangle for the slider and draws the label on the
	 * appropriate position.
	 */
	@Override
	public synchronized void render(final Screen screen) {
		screen.rectangle(this.color, this.x, this.y, this.width, this.height, false, 0, 0);
		screen.rectangle(this.color.darker(), this.sliderX - (this.sliderWidth / 2), this.y - (this.sliderHeight / 4), this.sliderWidth, this.height + (this.sliderHeight / 2), true, 0, 0);
		if (!this.label.isEmpty()) {
			switch (this.labelPosition) {
			case TOP:
				this.label.draw(screen, (int) this.x, (int) this.y - this.label.getHeight(screen), this.value());
				break;
			case BOTTOM:
				this.label.draw(screen, (int) this.x, (int) this.y + this.height + this.label.getAugmentedHeight(screen), this.value());
				break;
			case LEFT:
				this.label.drawCenteredWidthless(screen, (int) this.x - 10 - this.label.getWidth(screen), (int) this.y - (this.sliderHeight / 2), this.height + this.sliderHeight, this.value());
				break;
			case RIGHT:
				this.label.drawCenteredWidthless(screen, (int) this.x + this.width + 10, (int) this.y - (this.sliderHeight / 2), this.height + this.sliderHeight, this.value());
				break;
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
	public boolean containsMouse(final int x, final int y) {
		return new Rectangle((int) this.x(), (int) this.y(), this.width(), this.height()).contains(x, y);
	}

	/**
	 * Boxes the current value between the minimum and maximum and returns it.
	 */
	@Override
	public double value() {
		return Utilities.map(this.sliderX, this.x, this.x + this.width(), this.minimumValue, this.maximumValue, true);
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public double x() {
		return this.x;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public double y() {
		return this.y;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public int width() {
		return this.width;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public int height() {
		return this.height;
	}

	/**
	 * Self explanatory.
	 */
	public int sliderX() {
		return this.sliderX;
	}

	/**
	 * Self explanatory.
	 */
	public int sliderWidth() {
		return this.sliderWidth;
	}

	/**
	 * Self explanatory.
	 */
	public int sliderHeight() {
		return this.sliderHeight;
	}

	/**
	 * Self explanatory.
	 */
	public Label label() {
		return this.label;
	}

	/**
	 * Self explanatory.
	 */
	public ComponentLabelPosition labelPosition() {
		return this.labelPosition;
	}

	/**
	 * Self explanatory.
	 */
	public Color color() {
		return this.color;
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
	public void setSliderWidth(final int sliderWidth) {
		this.sliderWidth = sliderWidth;
	}

	/**
	 * Self explanatory.
	 */
	public void setSliderHeight(final int sliderHeight) {
		this.sliderHeight = sliderHeight;
	}

	/**
	 * Self explanatory.
	 */
	public void setLabel(final Label label) {
		this.label = label;
	}

	/**
	 * Self explanatory.
	 */
	public void setLabelPosition(final ComponentLabelPosition labelPosition) {
		this.labelPosition = labelPosition;
	}

	/**
	 * Self explanatory.
	 */
	public void setColor(final Color color) {
		this.color = color;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public Priority priority() {
		return Priority.LOW;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public String description() {
		return Engine.identifier(this) + " -> GSelector.class(" + this.x + ", " + this.y + ", " + this.width + ", " + this.height + ", " + this.label.toString() + ")";
	}
}