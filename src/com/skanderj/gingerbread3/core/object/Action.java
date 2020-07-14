package com.skanderj.gingerbread3.core.object;

/**
 * Represents an actions that will be executed.
 *
 * @author Skander
 *
 */
public interface Action {
	public static final Action DEFAULT_DO_NOTHING = new Action() {
		@Override
		public void execute(ApplicationObject object) {
			return;
		}
	};

	// Do what's here
	void execute(ApplicationObject object);
}
