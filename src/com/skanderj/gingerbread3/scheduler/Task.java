package com.skanderj.gingerbread3.scheduler;

import com.skanderj.gingerbread3.core.Updateable;
import com.skanderj.gingerbread3.core.object.Action;

/**
 *
 * @author Skander TODO
 *
 */
public interface Task extends Action, Updateable, Runnable {
	String identifier();

	void start();
}
