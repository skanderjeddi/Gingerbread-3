package com.skanderj.g3.inputdevice;

public interface InputDevice {
	public InputDeviceType getType();

	static enum InputDeviceType {
		KEYBOARD, MOUSE;
	}
}
