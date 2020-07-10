package com.skanderj.gingerbread3.display;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import com.skanderj.gingerbread3.core.G3Application;
import com.skanderj.gingerbread3.input.InputDevice;
import com.skanderj.gingerbread3.logging.Logger;
import com.skanderj.gingerbread3.logging.Logger.LogLevel;

/**
 * A class representing an abstract window. Subclasses Regular and Fullscreen
 * are pretty self explanatory.
 *
 * @author Skander
 *
 */
public abstract class Window {
	// Computer graphic devices
	private static GraphicsDevice[] DEFAULT_DEVICES = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
	// Used when using a fullscreen window
	private static DisplayMode cacheDisplayMode = null;

	// triple buffering is better, double buffering is faster
	public static final int DOUBLE_BUFFERING = 2, TRIPLE_BUFFERING = 3;

	protected String title;
	protected int width, height;

	protected int buffers;
	protected G3Application g3Application;

	protected Frame frame;
	protected Canvas canvas;

	protected boolean closeRequested, created;

	protected Screen screen;

	/**
	 * Self explanatory.
	 */
	public Window(final G3Application g3Application, final String title, final int width, final int height, final int buffers) {
		this.g3Application = g3Application;
		this.title = title;
		this.width = width;
		this.height = height;
		this.buffers = buffers;
		this.frame = new Frame();
		this.canvas = new Canvas();
		this.closeRequested = false;
		this.created = false;
		this.screen = new Screen(width, height);
	}

	public abstract void create();

	public abstract void destroy();

	public abstract void show();

	public abstract void hide();

	public abstract void resize();

	/**
	 * Self explanatory.
	 */
	public void registerInput(final InputDevice device) {
		Logger.log(Window.class, LogLevel.DEVELOPMENT, "Input device registered <type : %s | class : %s>", device.getType().name(), device.getClass().getSimpleName());
		switch (device.getType()) {
		case KEYBOARD:
			this.canvas.addKeyListener((KeyListener) device);
			this.canvas.setFocusTraversalKeysEnabled(false);
			break;
		case MOUSE:
			this.canvas.addMouseListener((MouseListener) device);
			this.canvas.addMouseMotionListener((MouseMotionListener) device);
			this.canvas.addMouseWheelListener((MouseWheelListener) device);
			break;
		default:
			return;
		}
	}

	public final void enableOpenGL() {
		System.setProperty("sun.java2d.opengl", "True");
		System.setProperty("sun.java2d.accthreshold", "0");
		System.setProperty("java.awt.headless", "false");
	}

	/**
	 * Asks the window to close (maybe on a keypress?).
	 */
	public void requestClosing() {
		this.closeRequested = true;
	}

	/**
	 * Used to allow mouse and keyboard input to actually go to the canvas and then
	 * the different components.
	 */
	public void requestFocus() {
		this.canvas.requestFocus();
	}

	/**
	 * #TODO explain this and implement it properly (it works but I want it wrapped
	 * in something clearer).
	 */
	public BufferStrategy getBufferStrategy() {
		final BufferStrategy bufferStrategy = this.canvas.getBufferStrategy();
		if (bufferStrategy == null) {
			this.canvas.createBufferStrategy(this.buffers);
			return this.getBufferStrategy();
		}
		return bufferStrategy;
	}

	/**
	 * Self explanatory.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Self explanatory.
	 */
	public void setTitle(final String title) {
		this.title = title;
		this.frame.setTitle(title);
	}

	/**
	 * Self explanatory.
	 */
	public int getWidth() {
		return this.canvas.getWidth();
	}

	/**
	 * Self explanatory.
	 */
	public void setWidth(final int width) {
		this.width = width;
		this.resize();
	}

	/**
	 * Self explanatory.
	 */
	public int getHeight() {
		return this.canvas.getHeight();
	}

	/**
	 * Self explanatory.
	 */
	public void setHeight(final int height) {
		this.height = height;
		this.resize();
	}

	/**
	 * Self explanatory.
	 */
	public int getBuffers() {
		return this.buffers;
	}

	/**
	 * Useless for now
	 */
	public G3Application getG3Application() {
		return this.g3Application;
	}

	/**
	 * Self explanatory.
	 */
	public boolean isCloseRequested() {
		return this.closeRequested;
	}

	public final Screen screen() {
		return this.screen;
	}

	/**
	 * A windowed.. window. Pretty basic.
	 *
	 * @author Skander
	 *
	 */
	public static class Regular extends Window {
		public Regular(final G3Application g3Application, final String title, final int width, final int height, final int buffers) {
			super(g3Application, title, width, height, buffers);
		}

