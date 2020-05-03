package com.skanderj.g3.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.skanderj.g3.log.Logger;
import com.skanderj.g3.log.Logger.LogLevel;

public final class ImageManager {
	private ImageManager() {
		return;
	}

	private static final Map<String, BufferedImage> imagesMap = new HashMap<String, BufferedImage>();

	public static final void registerImage(String identifier, String path) throws IOException {
		BufferedImage image = ImageIO.read(new File(path));
		ImageManager.imagesMap.put(identifier, image);
		Logger.log(ImageManager.class, LogLevel.DEBUG, "Succesfully registered image with identifier %s!", identifier);
	}

	public static final BufferedImage retrieveImage(String identifier) {
		BufferedImage image = ImageManager.imagesMap.get(identifier);
		if (image == null) {
			Logger.log(FontManager.class, Logger.LogLevel.SEVERE, "Could not find image with identifier %s!", identifier);
			return null;
		}
		return image;
	}
}
