package com.skanderj.g3.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Helper class - very easy to understand.
 *
 * @author Skander
 *
 */
public final class VisualString {
	private String content;
	private Color color, shadeColor;
	private Font font;

	public VisualString(String content, VisualStringProperties properties) {
		this(content, properties.getColor(), properties.getFont());
	}

	public VisualString(String content, VisualStringProperties properties, Color shadeColor) {
		this(content, properties.getColor(), properties.getFont(), shadeColor);
	}

	public VisualString(String content, Color color, Font font) {
		this(content, color, font, null);
	}

	public VisualString(String content, Color color, Font font, Color shadeColor) {
		this.content = content;
		this.color = color;
		this.font = font;
		this.shadeColor = shadeColor;
	}

	public final int getWidth(Graphics2D graphics, Object... args) {
		final FontMetrics metrics = graphics.getFontMetrics(this.font);
		return metrics.stringWidth(String.format(this.content, args));
	}

	public int getHeight(Graphics2D graphics) {
		final FontMetrics metrics = graphics.getFontMetrics(this.font);
		return metrics.getHeight();
	}

	public int getAugmentedHeight(Graphics2D graphics) {
		final FontMetrics metrics = graphics.getFontMetrics(this.font);
		return metrics.getHeight() + metrics.getAscent();
	}

	public boolean isEmpty() {
		return this.content.isEmpty();
	}

	public final void draw(Graphics2D graphics, int x0, int y0, Object... args) {
		VisualString.drawString(graphics, x0, y0, this, args);
	}

	public final void drawCentered(Graphics2D graphics, int x0, int y0, int width, int height, Object... args) {
		VisualString.drawCenteredString(graphics, x0, y0, width, height, this, args);
	}

	public final void drawCenteredWidthless(Graphics2D graphics, int x0, int y0, int height, Object... args) {
		VisualString.drawCenteredStringWidthless(graphics, x0, y0, height, this, args);
	}

	public final int drawCenteredAbsolute(Graphics2D graphics, int x0, int y0, int height, Object... args) {
		return VisualString.drawCenteredStringAbsolute(graphics, x0, y0, height, this, args);
	}

	private static final void drawString(Graphics2D graphics, int x0, int y0, VisualString string, Object... args) {
		if (string.shadeColor == null) {
			graphics.setColor(string.color);
			graphics.setFont(string.font);
			graphics.drawString(String.format(string.content, args), x0, y0);
		} else {
			graphics.setColor(string.shadeColor);
			graphics.drawString(String.format(string.content, args), x0 + 1, y0 + 1);
			graphics.setColor(string.color);
			graphics.drawString(String.format(string.content, args), x0 - 1, y0 - 1);
		}
	}

	private static final void drawCenteredString(Graphics2D graphics, int x0, int y0, int width, int height, VisualString string, Object... args) {
		graphics.setFont(string.font);
		graphics.setColor(string.color);
		final FontMetrics fontMetrics = graphics.getFontMetrics();
		final Rectangle2D rectangle2d = fontMetrics.getStringBounds(String.format(string.content, args), graphics);
		final int x = (width - (int) rectangle2d.getWidth()) / 2;
		final int y = (height - (int) rectangle2d.getHeight()) / 2 + fontMetrics.getAscent();
		if (string.shadeColor == null) {
			graphics.drawString(String.format(string.content, args), x0 + x, y0 + y);
		} else {
			graphics.setColor(string.shadeColor);
			graphics.drawString(String.format(string.content, args), x0 + x + 1, y0 + y + 1);
			graphics.setColor(string.color);
			graphics.drawString(String.format(string.content, args), x0 + x - 1, y0 + y - 1);
		}
	}

	private static final int drawCenteredStringWidthless(Graphics2D graphics, int x0, int y0, int height, VisualString string, Object... args) {
		graphics.setFont(string.font);
		graphics.setColor(string.color);
		final FontMetrics fontMetrics = graphics.getFontMetrics();
		final Rectangle2D rectangle2d = fontMetrics.getStringBounds(String.format(string.content, args), graphics);
		final int y = (height - (int) rectangle2d.getHeight()) / 2 + fontMetrics.getAscent();
		if (string.shadeColor == null) {
			graphics.drawString(String.format(string.content, args), x0, y0 + y);
		} else {
			graphics.setColor(string.shadeColor);
			graphics.drawString(String.format(string.content, args), x0 + 1, y0 + y + 1);
			graphics.setColor(string.color);
			graphics.drawString(String.format(string.content, args), x0 - 1, y0 + y - 1);
		}
		return y0 + y;
	}

	private static final int drawCenteredStringAbsolute(Graphics2D graphics, int x0, int y0, int height, VisualString string, Object... args) {
		graphics.setFont(string.font);
		graphics.setColor(string.color);
		final FontMetrics metrics = graphics.getFontMetrics();
		int y = (height - metrics.getHeight()) / 2;
		graphics.setColor(Color.BLACK);
		y = (height - metrics.getHeight()) / 2 + metrics.getAscent();
		graphics.drawString(String.format(string.content, args), x0, y + y0);
		return y0;
	}

	public String getContent() {
		return this.content;
	}

	public Color getColor() {
		return this.color;
	}

	public Color getShadeColor() {
		return this.shadeColor;
	}

	public Font getFont() {
		return this.font;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setShadeColor(Color shadeColor) {
		this.shadeColor = shadeColor;
	}

	public void setFont(Font font) {
		this.font = font;
	}
}
