package com.skanderj.gingerbread3.core;

import com.skanderj.gingerbread3.core.object.G3Object;

/**
 *
 * @author Skander
 *
 */
public interface Updatable extends Comparable<G3Object> {
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
	G3Application g3Application();

	/**
	 * Self explanatory.
	 */
	@Override
	default int compareTo(final G3Object g3Object) {
		return -(this.priority().priorityIndex - g3Object.priority().priorityIndex);
	}
}
