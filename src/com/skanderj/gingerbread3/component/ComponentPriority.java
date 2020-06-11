package com.skanderj.gingerbread3.component;

/**
 * Represents a component priority, or what order do the components get
 * updated/drawn in.
 *
 * @author Skander
 *
 */
public enum ComponentPriority {
	EXTREMELY_LOW(-10), LOW(-1), REGULAR(0), HIGH(1), EXTREMELY_HIGH(10), CRITICAL(100);

	int priorityIndex;

	private ComponentPriority(final int index) {
		this.priorityIndex = index;
	}

	public int getPriorityIndex() {
		return this.priorityIndex;
	}
}
