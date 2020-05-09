package com.skanderj.g3.util;

import java.awt.Color;
import java.awt.Font;

/**
 * Helper class - very easy to understand.
 * 
 * @author Skander
 *
 */
public final class TextProperties {
	private Font font;
	private Color color;

	public TextProperties(Font font, Color color) {
		this.font = font;
		this.color = color;
	}

	public final Font getFont() {
		return this.font;
	}

	public final Color getColor() {
		return this.color;
	}

	public final void setFont(Font font) {
		this.font = font;
	}

	public final void setColor(Color color) {
		this.color = color;
	}
}
