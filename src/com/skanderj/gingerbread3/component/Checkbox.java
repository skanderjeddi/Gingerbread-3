package com.skanderj.gingerbread3.component;

import com.skanderj.gingerbread3.component.action.ComponentAction;
import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.input.Mouse;

public abstract class Checkbox extends Component {
	protected ComponentState previousState, state;
	protected boolean isChecked;
	protected boolean hasFocus, mouseWasIn;
	protected ComponentAction[] actions;

	public Checkbox(final Game game) {
		super(game);
		this.isChecked = false;
		this.previousState = ComponentState.IDLE;
		this.state = ComponentState.IDLE;
		this.actions = new ComponentAction[5];
		// Set default action (do nothing) for every currentState
		for (int index = 0; index < this.actions.length; index += 1) {
			this.actions[index] = new ComponentAction.DefaultComponentAction();
		}
		this.hasFocus = false;
	}

	@Override
	public void update(final double delta, final Object... args) {
		this.previousState = this.state;
		final int mouseX = this.game.getMouse().getX(), mouseY = this.game.getMouse().getY();
		final boolean mouseIn = this.containsMouse(mouseX, mouseY), mouseClicked = this.game.getMouse().isButtonDown(Mouse.BUTTON_LEFT);
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
			this.state = ComponentState.ON_CLICK;
		}
		if (this.state == ComponentState.ON_CLICK) {
			this.isChecked = !this.isChecked;
			this.actions[4].execute(this.isChecked);
		}
		this.actions[this.state.getIdentifier()].execute(delta);
	}

	/**
	 * Sets the component action that will be executed when the provided
	 * currentState is the current currentState.
	 */
	public void setActionForState(final ComponentState state, final ComponentAction action) {
		this.actions[state.getIdentifier()] = action;
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
	public ComponentAction[] getActions() {
		return this.actions;
	}

	/**
	 * Self explanatory. Can be used to set multiple actions at once.
	 */
	public void setActions(final ComponentAction[] actions) {
		this.actions = actions;
	}

	/**
	 * Self explanatory.
	 */
	public boolean isChecked() {
		return this.isChecked;
	}

	/**
	 * Self explanatory.
	 */
	public void setChecked(final boolean isChecked) {
		this.isChecked = isChecked;
	}

	/**
	 * Self explanatory.
	 */
	public void setOnSwitchAction(final ComponentAction onSwitchAction) {
		this.actions[4] = onSwitchAction;
	}
}
