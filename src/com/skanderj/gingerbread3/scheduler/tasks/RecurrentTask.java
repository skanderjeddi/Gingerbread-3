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
	}

	@Override
	public void update() {
		if ((this.repeats != -1) && (this.repeatsCounter >= this.repeats)) {
			Registry.markForDeletion(this.identifier);
			Scheduler.delete(this);
		}
		if (this.timer == -1) {
			this.execute();
			this.timer = 0;
		}
		this.timer += 1;
		if (this.timer >= this.delay) {
			this.execute();
			this.timer = 0;
			this.repeatsCounter += 1;
			Logger.log(Scheduler.class, LogLevel.DEVELOPMENT, "Resetting recurrent task %s's timer", this.identifier);
		}
	}

	@Override
	public String identifier() {
		return this.identifier;
	}

	public int getDelay() {
		return this.delay;
	}

	public int getRepeats() {
		return this.repeats;
	}
}
