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
public abstract class LongTask implements Task, Runnable {
	private final Application source;
	private final String identifier;
	private final Thread thread;

	public LongTask(final Application source, final String identifier) {
		this.source = source;
		this.identifier = identifier;
		this.thread = new Thread(this, identifier);
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public final void start() {
		synchronized (this.thread) {
			this.thread.start();
		}
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void run() {
		synchronized (this.thread) {
			this.execute(ApplicationObject.constructFromUpdateable(this.source, this));
		}
		Engine.markForDeletion(this.identifier);
		Scheduler.delete(this.identifier);
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public final String identifier() {
		return this.identifier;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public Application source() {
		return this.source;
	}
}
