package com.skanderj.gingerbread3.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import com.skanderj.gingerbread3.core.object.GameObject;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.display.Window;
import com.skanderj.gingerbread3.input.Binds;
import com.skanderj.gingerbread3.input.Keyboard;
import com.skanderj.gingerbread3.input.Mouse;
import com.skanderj.gingerbread3.logging.Logger;
import com.skanderj.gingerbread3.logging.Logger.LogLevel;
import com.skanderj.gingerbread3.scene.Scenes;

/**
 * Most important class, all G3-based games must extend this class. Pretty self
 * explanatory.
 *
 * @author Skander
 *
 */
public abstract class Game extends ThreadWrapper {
	public static final int DEFAULT_SIZE = 400, DEFAULT_BUFFERS = 2;

	protected final double refreshRate;
	protected final Window window;
	protected Keyboard keyboard;
	protected final Mouse mouse;
	protected final Updatable profiler;

	private BufferStrategy bufferStrategy;

	/**
	 * Creates a fullscreen window on the requested screen (deviceId).
	 */
	public Game(final String identifier, final double refreshRate, final String title, final int buffers, final int deviceId, final Class<? extends Keyboard> localizedKeyboardClass) {
		super(identifier);
		this.initializeEngine();
		this.refreshRate = refreshRate;
		this.window = new Window.Fullscreen(this, title, buffers, deviceId);
		try {
			this.keyboard = localizedKeyboardClass.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException exception) {
			Logger.log(Game.class, LogLevel.FATAL, "Wrong class type for keyboard instantiation: %s", exception.getMessage());
		}
		this.mouse = new Mouse();
		this.profiler = new Updatable() {
			private int counter = 0;

			@Override
			public void update(final double delta) {
				this.counter += 1;
				if ((this.counter % refreshRate) == 0) {
					this.counter = 0;
					final Map<String, Object> args = Registry.parameters(Game.this.profilerIdentifier());
					if (args == null) {
						Logger.log(this.getClass().getEnclosingClass(), LogLevel.WARNING, "Skipping profiler output (null args)");
					} else {
						Logger.log(this.getClass().getEnclosingClass(), LogLevel.DEBUG, "%d frames last second for %d updates", args.get("frames"), args.get("updates"));
					}
				}
			}

			@Override
			public Priority priority() {
				return Priority.CRITICAL;
			}
		};
	}

	/**
	 * Creates a regular window.
	 */
	public Game(final String identifier, final double refreshRate, final String title, final int width, final int height, final int buffers, final Class<? extends Keyboard> localizedKeyboardClass) {
		super(identifier);
		this.initializeEngine();
		this.refreshRate = refreshRate;
		this.window = new Window.Regular(this, title, width, height, buffers);
		try {
			this.keyboard = localizedKeyboardClass.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException exception) {
			Logger.log(Game.class, LogLevel.FATAL, "Wrong class type for keyboard instantiation: %s", exception.getMessage());
		}
		this.mouse = new Mouse();
		this.profiler = new Updatable() {
			private int counter = 0;

			@Override
			public void update(final double delta) {
				this.counter += 1;
				if ((this.counter % refreshRate) == 0) {
					this.counter = 0;
					final Map<String, Object> args = Registry.parameters(Game.this.profilerIdentifier());
					if (args == null) {
						Logger.log(this.getClass().getEnclosingClass(), LogLevel.WARNING, "Skipping profiler output (null args)");
					} else {
						Logger.log(this.getClass().getEnclosingClass(), LogLevel.DEBUG, "%d frames last second for %d updates", args.get("frames"), args.get("updates"));
					}
				}
			}

			@Override
			public Priority priority() {
				return Priority.CRITICAL;
			}
		};
	}

	/**
	 * Called internally.
	 */
	private final void initializeEngine() {
		Logger.redirectSystemOutput();
	}

	/**
	 * Here is where all the resources loading should take place, through Audios,
	 * Images and Fonts.
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
	 * Register game objects here.
	 */
	public abstract void registerGameObjects();

	/**
	 * Register scenes here --- useful for better organization.
	 */
	public abstract void registerScenes();

	/**
	 * Create and register all the components here.
	 */
	public abstract void createComponents();

	/**
	 * Register all binds here.
	 */
	public abstract void registerBinds();

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
		this.registerGameObjects();
		Registry.register(this.profilerIdentifier(), GameObject.constructFromUpdatable(this, this.profiler));
		this.createComponents();
		this.registerScenes();
		this.registerBinds();
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
				Binds.update(this, delta);
				this.updateInputDevices();
				delta -= 1;
				shouldRender = true;
			}
			if (shouldRender) {
				frames++;
				this.bufferStrategy = this.window.getBufferStrategy();
				this.screen().clear(this.window, Color.BLACK);
				this.render(this.screen());
				this.screen().renderThrough((Graphics2D) this.bufferStrategy.getDrawGraphics());
				this.screen().drawTo();
				this.screen().dispose();
				this.bufferStrategy.show();
			}
			if ((System.currentTimeMillis() - resetTime) >= 1000) {
				resetTime += 1000;
				Registry.parameterize(this.profilerIdentifier(), new String[] { "frames", "updates" }, new Object[] { frames, updates });
				frames = 0;
				updates = 0;
			}
		}
	}

	protected synchronized final void useProfiler() {
		Registry.get(this.profilerIdentifier()).setShouldSkipRegistryChecks(true);
	}

	protected synchronized final String profilerIdentifier() {
		return this.identifier + "-profiler";
	}

	/**
	 * Updates game logic
	 *
	 * @param delta the delay between the current update and last update
	 */
	protected synchronized void update(final double delta) {
		Scenes.update(delta);
	}

	/**
	 * Renders the game
	 *
	 * @param screen used to draw the screen
	 */
	protected synchronized void render(final Screen screen) {
		Scenes.render(screen);
	}

	/**
	 * Called internally.
	 */
	private final void updateInputDevices() {
		this.keyboard.update();
		this.mouse.update();
	}

	/**
	 * Self explanatory.
	 */
	public synchronized final Window window() {
		return this.window;
	}

	/**
	 * Self explanatory.
	 */
	public synchronized final Screen screen() {
		return this.window.screen();
	}

	/**
	 * Self explanatory.
	 */
	public synchronized final Keyboard keyboard() {
		return this.keyboard;
	}

	/**
	 * Self explanatory.
	 */
	public synchronized final Mouse mouse() {
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
	public synchronized final void changeKeyboardLayout(final Class<? extends Keyboard> targetKeyboardClass) {
		try {
			this.keyboard = targetKeyboardClass.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException exception) {
			Logger.log(Game.class, LogLevel.FATAL, "Wrong class type for keyboard instantiation: %s", exception.getMessage());
		}
	}
}