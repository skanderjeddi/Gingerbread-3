package com.skanderj.gingerbread3.sprite;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import com.skanderj.gingerbread3.core.G3Application;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.core.object.G3Object;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.particle.Moveable;
import com.skanderj.gingerbread3.resources.Images;

/**
 * Represents a simple moveable.
 *
 * @author Skander
 *
 */
public class Sprite extends G3Object implements Moveable {
	private final String identifier;
	private final BufferedImage originalImage;
	private BufferedImage editedImage;
	private final int width, height;
	private int x, y;

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
		this.originalImage = image;
		this.width = width;
		this.height = height;
		this.editedImage = null;
		this.x = -this.width;
		this.y = -this.height;
	}

	public final void newBrightness(final float brightnessPercentage) {
		if (this.editedImage == null) {
			this.editedImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
		}
		final int[] pixel = { 0, 0, 0, 0 };
		final float[] hsbvals = { 0, 0, 0 };
		this.editedImage.getGraphics().drawImage(this.originalImage, 0, 0, null);
		// recalculare every pixel, changing the brightness
		for (int i = 0; i < this.editedImage.getHeight(); i++) {
			for (int j = 0; j < this.editedImage.getWidth(); j++) {
				// get the pixel data
				this.editedImage.getRaster().getPixel(j, i, pixel);
				// converts its data to hsb to change brightness
				Color.RGBtoHSB(pixel[0], pixel[1], pixel[2], hsbvals);
				// calculates the brightness component.
				float newBrightness = hsbvals[2] * brightnessPercentage;
				if (newBrightness > 1f) {
					newBrightness = 1f;
				}
				// create a new color with the new brightness
				final Color c = new Color(Color.HSBtoRGB(hsbvals[0], hsbvals[1], newBrightness));
				// set the new pixel
				this.editedImage.getRaster().setPixel(j, i, new int[] { c.getRed(), c.getGreen(), c.getBlue(), pixel[3] });
			}
		}
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public final void update() {
		return;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public final void render(final Screen screen) {
		if ((this.x > 0) && (this.y > 0)) {
			screen.image(this.image(), this.x, this.y, this.width, this.height);
		}
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
	public String identifier() {
		return this.identifier;
	}

	/**
	 * Self explanatory.
	 */
	public BufferedImage image() {
		if (this.editedImage != null) {
			return this.editedImage;
		}
		return this.originalImage;
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
	@Override
	public final Sprite copy() {
		return new Sprite(this.g3Application, this.identifier, this.editedImage == null ? this.originalImage : this.editedImage, this.width, this.height);
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setX(final int x) {
		this.x = x;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setY(final int y) {
		this.y = y;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public int x() {
		return this.x;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public int y() {
		return this.y;
	}

	@Override
	public String description() {
		return Registry.identifier(this) + " -> Sprite.class(" + this.width + "," + this.height + ")";
	}
}
