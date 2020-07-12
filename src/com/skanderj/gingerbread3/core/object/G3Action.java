package com.skanderj.gingerbread3.core.object;

/**
 * Represents an actions that will be executed.
 *
 * @author Skander
 *
 */
public interface G3Action {
	public static final G3Action DEFAULT_DO_NOTHING = new G3Action() {
		@Override
		public void execute() {
			return;
		}
	};

	// Do what's here
	void execute();
}
