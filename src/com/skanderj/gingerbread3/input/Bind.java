package com.skanderj.gingerbread3.input;

import java.util.Arrays;
import java.util.Map;

import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.core.object.Action;
import com.skanderj.gingerbread3.input.Keyboard.KeyState;
import com.skanderj.gingerbread3.logging.Logger;
import com.skanderj.gingerbread3.logging.Logger.LogLevel;
import com.skanderj.gingerbread3.scene.Scene;
import com.skanderj.gingerbread3.scene.Scenes;

/**
 *
 * @author Skander
 *
 */
public class Bind {
	private final Scene targetScene;
	private final Integer[] targetKeycodes;
	private final Keyboard.KeyState[] targetKeyStates;
	private final Action action;

	public Bind(final Scene scene, final Map<Integer, KeyState> mappings, final Action action) {
		this.targetScene = scene;
		this.targetKeycodes = mappings.keySet().toArray(new Integer[mappings.size()]);
		this.targetKeyStates = mappings.values().toArray(new KeyState[mappings.size()]);
		this.action = action;
	}

	public Bind(final String sceneIdentifier, final Integer[] keycodes, final KeyState[] states, final Action action) {
		this(Scenes.get(sceneIdentifier), keycodes, states, action);
	}

	public Bind(final Scene scene, final Integer[] keycodes, final KeyState[] states, final Action action) {
		if (keycodes.length != states.length) {
			Logger.log(Bind.class, LogLevel.FATAL, "Size mismatch between keys' array size and keystates' array size (%d vs %d)", keycodes.length, states.length);
		}
		this.targetScene = scene;
		this.targetKeycodes = keycodes;
		this.targetKeyStates = states;
		this.action = action;
	}

	/**
	 * Self explanatory.
	 */
	public Scene targetScene() {
		return this.targetScene;
	}

	/**
	 * Self explanatory.
	 */
	public Integer[] targetKeyCodes() {
		return this.targetKeycodes;
	}

	/**
	 * Self explanatory.
	 */
	public Keyboard.KeyState[] targetKeyStates() {
		return this.targetKeyStates;
	}

	/**
	 * Self explanatory.
	 */
	public Action action() {
		return this.action;
	}

	@Override
	public String toString() {
		return "<" + Registry.identifier(this.targetScene) + " | " + Arrays.toString(this.targetKeycodes) + " | " + Arrays.toString(this.targetKeyStates) + ">";
	}
}
