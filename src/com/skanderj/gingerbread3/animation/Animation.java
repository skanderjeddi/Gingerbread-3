package com.skanderj.gingerbread3.animation;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.object.GameObject;

/**
 * Animation interace, basis for all other animation classes.
 *
 * @author Skander
 *
 */
public abstract class Animation extends GameObject {
	public Animation(final Application application) {
		super(application);
	}
}
