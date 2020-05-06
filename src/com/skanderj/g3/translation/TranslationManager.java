package com.skanderj.g3.translation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.skanderj.g3.log.Logger;
import com.skanderj.g3.log.Logger.LogLevel;

public final class TranslationManager {
	public static final Language DEFAULT_LANGUAGE = Language.FRENCH;

	private static final Map<String, String> languageMap = new HashMap<String, String>();

	private TranslationManager() {
		return;
	}

	public static final boolean loadLanguage(Language language) {
		File languageFile = new File("res/" + language.identifier + ".lang");
		if (languageFile.exists()) {
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(languageFile));
				String line = new String();
				while ((line = bufferedReader.readLine()) != null) {
					TranslationManager.languageMap.put(line.split("=")[0], line.split("=")[1]);
				}
				bufferedReader.close();
				Logger.log(TranslationManager.class, LogLevel.INFO, "Successfully loaded translations for language id \"%s\"...", language.identifier);
				return true;
			} catch (IOException exception) {
				Logger.log(TranslationManager.class, LogLevel.SEVERE, "An exception occurred while loading translations from file for language id \"%s\": %s", language.identifier, exception.getMessage());
				return false;
			}
		} else {
			Logger.log(TranslationManager.class, LogLevel.SEVERE, "Could not find translations file for language id \"%s\"!", language.identifier);
			return false;
		}
	}

	private static final void loadDefaultLanguage() {
		Logger.log(TranslationManager.class, LogLevel.INFO, "Loading default translations for default language (%s)...", TranslationManager.DEFAULT_LANGUAGE.identifier);
		TranslationManager.loadLanguage(TranslationManager.DEFAULT_LANGUAGE);
	}

	public static final String getTranslation(String key, Object... args) {
		if (TranslationManager.languageMap.isEmpty()) {
			TranslationManager.loadDefaultLanguage();
		}
		return TranslationManager.languageMap.get(key) == null ? "(null)" : String.format(TranslationManager.languageMap.get(key), args);
	}

	public static enum Language {
		FRENCH("fr"), ENGLISH("en");

		private String identifier;

		private Language(String identifier) {
			this.identifier = identifier;
		}

		public String getIdentifier() {
			return this.identifier;
		}
	}
}
