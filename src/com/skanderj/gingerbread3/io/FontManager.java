package com.skanderj.gingerbread3.io;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.skanderj.gingerbread3.audio.AudioManager;
import com.skanderj.gingerbread3.logging.Logger;
import com.skanderj.gingerbread3.logging.Logger.LogLevel;

/**
 * A class used for handling all fonts purposes. Can't be instantiated, only
 * static methods. Fonts have to be in .TTF (for now!) #TODO
 *
 * @author Skander
 *
 */
public final class FontManager {
	private FontManager() {
		return;
	}

	// Fonts map by identifier for the custom fonts
	private static final Map<String, Font> fontsMap = new HashMap<String, Font>();

	/**
	 * Loads a font from the provided path. File must be .TTF format (for now).
	 * Returns true if the font was successfully registered, false otherwise.
	 */
	public static boolean register(final String identifier, final String path) {
		final long startTime = System.currentTimeMillis();
		final File fontFile = new File(path);
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(fontFile);
			final Font font = Font.createFont(Font.TRUETYPE_FONT, fileInputStream);
			final long endTime = System.currentTimeMillis();
			FontManager.fontsMap.put(identifier, font);
			Logger.log(FontManager.class, LogLevel.INFO, "NEW font from disk -> \"%s\" (%d ms)", identifier, endTime - startTime);
			return true;
		} catch (FontFormatException | IOException exception) {
			Logger.log(AudioManager.class, LogLevel.SEVERE, "An exception occurred while loading font from %s: %s", path, exception.getMessage());
			return false;
		}
	}

	/**
	 * Self explanatory.
	 */
	public static Font get(final String identifier) {
		final Font font = FontManager.fontsMap.get(identifier);
		if (font == null) {
			Logger.log(FontManager.class, Logger.LogLevel.SEVERE, "Could not find font with identifier \"%s\"", identifier);
			return null;
		}
		return font;
	}

	/**
	 * Self explanatory. Returns the corresponding font with a specific size.
	 */
	public static Font get(final String identifier, final int size) {
		final Font font = FontManager.fontsMap.get(identifier);
		if (font == null) {
			Logger.log(FontManager.class, Logger.LogLevel.SEVERE, "Could not find font with identifier \"%s\"", identifier);
			return null;
		}
		return font.deriveFont((float) size);
	}

	/**
	 * Self explanatory. Returns the corresponding font with a specific size and
	 * style.
	 */
	public static Font get(final String identifier, final int size, final int style) {
		final Font font = FontManager.fontsMap.get(identifier);
		if (font == null) {
			Logger.log(FontManager.class, Logger.LogLevel.SEVERE, "Could not find font with identifier \"%s\"", identifier);
			return null;
		}
		return font.deriveFont((float) size).deriveFont(style);
	}
}
