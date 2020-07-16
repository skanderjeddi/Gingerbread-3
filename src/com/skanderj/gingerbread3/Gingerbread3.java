package com.skanderj.gingerbread3;

/**
 * Main class, will be much more essential in the future.
 *
 * @author Skander
 *
 */
public final class Gingerbread3 {
	public static final String RELEASE = "R0.9.3";

	private static boolean splashScreen = false;

	/**
	 * Self explanatory.
	 */
	public static void enableSplashScreen() {
		Gingerbread3.splashScreen = true;
	}

	/**
	 * Self explanatory.
	 */
	public static boolean splashScreenEnabled() {
		return Gingerbread3.splashScreen;
	}
}
