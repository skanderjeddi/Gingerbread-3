package com.skanderj.gingerbead3.log;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.skanderj.gingerbead3.Gingerbread3;

/**
 * A class used for custom logging purposes. Features: system streams
 * redirection, (TODO) custom severity levels, process exiting when hitting a
 * fatal error (TODO make it toggleable), and much more.
 *
 * @author Skander
 *
 */
public final class Logger {
	// Debuggings' states
	private static boolean DEBUG = true, DEV_DEBUG = false;

	// References to the default system streams
	private final static PrintStream defaultSystemOutput = System.out;
	private final static PrintStream defaultSystemErrorOutput = System.err;

	// Date and time format, #TODO make it customizable
	private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("[hh:mm:ss]");

	// Redirection currentState
	private static boolean outputRedirected = false;

	private Logger() {
		return;
	}

	/**
	 * Self explanatory.
	 */
	public static void redirectSystemOutput() {
		if (Logger.outputRedirected) {
			return;
		} else {
			// Display the Gingerbread version message
			Logger.log(Gingerbread3.class, LogLevel.INFO, "Gingerbread-3 release %s - by SkanderJ", Gingerbread3.RELEASE);
			System.setOut(new Logger.LoggerPrintStream(Logger.defaultSystemOutput, Logger.defaultSystemErrorOutput));
			System.setErr(new Logger.LoggerPrintStream(Logger.defaultSystemErrorOutput, Logger.defaultSystemErrorOutput));
			Logger.outputRedirected = true;
			Logger.log(Logger.class, LogLevel.DEBUG, "Successfully redirected default output streams");
			Logger.DEBUG = false;
			Logger.DEV_DEBUG = false;
			Logger.log(Logger.class, LogLevel.INFO, "Disabled all debug messages");
		}
	}

	/**
	 * Self explanatory. A FATAL log level will exit all processes. The "message"
	 * string will be formatted with the "args" parameter.
	 */
	public static void log(final Class<?> clazz, final LogLevel logLevel, final String message, final Object... args) {
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
			Logger.defaultSystemErrorOutput.printf(Logger.simpleDateFormat.format(new Date()) + " [" + Logger.class.getSimpleName() + " / " + LogLevel.FATAL.name() + "]: A fatal log has been submitted from %s.class, exiting all processes \n", clazz.getSimpleName());
			System.exit(-1);
		}
	}

	/**
	 * Self explanatory.
	 */
	public static void setDebuggingState(final DebuggingType type, final boolean status) {
		Logger.redirectSystemOutput();
		switch (type) {
		case CLASSIC:
			Logger.DEBUG = status;
			Logger.log(Logger.class, LogLevel.INFO, "Enabled regular debugging messages");
			break;
		case DEVELOPMENT:
			Logger.log(Logger.class, LogLevel.INFO, "Enabled development debugging messages");
			Logger.DEV_DEBUG = status;
		}
	}

	/**
	 *
	 * @author Skander
	 *
	 */
	public enum LogLevel {
		INFO, DEBUG, DEV_DEBUG, IGNORE, SEVERE, ERROR, FATAL;
	}

	/**
	 *
	 * @author Skander
	 *
	 */
	public enum DebuggingType {
		CLASSIC, DEVELOPMENT;
	}

	/**
	 * Custom print streams for when redirection hasn't happened yet.
	 *
	 * @author Skander
	 *
	 */
	private static class LoggerPrintStream extends PrintStream {
		private final PrintStream printStream;

		public LoggerPrintStream(final OutputStream out, final PrintStream printStream) {
			super(out);
			this.printStream = printStream;
		}

		@Override
		public PrintStream printf(final String format, final Object... args) {
			return this.printStream.printf(Logger.simpleDateFormat.format(new Date()) + " [? / ?]: " + format, args);
		}

		@Override
		public PrintStream printf(final Locale l, final String format, final Object... args) {
			return this.printStream.printf(l, Logger.simpleDateFormat.format(new Date()) + " [? / ?]: " + format, args);
		}

		@Override
		public void print(final Object obj) {
			this.printStream.print(Logger.simpleDateFormat.format(new Date()) + " [? / ?]: " + obj);
		}

		@Override
		public void print(final boolean b) {
			this.print((Object) b);
		}

		@Override
		public void print(final char c) {
			this.print((Object) c);
		}

		@Override
		public void print(final char[] s) {
			this.print((Object) s);
		}

		@Override
		public void print(final double d) {
			this.print((Object) d);
		}

		@Override
		public void print(final float f) {
			this.print((Object) f);
		}

		@Override
		public void print(final int i) {
			this.print((Object) i);
		}

		@Override
		public void print(final long l) {
			this.print((Object) l);
		}

		@Override
		public void print(final String s) {
			this.print((Object) s);
		}

		@Override
		public void println(final Object x) {
			this.printStream.println(Logger.simpleDateFormat.format(new Date()) + " [? / ?]: " + x);
		}

		@Override
		public void println() {
			this.printStream.println();
		}

		@Override
		public void println(final boolean x) {
			this.println((Object) x);
		}

		@Override
		public void println(final char x) {
			this.println((Object) x);
		}

		@Override
		public void println(final char[] x) {
			this.println((Object) x);
		}

		@Override
		public void println(final double x) {
			this.println((Object) x);
		}

		@Override
		public void println(final float x) {
			this.println((Object) x);
		}

		@Override
		public void println(final int x) {
			this.println((Object) x);
		}

		@Override
		public void println(final long x) {
			this.println((Object) x);
		}

		@Override
		public void println(final String x) {
			this.println((Object) x);
		}
	}
}
