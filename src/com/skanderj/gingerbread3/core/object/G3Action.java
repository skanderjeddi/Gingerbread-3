package com.skanderj.gingerbread3.core.object;

/**
 * Represents an actions that will be executed.
 *
 * @author Skander
 *
 */
public interface G3Action {
	G3Action DEFAULT_DO_NOTHING = () -> {
		return;
	};

	// Do what's here
	void execute();
}
