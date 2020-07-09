package com.skanderj.gingerbread3.component;

import com.skanderj.gingerbread3.core.Application;

public abstract class Background extends Component {
	public Background(final Application application) {
		super(application);
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
