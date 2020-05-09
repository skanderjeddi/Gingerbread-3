package com.skanderj.g3.log;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.skanderj.g3.translation.TranslationManager;

/**
 * A class used for custom logging purposes. Features: system streams
 * redirection, (TODO) custom severity levels, process exiting when hitting a
 * fatal error (TODO make it toggleable), and much more.
 * 
 * @author Skander
 *
 */
public final class Logger {
	// Translation keys
	private static final String KEY_LOGGER_REDIRECT_SUCCESS = "key.logger.redirect_success";
	private static final String KEY_LOGGER_FATAL_QUITTING = "key.logger.fatal.quitting";

	// Debuggings' states
	private static boolean DEBUG = false, DEV_DEBUG = false;

	// References to the default system streams
	private final static PrintStream defaultSystemOutput = System.out;
	private final static PrintStream defaultSystemErrorOutput = System.err;

	// Date and time format, #TODO make it customizable
	private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("[hh:mm:ss]");

	// Redirection state
	private static boolean outputRedirected = false;

	private Logger() {
		return;
	}

	/**
	 * Self explanatory.
	 */
	public static final void redirectSystemOutput() {
		if (Logger.outputRedirected) {
			return;
		} else {
			System.setOut(new Logger.LoggerPrintStream(Logger.defaultSystemOutput, Logger.defaultSystemErrorOutput));
			System.setErr(new Logger.LoggerPrintStream(Logger.defaultSystemErrorOutput, Logger.defaultSystemErrorOutput));
			Logger.outputRedirected = true;
			Logger.log(Logger.class, LogLevel.DEBUG, TranslationManager.getKey(Logger.KEY_LOGGER_REDIRECT_SUCCESS));
		}
	}

	/**
	 * Self explanatory. A FATAL log level will exit all processes. The "message"
	 * string will be formatted with the "args" parameter.
	 */
	public static void log(Class<?> clazz, LogLevel logLevel, String message, Object... args) {
		String origin = new String();
		if (clazz.getEnclosingClass() != null) {
			origin = clazz.getEnclosingClass().getSimpleName() + "#" + clazz.getSimpleName();
		} else {
			origin = clazz.getSimpleName();
		}
		if ((logLevel == LogLevel.SEVERE) || (logLevel == LogLevel.ERROR) || (logLevel == LogLevel.FATAL)) {
			Logger.defaultSystemErrorOutput.printf(Logger.simpleDateFormat.format(new Date()) + " [" + origin + " / " + logLevel.name() + "]: " + message + "\n", args);
		} else {
			if (logLevel == LogLevel.DEBUG) {
				if (Logger.DEBUG) {
					Logger.defaultSystemOutput.printf(Logger.simpleDateFormat.format(new Date()) + " [" + origin + " / " + logLevel.name() + "]: " + message + "\n", args);
				}
			} else if (logLevel == LogLevel.DEV_DEBUG) {
				if (Logger.DEV_DEBUG) {
					Logger.defaultSystemOutput.printf(Logger.simpleDateFormat.format(new Date()) + " [" + origin + " / " + logLevel.name() + "]: " + message + "\n", args);
				}
			} else {
				Logger.defaultSystemOutput.printf(Logger.simpleDateFormat.format(new Date()) + " [" + origin + " / " + logLevel.name() + "]: " + message + "\n", args);
			}
		}
		if (logLevel == LogLevel.FATAL) {
			Logger.defaultSystemErrorOutput.printf(Logger.simpleDateFormat.format(new Date()) + " [" + Logger.class.getSimpleName() + " / " + LogLevel.FATAL.name() + "]: " + TranslationManager.getKey(Logger.KEY_LOGGER_FATAL_QUITTING) + "\n", clazz.getSimpleName());
			System.exit(-1);
		}
	}

	/**
	 * Self explanatory.
	 */
	public static final void setDebuggingState(DebuggingType type, boolean status) {
		switch (type) {
		case CLASSIC:
			Logger.DEBUG = status;
		case DEVELOPMENT:
			Logger.DEV_DEBUG = status;
		}
	}

	/**
	 * 
	 * @author Skander
	 *
	 */
	public static enum LogLevel {
		INFO, DEBUG, DEV_DEBUG, SEVERE, ERROR, FATAL;
	}

	/**
	 * 
	 * @author Skander
	 *
	 */
	public static enum DebuggingType {
		CLASSIC, DEVELOPMENT;
	}

	/**
	 * Custom print streams for when redirection hasn't happened yet.
	 * 
	 * @author Skander
	 *
	 */
	private static class LoggerPrintStream extends PrintStream {
		private PrintStream printStream;

		public LoggerPrintStream(OutputStream out, PrintStream printStream) {
			super(out);
			this.printStream = printStream;
		}

		@Override
		public PrintStream printf(String format, Object... args) {
			return this.printStream.printf(Logger.simpleDateFormat.format(new Date()) + " [? / ?]: " + format, args);
		}

		@Override
		public PrintStream printf(Locale l, String format, Object... args) {
			return this.printStream.printf(l, Logger.simpleDateFormat.format(new Date()) + " [? / ?]: " + format, args);
		}

		@Override
		public void print(Object obj) {
			this.printStream.print(Logger.simpleDateFormat.format(new Date()) + " [? / ?]: " + obj);
		}

		@Override
		public void print(boolean b) {
			this.print((Object) b);
		}

		@Override
		public void print(char c) {
			this.print((Object) c);
		}

		@Override
		public void print(char[] s) {
			this.print((Object) s);
		}

		@Override
		public void print(double d) {
			this.print((Object) d);
		}

		@Override
		public void print(float f) {
			this.print((Object) f);
		}

		@Override
		public void print(int i) {
			this.print((Object) i);
		}

		@Override
		public void print(long l) {
			this.print((Object) l);
		}

		@Override
		public void print(String s) {
			this.print((Object) s);
		}

		@Override
		public void println(Object x) {
			this.printStream.println(Logger.simpleDateFormat.format(new Date()) + " [? / ?]: " + x);
		}

		@Override
		public void println() {
			this.printStream.println();
		}

		@Override
		public void println(boolean x) {
			this.println((Object) x);
		}

		@Override
		public void println(char x) {
			this.println((Object) x);
		}

		@Override
		public void println(char[] x) {
			this.println((Object) x);
		}

		@Override
		public void println(double x) {
			this.println((Object) x);
		}

		@Override
		public void println(float x) {
			this.println((Object) x);
		}

		@Override
		public void println(int x) {
			this.println((Object) x);
		}

		@Override
		public void println(long x) {
			this.println((Object) x);
		}

		@Override
		public void println(String x) {
			this.println((Object) x);
		}
	}
}
