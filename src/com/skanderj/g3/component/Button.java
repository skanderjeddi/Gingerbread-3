package com.skanderj.g3.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import com.skanderj.g3.component.action.ButtonAction;
import com.skanderj.g3.inputdevice.Keyboard;
import com.skanderj.g3.inputdevice.Mouse;
import com.skanderj.g3.window.Window;

public abstract class Button implements Component {
	public static final Color TRANSPARENT_SHADE = new Color(0f, 0f, 0f, 0f);

	protected int x, y, width, height;
	protected String string;
	protected Font font;
	protected Color backgroundColor, borderColor, textColor, shadeColor;
	protected ButtonState previousState, state;
	protected ButtonAction[] actions;
	protected boolean hasFocus, mouseWasIn;

	public Button(int x, int y, int width, int height, String string, Font font, Color backgroundColor, Color borderColor, Color textColor, Color shadeColor) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.string = string;
		this.font = font;
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
		this.textColor = textColor;
		this.shadeColor = shadeColor;
		this.previousState = ButtonState.IDLE;
		this.state = ButtonState.IDLE;
		this.actions = new ButtonAction[4];
		for (int index = 0; index < this.actions.length; index += 1) {
			this.actions[index] = new ButtonAction.DefaultButtonAction();
		}
		this.hasFocus = false;
	}

	@Override
	public void update(double delta, Keyboard keyboard, Mouse mouse, Object... args) {
		this.previousState = this.state;
		int mouseX = mouse.getX(), mouseY = mouse.getY();
		boolean mouseIn = new Rectangle(this.x, this.y, this.width, this.height).contains(mouseX, mouseY), mouseClicked = mouse.isButtonDown(Mouse.BUTTON_LEFT);
		if (mouseIn && mouseClicked && !this.hasFocus) {
			this.hasFocus = true;
		}
		if (mouseClicked && this.hasFocus && this.mouseWasIn) {
			this.state = ButtonState.CLICKED;
			this.mouseWasIn = true;
		} else if (mouseIn && !mouseClicked) {
			this.state = ButtonState.HOVERED;
			this.hasFocus = false;
			this.mouseWasIn = true;
		} else {
			this.state = ButtonState.IDLE;
			this.hasFocus = false;
			this.mouseWasIn = false;
		}
		if ((this.previousState == ButtonState.CLICKED) && ((this.state == ButtonState.IDLE) || (this.state == ButtonState.HOVERED)) && mouseIn) {
			this.state = ButtonState.CLICK_FRAME;
		}
		this.actions[this.state.getIdentifier()].execute(delta, keyboard, mouse);
	}

	public final void setButtonAction(ButtonState state, ButtonAction action) {
		this.actions[state.getIdentifier()] = action;
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

	public final String getString() {
		return this.string;
	}

	public final Font getFont() {
		return this.font;
	}

	public final Color getBackgroundColor() {
		return this.backgroundColor;
	}

	public final Color getBorderColor() {
		return this.borderColor;
	}

	public final Color getTextColor() {
		return this.textColor;
	}

	public final Color getShadeColor() {
		return this.shadeColor;
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

	public final void setString(String string) {
		this.string = string;
	}

	public final void setFont(Font font) {
		this.font = font;
	}

	public final void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public final void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	public final void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	public final void setShadeColor(Color shadeColor) {
		this.shadeColor = shadeColor;
	}

	@Override
	public boolean canChangeFocus() {
		return this.state == ButtonState.IDLE;
	}

	@Override
	public void grantFocus() {
		return;
	}

	@Override
	public void revokeFocus() {
		return;
	}

	public boolean containsMouse(Mouse mouse) {
		return new Rectangle(this.x, this.y, this.width, this.height).contains(mouse.getX(), mouse.getY());
	}

	public final void drawCenteredString(Graphics2D graphics, String string, int x0, int y0, int width, int height, Font font, Color color) {
		graphics.setFont(font);
		graphics.setColor(color);
		final FontMetrics fontMetrics = graphics.getFontMetrics();
		final Rectangle2D rectangle2d = fontMetrics.getStringBounds(string, graphics);
		final int x = (width - (int) rectangle2d.getWidth()) / 2;
		final int y = ((height - (int) rectangle2d.getHeight()) / 2) + fontMetrics.getAscent();
		graphics.drawString(string, x0 + x, y0 + y);
	}

	public final void drawShadedCenteredString(Graphics2D graphics, String string, int x0, int y0, int width, int height, Font font, Color color, Color shadeColor) {
		graphics.setFont(font);
		graphics.setColor(color);
		final FontMetrics fontMetrics = graphics.getFontMetrics();
		final Rectangle2D rectangle2d = fontMetrics.getStringBounds(string, graphics);
		final int x = (width - (int) rectangle2d.getWidth()) / 2;
		final int y = ((height - (int) rectangle2d.getHeight()) / 2) + fontMetrics.getAscent();
		graphics.setColor(shadeColor);
		graphics.drawString(string, x0 + x + 1, y0 + y + 1);
		graphics.setColor(color);
		graphics.drawString(string, (x0 + x) - 1, (y0 + y) - 1);
	}

	@Override
	public boolean containsMouse(int x, int y) {
		return new Rectangle(this.x, this.y, this.width, this.height).contains(x, y);
	}

	public static class StraightEdge extends Button {

		public StraightEdge(int x, int y, int width, int height, String string, Font font, Color backgroundColor, Color borderColor, Color textColor, Color shadeColor) {
			super(x, y, width, height, string, font, backgroundColor, borderColor, textColor, shadeColor);
		}

		@Override
		public void render(Window window, Graphics2D graphics, Object... args) {
			graphics.setColor(this.backgroundColor);
			graphics.fillRect(this.x, this.y, this.width, this.height);
			this.drawShadedCenteredString(graphics, this.string, this.x, this.y, this.width, this.height, this.font, this.textColor, this.shadeColor);
			graphics.setColor(this.borderColor);
			graphics.drawRect(this.x, this.y, this.width, this.height);
		}
	}

	public static class RoundEdge extends Button {
		private int borderIncline;

		public RoundEdge(int x, int y, int width, int height, String string, Font font, Color backgroundColor, Color borderColor, Color textColor, Color shadeColor, int borderIncline) {
			super(x, y, width, height, string, font, backgroundColor, borderColor, textColor, shadeColor);
			this.borderIncline = borderIncline;
		}

		@Override
		public void render(Window window, Graphics2D graphics, Object... args) {
			graphics.setColor(this.backgroundColor);
			graphics.fillRoundRect(this.x, this.y, this.width, this.height, this.borderIncline, this.borderIncline);
			this.drawShadedCenteredString(graphics, this.string, this.x, this.y, this.width, this.height, this.font, this.textColor, this.shadeColor);
			graphics.setColor(this.borderColor);
			graphics.drawRoundRect(this.x, this.y, this.width, this.height, this.borderIncline, this.borderIncline);
		}

		public int getBorderIncline() {
			return this.borderIncline;
		}

		public void setBorderIncline(int borderIncline) {
			this.borderIncline = borderIncline;
		}
	}
}
