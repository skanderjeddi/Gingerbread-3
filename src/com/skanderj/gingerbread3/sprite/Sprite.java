package com.skanderj.gingerbread3.sprite;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.core.object.GameObject;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.io.Images;

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

	/**
	 * Self explanatory.
	 */
	public static final Sprite[] fromImages(final Game game, final String identifier, final BufferedImage... images) {
		final Sprite[] array = new Sprite[images.length];
		for (int i = 0; i < array.length; i += 1) {
			array[i] = new Sprite(game, String.format(identifier, i), images[i], images[i].getWidth(), images[i].getHeight());
			Registry.set(String.format(identifier, i), array[i]);
		}
		return array;
	}

	/**
	 * Self explanatory.
	 */
	public static final Sprite fromImage(final Game game, final String identifier, final String path, final int width, final int height, final int scaleMethod) {
		Images.register(identifier, path);
		final BufferedImage loadedImage = Images.get(identifier);
		final int loadedImageWidth = loadedImage.getWidth(), loadedImageHeight = loadedImage.getHeight();
		BufferedImage finalImage = new BufferedImage(loadedImageWidth, loadedImageHeight, loadedImage.getType());
		final AffineTransform affineTransform = new AffineTransform();
		affineTransform.scale((float) width / (float) loadedImageWidth, (float) height / (float) loadedImageWidth);
		final AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, scaleMethod);
		finalImage = affineTransformOp.filter(loadedImage, finalImage);
		final Sprite sprite = new Sprite(game, identifier, finalImage, width, height);
		Registry.set(identifier, sprite);
		return sprite;
	}

	public Sprite(final Game game, final String identifier, final BufferedImage image, final int width, final int height) {
		super(game);
		this.identifier = identifier;
		this.image = image;
		this.width = width;
		this.height = height;
	}

	private int x, y;

	/**
	 * Self explanatory.
	 */
	@Override
	public synchronized void update(final double delta, final Object... args) {
		this.x = (int) args[0];
		this.y = (int) args[1];
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public synchronized final void render(final Screen screen) {
		screen.image(this.image, this.x, this.y, this.width, this.height);
	}

	/**
	 * Self explanatory.
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Self explanatory.
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Self explanatory.
	 */
	public String getIdentifier() {
		return this.identifier;
	}

	/**
	 * Self explanatory.
	 */
	public BufferedImage getImage() {
		return this.image;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public Priority priority() {
		return Priority.REGULAR;
	}

	/**
	 * Self explanatory.
	 */
	public final Sprite copy() {
		return new Sprite(this.game, this.identifier, this.image, this.width, this.height);
	}
}