		@Override
		public void create() {
			this.canvas.setMinimumSize(new Dimension(this.width, this.height));
			this.canvas.setMaximumSize(new Dimension(this.width, this.height));
			this.canvas.setPreferredSize(new Dimension(this.width, this.height));
			this.frame.setTitle(this.title);
			this.frame.add(this.canvas);
			this.frame.setResizable(false);
			this.frame.pack();
			this.frame.setLocationRelativeTo(null);
			this.frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(final WindowEvent event) {
					Regular.this.requestClosing();
				}
			});
			this.created = true;
		}

		@Override
		public void destroy() {
			this.hide();
		}

		@Override
		public void show() {
			if (this.created) {
				this.frame.setVisible(true);
			} else {
				Logger.log(Window.class, LogLevel.FATAL, "You need to call create() before show() on any window instance");
			}
		}

		@Override
		public void hide() {
			this.frame.setVisible(false);
		}

		@Override
		public void resize() {
			this.canvas.setMinimumSize(new Dimension(this.width, this.height));
			this.canvas.setMaximumSize(new Dimension(this.width, this.height));
			this.canvas.setPreferredSize(new Dimension(this.width, this.height));
			this.frame.pack();
			this.frame.setLocationRelativeTo(null);
		}
	}

	/**
	 * A fullscreen window. Very basic.
	 *
	 * @author Skander
	 *
	 */
	public static class Fullscreen extends Window {
		public static final int DEFAULT_FALLBACK_DEVICE_ID = 0, DEFAULT_DEVICE_ID = 0;

		public static final int getDisplayDevicesCount() {
			return Window.DEFAULT_DEVICES.length;
		}

		public static final int isDeviceIDAvailable(final int deviceId) {
			if (deviceId >= Window.DEFAULT_DEVICES.length) {
				Logger.log(Window.Fullscreen.class, LogLevel.SEVERE, "Requested device id %d is not available, returning -1", deviceId);
				return -1;
			} else {
				return deviceId;
			}
		}

		private static final boolean isValid(final int deviceId) {
			try {
				Window.DEFAULT_DEVICES[deviceId].hashCode();
				return true;
			} catch (final Exception exception) {
				return false;
			}
		}

		private static final DisplayMode getDisplayMode(final int device, final int fallback) {
			if (Window.cacheDisplayMode != null) {
				return Window.cacheDisplayMode;
			}
			try {
				Window.cacheDisplayMode = Window.DEFAULT_DEVICES[device].getDisplayMode();
				return Window.cacheDisplayMode;
			} catch (final Exception exception) {
				Logger.log(Window.class, LogLevel.SEVERE, "Unable to retrieve the display mode for device %d, falling back on device %d", device, fallback);
				try {
					Window.cacheDisplayMode = Window.DEFAULT_DEVICES[fallback].getDisplayMode();
					return Window.cacheDisplayMode;
				} catch (final Exception exception2) {
					Logger.log(Window.class, LogLevel.FATAL, "Unable to retrieve the display mode for fallback device %d", fallback);
					return null;
				}
			}
		}

		private int deviceId;

		public Fullscreen(final G3Application g3Application, final String title, final int buffers) {
			this(g3Application, title, buffers, 0);
		}

		public Fullscreen(final G3Application g3Application, final String title, final int buffers, final int deviceId) {
			this(g3Application, title, buffers, deviceId, Fullscreen.DEFAULT_FALLBACK_DEVICE_ID);
		}

		public Fullscreen(final G3Application g3Application, final String title, final int buffers, final int deviceId, final int fallback) {
			super(g3Application, title, Fullscreen.getDisplayMode(deviceId, fallback).getWidth(), Fullscreen.getDisplayMode(deviceId, fallback).getHeight(), buffers);
			if (Fullscreen.isValid(deviceId)) {
				this.deviceId = deviceId;
			} else {
				this.deviceId = fallback;
			}
		}

		@Override
		public void create() {
			this.canvas.setMinimumSize(new Dimension(this.width, this.height));
			this.canvas.setMaximumSize(new Dimension(this.width, this.height));
			this.canvas.setPreferredSize(new Dimension(this.width, this.height));
			this.frame.setTitle(this.title);
			this.frame.add(this.canvas);
			this.frame.setResizable(false);
			this.frame.setLocationRelativeTo(null);
			this.frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(final WindowEvent event) {
					Fullscreen.this.requestClosing();
				}
			});
			this.frame.setUndecorated(true);
			this.created = true;
		}

		@Override
		public void destroy() {
			Window.DEFAULT_DEVICES[this.deviceId].setFullScreenWindow(null);
		}

		@Override
		public void show() {
			if (this.created) {
				Window.DEFAULT_DEVICES[this.deviceId].setFullScreenWindow(this.frame);
			} else {
				Logger.log(Window.class, LogLevel.FATAL, "You need to call create() before show() on any window instance");
			}
		}

		@Override
		public void hide() {
			Window.DEFAULT_DEVICES[this.deviceId].setFullScreenWindow(null);
			this.frame.setVisible(false);
		}

		@Override
		public void resize() {
			return;
		}
	}
}
