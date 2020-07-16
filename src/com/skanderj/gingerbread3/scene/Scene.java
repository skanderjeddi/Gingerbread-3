package com.skanderj.gingerbread3.scene;

import java.util.List;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.object.ApplicationObject;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.util.Utilities;

/**
 * Represents the current set of components and updates on screen.
 *
 * @author Skander
 *
 */
public abstract class Scene extends ApplicationObject {
	public Scene(final Application application) {
		super(application);
	}

	// Set the scene application objects
	public abstract List<String> sceneObjects();

	// Logic happens here
	@Override
	public synchronized void update() {
		Engine.update();
	}

	// Rendering happens here
	@Override
	public synchronized void render(final Screen screen) {
		Engine.render(screen);
	}

	/**
	 * What to do when the scene is first called into effect.
	 */
	public abstract void in();

	/**
	 * What to do when the scene is switched.
	 */
	public abstract void out();

	/**
	 * Entering transition.
	 */
	public String inTransition() {
		return Utilities.NULL_STRING;
	}

	/**
	 * Exiting transition.
	 */
	public String outTransition() {
		return Utilities.NULL_STRING;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public Priority priority() {
		return Priority.REGULAR;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public String description() {
		return Engine.identifier(this) + " -> Scene.class()";
	}
}
