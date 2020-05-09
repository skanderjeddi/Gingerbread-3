package com.skanderj.g3.io;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.skanderj.g3.audio.AudioManager;
import com.skanderj.g3.log.Logger;
import com.skanderj.g3.log.Logger.LogLevel;
import com.skanderj.g3.translation.TranslationManager;

/**
 * A class used for handling all fonts purposes. Can't be instantiated, only
 * static methods. Fonts have to be in .TTF (for now!) #TODO
 * 
 * @author Skander
 *
 */
public final class FontManager {
	// Translations keys
	private static final String KEY_FONT_MANAGER_SUCCESS = "key.fontmanager.success";
	private static final String KEY_FONT_MANAGER_EXCEPTION_LOADING = "key.fontmanager.exception.loading";
	private static final String KEY_FONT_MANAGER_MISSING_FONT = "key.fontmanager.missing_font";

	private FontManager() {
		return;
	}

	// Fonts map by identifier for the custom fonts
	private static final Map<String, Font> fontsMap = new HashMap<String, Font>();

	/**
	 * Loads a font from the provided path. File must be .TTF format (for now).
	 * Returns true if the font was successfully registered, false otherwise.
	 */
	public static final boolean registerFont(String identifier, String path) {
		File fontFile = new File(path);
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(fontFile);
			Font font = Font.createFont(Font.TRUETYPE_FONT, fileInputStream);
			FontManager.fontsMap.put(identifier, font);
			Logger.log(FontManager.class, LogLevel.INFO, TranslationManager.getKey(FontManager.KEY_FONT_MANAGER_SUCCESS, identifier));
			return true;
		} catch (FontFormatException | IOException exception) {
			Logger.log(AudioManager.class, LogLevel.SEVERE, TranslationManager.getKey(FontManager.KEY_FONT_MANAGER_EXCEPTION_LOADING, path, exception.getMessage()));
			return false;
		}
	}

	/**
	 * Self explanatory.
	 */
	public static final Font getFont(String identifier) {
		Font font = FontManager.fontsMap.get(identifier);
		if (font == null) {
			Logger.log(FontManager.class, Logger.LogLevel.SEVERE, TranslationManager.getKey(FontManager.KEY_FONT_MANAGER_MISSING_FONT, identifier));
			return null;
		}
		return font;
	}

	/**
	 * Self explanatory. Returns the corresponding font with a specific size.
	 */
	public static final Font getFont(String identifier, int size) {
		Font font = FontManager.fontsMap.get(identifier);
		if (font == null) {
			Logger.log(FontManager.class, Logger.LogLevel.SEVERE, TranslationManager.getKey(FontManager.KEY_FONT_MANAGER_MISSING_FONT, identifier));
			return null;
		}
		return font.deriveFont((float) size);
	}

	/**
	 * Self explanatory. Returns the corresponding font with a specific size and
	 * style.
	 */
	public static final Font getFont(String identifier, int size, int style) {
		Font font = FontManager.fontsMap.get(identifier);
		if (font == null) {
			Logger.log(FontManager.class, Logger.LogLevel.SEVERE, TranslationManager.getKey(FontManager.KEY_FONT_MANAGER_MISSING_FONT, identifier));
			return null;
		}
		return font.deriveFont((float) size).deriveFont(style);
	}
}
