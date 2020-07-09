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
	private Font font;
	private Color color;

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

	/**
	 * Self explanatory.
	 */
	public Font getFont() {
		return this.font;
	}

	/**
	 * Self explanatory.
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * Self explanatory.
	 */
	public void setFont(final Font font) {
		this.font = font;
	}

	/**
	 * Self explanatory.
	 */
	public void setColor(final Color color) {
		this.color = color;
	}
}
