package com.skanderj.g3.io;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class FontManager {
	private FontManager() {
		return;
	}

	private static final Map<String, Font> fontsMap = new HashMap<String, Font>();

	public static final void registerFont(String identifier, String path) throws FontFormatException, IOException {
		File fontFile = new File(path);
		FileInputStream fileInputStream = new FileInputStream(fontFile);
		Font font = Font.createFont(Font.TRUETYPE_FONT, fileInputStream);
		FontManager.fontsMap.put(identifier, font);
	}

	public static final Font getFont(String identifier) {
		return FontManager.fontsMap.get(identifier);
	}

	public static final Font getFont(String identifier, int size) {
		return FontManager.fontsMap.get(identifier).deriveFont((float) size);
	}
}
