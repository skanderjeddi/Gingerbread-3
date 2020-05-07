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
import com.skanderj.g3.translation.TranslationManager;

public final class ImageManager {
	private static final String KEY_IMAGE_MANAGER_SUCCESS = "key.imagemanager.success";
	private static final String KEY_IMAGE_MANAGER_EXCEPTION_LOADING = "key.imagemanager.exception.loading";
	private static final String KEY_IMAGE_MANAGER_MISSING_IMAGE = "key.imagemanager.missing_image";

	private ImageManager() {
		return;
	}

	private static final Map<String, BufferedImage> imagesMap = new HashMap<String, BufferedImage>();

	public static final boolean registerImage(String identifier, String path) {
		BufferedImage image;
		try {
			image = ImageIO.read(new File(path));
			ImageManager.imagesMap.put(identifier, image);
			Logger.log(ImageManager.class, LogLevel.INFO, TranslationManager.getKey(ImageManager.KEY_IMAGE_MANAGER_SUCCESS, identifier));
			return true;
		} catch (IOException exception) {
			Logger.log(AudioManager.class, LogLevel.SEVERE, TranslationManager.getKey(ImageManager.KEY_IMAGE_MANAGER_EXCEPTION_LOADING, path, exception.getMessage()));
			return false;
		}
	}

	public static final BufferedImage retrieveImage(String identifier) {
		BufferedImage image = ImageManager.imagesMap.get(identifier);
		if (image == null) {
			Logger.log(FontManager.class, Logger.LogLevel.SEVERE, TranslationManager.getKey(ImageManager.KEY_IMAGE_MANAGER_MISSING_IMAGE, identifier));
			return null;
		}
		return image;
	}
}
