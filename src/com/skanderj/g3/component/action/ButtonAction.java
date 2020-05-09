package com.skanderj.g3.component.action;

/**
 * Represents an action that will be executed when a button is in the adequate
 * state.
 * 
 * @author Skander
 *
 */
public interface ButtonAction {
	// Do what's here
	public void execute(Object... args);

	// Default action - do nothing
	public static class DefaultButtonAction implements ButtonAction {
		@Override
		public void execute(Object... args) {
			return;
		}
	}
}
