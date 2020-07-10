package com.skanderj.gingerbread3.scheduler.tasks;

import com.skanderj.gingerbread3.core.Registry;
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

	public DelayedTask(final String identifier, final int delay) {
		this.identifier = identifier;
		this.timer = 0;
		this.delay = delay;
	}

	@Override
	public void update() {
		this.timer += 1;
		if (this.timer >= this.delay) {
			this.execute();
			Registry.markForDeletion(this.identifier);
			Scheduler.delete(this);
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
