package com.skanderj.gingerbead3.core;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import com.skanderj.gingerbead3.Gingerbread3;
import com.skanderj.gingerbead3.display.Window;
import com.skanderj.gingerbead3.input.Keyboard;
import com.skanderj.gingerbead3.input.Mouse;
import com.skanderj.gingerbead3.log.Logger;
import com.skanderj.gingerbead3.log.Logger.DebuggingType;
import com.skanderj.gingerbead3.log.Logger.LogLevel;

public abstract class Game extends ThreadWrapper {
	public static final int DEFAULT_SIZE = 400, DEFAULT_BUFFERS = 2;

	private final double refreshRate;
	protected final Window window;
	protected Keyboard keyboard;
	protected Mouse mouse;

	public Game(String identifier, double refreshRate, String title, int buffers, int deviceId) {
		super(identifier);
		this.initializeEngine();
		this.refreshRate = refreshRate;
		this.window = new Window.Fullscreen(this, title, buffers, deviceId);
	}

	private final void initializeEngine() {
		Logger.redirectSystemOutput();
		// Disable both plain and dev debugging
		Logger.setDebuggingState(DebuggingType.CLASSIC, false);
		Logger.setDebuggingState(DebuggingType.DEVELOPMENT, false);
		// Display the Gingerbread version message
		Logger.log(Gingerbread3.class, LogLevel.INFO, "Gingerbread-3 release %s - by SkanderJ", Gingerbread3.RELEASE);
	}

	public Game(String identifier, double refreshRate, String title, int width, int height, int buffers) {
		super(identifier);
		this.initializeEngine();
		this.refreshRate = refreshRate;
		this.window = new Window.Regular(this, title, width, height, buffers);
	}

	public abstract void loadResources();

	public abstract void registerInputDevices();

	public abstract void initializeComponents();

	@Override
	protected void create() {
		this.loadResources();
		this.window.create();
		this.registerInputDevices();
		this.window.show();
		this.initializeComponents();
		this.window.requestFocus();
	}

	@Override
	protected void destroy() {
		this.window.hide();
		System.exit(0);
	}

	@Override
	protected void loop() {
		long startTime = System.nanoTime();
		final double nanosecondsPerTick = 1000000000D / this.refreshRate;
		int frames = 0;
		int updates = 0;
		long resetTime = System.currentTimeMillis();
		double delta = 0.0D;
		while (this.isRunning) {
			final long endTime = System.nanoTime();
			delta += (endTime - startTime) / nanosecondsPerTick;
			startTime = endTime;
			boolean shouldRender = false;
			while (delta >= 1) {
				updates++;
				this.update(delta);
				if (this.window.isCloseRequested()) {
					this.stop();
				}
				this.updateInputDevices();
				delta -= 1;
				shouldRender = true;
			}
			if (shouldRender) {
				frames++;
				final BufferStrategy bufferStrategy = this.window.getBufferStrategy();
				final Graphics graphics = bufferStrategy.getDrawGraphics();
				this.render((Graphics2D) graphics);
				graphics.dispose();
				bufferStrategy.show();
			}
			if (System.currentTimeMillis() - resetTime >= 1000) {
				resetTime += 1000;
				Logger.log(this.getClass(), LogLevel.DEBUG, "Last second frames: %d, last second updates: %d", frames, updates);
				frames = 0;
				updates = 0;
			}
		}
	}

	/**
	 * Updates game logic
	 *
	 * @param delta the delaya between the current update and last update
	 */
	public abstract void update(double delta);

	/**
	 * Renders the game
	 *
	 * @param graphics used to draw the screen
	 */
	public abstract void render(Graphics2D graphics);

	public final void updateInputDevices() {
		if (this.keyboard != null) {
			this.keyboard.update();
		}
		if (this.mouse != null) {
			this.mouse.update();
		}
	}
}