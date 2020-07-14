package com.skanderj.gingerbread3.scheduler.tasks;

import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.scheduler.Scheduler;
import com.skanderj.gingerbread3.scheduler.Task;

public abstract class LongTask implements Task, Runnable {
	private final String identifier;
	private final Thread thread;

	public LongTask(final String identifier) {
		this.identifier = identifier;
		this.thread = new Thread(this, identifier);
	}

	@Override
	public final void start() {
		synchronized (this.thread) {
			this.thread.start();
		}
	}

	@Override
	public void run() {
		synchronized (this.thread) {
			this.execute(null);
		}
		Engine.markForDeletion(this.identifier);
		Scheduler.delete(this);
	}

	@Override
	public final String identifier() {
		return this.identifier;
	}
}
