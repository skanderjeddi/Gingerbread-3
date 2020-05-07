package com.skanderj.g3.component;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.skanderj.g3.inputdevice.Keyboard;
import com.skanderj.g3.inputdevice.Mouse;
import com.skanderj.g3.util.GraphicString;
import com.skanderj.g3.util.Utilities;
import com.skanderj.g3.window.Window;

public class Slider implements Component {
	private int x, y, width, height, sliderWidth, sliderHeight, sliderX;
	private float minimumValue, maximumValue;
	private GraphicString label;
	private SliderLabelPosition labelPosition;
	private Color color;
	private boolean hasFocus, globalFocus;

	public Slider(int x, int y, int width, int height, int sliderWidth, int sliderHeight, float min, float max, float defaultValue, Color color) {
		this(x, y, width, height, sliderWidth, sliderHeight, min, max, defaultValue, color, null, null);
	}

	public Slider(int x, int y, int width, int height, int sliderWidth, int sliderHeight, float min, float max, float defaultValue, Color color, GraphicString label, SliderLabelPosition position) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sliderWidth = sliderWidth;
		this.sliderHeight = sliderHeight;
		this.minimumValue = Math.min(min, max);
		this.maximumValue = Math.max(min, max);
		this.sliderX = x + (int) Utilities.map(defaultValue, this.minimumValue, this.maximumValue, 0, width, true);
		this.label = label;
		this.labelPosition = position;
		this.color = color;
		this.hasFocus = false;
		this.globalFocus = false;
	}

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

	@Override
	public synchronized final void render(Window window, Graphics2D graphics, Object... args) {
		graphics.setColor(this.color);
		graphics.fillRect(this.x, this.y, this.width, this.height);
		graphics.setColor(this.color.darker());
		graphics.fillRect(this.sliderX, this.y - (this.sliderHeight / 4), this.sliderWidth, this.height + (this.sliderHeight / 2));
		if (!this.label.isEmpty()) {
			switch (this.labelPosition) {
			case TOP:
				this.label.draw(graphics, this.x, this.y - this.label.getHeight(graphics));
				break;
			case BOTTOM:
				this.label.draw(graphics, this.x, this.y + this.height + this.label.getAugmentedHeight(graphics));
				break;
			case LEFT:
				this.label.drawCenteredWidthless(graphics, this.x - 10 - this.label.getWidth(graphics), this.y - (this.sliderHeight / 2), this.height + this.sliderHeight);
				break;
			case RIGHT:
				this.label.drawCenteredWidthless(graphics, this.x + this.width + 10, this.y - (this.sliderHeight / 2), this.height + this.sliderHeight);
				break;
			}
		}
	}

	@Override
	public boolean canChangeFocus() {
		return !this.hasFocus;
	}

	@Override
	public void grantFocus() {
		this.globalFocus = true;
	}

	@Override
	public void revokeFocus() {
		this.globalFocus = false;
	}

	@Override
	public boolean containsMouse(int x, int y) {
		return new Rectangle(this.x, this.y, this.width, this.height).contains(x, y);
	}

	public final float getValue() {
		return Utilities.map(this.sliderX, this.x, this.x + this.width, this.minimumValue, this.maximumValue, true);
	}

	public final int getX() {
		return this.x;
	}

	public final int getY() {
		return this.y;
	}

	public final int getWidth() {
		return this.width;
	}

	public final int getHeight() {
		return this.height;
	}

	public final int getSliderWidth() {
		return this.sliderWidth;
	}

	public final int getSliderHeight() {
		return this.sliderHeight;
	}

	public final int getSliderX() {
		return this.sliderX;
	}

	public final float getMinimumValue() {
		return this.minimumValue;
	}

	public final float getMaximumValue() {
		return this.maximumValue;
	}

	public final GraphicString getLabel() {
		return this.label;
	}

	public final SliderLabelPosition getLabelPosition() {
		return this.labelPosition;
	}

	public final Color getColor() {
		return this.color;
	}

	public final void setX(int x) {
		this.x = x;
	}

	public final void setY(int y) {
		this.y = y;
	}

	public final void setWidth(int width) {
		this.width = width;
	}

	public final void setHeight(int height) {
		this.height = height;
	}

	public final void setSliderWidth(int sliderWidth) {
		this.sliderWidth = sliderWidth;
	}

	public final void setSliderHeight(int sliderHeight) {
		this.sliderHeight = sliderHeight;
	}

	public final void setSliderX(int sliderX) {
		this.sliderX = sliderX;
	}

	public final void setMinimumValue(float minimumValue) {
		this.minimumValue = minimumValue;
	}

	public final void setMaximumValue(float maximumValue) {
		this.maximumValue = maximumValue;
	}

	public final void setLabel(GraphicString label) {
		this.label = label;
	}

	public final void setLabelPosition(SliderLabelPosition labelPosition) {
		this.labelPosition = labelPosition;
	}

	public final void setColor(Color color) {
		this.color = color;
	}

	public static enum SliderLabelPosition {
		TOP, BOTTOM, LEFT, RIGHT;
	}
}
