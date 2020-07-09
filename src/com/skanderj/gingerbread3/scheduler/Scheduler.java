package com.skanderj.gingerbread3.scheduler;

import java.util.HashSet;
import java.util.Set;

import com.skanderj.gingerbread3.core.G3Application;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.core.object.G3Object;

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
	public static synchronized void schedule(final G3Application g3Application, final DelayedTask task) {
		Registry.register(task.getIdentifier(), G3Object.constructFromUpdatable(g3Application, task));
		Scheduler.schedule.add(task);
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
