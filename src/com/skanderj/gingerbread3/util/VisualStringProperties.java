package com.skanderj.gingerbread3.util;

import java.awt.Color;
import java.awt.Font;

/**
 * Helper class - very easy to understand.
 *
 * @author Skander
 *
 */
public final class VisualStringProperties {
	private Font font;
	private Color color;

	public VisualStringProperties(final Font font, final Color color) {
		this.font = font;
		this.color = color;
	}

	/**
	 * Self explanatory.
	 */
	public VisualStringProperties build(final Color color) {
		return new VisualStringProperties(this.font, color);
	}

	/**
	 * Self explanatory.
	 */
	public VisualStringProperties build(final int fontSize) {
		return new VisualStringProperties(this.font.deriveFont((float) fontSize), this.color);
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
