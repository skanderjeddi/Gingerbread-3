package com.skanderj.gingerbread3.display;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 *
 * @author Skander
 *
 */
public class Screen {
	public static final int DEFAULT_ORIGIN_X = 0, DEFAULT_ORIGIN_Y = 0;

	public static final Color HALF_WHITE = new Color(1f, 1f, 1f, 0.5f);

	private Graphics2D windowGraphics, drawGraphics;
	private final BufferedImage screenContent;

	public Screen(final int width, final int height) {
		this.screenContent = this.toCompatibleImage(this.newImage(width, height));
		this.drawGraphics = this.screenContent.createGraphics();
	}

	private final BufferedImage toCompatibleImage(final BufferedImage image) {
		// obtain the current system graphical settings
		final GraphicsConfiguration graphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

		/*
		 * if image is already compatible and optimized for current system settings,
		 * simply return it
		 */
		if (image.getColorModel().equals(graphicsConfiguration.getColorModel())) {
			return image;
		}

		// image is not optimized, so create a new image that is
		final BufferedImage newImage = graphicsConfiguration.createCompatibleImage(image.getWidth(), image.getHeight(), image.getTransparency());

		// get the graphics context of the new image to draw the old image on
		final Graphics2D graphics2d = newImage.createGraphics();

		// actually draw the image and dispose of context no longer needed
		graphics2d.drawImage(image, 0, 0, null);
		graphics2d.dispose();

		// return the new optimized image
		return newImage;
	}

	/**
	 * Self explanatory.
	 */
	public final synchronized void drawTo() {
		this.windowGraphics.drawImage(this.screenContent, Screen.DEFAULT_ORIGIN_X, Screen.DEFAULT_ORIGIN_Y, this.screenContent.getWidth(), this.screenContent.getHeight(), null);
	}

	/**
	 * Self explanatory, used internally.
	 */
	public final synchronized void dispose() {
		this.windowGraphics.dispose();
	}

	/**
	 * Self explanatory.
	 */
	public final void color(final Color color) {
		this.drawGraphics.setColor(color);
	}

	public final void reset() {
		this.drawGraphics.dispose();
		this.drawGraphics = this.screenContent.createGraphics();
	}

	/**
	 * Self explanatory.
	 */
	public final void clear(final Window window, final Color color) {
		this.drawGraphics.setColor(color);
		this.drawGraphics.fillRect(0, 0, window.getWidth(), window.getHeight());
	}

	/**
	 * Self explanatory.
	 */
	public final BufferedImage newImage(final int width, final int height) {
		return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	/**
	 * Self explanatory.
	 */
	public final void font(final Font font) {
		this.drawGraphics.setFont(font);
	}

	/**
	 * Self explanatory.
	 */
	public final FontMetrics fontMetrics() {
		return this.drawGraphics.getFontMetrics();
	}

	/**
	 * Self explanatory.
	 */
	public final FontMetrics fontMetrics(final Font font) {
		return this.drawGraphics.getFontMetrics(font);
	}

	/**
	 * Self explanatory.
	 */
	public final void image(final BufferedImage image, final int x, final int y, final int width, final int height) {
		this.drawGraphics.drawImage(image, x, y, width, height, null);
	}

	/**
	 * Self explanatory.
	 */
	public final void string(final String string, final int x, final int y) {
		this.drawGraphics.drawString(string, x, y);
	}

	/**
	 * Self explanatory.
	 */
	public final void rectangle(final Color color, final int x, final int y, final int width, final int height, final boolean fill) {
		this.rectangle(color, x, y, width, height, fill, 0, 0);
	}

	/**
	 * Self explanatory.
	 */
	public final void rectangle(final Color color, final int x, final int y, final int width, final int height, final boolean fill, final int arcWidth, final int arcHeight) {
		final Color tempColor = this.drawGraphics.getColor();
		{
			this.drawGraphics.setColor(color);
			if (fill) {
				this.drawGraphics.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
			} else {
				this.drawGraphics.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
			}
		}
		this.drawGraphics.setColor(tempColor);
	}

	/**
	 * Self explanatory.
	 */
	public final void translate(final int x, final int y) {
		this.drawGraphics.translate(x, y);
	}

	/**
	 * Self explanatory.
	 */
	public final void oval(final Color color, final int centerX, final int centerY, final int width, final int height, final boolean fill) {
		final Color tempColor = this.drawGraphics.getColor();
		{
			this.drawGraphics.setColor(color);
			if (fill) {
				this.drawGraphics.fillOval(centerX - (int) Math.floor(width / 2), centerY - (int) Math.floor(height / 2), width, height);
			} else {
				this.drawGraphics.drawOval(centerX - (int) Math.floor(width / 2), centerY - (int) Math.floor(height / 2), width, height);
			}
		}
		this.drawGraphics.setColor(tempColor);
	}

	public final void arc(final Color color, final int centerX, final int centerY, final int width, final int height, final int startAngle, final int arcAngle, final boolean fill) {
		final Color tempColor = this.drawGraphics.getColor();
		{
			this.drawGraphics.setColor(color);
			if (fill) {
				this.drawGraphics.drawArc(centerX - (int) Math.floor(width / 2), centerY - (int) Math.floor(height / 2), width, height, startAngle, arcAngle);
			} else {
				this.drawGraphics.fillArc(centerX - (int) Math.floor(width / 2), centerY - (int) Math.floor(height / 2), width, height, startAngle, arcAngle);
			}
		}
		this.drawGraphics.setColor(tempColor);
	}

	/**
	 * Lowers the rendering quality of the drawGraphics object as much as possible -
	 * no noticeable performance gain.
	 */
	public final void focusOnSpeed() {
		this.drawGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		this.drawGraphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
		this.drawGraphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		this.drawGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
	}

	/**
	 * Increases the rendering quality of the drawGraphics object as much as
	 * possible - no noticeable performance loss.
	 */
	public final void focusOnQuality() {
		this.drawGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		this.drawGraphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		this.drawGraphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		this.drawGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	}

	/**
	 * Self explanatory.
	 */
	public void renderThrough(final Graphics2D mainGraphics) {
		this.windowGraphics = mainGraphics;
	}

	/**
	 * Self explanatory.
	 */
	public Graphics2D drawGraphics() {
		return this.drawGraphics;
	}

	/**
	 * Self explanatory.
	 */
	public BufferedImage screenContent() {
		return this.screenContent;
	}

	/**
	 * Self explanatory.
	 */
	public final BufferedImage screenContentOnFrame() {
		final BufferedImage image = new BufferedImage(this.screenContent.getWidth(), this.screenContent.getHeight(), this.screenContent.getType());
		final int[] sourcePixels = ((DataBufferInt) this.screenContent.getRaster().getDataBuffer()).getData(), targetPixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		for (int index = 0; index < sourcePixels.length; index += 1) {
			targetPixels[index] = sourcePixels[index];
		}
		return image;
	}
}
