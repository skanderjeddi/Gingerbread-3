package com.skanderj.g3.component.basic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.skanderj.g3.component.ComponentManager;
import com.skanderj.g3.component.Slider;
import com.skanderj.g3.display.Window;
import com.skanderj.g3.input.Keyboard;
import com.skanderj.g3.input.Mouse;
import com.skanderj.g3.util.Utilities;
import com.skanderj.g3.util.VisualString;

/**
 * Represents a very basic slider with a label on on side.
 *
 * @author Skander
 *
 */
public final class G3Slider extends Slider {
	private int x, y, width, height;
	private int sliderX, sliderWidth, sliderHeight;
	private VisualString label;
	private SliderLabelPosition labelPosition;
	private Color color;

	public G3Slider(int x, int y, int width, int height, int sliderWidth, int sliderHeight, float min, float max, float defaultValue, Color color, VisualString label, SliderLabelPosition position) {
		super(min, max, defaultValue);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.label = label;
		this.sliderX = x + (int) Utilities.map(defaultValue, this.minimumValue, this.maximumValue, 0, width, true);
		this.sliderWidth = sliderWidth;
		this.sliderHeight = sliderHeight;
		this.labelPosition = position;
		this.color = color;
	}

	@Override
	public synchronized void update(double delta, Keyboard keyboard, Mouse mouse, Object... args) {
		super.update(delta, keyboard, mouse, args);
		if (this.hasFocus) {
			this.sliderX = (int) Utilities.map(mouse.getX(), this.x, this.x + this.getWidth(), this.x, this.x + this.getWidth(), true);
		}
	}

	/**
	 * Draws a simple rectangle for the slider and draws the label on the
	 * appropriate position.
	 */
	@Override
	public synchronized final void render(Window window, Graphics2D graphics, Object... args) {
		graphics.setColor(this.color);
		graphics.drawRect(this.x, this.y, this.width, this.height);
		graphics.setColor(this.color.darker());
		graphics.fillRect(this.sliderX - this.sliderWidth / 2, this.y - this.sliderHeight / 4, this.sliderWidth, this.height + this.sliderHeight / 2);
		if (!this.label.isEmpty()) {
			switch (this.labelPosition) {
			case TOP:
				this.label.draw(graphics, this.x, this.y - this.label.getHeight(graphics), this.getValue());
				break;
			case BOTTOM:
				this.label.draw(graphics, this.x, this.y + this.height + this.label.getAugmentedHeight(graphics), this.getValue());
				break;
			case LEFT:
				this.label.drawCenteredWidthless(graphics, this.x - 10 - this.label.getWidth(graphics), this.y - this.sliderHeight / 2, this.height + this.sliderHeight, this.getValue());
				break;
			case RIGHT:
				this.label.drawCenteredWidthless(graphics, this.x + this.width + 10, this.y - this.sliderHeight / 2, this.height + this.sliderHeight, this.getValue());
				break;
			}
		}
		if (ComponentManager.GRAPHICAL_DEBUG) {
			graphics.setColor(Color.RED);
			graphics.drawRect(this.x, this.y, this.width, this.height);
		}
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public boolean containsMouse(int x, int y) {
		return new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight()).contains(x, y);
	}

	/**
	 * Boxes the current value between the minimum and maximum and returns it.
	 */
	@Override
	public float getValue() {
		return Utilities.map(this.sliderX, this.x, this.x + this.getWidth(), this.minimumValue, this.maximumValue, true);
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
	public int getSliderX() {
		return this.sliderX;
	}

	/**
	 * Self explanatory.
	 */
	public int getSliderWidth() {
		return this.sliderWidth;
	}

	/**
	 * Self explanatory.
	 */
	public int getSliderHeight() {
		return this.sliderHeight;
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
	public SliderLabelPosition getLabelPosition() {
		return this.labelPosition;
	}

	/**
	 * Self explanatory.
	 */
	public Color getColor() {
		return this.color;
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
	public void setSliderWidth(int sliderWidth) {
		this.sliderWidth = sliderWidth;
	}

	/**
	 * Self explanatory.
	 */
	public void setSliderHeight(int sliderHeight) {
		this.sliderHeight = sliderHeight;
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
	public void setLabelPosition(SliderLabelPosition labelPosition) {
		this.labelPosition = labelPosition;
	}

	/**
	 * Self explanatory.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Possible sides for a slider's label.
	 *
	 * @author Skander
	 *
	 */
	public enum SliderLabelPosition {
		TOP, BOTTOM, LEFT, RIGHT;
	}
}