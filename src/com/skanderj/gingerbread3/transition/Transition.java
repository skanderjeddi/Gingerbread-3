package com.skanderj.gingerbread3.transition;

import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.core.object.GameObject;
import com.skanderj.gingerbread3.scene.SceneManager;

/**
 * Represents a basic graphical transition.
 *
 * @author Skander
 *
 */
public abstract class Transition extends GameObject {
	protected int duration, timer;

	public Transition(final Game game, final int duration) {
		super(game);
		this.duration = duration;
		this.timer = 0;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void update(final double delta, final Object... args) {
		this.timer += 1;
		if (this.timer >= this.duration) {
			Registry.skip(Registry.identifier(this));
			if (SceneManager.getCurrentTransition() == this) {
				SceneManager.transition(null);
			}
		}
	}

	/**
	 * Self explanatory.
	 */
	public final int getDuration() {
		return this.duration;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void sceneChange() {
		this.timer = 0;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public Priority priority() {
		return Priority.CRITICAL;
	}
}
