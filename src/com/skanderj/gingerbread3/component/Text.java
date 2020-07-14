package com.skanderj.gingerbread3.component;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.util.Label;

/**
 * Represents an abstract label, basis for other button classes which can
 * implement their rendering the way they please. See GText for a very basic,
 * ready-to-be-used example.
 *
 * @author Skander
 *
 */
public abstract class Text extends Component {
	protected Label label;

	/**
	 * Very basic constructor.
	 */
	public Text(final Application application, final Label label) {
		super(application);
		this.label = label;
	}

	/**
	 * Related to global components management.
	 */
	@Override
	public final boolean canChangeFocus() {
		return true;
	}

	/**
	 * Related to global components management.
	 */
	@Override
	public final void grantFocus() {
		return;
	}

	/**
	 * Related to global components management.
	 */
	@Override
	public final void revokeFocus() {
		return;
	}

	/**
	 * Self explanatory.
	 */
	public Label label() {
		return this.label;
	}

	/**
	 * Self explanatory.
	 */
	public void setLabel(final Label label) {
		this.label = label;
	}
}
