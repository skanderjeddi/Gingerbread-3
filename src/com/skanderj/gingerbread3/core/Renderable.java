package com.skanderj.gingerbread3.core;

import com.skanderj.gingerbread3.core.object.ApplicationObject;
import com.skanderj.gingerbread3.display.Screen;

/**
 *
 * @author Skander
 *
 */
public interface Renderable extends Comparable<ApplicationObject> {
	/**
	 * Self explanatory.
	 */
	void render(Screen screen);

	/**
	 * Self explanatory.
	 */
	Priority priority();

	/**
	 * Self explanatory.
	 */
	@Override
	default int compareTo(final ApplicationObject applicationObject) {
		return -(this.priority().priorityIndex - applicationObject.priority().priorityIndex);
	}
}
