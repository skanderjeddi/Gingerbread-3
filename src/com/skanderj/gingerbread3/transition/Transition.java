package com.skanderj.gingerbread3.transition;

import com.skanderj.gingerbread3.core.G3Application;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.core.object.G3Object;
import com.skanderj.gingerbread3.scene.Scenes;

/**
 * Represents a basic graphical transition.
 *
 * @author Skander
 *
 */
public abstract class Transition extends G3Object {
	protected int duration, timer;

	public Transition(final G3Application g3Application, final int duration) {
		super(g3Application);
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
			Registry.skip(Registry.identifier(this));
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
