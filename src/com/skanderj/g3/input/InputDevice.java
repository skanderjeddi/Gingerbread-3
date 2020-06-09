package com.skanderj.g3.input;

/**
 * Represents an input device for the window. For now, it's either a keyboard or
 * a mouse. TODO implementing controller support (somehow).
 *
 * @author Skander
 *
 */
public interface InputDevice {
	InputDeviceType getType();

	enum InputDeviceType {
		KEYBOARD, MOUSE;
	}
}
