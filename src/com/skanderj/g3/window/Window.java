package com.skanderj.g3.window;

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

import com.skanderj.g3.core.Game;
import com.skanderj.g3.log.Logger;
import com.skanderj.g3.log.Logger.LogLevel;
import com.skanderj.g3.translation.TranslationManager;
import com.skanderj.g3.window.inputdevice.InputDevice;

/**
 * A class representing an abstract window. Subclasses Regular and Fullscreen
 * are pretty self explanatory.
 *
 * @author Skander
 *
 */
public abstract class Window {
	// Translation keys
	private static final String KEY_WINDOW_INPUT_REGISTER = "key.window.input.register";
	private static final String KEY_WINDOW_CREATE_NO_CALL = "key.window.create_no_call";
	private static final String KEY_WINDOW_DEVICE_ID_NOT_AVAILABLE = "key.window.devid_id_not_available";
	private static final String KEY_WINDOW_NO_DISPLAY_MODE = "key.window.no_display_mode";
	private static final String KEY_WINDOW_NO_DISPLAY_MODE_FALLBACK = "key.window.no_display_mode.fallback";

	// Computer graphic devices
	private static GraphicsDevice[] DEFAULT_DEVICES = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
	// Used when using a fullscreen window
	private static DisplayMode cacheDisplayMode = null;

	// triple buffering is better, double buffering is faster
	public static final int DOUBLE_BUFFERING = 2, TRIPLE_BUFFERING = 3;

	protected String title;
	protected int width, height;

	protected int buffers;
	protected Game game;

	protected Frame frame;
	protected Canvas canvas;

	protected boolean closeRequested, created;

	/**
	 * Self explanatory.
	 */
	public Window(Game game, String title, int width, int height, int buffers) {
		this.game = game;
		this.title = title;
		this.width = width;
		this.height = height;
		this.buffers = buffers;
		this.frame = new Frame();
		this.canvas = new Canvas();
		this.closeRequested = false;
		this.created = false;
	}

	public abstract void create();

	public abstract void destroy();

	public abstract void show();

	public abstract void hide();

	public abstract void resize();

	/**
	 * Self explanatory.
	 */
	public void registerInput(InputDevice device) {
		Logger.log(Window.class, LogLevel.DEV_DEBUG, TranslationManager.getKey(Window.KEY_WINDOW_INPUT_REGISTER, device.getType().name()));
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
		BufferStrategy bufferStrategy = this.canvas.getBufferStrategy();
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
	public void setTitle(String title) {
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
	public void setWidth(int width) {
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
	public void setHeight(int height) {
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
	public Game getGame() {
		return this.game;
	}

	/**
	 * Self explanatory.
	 */
	public boolean isCloseRequested() {
		return this.closeRequested;
	}

	/**
	 * Self explanatory.
	 */
	public final int getWP(float proportion) {
		return (int) Math.floor(this.width * proportion);
	}

	/**
	 * Self explanatory.
	 */
	public final int getHP(float proportion) {
		return (int) Math.floor(this.height * proportion);
	}

	/**
	 * A windowed.. window. Pretty basic.
	 *
	 * @author Skander
	 *
	 */
	public static class Regular extends Window {
		public Regular(Game game, String title, int width, int height, int buffers) {
			super(game, title, width, height, buffers);
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
				public void windowClosing(WindowEvent event) {
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
				Logger.log(Window.class, LogLevel.FATAL, TranslationManager.getKey(Window.KEY_WINDOW_CREATE_NO_CALL));
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

		public static final int isDeviceIDAvailable(int deviceId) {
			if (deviceId >= Window.DEFAULT_DEVICES.length) {
				Logger.log(Window.Fullscreen.class, LogLevel.SEVERE, TranslationManager.getKey(Window.KEY_WINDOW_DEVICE_ID_NOT_AVAILABLE, deviceId));
				return -1;
			} else {
				return deviceId;
			}
		}

		private static final boolean isValid(int deviceId) {
			try {
				Window.DEFAULT_DEVICES[deviceId].hashCode();
				return true;
			} catch (Exception exception) {
				return false;
			}
		}

		private static final DisplayMode getDisplayMode(int device, int fallback) {
			if (Window.cacheDisplayMode != null) {
				return Window.cacheDisplayMode;
			}
			try {
				Window.cacheDisplayMode = Window.DEFAULT_DEVICES[device].getDisplayMode();
				return Window.cacheDisplayMode;
			} catch (Exception exception) {
				Logger.log(Window.class, LogLevel.SEVERE, TranslationManager.getKey(Window.KEY_WINDOW_NO_DISPLAY_MODE, device, fallback));
				try {
					Window.cacheDisplayMode = Window.DEFAULT_DEVICES[fallback].getDisplayMode();
					return Window.cacheDisplayMode;
				} catch (Exception exception2) {
					Logger.log(Window.class, LogLevel.FATAL, TranslationManager.getKey(Window.KEY_WINDOW_NO_DISPLAY_MODE_FALLBACK, fallback));
					return null;
				}
			}
		}

		private int deviceId;

		public Fullscreen(Game game, String title, int buffers) {
			this(game, title, buffers, 0);
		}

		public Fullscreen(Game game, String title, int buffers, int deviceId) {
			this(game, title, buffers, deviceId, Fullscreen.DEFAULT_FALLBACK_DEVICE_ID);
		}

		public Fullscreen(Game game, String title, int buffers, int deviceId, int fallback) {
			super(game, title, Fullscreen.getDisplayMode(deviceId, fallback).getWidth(), Fullscreen.getDisplayMode(deviceId, fallback).getHeight(), buffers);
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
				public void windowClosing(WindowEvent event) {
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
				Logger.log(Window.class, LogLevel.FATAL, TranslationManager.getKey(Window.KEY_WINDOW_CREATE_NO_CALL));
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
