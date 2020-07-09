package com.skanderj.gingerbread3.component;

import com.skanderj.gingerbread3.core.G3Application;
import com.skanderj.gingerbread3.input.Mouse;
import com.skanderj.gingerbread3.scheduler.Task;

/**
 * Represents an abstract checkbox. See GCheckbox for an actual implementation.
 *
 * @author Skander
 *
 */
public abstract class Checkbox extends Component {
	protected ComponentState previousState, state;
	protected boolean isChecked;
	protected boolean hasFocus, mouseWasIn;
	protected Task[] tasks;

	public Checkbox(final G3Application g3Application) {
		super(g3Application);
		this.isChecked = false;
		this.previousState = ComponentState.IDLE;
		this.state = ComponentState.IDLE;
		this.tasks = new Task[5];
		// Set default action (do nothing) for every currentState
		for (int index = 0; index < this.tasks.length; index += 1) {
			this.tasks[index] = Task.DEFAULT_DO_NOTHING;
		}
		this.hasFocus = false;
	}

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
		if (this.state == ComponentState.ACTIVE) {
			this.isChecked = !this.isChecked;
			this.tasks[4].execute(this.isChecked);
		}
		this.tasks[this.state.getIdentifier()].execute();
	}

	/**
	 * Sets the component action that will be executed when the provided
	 * currentState is the current currentState.
	 */
	public void setTaskForState(final ComponentState state, final Task task) {
		this.tasks[state.getIdentifier()] = task;
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
	public Task[] getTasks() {
		return this.tasks;
	}

	/**
	 * Self explanatory. Can be used to set multiple tasks at once.
	 */
	public void setActions(final Task[] actions) {
		this.tasks = actions;
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
	public void setOnSwitchTask(final Task onSwitchTask) {
		this.tasks[4] = onSwitchTask;
	}
}
