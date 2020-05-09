package com.skanderj.g3.component;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.skanderj.g3.component.action.ButtonAction;
import com.skanderj.g3.inputdevice.Keyboard;
import com.skanderj.g3.inputdevice.Mouse;
import com.skanderj.g3.io.FontManager;
import com.skanderj.g3.log.Logger;
import com.skanderj.g3.log.Logger.LogLevel;
import com.skanderj.g3.translation.TranslationManager;
import com.skanderj.g3.util.GraphicString;
import com.skanderj.g3.util.TextProperties;
import com.skanderj.g3.window.Window;

/**
 * Represents an abstract selector, basis for other selector classes which can
 * implement their rendering the way they please. See Selector#Basic for a basic
 * example.
 *
 * @author Skander
 *
 */
public abstract class Selector implements Component {
	/**
	 * A class representing each selector arrow.
	 *
	 * @author Skander
	 *
	 */
	static class SelectorArrow {
		protected ButtonState currentState, previousState;
		protected boolean hasFocus;
		protected boolean mouseWasIn;
		private ButtonAction[] actions;

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

		public final void setAction(ButtonState state, ButtonAction action) {
			if (state == ButtonState.ON_ACTUAL_CLICK) {
				Logger.log(Selector.SelectorArrow.class, LogLevel.ERROR, TranslationManager.getKey("selector.arrow.no.change.behavior"));
			} else {
				this.actions[state.getIdentifier()] = action;
			}
		}
	}

	// Positoion & size
	protected int x, y, width, height;
	// How to graphically display the options
	protected TextProperties properties;
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
	public Selector(int x, int y, int width, int height, TextProperties properties, String[] optionsArray) {
		this(x, y, width, height, properties, optionsArray, optionsArray[0]);
	}

