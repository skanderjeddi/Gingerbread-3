package com.skanderj.g3.component;

import com.skanderj.g3.component.action.ButtonAction;
import com.skanderj.g3.window.inputdevice.Keyboard;
import com.skanderj.g3.window.inputdevice.Mouse;

/**
 * Represents an abstract button, basis for other button classes which can
 * implement their rendering the way they please. See G3SEButton and G3REButton
 * for basic, ready-to-be-used examples.
 *
 * @author Skander
 *
 */
public abstract class Button implements Component {
	protected ButtonState previousState, state;
	protected ButtonAction[] actions;
	protected boolean hasFocus, mouseWasIn;

	/**
	 * Basic constructor: position.
	 */
	public Button() {
		this.previousState = ButtonState.IDLE;
		this.state = ButtonState.IDLE;
		this.actions = new ButtonAction[4];
		// Set default action (do nothing) for every currentState
		for (int index = 0; index < this.actions.length; index += 1) {
			this.actions[index] = new ButtonAction.DefaultButtonAction();
		}
		this.hasFocus = false;
	}

	/**
	 * This is where all the logic of the button happens. We check the mouse
	 * position and the mouse left click, and we deduce the currentState of the
	 * button then run the appropriate button action accordingly.
	 */
	@Override
	public void update(double delta, Keyboard keyboard, Mouse mouse, Object... args) {
		this.previousState = this.state;
		int mouseX = mouse.getX(), mouseY = mouse.getY();
		boolean mouseIn = this.containsMouse(mouseX, mouseY), mouseClicked = mouse.isButtonDown(Mouse.BUTTON_LEFT);
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
	 * Sets the button action that will be executed when the provided currentState
	 * is the current currentState.
	 */
	public void setButtonAction(ButtonState state, ButtonAction action) {
		this.actions[state.getIdentifier()] = action;
	}

	/**
	 * Related to global components management. We can only switch focus out of the
	 * button if it's completely idle.
	 */
	@Override
	public final boolean canChangeFocus() {
		return this.state == ButtonState.IDLE;
	}

	/**
	 * Related to global components management. Focus management is different for
	 * buttons so these do nothing.
	 */
	@Override
	public final void grantFocus() {
		return;
	}

	/**
	 * Related to global components management. Focus management is different for
	 * buttons so these do nothing.
	 */
	@Override
	public final void revokeFocus() {
		return;
	}

	/**
	 * Self explanatory.
	 */
	public ButtonAction[] getActions() {
		return this.actions;
	}

	/**
	 * Self explanatory. Can be used to set multiple actions at once.
	 */
	public void setActions(ButtonAction[] actions) {
		this.actions = actions;
	}

	/**
	 * Represents all the possible states of a button. IDLE: mouse is out and the
	 * button doesn't have focus. HOVERED: mouse is over the button, not clicked and
	 * no focus yet. CLICKED: mouse is over the button and clicked or mouse is
	 * clicked and focus is on the button. Basically means you can click and leave
	 * the button area to cancel your click. ON_ACTUAL_CLICK: On the transition
	 * between CLICKED and IDLE or between CLICKED and HOVERED, where you should
	 * assign the actual action to your button.
	 *
	 * @author Skander
	 *
	 */
	public static enum ButtonState {
		IDLE(0), HOVERED(1), CLICKED(2), ON_ACTUAL_CLICK(3);

		// An identifier for easier access in other classes (#Button)
		private final int identifier;

		private ButtonState(int identifier) {
			this.identifier = identifier;
		}

		public final int getIdentifier() {
			return this.identifier;
		}
	}
}
