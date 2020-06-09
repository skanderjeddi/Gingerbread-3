package com.skanderj.g3.component;

import com.skanderj.g3.input.Keyboard;
import com.skanderj.g3.input.Mouse;

/**
 * Represents an abstract slider, basis for other slider classes which can
 * implements their rendering the way they please. See G3Slider for a basic,
 * ready-to-be-used example.
 *
 * @author Skander
 *
 */
public abstract class Slider implements Component {
	protected float minimumValue, maximumValue;
	protected boolean hasFocus, globalFocus;

	/**
	 * Basic constructor: position, size, minimum/maximum value, default value,
	 */
	public Slider(float min, float max, float defaultValue) {
		this.minimumValue = Math.min(min, max);
		this.maximumValue = Math.max(min, max);
		this.hasFocus = false;
		this.globalFocus = false;
	}

	/**
	 * This is where all the logic of the slider happens. We check the mouse
	 * position and the mouse left click, and we deduce the currentState of the
	 * slider then move the slider accordingly.
	 */
	@Override
	public synchronized void update(double delta, Keyboard keyboard, Mouse mouse, Object... args) {
		if ((this.containsMouse(mouse.getX(), mouse.getY()) || this.hasFocus) && this.globalFocus) {
			if (mouse.isButtonDown(Mouse.BUTTON_LEFT)) {
				this.hasFocus = true;
			} else {
				this.hasFocus = false;
			}
		}
	}

	/**
	 * Related to global components management. We can only switch focus out of the
	 * slider if it's not currently being moved (represented by the Slider#hasFocus
	 * variable).
	 */
	@Override
	public final boolean canChangeFocus() {
		return !this.hasFocus;
	}

	/**
	 * Gives global focus.
	 */
	@Override
	public final void grantFocus() {
		this.globalFocus = true;
	}

	/**
	 * Removes global focus.
	 */
	@Override
	public final void revokeFocus() {
		this.globalFocus = false;
	}

	/**
	 * Self explanatory
	 */
	public abstract float getValue();

	/**
	 * Self explanatory.
	 */
	public float getMinimumValue() {
		return this.minimumValue;
	}

	/**
	 * Self explanatory.
	 */
	public float getMaximumValue() {
		return this.maximumValue;
	}

	/**
	 * Self explanatory.
	 */
	public void setMinimumValue(float minimumValue) {
		this.minimumValue = minimumValue;
	}

	/**
	 * Self explanatory.
	 */
	public void setMaximumValue(float maximumValue) {
		this.maximumValue = maximumValue;
	}
}
