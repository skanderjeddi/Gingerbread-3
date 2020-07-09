package com.skanderj.gingerbread3.transition;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.core.object.GameObject;
import com.skanderj.gingerbread3.scene.Scenes;

/**
 * Represents a basic graphical transition.
 *
 * @author Skander
 *
 */
public abstract class Transition extends GameObject {
	protected int duration, timer;

	public Transition(final Application application, final int duration) {
		super(application);
		this.duration = duration;
		this.timer = 0;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void update(final double delta) {
		this.timer += 1;
		if (this.timer >= this.duration) {
			Registry.skip(Registry.identifier(this));
			if (Scenes.transition() == this) {
				Scenes.transition(null);
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
