package com.skanderj.gingerbread3.core;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import com.skanderj.gingerbread3.display.Window;
import com.skanderj.gingerbread3.input.Keyboard;
import com.skanderj.gingerbread3.input.Mouse;
import com.skanderj.gingerbread3.log.Logger;
import com.skanderj.gingerbread3.log.Logger.LogLevel;
import com.skanderj.gingerbread3.scene.SceneManager;

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
	protected final Keyboard keyboard;
	protected final Mouse mouse;

	/**
	 * Creates a fullscreen window on the requested screen (deviceId).
	 */
	public Game(final String identifier, final double refreshRate, final String title, final int buffers, final int deviceId) {
		super(identifier);
		this.initializeEngine();
		this.refreshRate = refreshRate;
		this.window = new Window.Fullscreen(this, title, buffers, deviceId);
		this.keyboard = new Keyboard();
		this.mouse = new Mouse();
	}

	/**
	 * Creates a regular window.
	 */
	public Game(final String identifier, final double refreshRate, final String title, final int width, final int height, final int buffers) {
		super(identifier);
		this.initializeEngine();
		this.refreshRate = refreshRate;
		this.window = new Window.Regular(this, title, width, height, buffers);
		this.keyboard = new Keyboard();
		this.mouse = new Mouse();
	}

	/**
	 * Called internally.
	 */
	private final void initializeEngine() {
		this.displayRefreshRate = false;
		Logger.redirectSystemOutput();
	}

	/**
	 * Here is where all the resources loading should take place, through
	 * AudioManager, ImageManager and FontManager.
	 */
	public abstract void loadResources();

	/**
	 * Called internally.
	 */
	private final void registerInputDevices() {
		if (this.keyboard != null) {
			this.window.registerInput(this.keyboard);
		}
		if (this.mouse != null) {
			this.window.registerInput(this.mouse);
		}
	}

	/**
	 * Register scenes here --- useful for better organization.
	 */
	public abstract void registerScenes();

	/**
	 * Create and register all the components here.
	 */
	public abstract void createComponents();

	@Override
	protected void create() {
		final long startTime = System.currentTimeMillis();
		this.loadResources();
		this.window.create();
		this.registerInputDevices();
		this.window.show();
		this.postCreate();
		final long endTime = System.currentTimeMillis();
		Logger.log(this.getClass(), LogLevel.DEBUG, "Game creation took %d ms", endTime - startTime);
	}

	public void postCreate() {
		this.createComponents();
		this.registerScenes();
		this.window.requestFocus();
	}

	@Override
	protected void destroy() {
		this.window.hide();
		this.cleanup();
		System.exit(0);
	}

	/**
	 * Called after the window is disposed of but before the engine shuts down ---
	 * here you can save progress.
	 */
	public abstract void cleanup();

	@Override
	protected final void loop() {
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
			if ((System.currentTimeMillis() - resetTime) >= 1000) {
				resetTime += 1000;
				if (this.displayRefreshRate) {
					Logger.log(this.getClass(), LogLevel.DEBUG, "%d/%d", frames, updates);
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
	protected synchronized void update(final double delta) {
		SceneManager.updateScene(this, delta);
	}

	/**
	 * Renders the game
	 *
	 * @param graphics used to draw the screen
	 */
	protected synchronized void render(final Graphics2D graphics) {
		SceneManager.renderScene(this, graphics);
	}

	/**
	 * Called internally.
	 */
	private final void updateInputDevices() {
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