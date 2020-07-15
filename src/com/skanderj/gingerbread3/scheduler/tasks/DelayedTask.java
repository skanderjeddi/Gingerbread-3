package com.skanderj.gingerbread3.scheduler.tasks;

import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.scheduler.Scheduler;
import com.skanderj.gingerbread3.scheduler.Task;

/**
 *
 * @author Skander TODO
 *
 */
public abstract class DelayedTask implements Task {
	private final String identifier;
	private int timer;
	private final int delay;
	private final Thread thread;

	public DelayedTask(final String identifier, final int delay) {
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
			this.execute(null);
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
}
