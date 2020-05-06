package com.skanderj.g3.component;

public enum ButtonState {
	IDLE(0), HOVERED(1), CLICKED(2), CLICK_FRAME(3);

	private int identifier;

	private ButtonState(int identifier) {
		this.identifier = identifier;
	}

	public int getIdentifier() {
		return this.identifier;
	}
}
