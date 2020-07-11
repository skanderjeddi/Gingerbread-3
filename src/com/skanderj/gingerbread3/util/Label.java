package com.skanderj.gingerbread3.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;

import com.skanderj.gingerbread3.display.Screen;

/**
 * Helper class - very easy to understand.
 *
 * @author Skander
 *
 */
public final class Label {
	public String content;
	public Color color, shadeColor;
	public Font font;

	public Label(final String content, final LabelProperties properties) {
		this(content, properties.color, properties.font);
	}

	public Label(final String content, final LabelProperties properties, final Color shadeColor) {
		this(content, properties.color, properties.font, shadeColor);
	}

	public Label(final String content, final Color color, final Font font) {
		this(content, color, font, null);
	}

	public Label(final String content, final Color color, final Font font, final Color shadeColor) {
		this.content = content;
		this.color = color;
		this.font = font;
		this.shadeColor = shadeColor;
	}

	/**
	 * Self explanatory.
	 */
	public final int getWidth(final Screen screen, final Object... args) {
		final FontMetrics metrics = screen.fontMetrics(this.font);
		return metrics.stringWidth(String.format(this.content, args));
	}

	/**
	 * Self explanatory.
	 */
	public int getHeight(final Screen screen) {
		final FontMetrics metrics = screen.fontMetrics(this.font);
		return metrics.getHeight();
	}

	/**
	 * Self explanatory.
	 */
	public int getAugmentedHeight(final Screen screen) {
		final FontMetrics metrics = screen.fontMetrics(this.font);
		return metrics.getHeight() + metrics.getAscent();
	}

	/**
	 * Self explanatory.
	 */
	public boolean isEmpty() {
		return this.content.isEmpty();
	}

	/**
	 * Self explanatory.
	 */
	public final void draw(final Screen screen, final int x0, final int y0, final Object... args) {
		Label.drawString(screen, x0, y0, this, args);
	}

	/**
	 * Self explanatory.
	 */
	public final void drawCentered(final Screen screen, final int x0, final int y0, final int width, final int height, final Object... args) {
		Label.drawCenteredString(screen, x0, y0, width, height, this, args);
	}

	/**
	 * Self explanatory.
	 */
	public final void drawCenteredWidthless(final Screen screen, final int x0, final int y0, final int height, final Object... args) {
		Label.drawCenteredStringWidthless(screen, x0, y0, height, this, args);
	}

	/**
	 * Self explanatory.
	 */
	public final int drawCenteredAbsolute(final Screen screen, final int x0, final int y0, final int height, final Object... args) {
		return Label.drawCenteredStringAbsolute(screen, x0, y0, height, this, args);
	}

	/**
	 * Self explanatory.
	 */
	private static final void drawString(final Screen screen, final int x0, final int y0, final Label string, final Object... args) {
		final Label formatted = new Label(args.length > 0 ? String.format(string.content, args) : string.content, string.color, string.font);
		if (formatted.shadeColor == null) {
			screen.color(formatted.color);
			screen.font(formatted.font);
			screen.string(formatted.content, x0, y0);
		} else {
			screen.color(formatted.shadeColor);
			screen.string(formatted.content, x0 + 1, y0 + 1);
			screen.color(formatted.color);
			screen.string(formatted.content, x0 - 1, y0 - 1);
		}
	}

	/**
	 * Self explanatory.
	 */
	private static final void drawCenteredString(final Screen screen, final int x0, final int y0, final int width, final int height, final Label string, final Object... args) {
		final Label formatted = new Label(args.length > 0 ? String.format(string.content, args) : string.content, string.color, string.font);
		screen.font(string.font);
		screen.color(string.color);
		final FontMetrics fontMetrics = screen.fontMetrics();
		final Rectangle2D rectangle2d = fontMetrics.getStringBounds(formatted.content, screen.drawGraphics());
		final int x = (width - (int) rectangle2d.getWidth()) / 2;
		final int y = ((height - (int) rectangle2d.getHeight()) / 2) + fontMetrics.getAscent();
		if (formatted.shadeColor == null) {
			screen.string(formatted.content, x0 + x, y0 + y);
		} else {
			screen.color(formatted.shadeColor);
			screen.string(formatted.content, x0 + x + 1, y0 + y + 1);
			screen.color(formatted.color);
			screen.string(formatted.content, (x0 + x) - 1, (y0 + y) - 1);
		}
	}

	/**
	 * Self explanatory.
	 */
	private static final int drawCenteredStringWidthless(final Screen screen, final int x0, final int y0, final int height, final Label string, final Object... args) {
		final Label formatted = new Label(args.length > 0 ? String.format(string.content, args) : string.content, string.color, string.font);
		screen.font(formatted.font);
		screen.color(formatted.color);
		final FontMetrics fontMetrics = screen.fontMetrics();
		final Rectangle2D rectangle2d = fontMetrics.getStringBounds(formatted.content, screen.drawGraphics());
		final int y = ((height - (int) rectangle2d.getHeight()) / 2) + fontMetrics.getAscent();
		if (formatted.shadeColor == null) {
			screen.string(formatted.content, x0, y0 + y);
		} else {
			screen.color(formatted.shadeColor);
			screen.string(formatted.content, x0 + 1, y0 + y + 1);
			screen.color(formatted.color);
			screen.string(formatted.content, x0 - 1, (y0 + y) - 1);
		}
		return y0 + y;
	}

	/**
	 * Self explanatory.
	 */
	private static final int drawCenteredStringAbsolute(final Screen screen, final int x0, final int y0, final int height, final Label string, final Object... args) {
		final Label formatted = new Label(args.length > 0 ? String.format(string.content, args) : string.content, string.color, string.font);
		screen.font(formatted.font);
		screen.color(formatted.color);
		final FontMetrics metrics = screen.fontMetrics();
		int y = (height - metrics.getHeight()) / 2;
		screen.color(Color.BLACK);
		y = ((height - metrics.getHeight()) / 2) + metrics.getAscent();
		screen.string(formatted.content, x0, y + y0);
		return y0;
	}
}
