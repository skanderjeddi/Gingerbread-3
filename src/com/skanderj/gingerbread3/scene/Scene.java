package com.skanderj.gingerbread3.scene;

import java.util.List;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.core.object.GameObject;
import com.skanderj.gingerbread3.display.Screen;

/**
 * Represents the current set of components and updates on screen.
 *
 * @author Skander
 *
 */
public abstract class Scene extends GameObject {
	public Scene(final Application application) {
		super(application);
	}

	// Set the scene application objects
	public abstract List<String> sceneObjects();

	// Logic happens here
	@Override
	public synchronized void update(final double delta) {
		Registry.update(delta);
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
}
