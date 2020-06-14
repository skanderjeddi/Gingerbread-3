package com.skanderj.gingerbread3.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.skanderj.gingerbread3.audio.AudioManager;
import com.skanderj.gingerbread3.log.Logger;
import com.skanderj.gingerbread3.log.Logger.LogLevel;

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
	public static boolean register(final String identifier, final String path) {
		final long startTime = System.currentTimeMillis();
		BufferedImage image;
		try {
			image = ImageIO.read(new File(path));
			final long endTime = System.currentTimeMillis();
			ImageManager.imagesMap.put(identifier, image);
			Logger.log(ImageManager.class, LogLevel.INFO, "NEW image from disk -> \"%s\" (%d ms)", identifier, endTime - startTime);
			return true;
		} catch (final IOException exception) {
			Logger.log(AudioManager.class, LogLevel.SEVERE, "An exception occurred while loading image from %s: %s", path, exception.getMessage());
			return false;
		}
	}

	/**
	 * Loads all the audio files in the provided directory while adding "_0, _1, _2"
	 * to the identifier. Returns true if successful, false otherwise.
	 */
	public static boolean registerDirectory(final String identifier, final String path) {
		final File directory = new File(path);
		if (directory.isDirectory()) {
			int counter = 0;
			boolean success = true;
			for (final File file : directory.listFiles()) {
				if (!ImageManager.register(String.format(identifier, counter), file.getPath())) {
					success = false;
				}
				counter += 1;
			}
			return success;
		} else {
			Logger.log(ImageManager.class, Logger.LogLevel.SEVERE, "Provided path %s doesn't point to a directory", path);
			return false;
		}
	}

	/**
	 * Self explanatory.
	 */
	public static BufferedImage get(final String identifier) {
		final BufferedImage image = ImageManager.imagesMap.get(identifier);
		if (image == null) {
			Logger.log(FontManager.class, Logger.LogLevel.SEVERE, "Could not find image with identifier \"%s\"", identifier);
			return null;
		}
		return image;
	}

	/**
	 * Self explanatory.
	 */
	public static BufferedImage[] getUniqueID(final String identifier) {
		final List<BufferedImage> images = new ArrayList<BufferedImage>();
		for (final String id : ImageManager.imagesMap.keySet()) {
			if (id.contains(identifier)) {
				images.add(ImageManager.imagesMap.get(id));
			}
		}
		return images.toArray(new BufferedImage[images.size()]);
	}
}
