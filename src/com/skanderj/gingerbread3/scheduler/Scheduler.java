package com.skanderj.gingerbread3.scheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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
	private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(0);

	private static final Map<String, ScheduledFuture<?>> scheduledFutures = new HashMap<>();

	private Scheduler() {
		return;
	}

	public static void scheduleTask(final String identifier, final Task task) {
		Logger.log(Scheduler.class, LogLevel.INFO, "Scheduling task %s", identifier);
		Scheduler.schedule(identifier, task.asRunnable(), task.initialDelay(), task.period() != -1, task.period());
	}

	public static ScheduledFuture<?> schedule(final String identifier, final Runnable runnable, final int delay, final boolean repeat, final int delayBetweenRepeats) {
		ScheduledFuture<?> future = null;
		if (repeat) {
			future = Scheduler.executor.scheduleAtFixedRate(runnable, delay, delayBetweenRepeats, TimeUnit.MILLISECONDS);
		} else {
			future = Scheduler.executor.schedule(runnable, delay, TimeUnit.MILLISECONDS);
		}
		Scheduler.scheduledFutures.put(identifier, future);
		return future;
	}

	public static void cancel(final String identifier, final boolean finish) {
		Scheduler.scheduledFutures.get(identifier).cancel(!finish);
	}
}
