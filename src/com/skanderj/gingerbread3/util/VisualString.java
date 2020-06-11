package com.skanderj.gingerbread3.util;

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

	public VisualString(final String content, final VisualStringProperties properties) {
		this(content, properties.getColor(), properties.getFont());
	}

	public VisualString(final String content, final VisualStringProperties properties, final Color shadeColor) {
		this(content, properties.getColor(), properties.getFont(), shadeColor);
	}

	public VisualString(final String content, final Color color, final Font font) {
		this(content, color, font, null);
	}

	public VisualString(final String content, final Color color, final Font font, final Color shadeColor) {
		this.content = content;
		this.color = color;
		this.font = font;
		this.shadeColor = shadeColor;
	}

	public final int getWidth(final Graphics2D graphics, final Object... args) {
		final FontMetrics metrics = graphics.getFontMetrics(this.font);
		return metrics.stringWidth(String.format(this.content, args));
	}

	public int getHeight(final Graphics2D graphics) {
		final FontMetrics metrics = graphics.getFontMetrics(this.font);
		return metrics.getHeight();
	}

	public int getAugmentedHeight(final Graphics2D graphics) {
		final FontMetrics metrics = graphics.getFontMetrics(this.font);
		return metrics.getHeight() + metrics.getAscent();
	}

	public boolean isEmpty() {
		return this.content.isEmpty();
	}

	public final void draw(final Graphics2D graphics, final int x0, final int y0, final Object... args) {
		VisualString.drawString(graphics, x0, y0, this, args);
	}

	public final void drawCentered(final Graphics2D graphics, final int x0, final int y0, final int width, final int height, final Object... args) {
		VisualString.drawCenteredString(graphics, x0, y0, width, height, this, args);
	}

	public final void drawCenteredWidthless(final Graphics2D graphics, final int x0, final int y0, final int height, final Object... args) {
		VisualString.drawCenteredStringWidthless(graphics, x0, y0, height, this, args);
	}

	public final int drawCenteredAbsolute(final Graphics2D graphics, final int x0, final int y0, final int height, final Object... args) {
		return VisualString.drawCenteredStringAbsolute(graphics, x0, y0, height, this, args);
	}

	private static final void drawString(final Graphics2D graphics, final int x0, final int y0, final VisualString string, final Object... args) {
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

	private static final void drawCenteredString(final Graphics2D graphics, final int x0, final int y0, final int width, final int height, final VisualString string, final Object... args) {
		graphics.setFont(string.font);
		graphics.setColor(string.color);
		final FontMetrics fontMetrics = graphics.getFontMetrics();
		final Rectangle2D rectangle2d = fontMetrics.getStringBounds(String.format(string.content, args), graphics);
		final int x = (width - (int) rectangle2d.getWidth()) / 2;
		final int y = ((height - (int) rectangle2d.getHeight()) / 2) + fontMetrics.getAscent();
		if (string.shadeColor == null) {
			graphics.drawString(String.format(string.content, args), x0 + x, y0 + y);
		} else {
			graphics.setColor(string.shadeColor);
			graphics.drawString(String.format(string.content, args), x0 + x + 1, y0 + y + 1);
			graphics.setColor(string.color);
			graphics.drawString(String.format(string.content, args), (x0 + x) - 1, (y0 + y) - 1);
		}
	}

	private static final int drawCenteredStringWidthless(final Graphics2D graphics, final int x0, final int y0, final int height, final VisualString string, final Object... args) {
		graphics.setFont(string.font);
		graphics.setColor(string.color);
		final FontMetrics fontMetrics = graphics.getFontMetrics();
		final Rectangle2D rectangle2d = fontMetrics.getStringBounds(String.format(string.content, args), graphics);
		final int y = ((height - (int) rectangle2d.getHeight()) / 2) + fontMetrics.getAscent();
		if (string.shadeColor == null) {
			graphics.drawString(String.format(string.content, args), x0, y0 + y);
		} else {
			graphics.setColor(string.shadeColor);
			graphics.drawString(String.format(string.content, args), x0 + 1, y0 + y + 1);
			graphics.setColor(string.color);
			graphics.drawString(String.format(string.content, args), x0 - 1, (y0 + y) - 1);
		}
		return y0 + y;
	}

	private static final int drawCenteredStringAbsolute(final Graphics2D graphics, final int x0, final int y0, final int height, final VisualString string, final Object... args) {
		graphics.setFont(string.font);
		graphics.setColor(string.color);
		final FontMetrics metrics = graphics.getFontMetrics();
		int y = (height - metrics.getHeight()) / 2;
		graphics.setColor(Color.BLACK);
		y = ((height - metrics.getHeight()) / 2) + metrics.getAscent();
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

	public void setContent(final String content) {
		this.content = content;
	}

	public void setColor(final Color color) {
		this.color = color;
	}

	public void setShadeColor(final Color shadeColor) {
		this.shadeColor = shadeColor;
	}

	public void setFont(final Font font) {
		this.font = font;
	}
}
