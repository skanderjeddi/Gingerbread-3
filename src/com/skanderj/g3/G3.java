package com.skanderj.g3;

import com.skanderj.g3.log.Logger;
import com.skanderj.g3.log.Logger.DebuggingType;
import com.skanderj.g3.log.Logger.LogLevel;
import com.skanderj.g3.testing.G3Testing;
import com.skanderj.g3.translation.TranslationManager;

/**
 * Main class, will be much more essential in the future.
 *
 * @author Skander
 *
 */
public final class G3 {
	private static final String VERSION = "A.01";

	public static void main(String[] args) {
		// Very important call, will become mandatory in the future
		Logger.redirectSystemOutput();
		// Enable both plain and dev debugging
		Logger.setDebuggingState(DebuggingType.CLASSIC, true);
		Logger.setDebuggingState(DebuggingType.DEVELOPMENT, true);
		// Display the Gingerbread version message
		Logger.log(G3.class, LogLevel.INFO, TranslationManager.getKey("version.message", G3.VERSION));
		// Run the test game
		G3Testing.getTestingInstance().run();
	}
}
