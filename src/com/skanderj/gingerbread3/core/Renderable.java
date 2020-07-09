package com.skanderj.gingerbread3.core;

import com.skanderj.gingerbread3.core.object.G3Object;
import com.skanderj.gingerbread3.display.Screen;

/**
 *
 * @author Skander
 *
 */
public interface Renderable extends Comparable<G3Object> {
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
	default int compareTo(final G3Object g3Object) {
		return -(this.priority().priorityIndex - g3Object.priority().priorityIndex);
	}
}
