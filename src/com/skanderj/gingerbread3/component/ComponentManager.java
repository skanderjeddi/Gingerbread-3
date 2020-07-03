package com.skanderj.gingerbread3.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.skanderj.gingerbread3.audio.Audios;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.logging.Logger;
import com.skanderj.gingerbread3.logging.Logger.LogLevel;

/**
 * A class used for registering, handling, updating and rendering all the
 * graphical components and dealing with the focus. Can't be instantiated, only
 * static methods.
 *
 * @author Skander
 *
 */
public final class ComponentManager {
	// Graphical debug - mostly drawing components' bounds
	public static final boolean GRAPHICAL_DEBUG = false;

	// Components map, for easily finding any component from any class
	// Makes it possible to not instantiate any actual component
	private static final Map<String, Component> componentsMap = new HashMap<String, Component>();
	// Skipped components
	private static final Set<String> skippedComponents = new HashSet<String>();
	// Currently focused component on screen
	private static Component inFocus = null;

	private ComponentManager() {
		return;
	}

	/**
	 * Self explanatory.
	 */
	public static void register(final String identifier, final Component component) {
		ComponentManager.componentsMap.put(identifier, component);
		Registry.set(identifier, component);
	}

	/**
	 * Self explanatory.
	 */
	public static void register(final String[] identifiers, final Component[] components) {
		if (identifiers.length == components.length) {
			for (int i = 0; i < identifiers.length; i += 1) {
				ComponentManager.componentsMap.put(identifiers[i], components[i]);
			}
		} else {
			Logger.log(ComponentManager.class, LogLevel.ERROR, "Size mismatch between the identifiers and the components lists (%d vs %d)", identifiers.length, components.length);
		}
	}

	/**
	 * Self explanatory. Skips the component and doesn't update/render it..
	 */
	public static void skip(final String identifier) {
		ComponentManager.skippedComponents.add(identifier);
	}

	/**
	 * Self explanatory.
	 */
	public static final void skip(final String... identifiers) {
		for (final String identifier : identifiers) {
			ComponentManager.skip(identifier);
		}
	}

	/**
	 * Self explanatory. Unskips the component and updates/renders it..
	 */
	public static void unskip(final String identifier) {
		ComponentManager.skippedComponents.remove(identifier);
	}

	/**
	 * Self explanatory.
	 */
	public static final void unskip(final String... identifiers) {
		for (final String identifier : identifiers) {
			ComponentManager.unskip(identifier);
		}
	}

	public static void considerOnly(final List<String> identifiers) {
		ComponentManager.inFocus = null;
		ComponentManager.skippedComponents.clear();
		for (final String identifier : ComponentManager.componentsMap.keySet()) {
			if (identifiers.contains(identifier)) {
				continue;
			} else {
				ComponentManager.skip(identifier);
			}
		}
	}

	/**
	 * Self explanatory.
	 */
	public static Component get(final String identifier) {
		final Component component = ComponentManager.componentsMap.get(identifier);
		if (component == null) {
			Logger.log(Audios.class, Logger.LogLevel.SEVERE, "Could not find a component with identifier \"%s\"", identifier);
			return null;
		}
		return component;
	}

	/**
	 * Makes a list of all the active components.
	 */
	public static synchronized List<Component> activeComponents() {
		final List<Component> active = new ArrayList<Component>();
		for (final String identifier : ComponentManager.componentsMap.keySet()) {
			final Component component = ComponentManager.componentsMap.get(identifier);
			if (component.shouldSkipRegistryChecks()) {
				active.add(component);
				continue;
			}
			if (ComponentManager.skippedComponents.contains(identifier)) {
				continue;
			}
			active.add(component);
		}
		return active;
	}

	/**
	 * Gives focus the provided component if focus can be revoked from the currently
	 * focused component.
	 */
	public static synchronized void giveFocus(final String identifier) {
		final Component component = ComponentManager.componentsMap.get(identifier);
		if (component == null) {
			ComponentManager.inFocus = null;
			return;
		}
		if (ComponentManager.inFocus == null) {
			component.grantFocus();
			ComponentManager.inFocus = component;
		} else {
			if (ComponentManager.inFocus.canChangeFocus()) {
				ComponentManager.inFocus.revokeFocus();
				component.grantFocus();
				ComponentManager.inFocus = component;
			}
		}
	}

	/**
	 * Gives focus the provided component if focus can be revoked from the currently
	 * focused component.
	 */
	public static synchronized void giveFocus(final Component component) {
		if (component == null) {
			ComponentManager.inFocus = null;
			return;
		}
		if (ComponentManager.inFocus == null) {
			component.grantFocus();
			ComponentManager.inFocus = component;
		} else {
			if (ComponentManager.inFocus.canChangeFocus()) {
				ComponentManager.inFocus.revokeFocus();
				component.grantFocus();
				ComponentManager.inFocus = component;
			}
		}
	}

	/**
	 * Resets the currently focused component.
	 */
	public static synchronized void revokeFocus() {
		ComponentManager.inFocus = null;
	}

	/**
	 * Self explanatory.
	 *
	 * @return
	 */
	public static Component getInFocus() {
		return ComponentManager.inFocus;
	}
}
