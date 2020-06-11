package com.skanderj.gingerbread3.component.action;

/**
 * Represents an action that will be executed when a button is in the adequate
 * currentState.
 *
 * @author Skander
 *
 */
public interface ButtonAction {
	// Do what's here
	void execute(Object... args);

	// Default action - do nothing
	public static class DefaultButtonAction implements ButtonAction {
		@Override
		public void execute(final Object... args) {
			return;
		}
	}
}
