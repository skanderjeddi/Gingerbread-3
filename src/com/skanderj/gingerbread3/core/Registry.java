package com.skanderj.gingerbread3.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.skanderj.gingerbread3.component.Component;
import com.skanderj.gingerbread3.component.Components;
import com.skanderj.gingerbread3.core.object.G3Object;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.input.Mouse;
import com.skanderj.gingerbread3.logging.Logger;
import com.skanderj.gingerbread3.logging.Logger.LogLevel;
import com.skanderj.gingerbread3.scene.Scene;
import com.skanderj.gingerbread3.scene.Scenes;

/**
 * Probably the most important class and handles all the g3Application objects.
 *
 * @author Skander
 *
 */
public final class Registry {
	// All the g3Application objects are stored here
	private final static Map<String, G3Object> contents = new HashMap<>();
	// Args for each object
	private final static Map<String, Map<String, Object>> parameters = new HashMap<>();
	// G3Application objects to be deleted on the next update
	private final static Set<G3Object> deletions = new HashSet<>();
	// G3Application objects to be skipped
	private final static Set<G3Object> skips = new HashSet<>();

	private Registry() {
		return;
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized void skip(final String identifier) {
		Registry.skip(Registry.contents.get(identifier));
	}

	/**
	 * Self explanatory.
	 */
	private static synchronized void skip(final G3Object object) {
		Registry.skips.add(object);
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized void unskip(final String identifier) {
		Registry.unskip(Registry.contents.get(identifier));
	}

	/**
	 * Self explanatory.
	 */
	private static synchronized void unskip(final G3Object object) {
		Registry.skips.remove(object);
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized void markForDeletion(final String identifier) {
		Registry.markForDeletion(Registry.contents.get(identifier));
	}

	/**
	 * Self explanatory.
	 */
	private static synchronized void markForDeletion(final G3Object object) {
		Registry.deletions.add(object);
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized void register(final String identifier, final G3Object object) {
		String name = new String();
		if (object instanceof Scene) {
			name = "Scene";
		} else if (object instanceof Updateable) {
			name = "Updateable";
		} else if (object instanceof Renderable) {
			name = "Renderable";
		} else if (object instanceof G3Object) {
			name = "InnerG3Object";
		} else {
			name = object.getClass().getSimpleName();
		}
		Logger.log(Registry.class, LogLevel.DEBUG, "G3Object registered: <class : %s> -> \"%s\"", object.getClass().getSimpleName().equals("") ? object.getClass().getEnclosingClass().getSimpleName() + "#" + name : object.getClass().getSimpleName(), identifier);
		Registry.contents.put(identifier, object);
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized G3Object get(final String identifier) {
		return Registry.contents.getOrDefault(identifier, null);
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized String identifier(final G3Object object) {
		for (final String identifier : Registry.contents.keySet()) {
			if (Registry.contents.get(identifier) == object) {
				return identifier;
			}
		}
		return null;
	}

	public static void parameterize(final String identifier, final String[] identifiers, final Object[] args) {
		if (identifiers.length != args.length) {
			Logger.log(Registry.class, LogLevel.FATAL, "Size mismatch between identifiers and values (%d vs %d)", identifiers.length, args.length);
		}
		if (Registry.parameters.get(identifier) == null) {
			Registry.parameters.put(identifier, new HashMap<String, Object>());
		}
		for (int i = 0; i < identifiers.length; i += 1) {
			Registry.parameters.get(identifier).put(identifiers[i], args[i]);
		}
	}

	public static Map<String, Object> parameters(final String identifier) {
		return Registry.parameters.get(identifier);
	}

	/**
	 * Sorts then updates all the g3Application components.
	 */
	public static synchronized void update() {
		final List<G3Object> toUpdate = new ArrayList<>();
		if (!Registry.deletions.isEmpty()) {
			for (final G3Object object : Registry.deletions) {
				for (final String key : Registry.contents.keySet().toArray(new String[Registry.contents.size()])) {
					if (Registry.contents.get(key) == object) {
						Registry.contents.remove(key);
						Logger.log(Registry.class, LogLevel.DEBUG, "G3Object deleted: [\"%s\"]", key);
						return;
					}
				}
			}
		}
		Registry.deletions.clear();
		final List<String> allowedComponents = new ArrayList<>();
		if (Scenes.scene() != null) {
			allowedComponents.addAll(Scenes.scene().sceneObjects());
		}
		for (final String identifier : Registry.contents.keySet()) {
			final G3Object object = Registry.contents.get(identifier);
			if (object != null) {
				if (object.shouldSkipRegistryChecks()) {
					toUpdate.add(object);
					continue;
				}
				if (allowedComponents.contains(identifier)) {
					if (object.shouldSkipRegistryChecks()) {
						toUpdate.add(object);
						continue;
					}
					if ((object instanceof Component) || Registry.skips.contains(object)) {
						continue;
					}
					toUpdate.add(object);
				}
			}
		}
		toUpdate.addAll(Components.activeComponents());
		Collections.sort(toUpdate);
		for (final G3Object object : toUpdate) {
			if (object instanceof Component) {
				final Component component = (Component) object;
				if (component.containsMouse(component.application().mouse.getX(), component.application().mouse.getY()) && component.application().mouse.isButtonDown(Mouse.BUTTON_LEFT)) {
					Components.giveFocus(component);
				}
			}
			object.update();
		}
	}

	/**
	 * Sorts then renders all the g3Application components.
	 */
	public static synchronized void render(final Screen screen) {
		final List<G3Object> toRender = new ArrayList<>();
		final List<String> allowedComponents = new ArrayList<>();
		if (Scenes.scene() != null) {
			allowedComponents.addAll(Scenes.scene().sceneObjects());
		}
		for (final String identifier : Registry.contents.keySet()) {
			final G3Object object = Registry.contents.get(identifier);
			if (object != null) {
				if (object.shouldSkipRegistryChecks()) {
					toRender.add(object);
					continue;
				}
				if (allowedComponents.contains(identifier)) {
					if (object.shouldSkipRegistryChecks()) {
						toRender.add(object);
						continue;
					}
					if ((object instanceof Component) || Registry.skips.contains(object)) {
						continue;
					}
					toRender.add(object);
				}
			}
		}
		toRender.addAll(Components.activeComponents());
		Collections.sort(toRender);
		for (final G3Object object : toRender) {
			object.render(screen);
		}
	}

	/**
	 * Called when a new scene is switched to.
	 */
	public static synchronized void newScene() {
		for (final String identifier : Registry.contents.keySet()) {
			final G3Object object = Registry.contents.get(identifier);
			if (object != null) {
				object.sceneChange();
			}
		}
		Registry.skips.clear();
		Registry.deletions.clear();
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized void updateObject(final String identifier, final double delta) {
		Registry.contents.get(identifier).update();
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized void renderObject(final String identifier, final Screen screen) {
		Registry.contents.get(identifier).render(screen);
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized Collection<G3Object> getObjects() {
		return Registry.contents.values();
	}
}
