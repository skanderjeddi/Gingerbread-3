package com.skanderj.gingerbread3.sprite;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import com.skanderj.gingerbread3.core.G3Application;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.core.object.G3Object;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.resources.Images;

/**
 * Represents a simple sprite.
 *
 * @author Skander
 *
 */
public class Sprite extends G3Object {
	private final String identifier;
	private final BufferedImage image;
	private final int width, height;

	/**
	 * Self explanatory.
	 */
	public static final Sprite[] fromImages(final G3Application g3Application, final String identifier, final BufferedImage... images) {
		final Sprite[] array = new Sprite[images.length];
		for (int i = 0; i < array.length; i += 1) {
			array[i] = new Sprite(g3Application, String.format(identifier, i), images[i], images[i].getWidth(), images[i].getHeight());
			Registry.register(String.format(identifier, i), array[i]);
		}
		return array;
	}

	/**
	 * Self explanatory.
	 */
	public static final Sprite fromImage(final G3Application g3Application, final String identifier, final String path, final int width, final int height, final int scaleMethod) {
		Images.register(identifier, path);
		final BufferedImage loadedImage = Images.get(identifier);
		final int loadedImageWidth = loadedImage.getWidth(), loadedImageHeight = loadedImage.getHeight();
		BufferedImage finalImage = new BufferedImage(loadedImageWidth, loadedImageHeight, loadedImage.getType());
		final AffineTransform affineTransform = new AffineTransform();
		affineTransform.scale((float) width / (float) loadedImageWidth, (float) height / (float) loadedImageWidth);
		final AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, scaleMethod);
		finalImage = affineTransformOp.filter(loadedImage, finalImage);
		final Sprite sprite = new Sprite(g3Application, identifier, finalImage, width, height);
		Registry.register(identifier, sprite);
		return sprite;
	}

	public Sprite(final G3Application g3Application, final String identifier, final BufferedImage image, final int width, final int height) {
		super(g3Application);
		this.identifier = identifier;
		this.image = image;
		this.width = width;
		this.height = height;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public synchronized void update() {
		return;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public synchronized final void render(final Screen screen) {
		return;
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
		return new Sprite(this.g3Application, this.identifier, this.image, this.width, this.height);
	}
}
