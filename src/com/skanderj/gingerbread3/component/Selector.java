package com.skanderj.gingerbread3.component;

import java.util.ArrayList;
import java.util.List;

import com.skanderj.gingerbread3.core.G3Application;
import com.skanderj.gingerbread3.core.object.G3Action;
import com.skanderj.gingerbread3.input.Mouse;
import com.skanderj.gingerbread3.logging.Logger;
import com.skanderj.gingerbread3.logging.Logger.LogLevel;

/**
 * Represents an abstract selector, basis for other selector classes which can
 * implement their rendering the way they please. See GSelector for a basic,
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
		protected ComponentState currentState, previousState;
		protected boolean hasFocus;
		protected boolean mouseWasIn;
		private final G3Action[] g3Actions;

		public SelectorArrow() {
			this.currentState = ComponentState.IDLE;
			this.previousState = ComponentState.IDLE;
			this.hasFocus = false;
			this.mouseWasIn = false;
			this.g3Actions = new G3Action[4];
			// Set default actions (do nothing) for every currentState
			for (int index = 0; index < this.g3Actions.length; index += 1) {
				this.g3Actions[index] = G3Action.DEFAULT_DO_NOTHING;
			}
		}

		public final void setG3Action(final ComponentState state, final G3Action g3Action) {
			if (state == ComponentState.ACTIVE) {
				Logger.log(Selector.SelectorArrow.class, LogLevel.ERROR, "Can't change the on click behavior of a selector arrow");
			} else {
				this.g3Actions[state.identifier()] = g3Action;
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
	public Selector(final G3Application g3Application, final String[] optionsArray) {
		this(g3Application, optionsArray, optionsArray[0]);
	}

	/**
	 * Pretty self explanatory.
	 */
	public Selector(final G3Application g3Application, final String[] optionsArray, final String defaultOption) {
		super(g3Application);
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
		this.leftArrow.g3Actions[ComponentState.ACTIVE.identifier()] = () -> {
			Selector.this.currentOptionIndex -= 1;
			if (Selector.this.currentOptionIndex < 0) {
				Selector.this.currentOptionIndex = Selector.this.options.size() - 1;
			}
		};
		this.rightArrow.g3Actions[ComponentState.ACTIVE.identifier()] = () -> {
			Selector.this.currentOptionIndex += 1;
			Selector.this.currentOptionIndex %= Selector.this.options.size();
		};
	}

	/**
	 * This is where all the logic of the selector happens. We check the mouse
	 * position and the mouse left click, and we deduce the currentState of the
	 * selector arrows then run the appropriate actions accordingly.
	 */
	@Override
	public synchronized void update() {
		// Set the previous state for each individual arrow on the last update
		this.leftArrow.previousState = this.leftArrow.currentState;
		this.rightArrow.previousState = this.rightArrow.currentState;
		// Get the mouse position
		final int mouseX = this.g3Application.mouse().getX(), mouseY = this.g3Application.mouse().getY();
		// Left arrow handling, this block magically works and it took me a lot of time
		// but I couldn't for the life of me explain it..
		{
			final boolean mouseInLeft = this.leftArrowContainsMouse(mouseX, mouseY), mouseClicked = this.g3Application.mouse().isButtonDown(Mouse.BUTTON_LEFT);
			if (mouseInLeft && mouseClicked && !this.leftArrow.hasFocus) {
				this.leftArrow.hasFocus = true;
			}
			if (mouseClicked && this.leftArrow.hasFocus && this.leftArrow.mouseWasIn) {
				this.leftArrow.currentState = ComponentState.HELD;
				this.leftArrow.mouseWasIn = true;
			} else if (mouseInLeft && !mouseClicked) {
				this.leftArrow.currentState = ComponentState.HOVERED;
				this.leftArrow.hasFocus = false;
				this.leftArrow.mouseWasIn = true;
			} else {
				this.leftArrow.currentState = ComponentState.IDLE;
				this.leftArrow.hasFocus = false;
				this.leftArrow.mouseWasIn = false;
			}
			if ((this.leftArrow.previousState == ComponentState.HELD) && ((this.leftArrow.currentState == ComponentState.IDLE) || (this.leftArrow.currentState == ComponentState.HOVERED)) && mouseInLeft) {
				this.leftArrow.currentState = ComponentState.ACTIVE;
			}
		}
		// Right arrow handling, this block magically works and it took me a lot of time
		// but I couldn't for the life of me explain it..
		{
			final boolean mouseInRight = this.rightArrowContainsMouse(mouseX, mouseY), mouseClicked = this.g3Application.mouse().isButtonDown(Mouse.BUTTON_LEFT);
			if (mouseInRight && mouseClicked && !this.rightArrow.hasFocus) {
				this.rightArrow.hasFocus = true;
			}
			if (mouseClicked && this.rightArrow.hasFocus && this.rightArrow.mouseWasIn) {
				this.rightArrow.currentState = ComponentState.HELD;
				this.rightArrow.mouseWasIn = true;
			} else if (mouseInRight && !mouseClicked) {
				this.rightArrow.currentState = ComponentState.HOVERED;
				this.rightArrow.hasFocus = false;
				this.rightArrow.mouseWasIn = true;
			} else {
				this.rightArrow.currentState = ComponentState.IDLE;
				this.rightArrow.hasFocus = false;
				this.rightArrow.mouseWasIn = false;
			}
			if ((this.rightArrow.previousState == ComponentState.HELD) && ((this.rightArrow.currentState == ComponentState.IDLE) || (this.rightArrow.currentState == ComponentState.HOVERED)) && mouseInRight) {
				this.rightArrow.currentState = ComponentState.ACTIVE;
			}
		}
		this.leftArrow.g3Actions[this.leftArrow.currentState.identifier()].execute();
		this.rightArrow.g3Actions[this.rightArrow.currentState.identifier()].execute();
		this.currentOption = this.options.get(this.currentOptionIndex);
	}

	@Override
	public final boolean canChangeFocus() {
		return (this.leftArrow.currentState == ComponentState.IDLE) && (this.rightArrow.currentState == ComponentState.IDLE);
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
	public List<String> selections() {
		return this.options;
	}

	/**
	 * Self explanatory.
	 */
	public String defaultSelection() {
		return this.defaultOption;
	}

	/**
	 * Self explanatory.
	 */
	public String currentSelection() {
		return this.currentOption;
	}

	/**
	 * Self explanatory.
	 */
	public int currentSelectionIndex() {
		return this.currentOptionIndex;
	}

	/**
	 * Self explanatory.
	 */
	public SelectorArrow leftArrow() {
		return this.leftArrow;
	}

	/**
	 * Self explanatory.
	 */
	public SelectorArrow rightArrow() {
		return this.rightArrow;
	}

	/**
	 * Self explanatory.
	 */
	public void setOptions(final List<String> options) {
		this.options = options;
	}
}
