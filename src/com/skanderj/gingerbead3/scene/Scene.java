package com.skanderj.gingerbead3.scene;

import java.awt.Graphics2D;
import java.util.List;

import com.skanderj.gingerbead3.component.ComponentManager;
import com.skanderj.gingerbead3.display.Window;
import com.skanderj.gingerbead3.input.Keyboard;
import com.skanderj.gingerbead3.input.Mouse;

/**
 * Represents the current set of components and updates on screen.
 *
 * @author Skander
 *
 */
public abstract class Scene {
	// Set the scene components
	public abstract List<String> sceneComponents();

	// Logic happens here
	public synchronized void update(final double delta, final Keyboard keyboard, final Mouse mouse) {
		ComponentManager.update(delta, keyboard, mouse);
	}

	// Rendering happens here
	public synchronized void render(final Window window, final Graphics2D graphics) {
		ComponentManager.render(window, graphics);
	}

	/**
	 * What to do when the scene is first called into effect.
	 */
	public abstract void present();

	/**
	 * What to do when the scene is switched.
	 */
	public abstract void remove();
}
