package com.skanderj.gingerbread3.input;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.object.Action;
import com.skanderj.gingerbread3.input.Keyboard.KeyState;
import com.skanderj.gingerbread3.logging.Logger;
import com.skanderj.gingerbread3.logging.Logger.LogLevel;
import com.skanderj.gingerbread3.scene.Scenes;

/**
 *
 * @author Skander // TODO DOC
 *
 */
public class Binds {
	private final static Set<Bind> binds = new HashSet<>();

	private Binds() {
		return;
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized final void registerBind(final String targetScene, final Integer[] targetKeycodes, final Keyboard.KeyState[] targetState, final Action action) {
		Binds.registerBind(new Bind(targetScene, targetKeycodes, targetState, action));
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized final void registerBind(final Bind bind) {
		if (Binds.binds.add(bind)) {
			Logger.log(Binds.class, LogLevel.DEBUG, "Bind registered: \"%s\" -> <%s | %s>", Engine.identifier(bind.targetScene()), Arrays.toString(bind.targetKeyCodes()), Arrays.toString(bind.targetKeyStates()));
		}
	}

	/**
	 * Self explanatory. Logic happens here.
	 */
	public static synchronized final void update(final Application application) {
		final Keyboard keyboard = application.keyboard();
		if (keyboard != null) {
			final Integer[] keysDown = keyboard.getKeysByState(Keyboard.KeyState.DOWN);
			final Integer[] keysDownInFrame = keyboard.getKeysByState(Keyboard.KeyState.DOWN_IN_CURRENT_FRAME);
			final Map<Integer, KeyState> states = new HashMap<>();
			for (final int key : keysDown) {
				states.put(key, KeyState.DOWN);
			}
			for (final int key : keysDownInFrame) {
				states.put(key, KeyState.DOWN_IN_CURRENT_FRAME);
			}
			for (final Bind bind : Binds.binds) {
				if (Engine.identifier(Scenes.scene()).equals(Engine.identifier(bind.targetScene())) || bind.skipChecks()) {
					boolean execute = true;
					for (int i = 0; i < bind.targetKeyCodes().length; i += 1) {
						final int target = bind.targetKeyCodes()[i];
						final KeyState targetState = bind.targetKeyStates()[i];
						if (states.get(target) != targetState) {
							execute = false;
						}
					}
					if (execute) {
						bind.action().execute(null);
						Logger.log(Binds.class, LogLevel.DEBUG, "Executing bind %s", bind.toString());
					}
				}
			}
		}
	}
}
