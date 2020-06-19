package com.skanderj.gingerbread3.scene;

import java.util.List;

import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.core.object.GameObject;
import com.skanderj.gingerbread3.display.GraphicsWrapper;

/**
 * Represents the current set of components and updates on screen.
 *
 * @author Skander
 *
 */
public abstract class Scene extends GameObject {
	public Scene(final Game game) {
		super(game);
	}

	// Set the scene game objects
	public abstract List<String> sceneObjects();

	// Logic happens here
	@Override
	public synchronized void update(final double delta, final Object... args) {
		Registry.update(delta);
		// ComponentManager.update(delta, this.game.getKeyboard(),
		// this.game.getMouse());
	}

	// Rendering happens here
	@Override
	public synchronized void render(final GraphicsWrapper graphics) {
		Registry.render(graphics);
		// ComponentManager.render(this.game.getWindow(), graphics);
	}

	/**
	 * What to do when the scene is first called into effect.
	 */
	public abstract void present();

	/**
	 * What to do when the scene is switched.
	 */
	public abstract void remove();

	@Override
	public Priority priority() {
		return Priority.HIGH;
	}
}
