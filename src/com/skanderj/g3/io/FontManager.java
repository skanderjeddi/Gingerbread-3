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

public final class FontManager {
	private FontManager() {
		return;
	}

	private static final Map<String, Font> fontsMap = new HashMap<String, Font>();

	public static final boolean registerFont(String identifier, String path) {
		File fontFile = new File(path);
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(fontFile);
			Font font = Font.createFont(Font.TRUETYPE_FONT, fileInputStream);
			FontManager.fontsMap.put(identifier, font);
			Logger.log(FontManager.class, LogLevel.DEBUG, "Succesfully registered font with identifier %s!", identifier);
			return true;
		} catch (FontFormatException | IOException exception) {
			Logger.log(AudioManager.class, LogLevel.SEVERE, "An exception occurred while loading font from %s: %s", path, exception.getMessage());
			return false;
		}
	}

	public static final Font getFont(String identifier) {
		Font font = FontManager.fontsMap.get(identifier);
		if (font == null) {
			Logger.log(FontManager.class, Logger.LogLevel.SEVERE, "Could not find font with identifier %s!", identifier);
			return null;
		}
		return font;
	}

	public static final Font getFont(String identifier, int size) {
		Font font = FontManager.fontsMap.get(identifier);
		if (font == null) {
			Logger.log(FontManager.class, Logger.LogLevel.SEVERE, "Could not find font with identifier %s!", identifier);
			return null;
		}
		return font.deriveFont((float) size);
	}
}
