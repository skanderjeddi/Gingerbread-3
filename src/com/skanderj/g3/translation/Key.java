package com.skanderj.g3.translation;

/**
 * A shortcut class for calling TranslationManager.getKey(...)
 *
 * @author Skander
 *
 */
public final class Key {
	private Key() {
		return;
	}

	public static final String getKey(String identifier, Object... args) {
		return TranslationManager.getKey(identifier, args);
	}
}
