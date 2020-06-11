package com.skanderj.gingerbead3.component;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.skanderj.gingerbead3.audio.AudioManager;
import com.skanderj.gingerbead3.display.Window;
import com.skanderj.gingerbead3.input.Keyboard;
import com.skanderj.gingerbead3.input.Mouse;
import com.skanderj.gingerbead3.log.Logger;
import com.skanderj.gingerbead3.log.Logger.LogLevel;

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
	public static final boolean GRAPHICAL_DEBUG = true;

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
	public static void addComponent(final String identifier, final Component component) {
		ComponentManager.componentsMap.put(identifier, component);
	}

	/**
	 * Self explanatory.
	 */
	public static void addComponents(final String[] identifiers, final Component[] components) {
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
	public static void skipComponent(final String identifier) {
		ComponentManager.skippedComponents.add(identifier);
	}

	/**
	 * Self explanatory.
	 */
	public static final void skipComponents(final String... identifiers) {
		for (final String identifier : identifiers) {
			ComponentManager.skipComponent(identifier);
		}
	}

	/**
	 * Self explanatory. Unskips the component and updates/renders it..
	 */
	public static void unskipComponent(final String identifier) {
		ComponentManager.skippedComponents.remove(identifier);
	}

	/**
	 * Self explanatory.
	 */
	public static final void unskipComponents(final String... identifiers) {
		for (final String identifier : identifiers) {
			ComponentManager.unskipComponent(identifier);
		}
	}

	public static void onlyConsider(final List<String> identifiers) {
		ComponentManager.skippedComponents.clear();
		for (final String identifier : ComponentManager.componentsMap.keySet()) {
			if (identifiers.contains(identifier)) {
				continue;
			} else {
				ComponentManager.skipComponent(identifier);
			}
		}
	}

	/**
	 * Self explanatory.
	 */
	public static Component getComponent(final String identifier) {
		final Component component = ComponentManager.componentsMap.get(identifier);
		if (component == null) {
			Logger.log(AudioManager.class, Logger.LogLevel.SEVERE, "Could not find a component with identifier \"%s\"", identifier);
			return null;
		}
		return component;
	}

	/**
	 * Updates every component.
	 */
	public static final synchronized void update(final double delta, final Keyboard keyboard, final Mouse mouse, final Object... args) {
		final List<Component> toUpdate = new ArrayList<Component>();
		for (final String identifier : ComponentManager.componentsMap.keySet()) {
			if (ComponentManager.skippedComponents.contains(identifier)) {
				continue;
			}
			final Component component = ComponentManager.componentsMap.get(identifier);
			toUpdate.add(component);
		}
		Collections.sort(toUpdate);
		for (final Component component : toUpdate) {
			if (component.containsMouse(mouse.getX(), mouse.getY()) && mouse.isButtonDown(Mouse.BUTTON_LEFT)) {
				ComponentManager.giveFocus(component);
			}
			component.update(delta, keyboard, mouse, args);
		}
	}

	// TODO: not finished
	/**
	 * Updates a specific component (for special cases).
	 */
	public static final synchronized void updateSpecific(final String identifier, final double delta, final Keyboard keyboard, final Mouse mouse, final Object... args) {
		ComponentManager.componentsMap.get(identifier).update(delta, keyboard, mouse, args);
	}

	/**
	 * Draws every component.
	 */
	public static synchronized void render(final Window window, final Graphics2D graphics) {
		final List<Component> toRender = new ArrayList<Component>();
		for (final String identifier : ComponentManager.componentsMap.keySet()) {
			if (ComponentManager.skippedComponents.contains(identifier)) {
				continue;
			}
			final Component component = ComponentManager.componentsMap.get(identifier);
			toRender.add(component);
		}
		Collections.sort(toRender);
		for (final Component component : toRender) {
			component.render(window, graphics);
		}

	}

	// TODO: not finished
	/**
	 * Draws a specific component (for special cases).
	 */
	public static final synchronized void updateSpecific(final String identifier, final Window window, final Graphics2D graphics, final Object... args) {
		ComponentManager.componentsMap.get(identifier).render(window, graphics, args);
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
