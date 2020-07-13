package com.skanderj.gingerbread3.scheduler.tasks;

import com.skanderj.gingerbread3.core.Registry;
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

	private final String identifier;
	private int timer;
	private final int delay;
	private final int repeats;
	private int repeatsCounter;
	private final Thread thread;

	public RecurrentTask(final String identifier, final int delay) {
		this(identifier, delay, RecurrentTask.REPEAT_INDEFINITELY);
	}

	public RecurrentTask(final String identifier, final int delay, final int repeats) {
		this(identifier, delay, repeats, false);
	}

	public RecurrentTask(final String identifier, final int delay, final int repeats, final boolean delayOnFirstExecution) {
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

	@Override
	public final void start() {
		synchronized (this.thread) {
			this.thread.start();
		}
	}

	@Override
	public final void run() {
		while (true) {
			synchronized (this.thread) {
				if ((this.repeats != -1) && (this.repeatsCounter >= this.repeats)) {
					Registry.markForDeletion(this.identifier);
					Scheduler.delete(this);
					try {
						this.thread.join();
					} catch (final InterruptedException exception) {
						exception.printStackTrace();
					}
				}
				if (this.timer == -1) {
					this.execute(null);
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

	@Override
	public final void update() {
		synchronized (this.thread) {
			this.thread.notify();
		}
	}

	@Override
	public String identifier() {
		return this.identifier;
	}

	public int delay() {
		return this.delay;
	}

	public int repeats() {
		return this.repeats;
	}
}
