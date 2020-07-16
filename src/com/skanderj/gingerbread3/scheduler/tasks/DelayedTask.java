package com.skanderj.gingerbread3.scheduler.tasks;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.object.ApplicationObject;
import com.skanderj.gingerbread3.scheduler.Scheduler;
import com.skanderj.gingerbread3.scheduler.Task;

/**
 *
 * @author Skander TODO
 *
 */
public abstract class DelayedTask implements Task {
	private final Application source;
	private final String identifier;
	private int timer;
	private final int delay;
	private final Thread thread;

	public DelayedTask(final Application source, final String identifier, final int delay) {
		this.source = source;
		this.identifier = identifier;
		this.timer = 0;
		this.delay = delay;
		this.thread = new Thread(this, identifier);
	}

	@Override
	public final void start() {
		return;
	}

	@Override
	public final void run() {
		synchronized (this.thread) {
			this.execute(ApplicationObject.constructFromUpdateable(this.source, this));
		}
		Engine.markForDeletion(this.identifier);
		Scheduler.delete(this.identifier);
	}

	@Override
	public final void update() {
		this.timer += 1;
		if (this.timer >= this.delay) {
			synchronized (this.thread) {
				if (!this.thread.isAlive()) {
					this.thread.start();
				}
			}
		}
	}

	@Override
	public String identifier() {
		return this.identifier;
	}

	public int getDelay() {
		return this.delay;
	}

	@Override
	public Application source() {
		return this.source;
	}
}
