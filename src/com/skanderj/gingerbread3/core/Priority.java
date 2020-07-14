package com.skanderj.gingerbread3.core;

/**
 * Represents a application object priority, or what order do the objects get
 * updated/drawn in.
 *
 * @author Skander
 *
 */
public enum Priority {
	MONITOR(-100), EXTREMELY_LOW(-10), LOW(-1), REGULAR(0), HIGH(1), EXTREMELY_HIGH(10), CRITICAL(100);

	public int priorityIndex;

	private Priority(final int index) {
		this.priorityIndex = index;
	}

	public int priorityIndex() {
		return this.priorityIndex;
	}
}
