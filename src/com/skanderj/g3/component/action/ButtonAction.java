package com.skanderj.g3.component.action;

public interface ButtonAction {
	public void execute(Object... args);

	public static class DefaultButtonAction implements ButtonAction {
		@Override
		public void execute(Object... args) {
			return;
		}
	}
}
