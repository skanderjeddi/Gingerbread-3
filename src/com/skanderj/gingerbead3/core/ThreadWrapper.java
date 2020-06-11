package com.skanderj.gingerbead3.core;

/**
 * Ripped off of the older Gingerbread versions.
 *
 * @author Skander
 *
 */
public abstract class ThreadWrapper {
	public static final int EXIT_SUCCESS = 0, EXIT_FAILURE = -1;

	private final Thread thread;
	protected boolean isRunning;

	public ThreadWrapper(final String identifier) {
		this.thread = new Thread((Runnable) ThreadWrapper.this::run, String.format("%s", identifier));
		this.isRunning = false;
	}

	/**
	 * Starts the thread - does nothing if the same instance is already running
	 */
	public final void start() {
		if (this.isRunning) {
			return;
		} else {
			this.isRunning = true;
			this.thread.start();
		}
	}

	/**
	 * Called before entering the main loop
	 */
	protected abstract void create();

	/**
	 * Stops the thread - does nothing if the same instance is already running
	 */
	public final void stop() {
		if (this.isRunning) {
			this.isRunning = false;
			this.thread.interrupt();
		} else {
			return;
		}
	}

	/**
	 * Called after exiting the main loop
	 */
	protected abstract void destroy();

	private void run() {
		this.create();
		while (this.isRunning) {
			this.loop();
		}
		this.destroy();
	}

	/**
	 * Gets called continuously until thread is stopped
	 */
	protected abstract void loop();
}