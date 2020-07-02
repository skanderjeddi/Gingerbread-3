package com.skanderj.gingerbread3.input.binds;

import com.skanderj.gingerbread3.core.object.Action;
import com.skanderj.gingerbread3.input.Keyboard;
import com.skanderj.gingerbread3.input.Keyboard.KeyState;

public class Bind {
	private String scene;
	private int keycode;
	private Keyboard.KeyState state;
	private Action action;

	public Bind(final String scene, final int keycode, final KeyState state, final Action action) {
		this.scene = scene;
		this.keycode = keycode;
		this.state = state;
		this.action = action;
	}

	public String getScene() {
		return this.scene;
	}

	public int getKeycode() {
		return this.keycode;
	}

	public Keyboard.KeyState getState() {
		return this.state;
	}

	public Action getAction() {
		return this.action;
	}

	public void setScene(final String scene) {
		this.scene = scene;
	}

	public void setKeycode(final int keycode) {
		this.keycode = keycode;
	}

	public void setState(final Keyboard.KeyState state) {
		this.state = state;
	}

	public void setAction(final Action action) {
		this.action = action;
	}
}
