package com.skanderj.gingerbread3.input.binds;

import java.util.HashSet;
import java.util.Set;

import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.core.object.Action;
import com.skanderj.gingerbread3.input.Keyboard;
import com.skanderj.gingerbread3.scene.SceneManager;

public class BindsHandler {
	private final static Set<Bind> binds = new HashSet<Bind>();

	private BindsHandler() {
		return;
	}

	public static synchronized final void registerBind(final String targetScene, final int targetKeycode, final Keyboard.KeyState targetState, final Action action) {
		BindsHandler.registerBind(new Bind(targetScene, targetKeycode, targetState, action));
	}

	public static synchronized final void registerBind(final Bind bind) {
		BindsHandler.binds.add(bind);
	}
}
