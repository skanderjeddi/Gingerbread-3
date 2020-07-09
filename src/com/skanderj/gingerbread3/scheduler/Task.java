package com.skanderj.gingerbread3.scheduler;

/**
 * Represents an action that will be executed.
 *
 * @author Skander
 *
 */
public interface Task {
	Task DEFAULT_DO_NOTHING = args -> {
		return;
	};

	// Do what's here
	void execute(Object... args);
}
