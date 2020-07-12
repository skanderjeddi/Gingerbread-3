package com.skanderj.gingerbread3.scene;

import java.util.List;

import com.skanderj.gingerbread3.core.G3Application;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.core.object.G3Object;
import com.skanderj.gingerbread3.display.Screen;

/**
 * Represents the current set of components and updates on screen.
 *
 * @author Skander
 *
 */
public abstract class Scene extends G3Object {
	public Scene(final G3Application g3Application) {
		super(g3Application);
	}

	// Set the scene g3Application objects
	public abstract List<String> sceneObjects();

	// Logic happens here
	@Override
	public synchronized void update() {
		Registry.update();
	}

	// Rendering happens here
	@Override
	public synchronized void render(final Screen screen) {
		Registry.render(screen);
	}

	/**
	 * What to do when the scene is first called into effect.
	 */
	public abstract void enter();

	/**
	 * What to do when the scene is switched.
	 */
	public abstract void exit();

	@Override
	public Priority priority() {
		return Priority.HIGH;
	}

	@Override
	public String description() {
		return Registry.identifier(this) + " -> Scene.class()";
	}
}
