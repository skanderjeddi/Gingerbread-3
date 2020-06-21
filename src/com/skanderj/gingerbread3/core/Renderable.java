package com.skanderj.gingerbread3.core;

import com.skanderj.gingerbread3.core.object.GameObject;
import com.skanderj.gingerbread3.display.Screen;

/**
 *
 * @author Skander
 *
 */
public interface Renderable extends Comparable<GameObject> {
	/**
	 * Self explanatory.
	 */
	void render(Screen screen, Object... args);

	/**
	 * Self explanatory.
	 */
	Priority priority();

	/**
	 * Self explanatory.
	 */
	@Override
	default int compareTo(final GameObject gameObject) {
		return -(this.priority().priorityIndex - gameObject.priority().priorityIndex);
	}
}
