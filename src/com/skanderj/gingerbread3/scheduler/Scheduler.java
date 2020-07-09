package com.skanderj.gingerbread3.scheduler;

import java.util.HashSet;
import java.util.Set;

/**
 * Name is self explanatory.
 *
 * @author Skander
 *
 */
public final class Scheduler {
	// All scheduled tasks
	private static final Set<DelayedTask> schedule = new HashSet<DelayedTask>();

	private Scheduler() {
		return;
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized void delete(final DelayedTask task) {
		Scheduler.schedule.remove(task);
	}

	public static void update() {
		for (final DelayedTask delayedTask : Scheduler.schedule) {
			delayedTask.update();
		}
	}
}
