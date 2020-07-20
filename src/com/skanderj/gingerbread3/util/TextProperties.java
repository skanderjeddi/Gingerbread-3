package com.skanderj.gingerbread3.util;

import java.awt.Color;
import java.awt.Font;

/**
 * Helper class - very easy to understand.
 *
 * @author Skander
 *
 */
public final class TextProperties {
	public Font font;
	public Color color;

	public TextProperties(final Font font, final Color color) {
		this.font = font;
		this.color = color;
	}

	/**
	 * Self explanatory.
	 */
	public TextProperties build(final Color color) {
		return new TextProperties(this.font, color);
	}

	/**
	 * Self explanatory.
	 */
	public TextProperties build(final int fontSize) {
		return new TextProperties(this.font.deriveFont((float) fontSize), this.color);
	}
}
