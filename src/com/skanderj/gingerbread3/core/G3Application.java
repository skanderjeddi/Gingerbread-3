package com.skanderj.gingerbread3.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.imageio.ImageIO;

import com.skanderj.gingerbread3.core.object.G3Object;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.display.Window;
import com.skanderj.gingerbread3.input.Binds;
import com.skanderj.gingerbread3.input.Keyboard;
import com.skanderj.gingerbread3.input.Mouse;
import com.skanderj.gingerbread3.logging.Logger;
import com.skanderj.gingerbread3.logging.Logger.LogLevel;
import com.skanderj.gingerbread3.scene.Scenes;
import com.skanderj.gingerbread3.scheduler.Scheduler;

/**
 * Most important class, all G3-based apps must extend this class. Pretty self
 * explanatory.
 *
 * @author Skander
 *
 */
public abstract class G3Application extends ThreadWrapper {
	protected final double refreshRate;
	protected final Window window;
	protected Keyboard keyboard;
	protected final Mouse mouse;
	protected final Updatable profiler;

	private BufferStrategy bufferStrategy;

	/**
	 * Creates a fullscreen window on the requested screen (deviceId).
	 */
	public G3Application(final String identifier, final double refreshRate, final String title, final int buffers, final int deviceId, final Class<? extends Keyboard> localizedKeyboardClass) {
		super(identifier);
		this.initializeEngine();
		this.refreshRate = refreshRate;
		this.window = new Window.Fullscreen(this, title, buffers, deviceId);
		try {
			this.keyboard = localizedKeyboardClass.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException exception) {
			Logger.log(this.getClass(), LogLevel.FATAL, "Wrong class type for keyboard instantiation: %s", exception.getMessage());
		}
		this.mouse = new Mouse();
		this.profiler = new Updatable() {
			private int counter = 0;

			@Override
			public void update() {
				this.counter += 1;
				if ((this.counter % refreshRate) == 0) {
					this.counter = 0;
					final Map<String, Object> args = Registry.parameters(G3Application.this.profilerIdentifier());
					if (args == null) {
						Logger.log(this.application().getClass(), LogLevel.WARNING, "Skipping profiler output (null args)");
					} else {
						Logger.log(this.application().getClass(), LogLevel.DEBUG, "%d frames last second for %d updates", args.get("frames"), args.get("updates"));
					}
				}
			}

			@Override
			public Priority priority() {
				return Priority.CRITICAL;
			}

			@Override
			public G3Application application() {
				return G3Application.this;
			}
		};
	}

	/**
	 * Creates a regular window.
	 */
	public G3Application(final String identifier, final double refreshRate, final String title, final int width, final int height, final int buffers, final Class<? extends Keyboard> localizedKeyboardClass) {
		super(identifier);
		this.initializeEngine();
		this.refreshRate = refreshRate;
		this.window = new Window.Regular(this, title, width, height, buffers);
		try {
			this.keyboard = localizedKeyboardClass.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException exception) {
			Logger.log(G3Application.class, LogLevel.FATAL, "Wrong class type for keyboard instantiation: %s", exception.getMessage());
		}
		this.mouse = new Mouse();
		this.profiler = new Updatable() {
			private int counter = 0;

			@Override
			public void update() {
				this.counter += 1;
				if ((this.counter % refreshRate) == 0) {
					this.counter = 0;
					final Map<String, Object> args = Registry.parameters(G3Application.this.profilerIdentifier());
					if (args == null) {
						Logger.log(this.application().getClass(), LogLevel.WARNING, "Skipping profiler output (null args)");
					} else {
						Logger.log(this.application().getClass(), LogLevel.DEBUG, "%d frames last second for %d updates", args.get("frames"), args.get("updates"));
					}
				}
			}

			@Override
			public Priority priority() {
				return Priority.CRITICAL;
			}

			@Override
			public G3Application application() {
				return G3Application.this;
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
	 * Register g3Application objects here.
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
		Logger.log(this.getClass(), LogLevel.DEBUG, "G3Application creation took %d ms", endTime - startTime);
	}

	public void postCreate() {
		this.registerGameObjects();
		Registry.register(this.profilerIdentifier(), G3Object.constructFromUpdatable(this, this.profiler));
		this.createComponents();
		this.registerScenes();
		this.registerBinds();
		this.window.requestFocus();
	}

	@Override
	protected void destroy() {
		this.window.hide();
		this.cleanUp();
		System.exit(0);
	}

	/**
	 * Called after the window is disposed of but before the engine shuts down ---
	 * here you can save progress.
	 */
	public abstract void cleanUp();

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
				this.update();
				if (this.window.isCloseRequested()) {
					this.stop();
				}
				this.updateInputDevices();
				delta -= 1;
				shouldRender = true;
			}
			if (shouldRender) {
				frames++;
				this.bufferStrategy = this.window.getBufferStrategy();
				this.screen().reset();
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
		Logger.cleanUp();
	}

	/**
	 * Self explanatory.
	 */
	public final void screenshot(final String path) {
		final File outputFile = new File(path);
		try {
			outputFile.getParentFile().mkdirs();
			if (outputFile.createNewFile()) {
				ImageIO.write(this.screen().screenContentOnFrame(), "png", outputFile);
				Logger.log(this.getClass(), LogLevel.INFO, "Successfully saved screenshot to %s", path);
			}
		} catch (final IOException ioException) {
			Logger.log(this.getClass(), LogLevel.ERROR, "Couldn't save screenshot: %s", ioException.getMessage());
		}
	}

	protected synchronized final void useProfiler() {
		Registry.get(this.profilerIdentifier()).setShouldSkipRegistryChecks(true);
	}

	protected synchronized final String profilerIdentifier() {
		return this.identifier + "-profiler";
	}

	/**
	 * Updates g3Application logic
	 *
	 * @param delta the delay between the current update and last update
	 */
	protected synchronized void update() {
		Binds.update(this);
		Scheduler.update();
		Scenes.update();
	}

	/**
	 * Renders the g3Application
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
			Logger.log(G3Application.class, LogLevel.FATAL, "Wrong class type for keyboard instantiation: %s", exception.getMessage());
		}
	}
}