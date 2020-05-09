package com.skanderj.g3.inputdevice;

/**
 * Represents an input device for the window. For now, it's either a keyboard or
 * a mouse. TODO implementing controller support (somehow).
 * 
 * @author Skander
 *
 */
public interface InputDevice {
	public InputDeviceType getType();

	static enum InputDeviceType {
		KEYBOARD, MOUSE;
	}
}
