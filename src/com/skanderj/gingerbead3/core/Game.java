package com.skanderj.gingerbead3.core;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import com.skanderj.gingerbead3.display.Window;
import com.skanderj.gingerbead3.input.Keyboard;
import com.skanderj.gingerbead3.input.Mouse;
import com.skanderj.gingerbead3.log.Logger;
import com.skanderj.gingerbead3.log.Logger.LogLevel;
import com.skanderj.gingerbead3.scene.SceneManager;

/**
 * Most important class, all G3-based games must extend this class. Pretty self
 * explanatory.
 * 
 * @author Skander
 *
 */
public abstract class Game extends ThreadWrapper {
	public static final int DEFAULT_SIZE = 400, DEFAULT_BUFFERS = 2;

	private final double refreshRate;
	protected boolean displayRefreshRate;
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
		this.displayRefreshRate = false;
		Logger.redirectSystemOutput();
	}

	public Game(String identifier, double refreshRate, String title, int width, int height, int buffers) {
		super(identifier);
		this.initializeEngine();
		this.refreshRate = refreshRate;
		this.window = new Window.Regular(this, title, width, height, buffers);
	}

	public abstract void loadResources();

	public final void registerInputDevices() {
		if (this.keyboard != null) {
			this.window.registerInput(this.keyboard);
		}
		if (this.mouse != null) {
			this.window.registerInput(this.mouse);
		}
	}

	public abstract void registerComponents();

	@Override
	protected void create() {
		this.loadResources();
		this.window.create();
		this.registerInputDevices();
		this.window.show();
		this.postCreate();
	}

	public void postCreate() {
		this.registerComponents();
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
				if (this.displayRefreshRate) {
					Logger.log(this.getClass(), LogLevel.DEBUG, "Last second frames: %d, last second updates: %d", frames, updates);
				}
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
	public synchronized void update(double delta) {
		SceneManager.updateScene(this, delta);
	}

	/**
	 * Renders the game
	 *
	 * @param graphics used to draw the screen
	 */
	public synchronized void render(Graphics2D graphics) {
		SceneManager.renderScene(this, graphics);
	}

	public final void updateInputDevices() {
		if (this.keyboard != null) {
			this.keyboard.update();
		}
		if (this.mouse != null) {
			this.mouse.update();
		}
	}

	/**
	 * Self explanatory.
	 */
	public Keyboard getKeyboard() {
		return this.keyboard;
	}

	/**
	 * Self explanatory.
	 */
	public Mouse getMouse() {
		return this.mouse;
	}

	/**
	 * Self explanatory.
	 */
	public double getRefreshRate() {
		return this.refreshRate;
	}

	/**
	 * Self explanatory.
	 */
	public Window getWindow() {
		return this.window;
	}
}