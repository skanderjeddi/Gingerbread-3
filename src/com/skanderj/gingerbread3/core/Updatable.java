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
	void update(double delta);

	/**
	 * Self explanatory.
	 */
	Priority priority();

	/**
	 * Self explanatory.
	 */
	Application application();

	/**
	 * Self explanatory.
	 */
	@Override
	default int compareTo(final GameObject gameObject) {
		return -(this.priority().priorityIndex - gameObject.priority().priorityIndex);
	}
}
