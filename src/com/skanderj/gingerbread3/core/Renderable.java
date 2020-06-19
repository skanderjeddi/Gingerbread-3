package com.skanderj.gingerbread3.core;

import com.skanderj.gingerbread3.core.object.GameObject;
import com.skanderj.gingerbread3.display.GraphicsWrapper;

/**
 *
 * @author Skander
 *
 */
public interface Renderable extends Comparable<GameObject> {
	/**
	 * Self explanatory.
	 */
	void render(GraphicsWrapper wrapper, Object... args);

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
