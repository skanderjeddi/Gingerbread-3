package com.skanderj.gingerbread3.animation;

import com.skanderj.gingerbread3.core.G3Application;
import com.skanderj.gingerbread3.core.object.G3Object;

/**
 * Animation interace, basis for all other animation classes.
 *
 * @author Skander
 *
 */
public abstract class Animation extends G3Object {
	public Animation(final G3Application g3Application) {
		super(g3Application);
	}
}
