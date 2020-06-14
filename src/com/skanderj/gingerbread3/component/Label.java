package com.skanderj.gingerbread3.component;

import com.skanderj.gingerbread3.core.Game;
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
	protected VisualString graphicString;

	/**
	 * Very basic constructor.
	 */
	public Label(final Game game, final VisualString graphicString) {
		super(game);
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
	public void setGraphicString(final VisualString graphicString) {
		this.graphicString = graphicString;
	}
}
