package com.skanderj.gingerbread3.core;

/**
 * Represents a game object priority, or what order do the objects get
 * updated/drawn in.
 *
 * @author Skander
 *
 */
public enum Priority {
	EXTREMELY_LOW(-10), LOW(-1), REGULAR(0), HIGH(1), EXTREMELY_HIGH(10), CRITICAL(100);

	public int priorityIndex;

	private Priority(final int index) {
		this.priorityIndex = index;
	}

	public int getPriorityIndex() {
		return this.priorityIndex;
	}
}