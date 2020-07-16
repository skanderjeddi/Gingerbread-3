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
import com.skanderj.gingerbread3.core.object.ApplicationObject;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.input.Mouse;
import com.skanderj.gingerbread3.logging.Logger;
import com.skanderj.gingerbread3.logging.Logger.LogLevel;
import com.skanderj.gingerbread3.scene.Scene;
import com.skanderj.gingerbread3.scene.Scenes;
import com.skanderj.gingerbread3.transition.Transition;

/**
 * Probably the most important class and handles all the application objects.
 *
 * @author Skander
 *
 */
public final class Engine {
	// All the application objects are stored here
	private final static Map<String, ApplicationObject> contents = new HashMap<>();
	// Args for each object
	private final static Map<String, Map<String, Object>> parameters = new HashMap<>();
	// Application objects to be deleted on the next update
	private final static Set<ApplicationObject> deletions = new HashSet<>();
	// Application objects to be skipped
	private final static Set<ApplicationObject> skips = new HashSet<>();

	private Engine() {
		return;
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized void skip(final String identifier) {
		Engine.skip(Engine.contents.get(identifier));
	}

	/**
	 * Self explanatory.
	 */
	private static synchronized void skip(final ApplicationObject object) {
		Engine.skips.add(object);
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized void unskip(final String identifier) {
		Engine.unskip(Engine.contents.get(identifier));
	}

	/**
	 * Self explanatory.
	 */
	private static synchronized void unskip(final ApplicationObject object) {
		Engine.skips.remove(object);
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized void markForDeletion(final String identifier) {
		Engine.markForDeletion(Engine.contents.get(identifier));
	}

	/**
	 * Self explanatory.
	 */
	private static synchronized void markForDeletion(final ApplicationObject object) {
		Engine.deletions.add(object);
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized void register(final String identifier, final ApplicationObject object) {
		String name = new String();
		if (object instanceof Scene) {
			name = "Scene";
		} else if (object instanceof Updateable) {
			name = "Updateable";
		} else if (object instanceof Renderable) {
			name = "Renderable";
		} else if (object instanceof ApplicationObject) {
			name = "InnerG3Object";
		} else {
			name = object.getClass().getSimpleName();
		}
		Logger.log(Engine.class, LogLevel.DEBUG, "ApplicationObject registered: <class : %s> -> \"%s\"", object.getClass().getSimpleName().equals("") ? object.getClass().getEnclosingClass().getSimpleName() + "#" + name : object.getClass().getSimpleName(), identifier);
		Engine.contents.put(identifier, object);
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized ApplicationObject get(final String identifier) {
		return Engine.contents.getOrDefault(identifier, null);
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized String identifier(final ApplicationObject object) {
		for (final String identifier : Engine.contents.keySet()) {
			if (Engine.contents.get(identifier) == object) {
				return identifier;
			}
		}
		return null;
	}

	public static void parameterize(final String identifier, final String[] identifiers, final Object[] args) {
		if (identifiers.length != args.length) {
			Logger.log(Engine.class, LogLevel.FATAL, "Size mismatch between identifiers and values (%d vs %d)", identifiers.length, args.length);
		}
		if (Engine.parameters.get(identifier) == null) {
			Engine.parameters.put(identifier, new HashMap<String, Object>());
		}
		for (int i = 0; i < identifiers.length; i += 1) {
			Engine.parameters.get(identifier).put(identifiers[i], args[i]);
		}
	}

	public static Map<String, Object> parameters(final String identifier) {
		return Engine.parameters.get(identifier);
	}

	/**
	 * Sorts then updates all the application components.
	 */
	public static synchronized void update() {
		final List<ApplicationObject> toUpdate = new ArrayList<>();
		if (!Engine.deletions.isEmpty()) {
			for (final ApplicationObject object : Engine.deletions) {
				for (final String key : Engine.contents.keySet().toArray(new String[Engine.contents.size()])) {
					if (Engine.contents.get(key) == object) {
						Engine.contents.remove(key);
						Logger.log(Engine.class, LogLevel.DEBUG, "ApplicationObject deleted: [\"%s\"]", key);
						return;
					}
				}
			}
		}
		Engine.deletions.clear();
		final List<String> allowedComponents = new ArrayList<>();
		if (Scenes.scene() != null) {
			allowedComponents.addAll(Scenes.scene().sceneObjects());
		}
		for (final String identifier : Engine.contents.keySet()) {
			final ApplicationObject object = Engine.contents.get(identifier);
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
					if ((object instanceof Component) || Engine.skips.contains(object)) {
						continue;
					}
					toUpdate.add(object);
				}
			}
		}
		toUpdate.addAll(Components.activeComponents());
		Collections.sort(toUpdate);
		for (final ApplicationObject object : toUpdate) {
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
	 * Sorts then renders all the application components.
	 */
	public static synchronized void render(final Screen screen) {
		final List<ApplicationObject> toRender = new ArrayList<>();
		final List<String> allowedComponents = new ArrayList<>();
		if (Scenes.scene() != null) {
			allowedComponents.addAll(Scenes.scene().sceneObjects());
		}
		for (final String identifier : Engine.contents.keySet()) {
			final ApplicationObject object = Engine.contents.get(identifier);
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
					if ((object instanceof Component) || Engine.skips.contains(object)) {
						continue;
					}
					toRender.add(object);
				}
			}
		}
		toRender.addAll(Components.activeComponents());
		Collections.sort(toRender);
		for (final ApplicationObject object : toRender) {
			object.render(screen);
		}
	}

	/**
	 * Called when a new scene is switched to.
	 */
	public static synchronized void newScene() {
		for (final String identifier : Engine.contents.keySet()) {
			final ApplicationObject object = Engine.contents.get(identifier);
			if (object != null) {
				object.sceneChange();
			}
		}
		for (final ApplicationObject applicationObject : Engine.skips.toArray(new ApplicationObject[Engine.skips.size()])) {
			if (applicationObject instanceof Transition) {
				continue;
			} else {
				Engine.skips.remove(applicationObject);
			}
		}
		Engine.deletions.clear();
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized void updateObject(final String identifier, final double delta) {
		Engine.contents.get(identifier).update();
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized void renderObject(final String identifier, final Screen screen) {
		Engine.contents.get(identifier).render(screen);
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized Collection<ApplicationObject> getObjects() {
		return Engine.contents.values();
	}
}
