package com.skanderj.gingerbread3.display;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class GraphicsWrapper {
	public static final int DEFAULT_ORIGIN_X = 0, DEFAULT_ORIGIN_Y = 0;

	private final Graphics2D graphics;

	public GraphicsWrapper(final Graphics2D graphics) {
		this.graphics = graphics;
	}

	public final synchronized void dispose() {
		this.graphics.dispose();
	}

	public final void color(final Color color) {
		this.graphics.setColor(color);
	}

	public final void clear(final Window window, final Color color) {
		this.graphics.setColor(color);
		this.graphics.fillRect(0, 0, window.getWidth(), window.getHeight());
	}

	public final BufferedImage newImage(final int width, final int height) {
		return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	public final void font(final Font font) {
		this.graphics.setFont(font);
	}

	public final FontMetrics fontMetrics() {
		return this.graphics.getFontMetrics();
	}

	public final FontMetrics fontMetrics(final Font font) {
		return this.graphics.getFontMetrics(font);
	}

	public final void image(final BufferedImage image, final int x, final int y, final int width, final int height) {
		this.graphics.drawImage(image, x, y, width, height, null);
	}

	public final void string(final String string, final int x, final int y) {
		this.graphics.drawString(string, x, y);
	}

	public final void rectangle(final Color color, final int x, final int y, final int width, final int height, final boolean fill) {
		this.rectangle(color, x, y, width, height, fill, 0, 0);
	}

	public final void rectangle(final Color color, final int x, final int y, final int width, final int height, final boolean fill, final int arcWidth, final int arcHeight) {
		final Color tempColor = this.graphics.getColor();
		{
			this.graphics.setColor(color);
			if (fill) {
				this.graphics.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
			} else {
				this.graphics.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
			}
		}
		this.graphics.setColor(tempColor);
	}

	public final void oval(final Color color, final int centerX, final int centerY, final int width, final int height, final boolean fill) {
		final Color tempColor = this.graphics.getColor();
		{
			this.graphics.setColor(color);
			if (fill) {
				this.graphics.fillOval(centerX - (int) Math.floor(width / 2), centerY - (int) Math.floor(height / 2), width, height);
			} else {
				this.graphics.drawOval(centerX - (int) Math.floor(width / 2), centerY - (int) Math.floor(height / 2), width, height);
			}
		}
		this.graphics.setColor(tempColor);
	}

	/**
	 * Lowers the rendering quality of the graphics object as much as possible - no
	 * noticeable performance gain.
	 */
	public final void speed() {
		this.graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		this.graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
		this.graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		this.graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
	}

	/**
	 * Increases the rendering quality of the graphics object as much as possible -
	 * no noticeable performance loss.
	 */
	public final void quality() {
		this.graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		this.graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		this.graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		this.graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	}

	public Graphics2D getGraphics() {
		return this.graphics;
	}
}
