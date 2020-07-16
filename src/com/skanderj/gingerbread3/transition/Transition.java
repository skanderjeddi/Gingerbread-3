package com.skanderj.gingerbread3.transition;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.object.ApplicationObject;

/**
 * Represents a basic graphical transition.
 *
 * @author Skander
 *
 */
public abstract class Transition extends ApplicationObject {
	protected int duration, timer;
	protected boolean isDone;

	public Transition(final Application application, final int duration) {
		super(application);
		this.duration = duration;
		this.timer = 0;
		this.isDone = false;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void update() {
		if (!this.isDone) {
			this.timer += 1;
			if (this.timer >= this.duration) {
				this.isDone = true;
				Engine.skip(Engine.identifier(this));
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
		this.isDone = false;
		this.timer = 0;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public Priority priority() {
		return Priority.EXTREMELY_LOW;
	}

	/**
	 * Self explanatory.
	 */
	public boolean isDone() {
		return this.isDone;
	}
}
