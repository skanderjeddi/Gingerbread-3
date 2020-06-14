package com.skanderj.gingerbread3.sprite;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.core.object.GameObject;
import com.skanderj.gingerbread3.display.GraphicsWrapper;
import com.skanderj.gingerbread3.io.ImageManager;

/**
 * Represents a simple sprite.
 *
 * @author Skander
 *
 */
public class Sprite extends GameObject {
	private final String identifier;
	private final BufferedImage image;
	private final int width, height;

	public static final Sprite[] fromImages(final Game game, final String identifier, final BufferedImage... images) {
		final Sprite[] array = new Sprite[images.length];
		for (int i = 0; i < array.length; i += 1) {
			array[i] = new Sprite(game, String.format(identifier, i), images[i], images[i].getWidth(), images[i].getHeight());
		}
		return array;
	}

	public static final Sprite fromImage(final Game game, final String identifier, final String path, final int width, final int height, final int scaleMethod) {
		ImageManager.register(identifier, path);
		final BufferedImage loadedImage = ImageManager.get(identifier);
		final int loadedImageWidth = loadedImage.getWidth(), loadedImageHeight = loadedImage.getHeight();
		BufferedImage finalImage = new BufferedImage(loadedImageWidth, loadedImageHeight, loadedImage.getType());
		final AffineTransform affineTransform = new AffineTransform();
		affineTransform.scale((float) width / (float) loadedImageWidth, (float) height / (float) loadedImageWidth);
		final AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, scaleMethod);
		finalImage = affineTransformOp.filter(loadedImage, finalImage);
		return new Sprite(game, identifier, finalImage, width, height);
	}

	public Sprite(final Game game, final String identifier, final BufferedImage image, final int width, final int height) {
		super(game);
		this.identifier = identifier;
		this.image = image;
		this.width = width;
		this.height = height;
	}

	@Override
	public synchronized void update(final double delta, final Object... args) {
		return;
	}

	@Override
	public synchronized final void render(final GraphicsWrapper graphics, final Object... args) {
		graphics.image(this.image, (int) args[0], (int) args[1], this.width, this.height);
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
