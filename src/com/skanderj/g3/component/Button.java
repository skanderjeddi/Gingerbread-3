package com.skanderj.g3.component;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.skanderj.g3.component.action.ButtonAction;
import com.skanderj.g3.inputdevice.Keyboard;
import com.skanderj.g3.inputdevice.Mouse;
import com.skanderj.g3.util.GraphicString;
import com.skanderj.g3.window.Window;

/**
 * Represents an abstract button, basis for other button classes which can
 * implement their rendering the way they please. See Button#StraightEdge and
 * Button#RoundEdge for basic examples.
 * 
 * @author Skander
 *
 */
public abstract class Button implements Component {
	protected int x, y, width, height;
	protected GraphicString label;
	protected Color backgroundColor, borderColor;
	protected ButtonState previousState, state;
	protected ButtonAction[] actions;
	protected boolean hasFocus, mouseWasIn;

	/**
	 * Basic constructor: position, size, label, color and border color.
	 */
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
		// Set default action (do nothing) for every state
		for (int index = 0; index < this.actions.length; index += 1) {
			this.actions[index] = new ButtonAction.DefaultButtonAction();
		}
		this.hasFocus = false;
	}

	/**
	 * This is where all the logic of the button happens. We check the mouse
	 * position and the mouse left click, and we deduce the state of the button then
	 * run the appropriate button action accordingly.
	 */
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
			this.state = ButtonState.ON_ACTUAL_CLICK;
		}
		this.actions[this.state.getIdentifier()].execute(delta, keyboard, mouse);
	}

	/**
	 * Sets the button action that will be executed when the provided state is the
	 * current state.
	 */
	public final void setButtonAction(ButtonState state, ButtonAction action) {
		this.actions[state.getIdentifier()] = action;
	}

	/**
	 * Related to global components management. We can only switch focus out of the
	 * button if it's completely idle.
	 */
	@Override
	public boolean canChangeFocus() {
		return this.state == ButtonState.IDLE;
	}

	/**
	 * Related to global components management. Focus management is different for
	 * buttons so these do nothing.
	 */
	@Override
	public void grantFocus() {
		return;
	}

	/**
	 * Related to global components management. Focus management is different for
	 * buttons so these do nothing.
	 */
	@Override
	public void revokeFocus() {
		return;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public boolean containsMouse(int x, int y) {
		return new Rectangle(this.x, this.y, this.width, this.height).contains(x, y);
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
	public final GraphicString getLabel() {
		return this.label;
	}

	/**
	 * Self explanatory.
	 */
	public final Color getBackgroundColor() {
		return this.backgroundColor;
	}

	/**
	 * Self explanatory.
	 */
	public final Color getBorderColor() {
		return this.borderColor;
	}

	/**
	 * Self explanatory.
	 */
	public final ButtonAction[] getActions() {
		return this.actions;
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
	public final void setLabel(GraphicString label) {
		this.label = label;
	}

	/**
	 * Self explanatory.
	 */
	public final void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Self explanatory.
	 */
	public final void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	/**
	 * Self explanatory. Can be used to set multiple actions at once.
	 */
	public final void setActions(ButtonAction[] actions) {
		this.actions = actions;
	}

	/**
	 * A straight edges version of the button. Very basic.
	 * 
	 * @author Skander
	 *
	 */
	public static class StraightEdge extends Button {

		public StraightEdge(int x, int y, int width, int height, GraphicString label, Color backgroundColor, Color borderColor) {
			super(x, y, width, height, label, backgroundColor, borderColor);
		}

		/**
		 * Draws a simple rectangle for the background, draws the border and the label.
		 */
		@Override
		public void render(Window window, Graphics2D graphics, Object... args) {
			graphics.setColor(this.backgroundColor);
			graphics.fillRect(this.x, this.y, this.width, this.height);
			this.label.drawCentered(graphics, this.x, this.y, this.width, this.height);
			graphics.setColor(this.borderColor);
			graphics.drawRect(this.x, this.y, this.width, this.height);
		}
	}

	/**
	 * A round edges version of the button. Still very basic.
	 * 
	 * @author Skander
	 *
	 */
	public static class RoundEdge extends Button {
		// Border incline = how many pixels will be shaved off at each edge
		private int borderIncline;

		public RoundEdge(int x, int y, int width, int height, GraphicString label, Color backgroundColor, Color borderColor, int borderIncline) {
			super(x, y, width, height, label, backgroundColor, borderColor);
			this.borderIncline = borderIncline;
		}

		/**
		 * Draws a simple round rectangle for the background, draws the border and the
		 * label.
		 */
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
