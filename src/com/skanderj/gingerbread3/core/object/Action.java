package com.skanderj.gingerbread3.core.object;

/**
 * Represents an action that will be executed.
 *
 * @author Skander
 *
 */
public interface Action {
	// Do what's here
	void execute(Object... args);

	// Default action - do nothing
	public static class DefaultAction implements Action {
		@Override
		public void execute(final Object... args) {
			return;
		}
	}
}
