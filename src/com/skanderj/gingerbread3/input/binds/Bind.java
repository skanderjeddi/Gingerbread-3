package com.skanderj.gingerbread3.input.binds;

import com.skanderj.gingerbread3.core.object.Action;
import com.skanderj.gingerbread3.input.Keyboard;
import com.skanderj.gingerbread3.input.Keyboard.KeyState;

/**
 *
 * @author Skander
 *
 */
public class Bind {
	private final String targetScene;
	private final int targetKeycode;
	private final Keyboard.KeyState targetKeyState;
	private final Action action;

	public Bind(final String scene, final int keycode, final KeyState state, final Action action) {
		this.targetScene = scene;
		this.targetKeycode = keycode;
		this.targetKeyState = state;
		this.action = action;
	}

	/**
	 * Self explanatory.
	 */
	public String getTargetScene() {
		return targetScene;
	}

	/**
	 * Self explanatory.
	 */
	public int getTargetKeycode() {
		return targetKeycode;
	}

	/**
	 * Self explanatory.
	 */
	public Keyboard.KeyState getTargetKeyState() {
		return targetKeyState;
	}

	/**
	 * Self explanatory.
	 */
	public Action getAction() {
		return action;
	}
}
