package com.skanderj.gingerbread3.animation;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.object.ApplicationObject;

/**
 * Animation interace, basis for all other animation classes.
 *
 * @author Skander
 *
 */
public abstract class Animation extends ApplicationObject {
	public Animation(final Application application) {
		super(application);
	}
}
