package com.skanderj.gingerbead3.component;

import java.util.ArrayList;
import java.util.List;

import com.skanderj.gingerbead3.component.Button.ButtonState;
import com.skanderj.gingerbead3.component.action.ButtonAction;
import com.skanderj.gingerbead3.input.Keyboard;
import com.skanderj.gingerbead3.input.Mouse;
import com.skanderj.gingerbead3.log.Logger;
import com.skanderj.gingerbead3.log.Logger.LogLevel;

/**
 * Represents an abstract selector, basis for other selector classes which can
 * implement their rendering the way they please. See G3Selector for a basic,
 * ready-to-be-used example.
 *
 * @author Skander
 *
 */
public abstract class Selector extends Component {
	/**
	 * A class representing each selector arrow.
	 *
	 * @author Skander
	 *
	 */
	public static class SelectorArrow {
		protected ButtonState currentState, previousState;
		protected boolean hasFocus;
		protected boolean mouseWasIn;
		private final ButtonAction[] actions;

		public SelectorArrow() {
			this.currentState = ButtonState.IDLE;
			this.previousState = ButtonState.IDLE;
			this.hasFocus = false;
			this.mouseWasIn = false;
			this.actions = new ButtonAction[4];
			// Set default action (do nothing) for every currentState
			for (int index = 0; index < this.actions.length; index += 1) {
				this.actions[index] = new ButtonAction.DefaultButtonAction();
			}
		}

		public final void setAction(final ButtonState state, final ButtonAction action) {
			if (state == ButtonState.ON_CLICK) {
				Logger.log(Selector.SelectorArrow.class, LogLevel.ERROR, "Can't change the on click behavior of a selector arrow");
			} else {
				this.actions[state.getIdentifier()] = action;
			}
		}
	}

	// The list of options
	protected List<String> options;
	// Default (first) option and the current option to display
	protected String defaultOption, currentOption;
	// Current option index for tracking
	protected int currentOptionIndex;
	// Arrow objects to encapsulate multiple fields
	protected final SelectorArrow leftArrow, rightArrow;

	/**
	 * Nothing to say, calls the 2nd constructor with the first element of the
	 * options array as the default options.
	 */
	public Selector(final String[] optionsArray) {
		this(optionsArray, optionsArray[0]);
	}

	/**
	 * Pretty self explanatory.
	 */
	public Selector(final String[] optionsArray, final String defaultOption) {
		this.options = new ArrayList<String>();
		for (final String option : optionsArray) {
			this.options.add(option);
		}
		if (!this.options.contains(defaultOption)) {
			this.options.add(defaultOption);
		}
		this.currentOption = defaultOption;
		this.currentOptionIndex = this.options.lastIndexOf(defaultOption);
		this.leftArrow = new SelectorArrow();
		this.rightArrow = new SelectorArrow();
		this.leftArrow.actions[ButtonState.ON_CLICK.getIdentifier()] = args -> {
			Selector.this.currentOptionIndex -= 1;
			if (Selector.this.currentOptionIndex < 0) {
				Selector.this.currentOptionIndex = Selector.this.options.size() - 1;
			}
		};
		this.rightArrow.actions[ButtonState.ON_CLICK.getIdentifier()] = args -> {
			Selector.this.currentOptionIndex += 1;
			Selector.this.currentOptionIndex %= Selector.this.options.size();
		};
	}

