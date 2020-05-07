package com.skanderj.g3.component;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import com.skanderj.g3.audio.AudioManager;
import com.skanderj.g3.inputdevice.Keyboard;
import com.skanderj.g3.inputdevice.Mouse;
import com.skanderj.g3.log.Logger;
import com.skanderj.g3.window.Window;

public final class ComponentManager {
	private static final Map<String, Component> componentsMap = new HashMap<String, Component>();
	private static Component inFocus = null;

	private ComponentManager() {
		return;
	}

	public static final void addComponent(String identifier, Component component) {
		ComponentManager.componentsMap.put(identifier, component);
	}

	public static final Component getComponent(String identifier) {
		Component component = componentsMap.get(identifier);
		if (component == null) {
			Logger.log(AudioManager.class, Logger.LogLevel.SEVERE, "Cound not find audio with identifier \"%s\"!", identifier);
			return null;
		}
		return component;
	}

	public static final synchronized void update(double delta, Keyboard keyboard, Mouse mouse) {
		for (String identifier : ComponentManager.componentsMap.keySet()) {
			Component component = ComponentManager.componentsMap.get(identifier);
			if (component.containsMouse(mouse.getX(), mouse.getY()) && mouse.isButtonDown(Mouse.BUTTON_LEFT)) {
				ComponentManager.giveFocus(identifier);
			}
			component.update(delta, keyboard, mouse);
		}
	}

	public static final synchronized void updateSpecific(String identifier, double delta, Keyboard keyboard, Mouse mouse, Object... args) {
		ComponentManager.componentsMap.get(identifier).update(delta, keyboard, mouse, args);
	}

	public static final synchronized void render(Window window, Graphics2D graphics) {
		for (Component component : ComponentManager.componentsMap.values()) {
			component.render(window, graphics);
		}
	}

	public static final synchronized void updateSpecific(String identifier, Window window, Graphics2D graphics, Object... args) {
		ComponentManager.componentsMap.get(identifier).render(window, graphics, args);
	}

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

	public static final synchronized void revokeFocus() {
		ComponentManager.inFocus = null;
	}

	public static Component getInFocus() {
		return ComponentManager.inFocus;
	}
}
