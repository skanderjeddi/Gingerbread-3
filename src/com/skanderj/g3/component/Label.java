package com.skanderj.g3.component;

import com.skanderj.g3.util.VisualString;

/**
 * Represents an abstract label, basis for other button classes which can
 * implement their rendering the way they please. See G3Label for a very basic,
 * ready-to-be-used example.
 *
 * @author Skander
 *
 */
public abstract class Label implements Component {
	protected VisualString graphicString;

	/**
	 * Very basic constructor.
	 */
	public Label(VisualString graphicString) {
		this.graphicString = graphicString;
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
	public VisualString getGraphicString() {
		return this.graphicString;
	}

	/**
	 * Self explanatory.
	 */
	public void setGraphicString(VisualString graphicString) {
		this.graphicString = graphicString;
	}
}
