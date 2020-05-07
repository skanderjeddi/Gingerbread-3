package com.skanderj.g3.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public final class GraphicString {
	private String content;
	private Color color, shadeColor;
	private Font font;

	public GraphicString(String content, Color color, Font font) {
		this(content, color, font, null);
	}

	public GraphicString(String content, Color color, Font font, Color shadeColor) {
		this.content = content;
		this.color = color;
		this.font = font;
		this.shadeColor = shadeColor;
	}

	public final int getWidth(Graphics2D graphics) {
		FontMetrics metrics = graphics.getFontMetrics(this.font);
		return metrics.stringWidth(this.content);
	}

	public final int getHeight(Graphics2D graphics) {
		FontMetrics metrics = graphics.getFontMetrics(this.font);
		return metrics.getHeight();
	}

	public final int getAugmentedHeight(Graphics2D graphics) {
		FontMetrics metrics = graphics.getFontMetrics(this.font);
		return metrics.getHeight() + metrics.getAscent();
	}

	public final boolean isEmpty() {
		return this.content.isEmpty();
	}

	public final void draw(Graphics2D graphics, int x0, int y0) {
		GraphicString.drawString(graphics, x0, y0, this);
	}

	public final void drawCentered(Graphics2D graphics, int x0, int y0, int width, int height) {
		GraphicString.drawCenteredString(graphics, x0, y0, width, height, this);
	}

	public final void drawCenteredWidthless(Graphics2D graphics, int x0, int y0, int height) {
		GraphicString.drawCenteredStringWidthless(graphics, x0, y0, height, this);
	}

	private static final void drawString(Graphics2D graphics, int x0, int y0, GraphicString string) {
		if (string.shadeColor == null) {
			graphics.setColor(string.color);
			graphics.setFont(string.font);
			graphics.drawString(string.content, x0, y0);
		} else {
			graphics.setColor(string.shadeColor);
			graphics.drawString(string.content, x0 + 1, y0 + 1);
			graphics.setColor(string.color);
			graphics.drawString(string.content, x0 - 1, y0 - 1);
		}
	}

	private static final void drawCenteredString(Graphics2D graphics, int x0, int y0, int width, int height, GraphicString string) {
		graphics.setFont(string.font);
		graphics.setColor(string.color);
		final FontMetrics fontMetrics = graphics.getFontMetrics();
		final Rectangle2D rectangle2d = fontMetrics.getStringBounds(string.content, graphics);
		final int x = (width - (int) rectangle2d.getWidth()) / 2;
		final int y = ((height - (int) rectangle2d.getHeight()) / 2) + fontMetrics.getAscent();
		if (string.shadeColor == null) {
			graphics.drawString(string.content, x0 + x, y0 + y);
		} else {
			graphics.setColor(string.shadeColor);
			graphics.drawString(string.content, x0 + x + 1, y0 + y + 1);
			graphics.setColor(string.color);
			graphics.drawString(string.content, x0 + x - 1, y0 + y - 1);
		}
	}

	private static final int drawCenteredStringWidthless(Graphics2D graphics, int x0, int y0, int height, GraphicString string) {
		graphics.setFont(string.font);
		graphics.setColor(string.color);
		final FontMetrics fontMetrics = graphics.getFontMetrics();
		final Rectangle2D rectangle2d = fontMetrics.getStringBounds(string.content, graphics);
		final int y = ((height - (int) rectangle2d.getHeight()) / 2) + fontMetrics.getAscent();
		if (string.shadeColor == null) {
			graphics.drawString(string.content, x0, y0 + y);
		} else {
			graphics.setColor(string.shadeColor);
			graphics.drawString(string.content, x0 + 1, y0 + y + 1);
			graphics.setColor(string.color);
			graphics.drawString(string.content, x0 - 1, y0 + y - 1);
		}
		return y0 + y;
	}

	public final String getContent() {
		return content;
	}

	public final Color getColor() {
		return color;
	}

	public final Color getShadeColor() {
		return shadeColor;
	}

	public final Font getFont() {
		return font;
	}

	public final void setContent(String content) {
		this.content = content;
	}

	public final void setColor(Color color) {
		this.color = color;
	}

	public final void setShadeColor(Color shadeColor) {
		this.shadeColor = shadeColor;
	}

	public final void setFont(Font font) {
		this.font = font;
	}
}
