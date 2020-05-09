package com.skanderj.g3.component;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.skanderj.g3.audio.AudioManager;
import com.skanderj.g3.inputdevice.Keyboard;
import com.skanderj.g3.inputdevice.Mouse;
import com.skanderj.g3.log.Logger;
import com.skanderj.g3.log.Logger.LogLevel;
import com.skanderj.g3.translation.TranslationManager;
import com.skanderj.g3.window.Window;

/**
 * A class used for registering, handling, updating and rendering all the
 * graphical components and dealing with the focus. Can't be instantiated, only
 * static methods.
 *
 * @author Skander
 *
 */
public final class ComponentManager {
	// Translations keys
	private static final String KEY_COMPONENT_MANAGER_MISSING_COMPONENT = "key.componentmanager.missing_component";
	private static final String KEY_COMPONENT_MANAGER_SIZE_MISMATCH_IDENTIFIERS_COMPONENTS = "key.componentmanager.size.mismatch.identifiers.components";

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
	public static final void addComponent(String identifier, Component component) {
		ComponentManager.componentsMap.put(identifier, component);
	}

	/**
	 * Self explanatory.
	 */
	public static final void addComponents(String[] identifiers, Component[] components) {
		if (identifiers.length == components.length) {
			for (int i = 0; i < identifiers.length; i += 1) {
				ComponentManager.componentsMap.put(identifiers[i], components[i]);
			}
		} else {
			Logger.log(ComponentManager.class, LogLevel.ERROR, TranslationManager.getKey(ComponentManager.KEY_COMPONENT_MANAGER_SIZE_MISMATCH_IDENTIFIERS_COMPONENTS, identifiers.length, components.length));
		}
	}

	/**
	 * Self explanatory. Skips the component and doesn't update/render it..
	 */
	public static final void skipComponent(String identifier) {
		ComponentManager.skippedComponents.add(identifier);
	}

	/**
	 * Self explanatory.
	 */
	public static final void skipComponents(String... identifiers) {
		for (String identifier : identifiers) {
			ComponentManager.skipComponent(identifier);
		}
	}

	/**
	 * Self explanatory. Unskips the component and updates/renders it..
	 */
	public static final void unskipComponent(String identifier) {
		ComponentManager.skippedComponents.remove(identifier);
	}

	/**
	 * Self explanatory.
	 */
	public static final void unskipComponents(String... identifiers) {
		for (String identifier : identifiers) {
			ComponentManager.unskipComponent(identifier);
		}
	}

	/**
	 * Self explanatory.
	 */
	public static final Component getComponent(String identifier) {
		Component component = ComponentManager.componentsMap.get(identifier);
		if (component == null) {
			Logger.log(AudioManager.class, Logger.LogLevel.SEVERE, TranslationManager.getKey(ComponentManager.KEY_COMPONENT_MANAGER_MISSING_COMPONENT, identifier));
			return null;
		}
		return component;
	}

	/**
	 * Updates every component.
	 */
	public static final synchronized void update(double delta, Keyboard keyboard, Mouse mouse, Object... args) {
		for (String identifier : ComponentManager.componentsMap.keySet()) {
			if (ComponentManager.skippedComponents.contains(identifier)) {
				continue;
			}
			Component component = ComponentManager.componentsMap.get(identifier);
			if (component.containsMouse(mouse.getX(), mouse.getY()) && mouse.isButtonDown(Mouse.BUTTON_LEFT)) {
				ComponentManager.giveFocus(identifier);
			}
			component.update(delta, keyboard, mouse, args);
		}
	}

	// TODO: not finished
	/**
	 * Updates a specific component (for special cases).
	 */
	public static final synchronized void updateSpecific(String identifier, double delta, Keyboard keyboard, Mouse mouse, Object... args) {
		ComponentManager.componentsMap.get(identifier).update(delta, keyboard, mouse, args);
	}

	/**
	 * Draws every component.
	 */
	public static final synchronized void render(Window window, Graphics2D graphics) {
		for (String identifier : ComponentManager.componentsMap.keySet()) {
			if (ComponentManager.skippedComponents.contains(identifier)) {
				continue;
			}
			Component component = ComponentManager.componentsMap.get(identifier);
			component.render(window, graphics);
		}
	}

	// TODO: not finished
	/**
	 * Draws a specific component (for special cases).
	 */
	public static final synchronized void updateSpecific(String identifier, Window window, Graphics2D graphics, Object... args) {
		ComponentManager.componentsMap.get(identifier).render(window, graphics, args);
	}

	/**
	 * Gives focus the provided component if focus can be revoked from the currently
	 * focused component.
	 */
	public static final synchronized void giveFocus(String identifier) {
		Component component = ComponentManager.componentsMap.get(identifier);
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
	public static final synchronized void revokeFocus() {
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
