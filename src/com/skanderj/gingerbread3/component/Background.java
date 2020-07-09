package com.skanderj.gingerbread3.component;

import com.skanderj.gingerbread3.core.G3Application;

/**
 *
 * @author Skander
 *
 */
public abstract class Background extends Component {
	public Background(final G3Application g3Application) {
		super(g3Application);
	}

	/**
	 * Always can change focus out of background.
	 */
	@Override
	public boolean canChangeFocus() {
		return true;
	}

	/**
	 * Related to global components management. Focus management is different for
	 * backgrounds so these do nothing.
	 */
	@Override
	public void grantFocus() {
		return;
	}

	/**
	 * Related to global components management. Focus management is different for
	 * backgrounds so these do nothing.
	 */
	@Override
	public void revokeFocus() {
		return;
	}
}
