package com.skanderj.g3.component;

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
public enum ButtonState {
	IDLE(0), HOVERED(1), CLICKED(2), ON_ACTUAL_CLICK(3);

	// An identifier for easier access in other classes (#Button)
	private int identifier;

	private ButtonState(int identifier) {
		this.identifier = identifier;
	}

	public int getIdentifier() {
		return this.identifier;
	}
}
