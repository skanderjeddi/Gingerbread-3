package com.skanderj.gingerbread3.util;

import java.awt.Color;
import java.awt.Font;

/**
 * Helper class - very easy to understand.
 *
 * @author Skander
 *
 */
public final class LabelProperties {
	public Font font;
	public Color color;

	public LabelProperties(final Font font, final Color color) {
		this.font = font;
		this.color = color;
	}

	/**
	 * Self explanatory.
	 */
	public LabelProperties build(final Color color) {
		return new LabelProperties(this.font, color);
	}

	/**
	 * Self explanatory.
	 */
	public LabelProperties build(final int fontSize) {
		return new LabelProperties(this.font.deriveFont((float) fontSize), this.color);
	}
}