	/**
	 * Pretty self explanatory.
	 */
	public Selector(int x, int y, int width, int height, TextProperties properties, String[] optionsArray, String defaultOption) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.properties = properties;
		this.options = new ArrayList<String>();
		for (String option : optionsArray) {
			this.options.add(option);
		}
		if (!this.options.contains(defaultOption)) {
			this.options.add(defaultOption);
		}
		this.currentOption = defaultOption;
		this.currentOptionIndex = this.options.lastIndexOf(defaultOption);
		this.leftArrow = new SelectorArrow();
		this.rightArrow = new SelectorArrow();
		this.leftArrow.actions[ButtonState.ON_ACTUAL_CLICK.getIdentifier()] = new ButtonAction() {
			@Override
			public void execute(Object... args) {
				Selector.this.currentOptionIndex -= 1;
				if (Selector.this.currentOptionIndex < 0) {
					Selector.this.currentOptionIndex = Selector.this.options.size() - 1;
				}
			}
		};
		this.rightArrow.actions[ButtonState.ON_ACTUAL_CLICK.getIdentifier()] = new ButtonAction() {
			@Override
			public void execute(Object... args) {
				Selector.this.currentOptionIndex += 1;
				Selector.this.currentOptionIndex %= Selector.this.options.size();
			}
		};
	}

	/**
	 * This is where all the logic of the selector happens. We check the mouse
	 * position and the mouse left click, and we deduce the currentState of the
	 * selector arrows then run the appropriate action accordingly.
	 */
	@Override
	public void update(double delta, Keyboard keyboard, Mouse mouse, Object... args) {
		// Set the previous state for each individual arrow on the last update
		this.leftArrow.previousState = this.leftArrow.currentState;
		this.rightArrow.previousState = this.rightArrow.currentState;
		// Get the mouse position
		int mouseX = mouse.getX(), mouseY = mouse.getY();
		// Left arrow handling, this block magically works and it took me a lot of time
		// but I couldn't for the life of me explain it..
		{
			boolean mouseInLeft = this.leftArrowContainsMouse(mouseX, mouseY), mouseClicked = mouse.isButtonDown(Mouse.BUTTON_LEFT);
			if (mouseInLeft && mouseClicked && !this.leftArrow.hasFocus) {
				this.leftArrow.hasFocus = true;
			}
			if (mouseClicked && this.leftArrow.hasFocus && this.leftArrow.mouseWasIn) {
				this.leftArrow.currentState = ButtonState.CLICKED;
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
			if ((this.leftArrow.previousState == ButtonState.CLICKED) && ((this.leftArrow.currentState == ButtonState.IDLE) || (this.leftArrow.currentState == ButtonState.HOVERED)) && mouseInLeft) {
				this.leftArrow.currentState = ButtonState.ON_ACTUAL_CLICK;
			}
		}
		// Right arrow handling, this block magically works and it took me a lot of time
		// but I couldn't for the life of me explain it..
		{
			boolean mouseInRight = this.rightArrowContainsMouse(mouseX, mouseY), mouseClicked = mouse.isButtonDown(Mouse.BUTTON_LEFT);
			if (mouseInRight && mouseClicked && !this.rightArrow.hasFocus) {
				this.rightArrow.hasFocus = true;
			}
			if (mouseClicked && this.rightArrow.hasFocus && this.rightArrow.mouseWasIn) {
				this.rightArrow.currentState = ButtonState.CLICKED;
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
			if ((this.rightArrow.previousState == ButtonState.CLICKED) && ((this.rightArrow.currentState == ButtonState.IDLE) || (this.rightArrow.currentState == ButtonState.HOVERED)) && mouseInRight) {
				this.rightArrow.currentState = ButtonState.ON_ACTUAL_CLICK;
			}
		}
		this.leftArrow.actions[this.leftArrow.currentState.getIdentifier()].execute(delta, keyboard, mouse);
		this.rightArrow.actions[this.rightArrow.currentState.getIdentifier()].execute(delta, keyboard, mouse);
		this.currentOption = this.options.get(this.currentOptionIndex);
	}

	@Override
	public boolean canChangeFocus() {
		return (this.leftArrow.currentState == ButtonState.IDLE) && (this.rightArrow.currentState == ButtonState.IDLE);
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
	 * Dealt with differently.
	 */
	@Override
	public boolean containsMouse(int x, int y) {
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
	public final TextProperties getProperties() {
		return this.properties;
	}

	/**
	 * Self explanatory.
	 */
	public final List<String> getOptions() {
		return this.options;
	}

	/**
	 * Self explanatory.
	 */
	public final String getDefaultOption() {
		return this.defaultOption;
	}

	/**
	 * Self explanatory.
	 */
	public final String getCurrentOption() {
		return this.currentOption;
	}

	/**
	 * Self explanatory.
	 */
	public final int getCurrentOptionIndex() {
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
	public final void setProperties(TextProperties properties) {
		this.properties = properties;
	}

	/**
	 * Self explanatory.
	 */
	public final void setOptions(List<String> options) {
		this.options = options;
	}

	/**
	 * A basic selector with rectangle arrows.
	 *
	 * @author Skander
	 *
	 */
	public static class Basic extends Selector {
		private int arrowSize;

		/**
		 * Self explanatory.
		 */
		public Basic(int x, int y, int width, int height, int arrowSize, TextProperties properties, String[] optionsArray) {
			this(x, y, width, height, arrowSize, properties, optionsArray, optionsArray[0]);
		}

		/**
		 * Self explanatory.
		 */
		public Basic(int x, int y, int width, int height, int arrowSize, TextProperties properties, String[] optionsArray, String defaultOption) {
			super(x, y, width, height, properties, optionsArray, defaultOption);
			this.arrowSize = arrowSize;
		}

		@Override
		/**
		 * Very basic rendering, 2 rectangles for the arrows.
		 */
		public void render(Window window, Graphics2D graphics, Object... args) {
			graphics.setColor(Color.WHITE);
			graphics.fillRect(this.x, this.y, this.width, this.height);
			new GraphicString(this.currentOption, this.properties, this.properties.getColor().darker().darker()).drawCentered(graphics, this.x, this.y, this.width, this.height);
			graphics.setColor(Color.WHITE);
			graphics.fillRect(this.x - 10 - this.arrowSize, this.y, this.arrowSize, this.height);
			new GraphicString("<", Color.BLACK, FontManager.getFont("lunchtime", 24)).drawCentered(graphics, this.x - 10 - this.arrowSize, this.y, this.arrowSize, this.height);
			graphics.setColor(Color.WHITE);
			graphics.fillRect(this.x + this.width + 10, this.y, this.arrowSize, this.height);
			new GraphicString(">", Color.BLACK, FontManager.getFont("lunchtime", 24)).drawCentered(graphics, this.x + this.width + 10, this.y, this.arrowSize, this.height);
		}

		/**
		 * Self explanatory.
		 */
		@Override
		public boolean leftArrowContainsMouse(int x, int y) {
			return new Rectangle(this.x - 10 - this.arrowSize, this.y, this.arrowSize, this.height).contains(x, y);
		}

		/**
		 * Self explanatory.
		 */
		@Override
		public boolean rightArrowContainsMouse(int x, int y) {
			return new Rectangle(this.x + this.width + 10, this.y, this.arrowSize, this.height).contains(x, y);
		}

		/**
		 * Self explanatory.
		 */
		public final int getArrowSize() {
			return this.arrowSize;
		}

		/**
		 * Self explanatory.
		 */
		public final void setArrowSize(int arrowSize) {
			this.arrowSize = arrowSize;
		}
	}
}
