package com.skanderj.gingerbread3.component;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.util.VisualString;

/**
 * Represents an abstract label, basis for other button classes which can
 * implement their rendering the way they please. See G3Label for a very basic,
 * ready-to-be-used example.
 *
 * @author Skander
 *
 */
public abstract class Label extends Component {
	protected VisualString visualString;

	/**
	 * Very basic constructor.
	 */
	public Label(final Application application, final VisualString visualString) {
		super(application);
		this.visualString = visualString;
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
	public VisualString getVisualString() {
		return this.visualString;
	}

	/**
	 * Self explanatory.
	 */
	public void setVisualString(final VisualString visualString) {
		this.visualString = visualString;
	}
}
