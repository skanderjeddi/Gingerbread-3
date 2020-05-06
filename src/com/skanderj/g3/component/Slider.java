package com.skanderj.g3.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import com.skanderj.g3.inputdevice.Keyboard;
import com.skanderj.g3.inputdevice.Mouse;
import com.skanderj.g3.io.FontManager;
import com.skanderj.g3.util.Utilities;
import com.skanderj.g3.window.Window;

public class Slider implements Component {
	private int x, y, width, height, sliderWidth, sliderHeight, sliderX;
	private float min, max;
	private String label;
	private Color color;
	private boolean hasFocus, globalFocus;

	public Slider(int x, int y, int width, int height, int sliderWidth, int sliderHeight, float min, float max, float defaultValue, Color color, String label) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sliderWidth = sliderWidth;
		this.sliderHeight = sliderHeight;
		this.min = Math.min(min, max);
		this.max = Math.max(min, max);
		this.sliderX = x + (int) Utilities.map(defaultValue, this.min, this.max, 0, width, true);
		this.label = label;
		this.color = color;
		this.hasFocus = false;
		this.globalFocus = false;
	}

	@Override
	public synchronized final void update(double delta, Keyboard keyboard, Mouse mouse, Object... args) {
		if ((new Rectangle(this.x, this.y, this.width, this.height).contains(mouse.getX(), mouse.getY()) || this.hasFocus) && globalFocus) {
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
		FontMetrics fontMetrics = graphics.getFontMetrics();
		graphics.fillRect(this.sliderX, this.y - (this.sliderHeight / 4), this.sliderWidth, this.height + (this.sliderHeight / 2));
		if (!this.label.isEmpty()) {
			this.drawCenteredString(graphics, this.label, this.x - 10 - fontMetrics.stringWidth(this.label), this.y - (this.sliderHeight / 2), this.height + this.sliderHeight, FontManager.getFont("roboto", 18), this.color.brighter());
		}
	}

	public final int drawCenteredString(Graphics2D graphics, String string, int x0, int y0, int height, Font font, Color color) {
		graphics.setFont(font);
		graphics.setColor(color);
		final FontMetrics fontMetrics = graphics.getFontMetrics();
		final Rectangle2D rectangle2d = fontMetrics.getStringBounds(string, graphics);
		final int y = ((height - (int) rectangle2d.getHeight()) / 2) + fontMetrics.getAscent();
		graphics.drawString(string, x0, y0 + y);
		return y0 + y;
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

	public final int getSliderHeight() {
		return this.sliderHeight;
	}

	public final int getSliderX() {
		return this.sliderX;
	}

	public final float getMin() {
		return this.min;
	}

	public final float getMax() {
		return this.max;
	}

	public final float getValue() {
		return Utilities.map(this.sliderX, this.x, this.x + this.width, this.min, this.max, true);
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

	public final void setSliderHeight(int sliderHeight) {
		this.sliderHeight = sliderHeight;
	}

	public final void setSliderX(int sliderX) {
		this.sliderX = sliderX;
	}

	public final void setMin(float min) {
		this.min = min;
	}

	public final void setMax(float max) {
		this.max = max;
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
}
