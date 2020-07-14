package com.skanderj.gingerbread3.scheduler;

import java.util.HashSet;
import java.util.Set;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.core.object.ApplicationObject;
import com.skanderj.gingerbread3.logging.Logger;
import com.skanderj.gingerbread3.logging.Logger.LogLevel;

/**
 * Name is self explanatory.
 *
 * @author Skander
 *
 */
public final class Scheduler {
	// All scheduled actions
	private static final Set<Task> schedule = new HashSet<>();

	private Scheduler() {
		return;
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized void scheduleTask(final Application application, final Task task) {
		Registry.register(task.identifier(), ApplicationObject.constructFromUpdateable(application, task));
		Scheduler.schedule.add(task);
		task.start();
	}

	/**
	 * Self explanatory.
	 */
	public static synchronized void delete(final Task task) {
		Scheduler.schedule.remove(task);
	}

	public static void update() {
		for (final Task task : Scheduler.schedule.toArray(new Task[Scheduler.schedule.size()])) {
			Logger.log(Scheduler.class, LogLevel.DEVELOPMENT, "Executing task %s", task.identifier());
			task.update();
		}
	}
}
