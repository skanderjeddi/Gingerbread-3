package com.skanderj.g3.log;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.skanderj.g3.G3;

public final class Logger {
	private final static PrintStream defaultSystemOutput = System.out;
	private final static PrintStream defaultSystemErrorOutput = System.err;

	private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("[hh:mm:ss]");

	private static boolean outputRedirected = false;

	private Logger() {
		return;
	}

	public static void redirectSystemOutput() {
		if (Logger.outputRedirected) {
			return;
		} else {
			System.setOut(new Logger.LoggerPrintStream(Logger.defaultSystemOutput, Logger.defaultSystemErrorOutput));
			System.setErr(new Logger.LoggerPrintStream(Logger.defaultSystemErrorOutput, Logger.defaultSystemErrorOutput));
			Logger.outputRedirected = true;
			Logger.log(Logger.class, LogLevel.INFO, "Successfully redirected default output streams");
		}
	}

	public static void log(Class<?> clazz, LogLevel logLevel, String message, Object... args) {
		if ((logLevel == LogLevel.SEVERE) || (logLevel == LogLevel.ERROR) || (logLevel == LogLevel.FATAL)) {
			Logger.defaultSystemErrorOutput.printf(Logger.simpleDateFormat.format(new Date()) + " [" + clazz.getSimpleName() + " / " + logLevel.name() + "]: " + message + "\n", args);
		} else {
			if (logLevel == LogLevel.DEBUG) {
				if (G3.DEBUG) {
					Logger.defaultSystemOutput.printf(Logger.simpleDateFormat.format(new Date()) + " [" + clazz.getSimpleName() + " / " + logLevel.name() + "]: " + message + "\n", args);
				}
			} else {
				Logger.defaultSystemOutput.printf(Logger.simpleDateFormat.format(new Date()) + " [" + clazz.getSimpleName() + " / " + logLevel.name() + "]: " + message + "\n", args);
			}
		}
		if (logLevel == LogLevel.FATAL) {
			Logger.defaultSystemErrorOutput.printf(Logger.simpleDateFormat.format(new Date()) + " [" + Logger.class.getSimpleName() + " / " + LogLevel.FATAL.name() + "]: A fatal log level has been submitted from %s.class, exiting all processes.." + "\n", clazz.getSimpleName());
			System.exit(-1);
		}
	}

	public static enum LogLevel {
		INFO, DEBUG, SEVERE, ERROR, FATAL;
	}

	private static class LoggerPrintStream extends PrintStream {
		private PrintStream printStream;

		public LoggerPrintStream(OutputStream out, PrintStream printStream) {
			super(out);
			this.printStream = printStream;
		}

		@Override
		public PrintStream printf(String format, Object... args) {
			return this.printStream.printf(Logger.simpleDateFormat.format(new Date()) + " [DIRECT PRINTF / [SEVERE/?]]: " + format, args);
		}

		@Override
		public PrintStream printf(Locale l, String format, Object... args) {
			return this.printStream.printf(l, Logger.simpleDateFormat.format(new Date()) + " [DIRECT PRINTF / [SEVERE/?]]: " + format, args);
		}

		@Override
		public void print(Object obj) {
			this.printStream.print(Logger.simpleDateFormat.format(new Date()) + " [DIRECT PRINT / [SEVERE/?]]: " + obj);
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
			this.printStream.println(Logger.simpleDateFormat.format(new Date()) + " [DIRECT PRINTLN / [SEVERE/?]]: " + x);
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
