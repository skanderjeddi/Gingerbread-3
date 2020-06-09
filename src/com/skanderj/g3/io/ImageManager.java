package com.skanderj.g3.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.skanderj.g3.audio.AudioManager;
import com.skanderj.g3.log.Logger;
import com.skanderj.g3.log.Logger.LogLevel;

/**
 * A class used for handling all images purposes. Can't be instantiated, only
 * static methods. No specific format is required.
 *
 * @author Skander
 *
 */
public final class ImageManager {
	private ImageManager() {
		return;
	}

	// Images map
	private static final Map<String, BufferedImage> imagesMap = new HashMap<String, BufferedImage>();

	/**
	 * Loads an image from the provided path. Returns true if the font was
	 * successfully registered, false otherwise.
	 */
	public static boolean registerImage(String identifier, String path) {
		BufferedImage image;
		try {
			image = ImageIO.read(new File(path));
			ImageManager.imagesMap.put(identifier, image);
			Logger.log(ImageManager.class, LogLevel.INFO, "Successfully registered image with identifier \"%s\"", identifier);
			return true;
		} catch (final IOException exception) {
			Logger.log(AudioManager.class, LogLevel.SEVERE, "An exception occurred while loading image from %s: %s", path, exception.getMessage());
			return false;
		}
	}

	/**
	 * Self explanatory.
	 */
	public static BufferedImage retrieveImage(String identifier) {
		final BufferedImage image = ImageManager.imagesMap.get(identifier);
		if (image == null) {
			Logger.log(FontManager.class, Logger.LogLevel.SEVERE, "Could not find image with identifier \"%s\"", identifier);
			return null;
		}
		return image;
	}
}
