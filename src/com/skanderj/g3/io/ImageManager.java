package com.skanderj.g3.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public final class ImageManager {
	private ImageManager() {
		return;
	}

	private static final Map<String, BufferedImage> imagesMap = new HashMap<String, BufferedImage>();

	public static final void registerImage(String identifier, String path) throws IOException {
		BufferedImage image = ImageIO.read(new File(path));
		ImageManager.imagesMap.put(identifier, image);
	}

	public static final BufferedImage retrieveImage(String identifier) {
		return ImageManager.imagesMap.get(identifier);
	}
}
