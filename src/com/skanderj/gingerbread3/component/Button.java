package com.skanderj.gingerbread3.component;

import com.skanderj.gingerbread3.core.G3Application;
import com.skanderj.gingerbread3.core.object.G3Action;
import com.skanderj.gingerbread3.input.Mouse;

/**
 * Represents an abstract button, basis for other button classes which can
 * implement their rendering the way they please. See GStraightEdgesButton and
 * GRoundEdgesButton for basic, ready-to-be-used examples.
 *
 * @author Skander
 *
 */
public abstract class Button extends Component {
	protected ComponentState previousState, state;
	protected G3Action[] g3Actions;
	protected boolean hasFocus, mouseWasIn;

	/**
	 * Basic constructor: position.
	 */
	public Button(final G3Application g3Application) {
		super(g3Application);
		this.previousState = ComponentState.IDLE;
		this.state = ComponentState.IDLE;
		this.g3Actions = new G3Action[4];
		// Set default actions (do nothing) for every currentState
		for (int index = 0; index < this.g3Actions.length; index += 1) {
			this.g3Actions[index] = G3Action.DEFAULT_DO_NOTHING;
		}
		this.hasFocus = false;
	}

	/**
	 * This is where all the logic of the button happens. We check the mouse
	 * position and the mouse left click, and we deduce the currentState of the
	 * button then run the appropriate button actions accordingly.
	 */
	@Override
	public void update() {
		this.previousState = this.state;
		final int mouseX = this.g3Application.mouse().getX(), mouseY = this.g3Application.mouse().getY();
		final boolean mouseIn = this.containsMouse(mouseX, mouseY), mouseClicked = this.g3Application.mouse().isButtonDown(Mouse.BUTTON_LEFT);
		if (mouseIn && mouseClicked && !this.hasFocus) {
			this.hasFocus = true;
		}
		if (mouseClicked && this.hasFocus && this.mouseWasIn) {
			this.state = ComponentState.HELD;
			this.mouseWasIn = true;
		} else if (mouseIn && !mouseClicked) {
			this.state = ComponentState.HOVERED;
			this.hasFocus = false;
			this.mouseWasIn = true;
		} else {
			this.state = ComponentState.IDLE;
			this.hasFocus = false;
			this.mouseWasIn = false;
		}
		if ((this.previousState == ComponentState.HELD) && ((this.state == ComponentState.IDLE) || (this.state == ComponentState.HOVERED)) && mouseIn) {
			this.state = ComponentState.ACTIVE;
		}
		this.g3Actions[this.state.getIdentifier()].execute();
	}

	/**
	 * Sets the button actions that will be executed when the provided currentState
	 * is the current currentState.
	 */
	public void setG3ActionForState(final ComponentState state, final G3Action g3Action) {
		this.g3Actions[state.getIdentifier()] = g3Action;
	}

	/**
	 * Related to global components management. We can only switch focus out of the
	 * button if it's completely idle.
	 */
	@Override
	public final boolean canChangeFocus() {
		return this.state == ComponentState.IDLE;
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
	public G3Action[] getG3Actions() {
		return this.g3Actions;
	}

	/**
	 * Self explanatory. Can be used to set multiple actions at once.
	 */
	public void setActions(final G3Action[] actions) {
		this.g3Actions = actions;
	}
}
