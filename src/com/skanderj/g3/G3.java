package com.skanderj.g3;

import com.skanderj.g3.log.Logger;
import com.skanderj.g3.log.Logger.DebuggingType;
import com.skanderj.g3.log.Logger.LogLevel;
import com.skanderj.g3.testing.G3Testing;
import com.skanderj.g3.translation.TranslationManager;

public final class G3 {
	private static final String VERSION = "A.01";

	public static void main(String[] args) {
		Logger.redirectSystemOutput();
		Logger.setDebuggingState(DebuggingType.CLASSIC, true);
		Logger.setDebuggingState(DebuggingType.DEVELOPMENT, true);
		Logger.log(G3.class, LogLevel.INFO, TranslationManager.getKey("version.message", G3.VERSION));
		G3Testing.getTestingInstance().run();
	}
}
