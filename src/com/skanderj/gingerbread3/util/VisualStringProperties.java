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

	public Font getFont() {
		return this.font;
	}

	public Color getColor() {
		return this.color;
	}

	public void setFont(final Font font) {
		this.font = font;
	}

	public void setColor(final Color color) {
		this.color = color;
	}
}
