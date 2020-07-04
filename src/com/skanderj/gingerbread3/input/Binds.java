package com.skanderj.gingerbread3.input;

import java.util.HashSet;
import java.util.Set;

import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.core.object.Action;
import com.skanderj.gingerbread3.logging.Logger;
import com.skanderj.gingerbread3.logging.Logger.LogLevel;

/**
 *
 * @author Skander // TODO DOC
 *
 */
public class Binds {
	private final static Set<Bind> binds = new HashSet<Bind>();

	private Binds() {
		return;
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized final void registerBind(final String targetScene, final int targetKeycode, final Keyboard.KeyState targetState, final Action action) {
		Binds.registerBind(new Bind(targetScene, targetKeycode, targetState, action));
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized final void registerBind(final Bind bind) {
		if (Binds.binds.add(bind)) {
			Logger.log(Binds.class, LogLevel.DEBUG, "Bind registered: \"%s\" -> <%d | %s>", bind.getTargetScene(), bind.getTargetKeycode(), bind.getTargetKeyState().name());
		}
	}

	/**
	 * Self explanatory. Logic happens here.
	 */
	public static synchronized final void update(final Game game, final double delta, final Object... args) {
		final Keyboard keyboard = game.keyboard();
		if (keyboard != null) {
			final Integer[] keysDown = keyboard.getKeysByState(Keyboard.KeyState.DOWN);
			final Integer[] keysDownInFrame = keyboard.getKeysByState(Keyboard.KeyState.DOWN_IN_FRAME);
			for (final int keyDown : keysDown) {
				for (final Bind bind : Binds.binds) {
					if ((bind.getTargetKeycode() == keyDown) && (bind.getTargetKeyState() == Keyboard.KeyState.DOWN)) {
						bind.getAction().execute(args);
					}
				}
			}
			for (final int keyDown : keysDownInFrame) {
				for (final Bind bind : Binds.binds) {
					if ((bind.getTargetKeycode() == keyDown) && (bind.getTargetKeyState() == Keyboard.KeyState.DOWN_IN_FRAME)) {
						bind.getAction().execute(args);
					}
				}
			}
		}
	}
}
