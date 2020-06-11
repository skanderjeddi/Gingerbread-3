package com.skanderj.gingerbead3.translation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.skanderj.gingerbead3.log.Logger;
import com.skanderj.gingerbead3.log.Logger.LogLevel;

/**
 * A class used to load and retrieve translations from .lang files - a more
 * basic copy of the Minecraft translation system.
 *
 * @author Skander
 *
 */
public final class TranslationManager {
	// Default language will always be ENGLISH because ENGLISH >>>>
	public static final Language DEFAULT_LANGUAGE = Language.ENGLISH;

	// Translations map, only 1 language at a time (#TODO maybe change that?)
	private static final Map<String, String> languageMap = new HashMap<String, String>();

	private TranslationManager() {
		return;
	}

	/**
	 * Self explanatory. Returns true if successful, false otherwise. #TODO
	 * customize loading path.
	 */
	public static boolean loadLanguage(final Language language) {
		final File languageFile = new File("res/" + language.identifier + ".lang");
		if (languageFile.exists()) {
			try {
				final BufferedReader bufferedReader = new BufferedReader(new FileReader(languageFile));
				String line = new String();
				while ((line = bufferedReader.readLine()) != null) {
					if (line.startsWith("#") || line.isBlank()) {
						continue;
					}
					TranslationManager.languageMap.put(line.split("=")[0], line.split("=")[1]);
				}
				bufferedReader.close();
				Logger.log(TranslationManager.class, LogLevel.INFO, "Successfully loaded translations for language id \"%s\"...", language.identifier);
				return true;
			} catch (final IOException exception) {
				Logger.log(TranslationManager.class, LogLevel.SEVERE, "An exception occurred while loading translations from file for language id \"%s\": %s", language.identifier, exception.getMessage());
				return false;
			}
		} else {
			Logger.log(TranslationManager.class, LogLevel.SEVERE, "Could not find translations file for language id \"%s\"!", language.identifier);
			return false;
		}
	}

	/**
	 * Gets called if a translation is pulled before any language is properly
	 * loaded.
	 */
	private static void loadDefaultLanguage() {
		Logger.log(TranslationManager.class, LogLevel.INFO, "Loading default translations for default language (%s)...", TranslationManager.DEFAULT_LANGUAGE.identifier);
		TranslationManager.loadLanguage(TranslationManager.DEFAULT_LANGUAGE);
	}

	/**
	 * Self explanatory.
	 */
	public static final String getKey(final String key, final Object... args) {
		if (TranslationManager.languageMap.isEmpty()) {
			TranslationManager.loadDefaultLanguage();
		}
		return TranslationManager.languageMap.get(key) == null ? "(null)" : String.format(TranslationManager.languageMap.get(key), args);
	}

	/**
	 * Language represenation with id for reference.
	 *
	 * @author Skander
	 *
	 */
	public enum Language {
		FRENCH("fr"), ENGLISH("en");

		private String identifier;

		private Language(final String identifier) {
			this.identifier = identifier;
		}

		public String getIdentifier() {
			return this.identifier;
		}
	}
}
