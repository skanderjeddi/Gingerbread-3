package com.skanderj.gingerbread3.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.skanderj.gingerbread3.component.Component;
import com.skanderj.gingerbread3.component.ComponentManager;
import com.skanderj.gingerbread3.core.object.GameObject;
import com.skanderj.gingerbread3.display.GraphicsWrapper;
import com.skanderj.gingerbread3.input.Mouse;
import com.skanderj.gingerbread3.log.Logger;
import com.skanderj.gingerbread3.log.Logger.LogLevel;
import com.skanderj.gingerbread3.scene.SceneManager;

/**
 * Probably the most important class and handles all the game objects.
 *
 * @author Skander
 *
 */
public final class Registry {
	private final static Map<String, GameObject> contents = new HashMap<String, GameObject>();
	private final static Set<GameObject> deletions = new HashSet<GameObject>();
	private final static Set<GameObject> skips = new HashSet<GameObject>();

	private Registry() {
		return;
	}

	public static synchronized void skip(final String identifier) {
		Registry.skip(Registry.contents.get(identifier));
	}

	public static synchronized void skip(final GameObject object) {
		Registry.skips.add(object);
	}

	public static synchronized void markForDeletion(final String identifier) {
		Registry.markForDeletion(Registry.contents.get(identifier));
	}

	public static synchronized void markForDeletion(final GameObject object) {
		Registry.deletions.add(object);
	}

	public static synchronized void set(final String identifier, final GameObject object) {
		Logger.log(Registry.class, LogLevel.DEBUG, "NEW game object -> \"%s\" <class : %s>", identifier, object.getClass().getSimpleName());
		Registry.contents.put(identifier, object);
	}

	public static synchronized GameObject get(final String identifier) {
		return Registry.contents.getOrDefault(identifier, null);
	}

	public static synchronized String identifier(final GameObject object) {
		for (String identifier : contents.keySet()) {
			if (contents.get(identifier) == object) {
				return identifier;
			}
		}
		return null;
	}

	public static synchronized final void update(final double delta, final Object... args) {
		final List<GameObject> toUpdate = new ArrayList<GameObject>();
		if (!Registry.deletions.isEmpty()) {
			for (final GameObject object : Registry.deletions) {
				for (final String key : Registry.contents.keySet().toArray(new String[Registry.contents.size()])) {
					if (Registry.contents.get(key) == object) {
						Registry.contents.remove(key);
						Logger.log(Registry.class, LogLevel.DEBUG, "DELETED game object [\"%s\"]", key);
						return;
					}
				}
			}
		}
		Registry.deletions.clear();
		final List<String> allowedComponents = new ArrayList<String>();
		if (SceneManager.scene() != null) {
			allowedComponents.addAll(SceneManager.scene().sceneObjects());
		}
		for (final String identifier : Registry.contents.keySet()) {
			if (allowedComponents.contains(identifier)) {
				final GameObject object = Registry.contents.get(identifier);
				if (object != null) {
					if ((object instanceof Component) || Registry.skips.contains(object)) {
						continue;
					}
					toUpdate.add(object);
				}
			}
		}
		toUpdate.addAll(ComponentManager.activeComponents());
		Collections.sort(toUpdate);
		for (final GameObject object : toUpdate) {
			if (object instanceof Component) {
				Component component = (Component) object;
				if (component.containsMouse(component.getGame().getMouse().getX(), component.getGame().getMouse().getY()) && component.getGame().getMouse().isButtonDown(Mouse.BUTTON_LEFT)) {
					ComponentManager.giveFocus(component);
				}
			}
			object.update(delta, args);
		}
	}

	public static synchronized final void render(final GraphicsWrapper graphics, final Object... args) {
		final List<GameObject> toRender = new ArrayList<GameObject>();
		final List<String> allowedComponents = new ArrayList<String>();
		if (SceneManager.scene() != null) {
			allowedComponents.addAll(SceneManager.scene().sceneObjects());
		}
		for (final String identifier : Registry.contents.keySet()) {
			if (allowedComponents.contains(identifier)) {
				final GameObject object = Registry.contents.get(identifier);
				if (object != null) {
					if ((object instanceof Component) || Registry.skips.contains(object)) {
						continue;
					}
					toRender.add(object);
				}
			}
		}
		toRender.addAll(ComponentManager.activeComponents());
		Collections.sort(toRender);
		for (final GameObject object : toRender) {
			object.render(graphics, args);
		}
	}

	public static synchronized void newScene() {
		for (final String identifier : Registry.contents.keySet()) {
			final GameObject object = Registry.contents.get(identifier);
			if (object != null) {
				object.sceneChange();
			}
		}
		Registry.skips.clear();
		Registry.deletions.clear();
	}

	public static synchronized final void updateObject(final String identifier, final double delta, final Object... args) {
		Registry.contents.get(identifier).update(delta, args);
	}

	public static synchronized final void renderObject(final String identifier, final GraphicsWrapper graphics, final Object... args) {
		Registry.contents.get(identifier).render(graphics, args);
	}
}
