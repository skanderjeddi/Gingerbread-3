package com.skanderj.gingerbread3.core.object;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skanderj.gingerbread3.component.Component;
import com.skanderj.gingerbread3.component.ComponentManager;
import com.skanderj.gingerbread3.log.Logger;
import com.skanderj.gingerbread3.log.Logger.LogLevel;
import com.skanderj.gingerbread3.scene.SceneManager;

public final class GameRegistry {
	private final static Map<String, GameObject> registry = new HashMap<String, GameObject>();

	private GameRegistry() {
		return;
	}

	public static synchronized void set(final String identifier, final GameObject object) {
		Logger.log(GameRegistry.class, LogLevel.DEBUG, "NEW game object -> \"%s\" <class : %s>", identifier, object.getClass().getSimpleName());
		GameRegistry.registry.put(identifier, object);
	}

	public static synchronized GameObject get(final String identifier) {
		return GameRegistry.registry.getOrDefault(identifier, null);
	}

	public static synchronized final void update(final double delta, final Object... args) {
		ComponentManager.update(delta, args);
		final List<String> allowedComponents = new ArrayList<String>();
		if (SceneManager.getCurrent() != null) {
			allowedComponents.addAll(SceneManager.getCurrent().sceneObjects());
		}
		for (final String identifier : GameRegistry.registry.keySet()) {
			if (allowedComponents.contains(identifier)) {
				final GameObject object = GameRegistry.registry.get(identifier);
				if (object instanceof Component) {
					continue;
				}
				object.update(delta, args);
			}
		}
	}

	public static synchronized final void render(final Graphics2D graphics, final Object... args) {
		ComponentManager.render(graphics, args);
		final List<String> allowedComponents = new ArrayList<String>();
		if (SceneManager.getCurrent() != null) {
			allowedComponents.addAll(SceneManager.getCurrent().sceneObjects());
		}
		for (final String identifier : GameRegistry.registry.keySet()) {
			if (allowedComponents.contains(identifier)) {
				final GameObject object = GameRegistry.registry.get(identifier);
				if (object instanceof Component) {
					continue;
				}
				object.render(graphics, args);
			}
		}
	}

	public static synchronized final void updateObject(final String identifier, final double delta, final Object... args) {
		GameRegistry.registry.get(identifier).update(delta, args);
	}

	public static synchronized final void renderObject(final String identifier, final Graphics2D graphics, final Object... args) {
		GameRegistry.registry.get(identifier).render(graphics, args);
	}
}
