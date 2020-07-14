package com.skanderj.gingerbread3.transition;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.object.ApplicationObject;
import com.skanderj.gingerbread3.scene.Scenes;

/**
 * Represents a basic graphical transition.
 *
 * @author Skander
 *
 */
public abstract class Transition extends ApplicationObject {
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
	public void update() {
		this.timer += 1;
		if (this.timer >= this.duration) {
			Engine.skip(Engine.identifier(this));
			if (Scenes.transition() == this) {
				Scenes.transition(null);
			}
		}
	}

	/**
	 * Self explanatory.
	 */
	public final int duration() {
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
