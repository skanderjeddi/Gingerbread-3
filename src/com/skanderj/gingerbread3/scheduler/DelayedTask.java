package com.skanderj.gingerbread3.scheduler;

import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.core.Updatable;

public abstract class DelayedTask implements Updatable, Task {
	private final String identifier;
	private int timer;
	private final int executeAfter;

	public DelayedTask(final String identifier, final int executeAfter) {
		this.identifier = identifier;
		this.timer = 0;
		this.executeAfter = executeAfter;
	}

	@Override
	public void update() {
		this.timer += 1;
		if (this.timer >= this.executeAfter) {
			this.execute();
			Registry.markForDeletion(this.identifier);
			Scheduler.delete(this);
		}
	}
}