	/**
	 * This is where all the logic of the selector happens. We check the mouse
	 * position and the mouse left click, and we deduce the currentState of the
	 * selector arrows then run the appropriate action accordingly.
	 */
	@Override
	public synchronized void update(final double delta, final Keyboard keyboard, final Mouse mouse, final Object... args) {
		// Set the previous state for each individual arrow on the last update
		this.leftArrow.previousState = this.leftArrow.currentState;
		this.rightArrow.previousState = this.rightArrow.currentState;
		// Get the mouse position
		final int mouseX = mouse.getX(), mouseY = mouse.getY();
		// Left arrow handling, this block magically works and it took me a lot of time
		// but I couldn't for the life of me explain it..
		{
			final boolean mouseInLeft = this.leftArrowContainsMouse(mouseX, mouseY), mouseClicked = mouse.isButtonDown(Mouse.BUTTON_LEFT);
			if (mouseInLeft && mouseClicked && !this.leftArrow.hasFocus) {
				this.leftArrow.hasFocus = true;
			}
			if (mouseClicked && this.leftArrow.hasFocus && this.leftArrow.mouseWasIn) {
				this.leftArrow.currentState = ButtonState.HELD;
				this.leftArrow.mouseWasIn = true;
			} else if (mouseInLeft && !mouseClicked) {
				this.leftArrow.currentState = ButtonState.HOVERED;
				this.leftArrow.hasFocus = false;
				this.leftArrow.mouseWasIn = true;
			} else {
				this.leftArrow.currentState = ButtonState.IDLE;
				this.leftArrow.hasFocus = false;
				this.leftArrow.mouseWasIn = false;
			}
			if ((this.leftArrow.previousState == ButtonState.HELD) && ((this.leftArrow.currentState == ButtonState.IDLE) || (this.leftArrow.currentState == ButtonState.HOVERED)) && mouseInLeft) {
				this.leftArrow.currentState = ButtonState.ON_CLICK;
			}
		}
		// Right arrow handling, this block magically works and it took me a lot of time
		// but I couldn't for the life of me explain it..
		{
			final boolean mouseInRight = this.rightArrowContainsMouse(mouseX, mouseY), mouseClicked = mouse.isButtonDown(Mouse.BUTTON_LEFT);
			if (mouseInRight && mouseClicked && !this.rightArrow.hasFocus) {
				this.rightArrow.hasFocus = true;
			}
			if (mouseClicked && this.rightArrow.hasFocus && this.rightArrow.mouseWasIn) {
				this.rightArrow.currentState = ButtonState.HELD;
				this.rightArrow.mouseWasIn = true;
			} else if (mouseInRight && !mouseClicked) {
				this.rightArrow.currentState = ButtonState.HOVERED;
				this.rightArrow.hasFocus = false;
				this.rightArrow.mouseWasIn = true;
			} else {
				this.rightArrow.currentState = ButtonState.IDLE;
				this.rightArrow.hasFocus = false;
				this.rightArrow.mouseWasIn = false;
			}
			if ((this.rightArrow.previousState == ButtonState.HELD) && ((this.rightArrow.currentState == ButtonState.IDLE) || (this.rightArrow.currentState == ButtonState.HOVERED)) && mouseInRight) {
				this.rightArrow.currentState = ButtonState.ON_CLICK;
			}
		}
		this.leftArrow.actions[this.leftArrow.currentState.getIdentifier()].execute(delta, keyboard, mouse);
		this.rightArrow.actions[this.rightArrow.currentState.getIdentifier()].execute(delta, keyboard, mouse);
		this.currentOption = this.options.get(this.currentOptionIndex);
	}

	@Override
	public final boolean canChangeFocus() {
		return (this.leftArrow.currentState == ButtonState.IDLE) && (this.rightArrow.currentState == ButtonState.IDLE);
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
	 * Dealt with differently.
	 */
	@Override
	public boolean containsMouse(final int x, final int y) {
		return this.leftArrowContainsMouse(x, y) || this.rightArrowContainsMouse(x, y);
	}

	/**
	 * Graphic implementation dependent.
	 */
	public abstract boolean leftArrowContainsMouse(int x, int y);

	/**
	 * Graphic implementation dependent.
	 */
	public abstract boolean rightArrowContainsMouse(int x, int y);

	/**
	 * Self explanatory.
	 */
	public List<String> getOptions() {
		return this.options;
	}

	/**
	 * Self explanatory.
	 */
	public String getDefaultOption() {
		return this.defaultOption;
	}

	/**
	 * Self explanatory.
	 */
	public String getCurrentOption() {
		return this.currentOption;
	}

	/**
	 * Self explanatory.
	 */
	public int getCurrentOptionIndex() {
		return this.currentOptionIndex;
	}

	/**
	 * Self explanatory.
	 */
	public SelectorArrow getLeftArrow() {
		return this.leftArrow;
	}

	/**
	 * Self explanatory.
	 */
	public SelectorArrow getRightArrow() {
		return this.rightArrow;
	}

	/**
	 * Self explanatory.
	 */
	public void setOptions(final List<String> options) {
		this.options = options;
	}
}
