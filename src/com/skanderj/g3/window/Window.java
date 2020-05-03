package com.skanderj.g3.window;

import java.awt.Canvas;
import java.awt.Dimension;
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
import com.skanderj.g3.inputdevice.InputDevice;

public abstract class Window {
	public static GraphicsDevice DEFAULT_DEVICE = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
	public static int DEFAULT_DEVICE_WIDTH = Window.DEFAULT_DEVICE.getDisplayMode().getWidth(), DEFAULT_DEVICE_HEIGHT = Window.DEFAULT_DEVICE.getDisplayMode().getHeight();

	protected String title;
	protected int width, height;

	protected int buffers;
	protected Game game;

	protected Frame frame;
	protected Canvas canvas;

	protected boolean closeRequested;

	public Window(Game game, String title, int width, int height, int buffers) {
		this.game = game;
		this.title = title;
		this.width = width;
		this.height = height;
		this.buffers = buffers;
		this.frame = new Frame();
		this.canvas = new Canvas();
		this.closeRequested = false;
	}

	public abstract void create();

	public abstract void destroy();

	public abstract void show();

	public abstract void hide();

	public abstract void resize();

	public void registerInput(InputDevice device) {
		switch (device.getType()) {
		case KEYBOARD:
			this.canvas.addKeyListener((KeyListener) device);
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

	public void requestClosing() {
		this.closeRequested = true;
	}

	public void requestFocus() {
		this.canvas.requestFocus();
	}

	public BufferStrategy getBufferStrategy() {
		BufferStrategy bufferStrategy = this.canvas.getBufferStrategy();
		if (bufferStrategy == null) {
			this.canvas.createBufferStrategy(this.buffers);
			return this.getBufferStrategy();
		}
		return bufferStrategy;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
		this.frame.setTitle(title);
	}

	public int getWidth() {
		return this.canvas.getWidth();
	}

	public void setWidth(int width) {
		this.width = width;
		this.resize();
	}

	public int getHeight() {
		return this.canvas.getHeight();
	}

	public void setHeight(int height) {
		this.height = height;
		this.resize();
	}

	public int getBuffers() {
		return this.buffers;
	}

	public Game getGame() {
		return this.game;
	}

	public boolean isCloseRequested() {
		return this.closeRequested;
	}

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
		}

		@Override
		public void destroy() {
			this.hide();
		}

		@Override
		public void show() {
			this.frame.setVisible(true);
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

	public static class Fullscreen extends Window {
		public Fullscreen(Game game, String title, int buffers) {
			super(game, title, Window.DEFAULT_DEVICE_WIDTH, Window.DEFAULT_DEVICE_HEIGHT, buffers);
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
		}

		@Override
		public void destroy() {
			Window.DEFAULT_DEVICE.setFullScreenWindow(null);
		}

		@Override
		public void show() {
			Window.DEFAULT_DEVICE.setFullScreenWindow(this.frame);
		}

		@Override
		public void hide() {
			Window.DEFAULT_DEVICE.setFullScreenWindow(null);
			this.frame.setVisible(false);
		}

		@Override
		public void resize() {
			return;
		}
	}
}
