package com.skanderj.gingerbread3.core;

import com.skanderj.gingerbread3.core.object.GameObject;

/**
 *
 * @author Skander
 *
 */
public interface Updatable extends Comparable<GameObject> {
	/**
	 * Self explanatory.
	 */
	void update(double delta, Object... args);

	/**
	 * Self explanatory.
	 */
	Priority priority();

	/**
	 * Self explanatory.
	 */
	@Override
	default int compareTo(final GameObject o) {
		return -(this.priority().priorityIndex - o.priority().priorityIndex);
	}
}
