package com.skanderj.g3.util;

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

	public VisualStringProperties(Font font, Color color) {
		this.font = font;
		this.color = color;
	}

	public Font getFont() {
		return this.font;
	}

	public Color getColor() {
		return this.color;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
