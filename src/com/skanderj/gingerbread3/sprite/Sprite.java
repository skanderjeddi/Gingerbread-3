package com.skanderj.gingerbread3.sprite;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import com.skanderj.gingerbread3.io.ImageManager;

public class Sprite {
	private final String identifier;
	private final BufferedImage image;
	private final int width, height;

	public static final Sprite[] fromImages(final String identifier, final BufferedImage... images) {
		final Sprite[] array = new Sprite[images.length];
		for (int i = 0; i < array.length; i += 1) {
			array[i] = new Sprite(String.format(identifier, i), images[i], images[i].getWidth(), images[i].getHeight());
		}
		return array;
	}

	public Sprite(final String identifier, final String path, final int width, final int height, final int scaleMethod) {
		this.identifier = identifier;
		ImageManager.registerImage(identifier, path);
		final BufferedImage loadedImage = ImageManager.retrieveImage(identifier);
		final int loadedImageWidth = loadedImage.getWidth(), loadedImageHeight = loadedImage.getHeight();
		BufferedImage finalImage = new BufferedImage(loadedImageWidth, loadedImageHeight, loadedImage.getType());
		final AffineTransform affineTransform = new AffineTransform();
		affineTransform.scale((float) width / (float) loadedImageWidth, (float) height / (float) loadedImageWidth);
		final AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, scaleMethod);
		finalImage = affineTransformOp.filter(loadedImage, finalImage);
		this.image = finalImage;
		this.width = width;
		this.height = height;
	}

	public Sprite(final String identifier, final BufferedImage image, final int width, final int height) {
		this.identifier = identifier;
		this.image = image;
		this.width = width;
		this.height = height;
	}

	public synchronized final void render(final Graphics2D graphics, final int x, final int y) {
		graphics.drawImage(this.image, x, y, this.width, this.height, null);
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public String getIdentifier() {
		return this.identifier;
	}

	public BufferedImage getImage() {
		return this.image;
	}
}
