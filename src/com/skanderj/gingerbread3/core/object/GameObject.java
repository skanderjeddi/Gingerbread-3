package com.skanderj.gingerbread3.core.object;

import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.Renderable;
import com.skanderj.gingerbread3.core.Updatable;
import com.skanderj.gingerbread3.display.GraphicsWrapper;

/**
 * Represents a game object, the building blocks of any game.
 *
 * @author Skander
 *
 */
public abstract class GameObject implements Comparable<GameObject> {
	/**
	 * Self explanatory.
	 */
	public static final GameObject constructFromUpdatable(final Game game, final Updatable updatable) {
		return new GameObject(game) {

			@Override
			public void update(final double delta, final Object... args) {
				updatable.update(delta, args);
			}

			@Override
			public void render(final GraphicsWrapper graphics) {
				return;
			}

			@Override
			public Priority priority() {
				return updatable.priority();
			}
		};
	}

	/**
	 * Self explanatory.
	 */
	public static final GameObject constructFromRenderable(final Game game, final Renderable renderable) {
		return new GameObject(game) {

			@Override
			public void update(final double delta, final Object... args) {
				return;
			}

			@Override
			public void render(final GraphicsWrapper graphics) {
				renderable.render(graphics);
			}

			@Override
			public Priority priority() {
				return renderable.priority();
			}
		};
	}

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
	public abstract void render(GraphicsWrapper graphics);

	public void sceneChange() {
		return;
	}

	public final Game getGame() {
		return this.game;
	}

	public abstract Priority priority();

	// Priority comparison, could be nicer but flemme
	@Override
	public int compareTo(final GameObject o) {
		return -(this.priority().priorityIndex - o.priority().priorityIndex);
	}
}
