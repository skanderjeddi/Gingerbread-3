package com.skanderj.g3.inputdevice;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Mouse extends MouseAdapter implements InputDevice {
	private static final int BUTTON_COUNT = 3;

	public static final int BUTTON_LEFT = MouseEvent.BUTTON1;
	public static final int BUTTON_MIDDLE = MouseEvent.BUTTON2;
	public static final int BUTTON_RIGHT = MouseEvent.BUTTON3;

	private final boolean[] cache;
	private final MouseState[] buttonsStates;

	private int x, y, currentX, currentY;

	public Mouse() {
		this.cache = new boolean[Mouse.BUTTON_COUNT];
		this.buttonsStates = new MouseState[Mouse.BUTTON_COUNT];
		for (int index = 0; index < Mouse.BUTTON_COUNT; index++) {
			this.buttonsStates[index] = MouseState.UP;
		}
	}

	public synchronized void update() {
		this.x = this.currentX;
		this.y = this.currentY;
		for (int index = 0; index < Mouse.BUTTON_COUNT; index++) {
			if (this.cache[index] == true) {
				if (this.buttonsStates[index] == MouseState.UP) {
					this.buttonsStates[index] = MouseState.DOWN_IN_FRAME;
				} else {
					this.buttonsStates[index] = MouseState.DOWN;
				}
			} else {
				this.buttonsStates[index] = MouseState.UP;
			}
		}
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public boolean isButtonDownInFrame(final int button) {
		return this.buttonsStates[(button - 1)] == MouseState.DOWN_IN_FRAME;
	}

	public boolean isButtonDown(final int button) {
		return (this.buttonsStates[(button - 1)] == MouseState.DOWN_IN_FRAME) || (this.buttonsStates[(button - 1)] == MouseState.DOWN);
	}

	@Override
	public synchronized void mousePressed(final MouseEvent mouseEvent) {
		this.cache[(mouseEvent.getButton() - 1)] = true;
	}

	@Override
	public synchronized void mouseReleased(final MouseEvent mouseEvent) {
		this.cache[(mouseEvent.getButton() - 1)] = false;
	}

	@Override
	public synchronized void mouseEntered(final MouseEvent mouseEvent) {
		this.mouseMoved(mouseEvent);
	}

	@Override
	public synchronized void mouseExited(final MouseEvent mouseEvent) {
		this.mouseMoved(mouseEvent);
	}

	@Override
	public synchronized void mouseDragged(final MouseEvent mouseEvent) {
		this.mouseMoved(mouseEvent);
	}

	@Override
	public synchronized void mouseMoved(final MouseEvent mouseEvent) {
		this.currentX = mouseEvent.getX();
		this.currentY = mouseEvent.getY();
	}

	private static enum MouseState {
		UP, DOWN, DOWN_IN_FRAME;
	}

	@Override
	public InputDeviceType getType() {
		return InputDeviceType.MOUSE;
	}
}