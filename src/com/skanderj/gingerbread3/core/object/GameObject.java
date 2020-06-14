package com.skanderj.gingerbread3.core.object;

import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.display.GraphicsWrapper;

/**
 * Represents a game object, the building blocks of any game.
 *
 * @author Skander
 *
 */
public abstract class GameObject implements Comparable<GameObject> {
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

	public void sceneChange() {
		return;
	}

	public final Game getGame() {
		return this.game;
	}

	public abstract GameObjectPriority priority();

	// Priority comparison, could be nicer but flemme
	@Override
	public int compareTo(final GameObject o) {
		return -(this.priority().priorityIndex - o.priority().priorityIndex);
	}
}
