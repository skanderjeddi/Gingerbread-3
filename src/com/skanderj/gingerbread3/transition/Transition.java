package com.skanderj.gingerbread3.transition;

import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.core.object.GameObject;
import com.skanderj.gingerbread3.core.object.GameObjectPriority;
import com.skanderj.gingerbread3.core.object.GameRegistry;
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

	@Override
	public void update(final double delta, final Object... args) {
		this.timer += 1;
		if (this.timer >= this.duration) {
			GameRegistry.skip(this);
			if (SceneManager.getCurrentTransition() == this) {
				SceneManager.transition(null);
			}
		}
	}

	public final int getDuration() {
		return this.duration;
	}

	@Override
	public void sceneChange() {
		this.timer = 0;
	}

	@Override
	public GameObjectPriority priority() {
		return GameObjectPriority.CRITICAL;
	}
}
