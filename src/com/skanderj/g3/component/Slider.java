package com.skanderj.g3.component;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.skanderj.g3.inputdevice.Keyboard;
import com.skanderj.g3.inputdevice.Mouse;
import com.skanderj.g3.util.GraphicString;
import com.skanderj.g3.util.Utilities;
import com.skanderj.g3.window.Window;

/**
 * Represents an abstract slider, basis for other slider classes which can
 * implements their rendering the way they please. See Slider#Basic for a basic
 * example.
 *
 * @author Skander
 *
 */
public abstract class Slider implements Component {
	protected int x, y, width, height, sliderWidth, sliderHeight, sliderX;
	protected float minimumValue, maximumValue;
	protected boolean hasFocus, globalFocus;

	/**
	 * Basic constructor: position, size, minimum/maximum value, default value,
	 */
	public Slider(int x, int y, int width, int height, int sliderWidth, int sliderHeight, float min, float max, float defaultValue) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sliderWidth = sliderWidth;
		this.sliderHeight = sliderHeight;
		this.minimumValue = Math.min(min, max);
		this.maximumValue = Math.max(min, max);
		this.sliderX = x + (int) Utilities.map(defaultValue, this.minimumValue, this.maximumValue, 0, width, true);
		this.hasFocus = false;
		this.globalFocus = false;
	}

	/**
	 * This is where all the logic of the slider happens. We check the mouse
	 * position and the mouse left click, and we deduce the currentState of the
	 * slider then move the slider accordingly.
	 */
	@Override
	public synchronized final void update(double delta, Keyboard keyboard, Mouse mouse, Object... args) {
		if ((new Rectangle(this.x, this.y, this.width, this.height).contains(mouse.getX(), mouse.getY()) || this.hasFocus) && this.globalFocus) {
			if (mouse.isButtonDown(Mouse.BUTTON_LEFT)) {
				this.sliderX = (int) Utilities.map(mouse.getX(), this.x, this.x + this.width, this.x, this.x + this.width, true);
				this.hasFocus = true;
			} else {
				this.hasFocus = false;
			}
		}
	}

	/**
	 * Related to global components management. We can only switch focus out of the
	 * slider if it's not currently being moved (represented by the Slider#hasFocus
	 * variable).
	 */
	@Override
	public boolean canChangeFocus() {
		return !this.hasFocus;
	}

	/**
	 * Gives global focus.
	 */
	@Override
	public void grantFocus() {
		this.globalFocus = true;
	}

	/**
	 * Removes global focus.
	 */
	@Override
	public void revokeFocus() {
		this.globalFocus = false;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public boolean containsMouse(int x, int y) {
		return new Rectangle(this.x, this.y, this.width, this.height).contains(x, y);
	}

	/**
	 * Boxes the current value between the minimum and maximum and returns it.
	 */
	public final float getValue() {
		return Utilities.map(this.sliderX, this.x, this.x + this.width, this.minimumValue, this.maximumValue, true);
	}

	/**
	 * Self explanatory.
	 */
	public final int getX() {
		return this.x;
	}

	/**
	 * Self explanatory.
	 */
	public final int getY() {
		return this.y;
	}

	/**
	 * Self explanatory.
	 */
	public final int getWidth() {
		return this.width;
	}

	/**
	 * Self explanatory.
	 */
	public final int getHeight() {
		return this.height;
	}

	/**
	 * Self explanatory.
	 */
	public final int getSliderWidth() {
		return this.sliderWidth;
	}

	/**
	 * Self explanatory.
	 */
	public final int getSliderHeight() {
		return this.sliderHeight;
	}

	/**
	 * Self explanatory.
	 */
	public final int getSliderX() {
		return this.sliderX;
	}

	/**
	 * Self explanatory.
	 */
	public final float getMinimumValue() {
		return this.minimumValue;
	}

	/**
	 * Self explanatory.
	 */
	public final float getMaximumValue() {
		return this.maximumValue;
	}

	/**
	 * Self explanatory.
	 */
	public final void setX(int x) {
		this.x = x;
	}

	/**
	 * Self explanatory.
	 */
	public final void setY(int y) {
		this.y = y;
	}

	/**
	 * Self explanatory.
	 */
	public final void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Self explanatory.
	 */
	public final void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Self explanatory.
	 */
	public final void setSliderWidth(int sliderWidth) {
		this.sliderWidth = sliderWidth;
	}

	/**
	 * Self explanatory.
	 */
	public final void setSliderHeight(int sliderHeight) {
		this.sliderHeight = sliderHeight;
	}

	/**
	 * Self explanatory.
	 */
	public final void setSliderX(int sliderX) {
		this.sliderX = sliderX;
	}

	/**
	 * Self explanatory.
	 */
	public final void setMinimumValue(float minimumValue) {
		this.minimumValue = minimumValue;
	}

	/**
	 * Self explanatory.
	 */
	public final void setMaximumValue(float maximumValue) {
		this.maximumValue = maximumValue;
	}

	/**
	 * Represents a very basic slider with a label on on side.
	 *
	 * @author Skander
	 *
	 */
	public static class Basic extends Slider {
		private GraphicString label;
		private SliderLabelPosition labelPosition;
		private Color color;

		public Basic(int x, int y, int width, int height, int sliderWidth, int sliderHeight, float min, float max, float defaultValue, Color color, GraphicString label, SliderLabelPosition position) {
			super(x, y, width, height, sliderWidth, sliderHeight, min, max, defaultValue);
			this.label = label;
			this.labelPosition = position;
			this.color = color;
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
			graphics.fillRect(this.sliderX - (this.sliderWidth / 2), this.y - (this.sliderHeight / 4), this.sliderWidth, this.height + (this.sliderHeight / 2));
			if (!this.label.isEmpty()) {
				switch (this.labelPosition) {
				case TOP:
					this.label.draw(graphics, this.x, this.y - this.label.getHeight(graphics), this.getValue());
					break;
				case BOTTOM:
					this.label.draw(graphics, this.x, this.y + this.height + this.label.getAugmentedHeight(graphics), this.getValue());
					break;
				case LEFT:
					this.label.drawCenteredWidthless(graphics, this.x - 10 - this.label.getWidth(graphics), this.y - (this.sliderHeight / 2), this.height + this.sliderHeight, this.getValue());
					break;
				case RIGHT:
					this.label.drawCenteredWidthless(graphics, this.x + this.width + 10, this.y - (this.sliderHeight / 2), this.height + this.sliderHeight, this.getValue());
					break;
				}
			}
		}

		/**
		 * Self explanatory.
		 */
		public final GraphicString getLabel() {
			return this.label;
		}

		/**
		 * Self explanatory.
		 */
		public final SliderLabelPosition getLabelPosition() {
			return this.labelPosition;
		}

		/**
		 * Self explanatory.
		 */
		public final Color getColor() {
			return this.color;
		}

		/**
		 * Self explanatory.
		 */
		public final void setLabel(GraphicString label) {
			this.label = label;
		}

		/**
		 * Self explanatory.
		 */
		public final void setLabelPosition(SliderLabelPosition labelPosition) {
			this.labelPosition = labelPosition;
		}

		/**
		 * Self explanatory.
		 */
		public final void setColor(Color color) {
			this.color = color;
		}

		/**
		 * Possible sides for a slider's label.
		 *
		 * @author Skander
		 *
		 */
		public static enum SliderLabelPosition {
			TOP, BOTTOM, LEFT, RIGHT;
		}

	}
}
