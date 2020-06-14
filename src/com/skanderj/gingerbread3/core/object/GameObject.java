package com.skanderj.gingerbread3.core.object;

import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.display.GraphicsWrapper;

/**
 * Represents a game object, the building blocks of any game.
 *
 * @author Skander
 *
 */
public abstract class GameObject {
	protected final Game game;

	public GameObject(final Game game) {
		this.game = game;
	}

	/**
	 * Updates the component - called by other classes.
	 */
	public abstract void update(double delta, Object... args);

	/**
	 * Renders the component - called by other classes.
	 */
	public abstract void render(GraphicsWrapper graphics, Object... args);

	public final Game getGame() {
		return this.game;
	}
}
