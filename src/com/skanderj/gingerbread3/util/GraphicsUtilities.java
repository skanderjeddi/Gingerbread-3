package com.skanderj.gingerbread3.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.skanderj.gingerbread3.component.Component;
import com.skanderj.gingerbread3.display.Window;

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

	public static void clear(final Window window, final Graphics2D graphics, final Color color) {
		graphics.setColor(color);
		graphics.fillRect(0, 0, window.getWidth(), window.getHeight());
	}

	public static BufferedImage image(final int width, final int height) {
		return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	public static void rectangle(final Graphics2D graphics, final Color color, final int x, final int y, final int width, final int height, final boolean fill, final int arcWidth, final int arcHeight) {
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

	public static void oval(final Graphics2D graphics, final Color color, final int centerX, final int centerY, final int width, final int height, final boolean fill) {
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

	public static Point centerComponent(final Window window, final Component component) {
		return new Point((window.getWidth() / 2) - (component.getWidth() / 2), (window.getHeight() / 2) - (window.getHeight() / 2));
	}

	/**
	 * Lowers the rendering quality of the graphics object as much as possible - no
	 * noticeable performance gain.
	 */
	public static void speed(final Graphics2D graphics) {
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
		graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
	}

	/**
	 * Increases the rendering quality of the graphics object as much as possible -
	 * no noticeable performance loss.
	 */
	public static void quality(final Graphics2D graphics) {
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	}
}
