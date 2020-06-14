package com.skanderj.gingerbread3.core.object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.skanderj.gingerbread3.component.Component;
import com.skanderj.gingerbread3.component.ComponentManager;
import com.skanderj.gingerbread3.display.GraphicsWrapper;
import com.skanderj.gingerbread3.log.Logger;
import com.skanderj.gingerbread3.log.Logger.LogLevel;
import com.skanderj.gingerbread3.scene.SceneManager;

/**
 * Probably the most important class and handles all the game objects.
 *
 * @author Skander
 *
 */
public final class GameRegistry {
	private final static Map<String, GameObject> registry = new HashMap<String, GameObject>();
	private final static Set<GameObject> toDelete = new HashSet<GameObject>();
	private final static Set<GameObject> toSkip = new HashSet<GameObject>();

	private GameRegistry() {
		return;
	}

	public static synchronized void skip(final String identifier) {
		GameRegistry.skip(GameRegistry.registry.get(identifier));
	}

	public static synchronized void skip(final GameObject object) {
		GameRegistry.toSkip.add(object);
	}

	public static synchronized void markForDeletion(final String identifier) {
		GameRegistry.markForDeletion(GameRegistry.registry.get(identifier));
	}

	public static synchronized void markForDeletion(final GameObject object) {
		GameRegistry.toDelete.add(object);
	}

	public static synchronized void set(final String identifier, final GameObject object) {
		Logger.log(GameRegistry.class, LogLevel.DEBUG, "NEW game object -> \"%s\" <class : %s>", identifier, object.getClass().getSimpleName());
		GameRegistry.registry.put(identifier, object);
	}

	public static synchronized GameObject get(final String identifier) {
		return GameRegistry.registry.getOrDefault(identifier, null);
	}

	public static synchronized final void update(final double delta, final Object... args) {
		final List<GameObject> toUpdate = new ArrayList<GameObject>();
		if (!GameRegistry.toDelete.isEmpty()) {
			for (final GameObject object : GameRegistry.toDelete) {
				for (final String key : GameRegistry.registry.keySet().toArray(new String[GameRegistry.registry.size()])) {
					if (GameRegistry.registry.get(key) == object) {
						GameRegistry.registry.remove(key);
						Logger.log(GameRegistry.class, LogLevel.DEBUG, "DELETED game object [\"%s\"]", key);
						return;
					}
				}
			}
		}
		GameRegistry.toDelete.clear();
		ComponentManager.update(delta, args);
		final List<String> allowedComponents = new ArrayList<String>();
		if (SceneManager.scene() != null) {
			allowedComponents.addAll(SceneManager.scene().sceneObjects());
		}
		for (final String identifier : GameRegistry.registry.keySet()) {
			if (allowedComponents.contains(identifier)) {
				final GameObject object = GameRegistry.registry.get(identifier);
				if (object != null) {
					if ((object instanceof Component) || GameRegistry.toSkip.contains(object)) {
						continue;
					}
					toUpdate.add(object);
				}
			}
		}
		Collections.sort(toUpdate);
		for (final GameObject object : toUpdate) {
			object.update(delta, args);
		}
	}

	public static synchronized final void render(final GraphicsWrapper graphics, final Object... args) {
		final List<GameObject> toRender = new ArrayList<GameObject>();
		ComponentManager.render(graphics, args);
		final List<String> allowedComponents = new ArrayList<String>();
		if (SceneManager.scene() != null) {
			allowedComponents.addAll(SceneManager.scene().sceneObjects());
		}
		for (final String identifier : GameRegistry.registry.keySet()) {
			if (allowedComponents.contains(identifier)) {
				final GameObject object = GameRegistry.registry.get(identifier);
				if (object != null) {
					if ((object instanceof Component) || GameRegistry.toSkip.contains(object)) {
						continue;
					}
					toRender.add(object);
				}
			}
		}
		for (final GameObject object : toRender) {
			object.render(graphics, args);
		}
	}

	public static synchronized void newScene() {
		for (final String identifier : GameRegistry.registry.keySet()) {
			final GameObject object = GameRegistry.registry.get(identifier);
			if (object != null) {
				object.sceneChange();
			}
		}
		GameRegistry.toSkip.clear();
		GameRegistry.toDelete.clear();
	}

	public static synchronized final void updateObject(final String identifier, final double delta, final Object... args) {
		GameRegistry.registry.get(identifier).update(delta, args);
	}

	public static synchronized final void renderObject(final String identifier, final GraphicsWrapper graphics, final Object... args) {
		GameRegistry.registry.get(identifier).render(graphics, args);
	}
}
