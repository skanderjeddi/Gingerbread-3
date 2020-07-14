package com.skanderj.gingerbread3.core;

import com.skanderj.gingerbread3.core.object.ApplicationObject;

/**
 *
 * @author Skander
 *
 */
public interface Updateable extends Comparable<ApplicationObject> {
	/**
	 * Self explanatory.
	 */
	void update();

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
	default int compareTo(final ApplicationObject applicationObject) {
		return -(this.priority().priorityIndex - applicationObject.priority().priorityIndex);
	}
}
