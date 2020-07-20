package com.skanderj.gingerbread3.component;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.util.Text;

/**
 * Represents an abstract text, basis for other button classes which can
 * implement their rendering the way they please. See GLabel for a very basic,
 * ready-to-be-used example.
 *
 * @author Skander
 *
 */
public abstract class Label extends Component {
	protected Text text;

	/**
	 * Very basic constructor.
	 */
	public Label(final Application application, final Text text) {
		super(application);
		this.text = text;
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
	public Text text() {
		return this.text;
	}

	/**
	 * Self explanatory.
	 */
	public void setText(final Text text) {
		this.text = text;
	}
}
