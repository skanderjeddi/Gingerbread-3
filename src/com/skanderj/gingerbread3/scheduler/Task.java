package com.skanderj.gingerbread3.scheduler;

import com.skanderj.gingerbread3.core.Updateable;
import com.skanderj.gingerbread3.core.object.G3Action;

/**
 *
 * @author Skander TODO
 *
 */
public interface Task extends G3Action, Updateable, Runnable {
	String identifier();

	void start();
}
