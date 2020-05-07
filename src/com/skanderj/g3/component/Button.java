package com.skanderj.g3.component;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.skanderj.g3.component.action.ButtonAction;
import com.skanderj.g3.inputdevice.Keyboard;
import com.skanderj.g3.inputdevice.Mouse;
import com.skanderj.g3.util.GraphicString;
import com.skanderj.g3.window.Window;

public abstract class Button implements Component {
	public static final Color TRANSPARENT_SHADE = new Color(0f, 0f, 0f, 0f);

	protected int x, y, width, height;
	protected GraphicString label;
	protected Color backgroundColor, borderColor;
	protected ButtonState previousState, state;
	protected ButtonAction[] actions;
	protected boolean hasFocus, mouseWasIn;

	public Button(int x, int y, int width, int height, GraphicString label, Color backgroundColor, Color borderColor) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.label = label;
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
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

	@Override
	public boolean containsMouse(int x, int y) {
		return new Rectangle(this.x, this.y, this.width, this.height).contains(x, y);
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

	public final GraphicString getLabel() {
		return this.label;
	}

	public final Color getBackgroundColor() {
		return this.backgroundColor;
	}

	public final Color getBorderColor() {
		return this.borderColor;
	}

	public final ButtonAction[] getActions() {
		return this.actions;
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

	public final void setLabel(GraphicString label) {
		this.label = label;
	}

	public final void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public final void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	public final void setActions(ButtonAction[] actions) {
		this.actions = actions;
	}

	public static class StraightEdge extends Button {

		public StraightEdge(int x, int y, int width, int height, GraphicString label, Color backgroundColor, Color borderColor) {
			super(x, y, width, height, label, backgroundColor, borderColor);
		}

		@Override
		public void render(Window window, Graphics2D graphics, Object... args) {
			graphics.setColor(this.backgroundColor);
			graphics.fillRect(this.x, this.y, this.width, this.height);
			this.label.drawCentered(graphics, this.x, this.y, this.width, this.height);
			graphics.setColor(this.borderColor);
			graphics.drawRect(this.x, this.y, this.width, this.height);
		}
	}

	public static class RoundEdge extends Button {
		private int borderIncline;

		public RoundEdge(int x, int y, int width, int height, GraphicString label, Color backgroundColor, Color borderColor, int borderIncline) {
			super(x, y, width, height, label, backgroundColor, borderColor);
			this.borderIncline = borderIncline;
		}

		@Override
		public void render(Window window, Graphics2D graphics, Object... args) {
			graphics.setColor(this.backgroundColor);
			graphics.fillRoundRect(this.x, this.y, this.width, this.height, this.borderIncline, this.borderIncline);
			this.label.drawCentered(graphics, this.x, this.y, this.width, this.height);
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
