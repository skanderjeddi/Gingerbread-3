package com.skanderj.gingerbread3.component;

public abstract class Background extends Component {
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
