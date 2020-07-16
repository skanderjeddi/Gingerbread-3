package com.skanderj.gingerbread3.scheduler.tasks;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.object.ApplicationObject;
import com.skanderj.gingerbread3.logging.Logger;
import com.skanderj.gingerbread3.logging.Logger.LogLevel;
import com.skanderj.gingerbread3.scheduler.Scheduler;
import com.skanderj.gingerbread3.scheduler.Task;

/**
 *
 * @author Skander TODO
 *
 */
public abstract class RecurrentTask implements Task {
	public static final int REPEAT_INDEFINITELY = -1;

	private final Application source;
	private final String identifier;
	private int timer;
	private final int delay;
	private final int repeats;
	private int repeatsCounter;
	private final Thread thread;

	public RecurrentTask(final Application source, final String identifier, final int delay) {
		this(source, identifier, delay, RecurrentTask.REPEAT_INDEFINITELY);
	}

	public RecurrentTask(final Application source, final String identifier, final int delay, final int repeats) {
		this(source, identifier, delay, repeats, false);
	}

	public RecurrentTask(final Application source, final String identifier, final int delay, final int repeats, final boolean delayOnFirstExecution) {
		this.source = source;
		this.identifier = identifier;
		this.delay = delay;
		this.repeats = repeats;
		this.repeatsCounter = 0;
		if (delayOnFirstExecution) {
			this.timer = 0;
		} else {
			this.timer = -1;
		}
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
	public final void run() {
		while (true) {
			synchronized (this.thread) {
				if ((this.repeats != -1) && (this.repeatsCounter >= this.repeats)) {
					Engine.markForDeletion(this.identifier);
					Scheduler.delete(this.identifier);
					try {
						this.thread.join();
					} catch (final InterruptedException exception) {
						exception.printStackTrace();
					}
				}
				if (this.timer == -1) {
					this.execute(ApplicationObject.constructFromUpdateable(this.source, this));
					this.timer = 0;
				}
				this.timer += 1;
				if (this.timer >= this.delay) {
					this.execute(null);
					this.timer = 0;
					this.repeatsCounter += 1;
					Logger.log(Scheduler.class, LogLevel.DEVELOPMENT, "Resetting recurrent task %s's timer", this.identifier);
				}
				try {
					this.thread.wait();
				} catch (final InterruptedException interruptedException) {
					interruptedException.printStackTrace();
				}
			}
		}
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public final void update() {
		synchronized (this.thread) {
			this.thread.notify();
		}
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public String identifier() {
		return this.identifier;
	}

	/**
	 * Self explanatory.
	 */
	public int delay() {
		return this.delay;
	}

	/**
	 * Self explanatory.
	 */
	public int repeats() {
		return this.repeats;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public Application source() {
		return this.source;
	}
}
