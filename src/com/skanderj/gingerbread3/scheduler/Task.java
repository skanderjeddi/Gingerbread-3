package com.skanderj.gingerbread3.scheduler;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.util.Utilities;

public abstract class Task {
	public static final int DO_NOT_REPEAT = -1;

	private final Application source;
	private final int initialDelay;
	private final int period;

	public Task(final Application source, final int initialDelayInFrames) {
		this(source, initialDelayInFrames, Task.DO_NOT_REPEAT);
	}

	public Task(final Application source, final int initialDelayInFrames, final int periodInFrames) {
		this.source = source;
		this.initialDelay = Utilities.framesToMS(initialDelayInFrames, source.refreshRate());
		this.period = periodInFrames == -1 ? -1 : Utilities.framesToMS(periodInFrames, source.refreshRate());
	}

	public abstract void execute();

	public final Runnable asRunnable() {
		return () -> Task.this.execute();
	}

	public int initialDelay() {
		return this.initialDelay;
	}

	public int period() {
		return this.period;
	}

	public Application source() {
		return this.source;
	}
}
