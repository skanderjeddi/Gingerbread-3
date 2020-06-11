package com.skanderj.gingerbead3.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.skanderj.gingerbead3.display.Window;

/**
 *
 * @author Skander
 *
 */
public final class GraphicsUtilities {
	public static final int DEFAULT_ORIGIN_X = 0, DEFAULT_ORIGIN_Y = GraphicsUtilities.DEFAULT_ORIGIN_X;

	private GraphicsUtilities() {
		return;
	}

	public static void clear(Window window, Graphics2D graphics, Color color) {
		graphics.setColor(color);
		graphics.fillRect(0, 0, window.getWidth(), window.getHeight());
	}

	public static void rectangle(Graphics2D graphics, Color color, int x, int y, int width, int height, boolean fill, int arcWidth, int arcHeight) {
		final Color tempColor = graphics.getColor();
		{
			graphics.setColor(color);
			if (fill) {
				graphics.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
			} else {
				graphics.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
			}
		}
		graphics.setColor(tempColor);
	}

	public static void oval(Graphics2D graphics, Color color, int centerX, int centerY, int width, int height, boolean fill) {
		final Color tempColor = graphics.getColor();
		{
			graphics.setColor(color);
			if (fill) {
				graphics.fillOval(centerX - (int) Math.floor(width / 2), centerY - (int) Math.floor(height / 2), width, height);
			} else {
				graphics.drawOval(centerX - (int) Math.floor(width / 2), centerY - (int) Math.floor(height / 2), width, height);
			}
		}
		graphics.setColor(tempColor);
	}

	/**
	 * Lowers the rendering quality of the graphics object as much as possible - no
	 * noticeable performance gain.
	 */
	public static void speed(Graphics2D graphics) {
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
		graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
	}

	/**
	 * Increases the rendering quality of the graphics object as much as possible -
	 * no noticeable performance loss.
	 */
	public static void quality(Graphics2D graphics) {
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	}
}
