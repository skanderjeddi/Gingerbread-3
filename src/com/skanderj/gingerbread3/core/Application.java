package com.skanderj.gingerbread3.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.skanderj.gingerbread3.Gingerbread3;
import com.skanderj.gingerbread3.component.Components;
import com.skanderj.gingerbread3.component.boilerplates.GBackgroundColor;
import com.skanderj.gingerbread3.component.boilerplates.GBackgroundImage;
import com.skanderj.gingerbread3.component.boilerplates.GLabel;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.display.Window;
import com.skanderj.gingerbread3.input.Binds;
import com.skanderj.gingerbread3.input.Keyboard;
import com.skanderj.gingerbread3.input.Keyboard.KeyState;
import com.skanderj.gingerbread3.input.Mouse;
import com.skanderj.gingerbread3.logging.Logger;
import com.skanderj.gingerbread3.logging.Logger.LogLevel;
import com.skanderj.gingerbread3.resources.Fonts;
import com.skanderj.gingerbread3.resources.Images;
import com.skanderj.gingerbread3.scene.Scene;
import com.skanderj.gingerbread3.scene.Scenes;
import com.skanderj.gingerbread3.scheduler.Scheduler;
import com.skanderj.gingerbread3.scheduler.Task;
import com.skanderj.gingerbread3.transition.boilerplates.FadeInTransition;
import com.skanderj.gingerbread3.transition.boilerplates.FadeOutTransition;
import com.skanderj.gingerbread3.util.Text;
import com.skanderj.gingerbread3.util.Utilities;

/**
 * Most important class, all G3-based apps must extend this class. Pretty self
 * explanatory.
 *
 * @author Skander
 *
 */
public abstract class Application extends ThreadWrapper {
	protected final double refreshRate;
	protected final Window window;
	protected Keyboard keyboard;
	protected final Mouse mouse;

	private BufferStrategy bufferStrategy;

	/**
	 * Creates a fullscreen window on the requested screen (deviceId).
	 */
	public Application(final String identifier, final double refreshRate, final String title, final int buffers, final int deviceId, final Class<? extends Keyboard> localizedKeyboardClass) {
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
	}

	/**
	 * Creates a regular window.
	 */
	public Application(final String identifier, final double refreshRate, final String title, final int width, final int height, final int buffers, final Class<? extends Keyboard> localizedKeyboardClass) {
		super(identifier);
		this.initializeEngine();
		this.refreshRate = refreshRate;
		this.window = new Window.Regular(this, title, width, height, buffers);
		try {
			this.keyboard = localizedKeyboardClass.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException exception) {
			Logger.log(Application.class, LogLevel.FATAL, "Wrong class type for keyboard instantiation: %s", exception.getMessage());
		}
		this.mouse = new Mouse();
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
	 * Register application objects here.
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
		Logger.log(this.getClass(), LogLevel.DEBUG, "Application creation took %d ms", endTime - startTime);
	}

	public void postCreate() {
		this.registerGameObjects();
		this.createComponents();
		this.registerScenes();
		if (Gingerbread3.splashScreenEnabled()) {
			this.setupSplashscreen();
		} else {
			Scenes.switchTo(this.firstScene());
		}
		this.registerBinds();
		this.window.requestFocus();
	}

	/**
	 * Self explanatory.
	 */
	public abstract String firstScene();

	@Override
	protected void destroy() {
		this.window.hide();
		this.cleanUp();
		System.exit(0);
	}

	/**
	 * Self explanatory.
	 */
	private final void setupSplashscreen() {
		Images.register("gingerbread-logo", "res/g3-logo.png");
		Fonts.load("lunchds", "res/fonts/lunchds.ttf");
		Components.register("splash-background", new GBackgroundColor(this, 0, 0, this.window.getWidth(), this.window.getHeight(), Color.PINK));
		Components.register("gingerbread-logo", new GBackgroundImage(this, (this.window.getWidth() / 2) - 50, (this.window.getHeight() / 2) - 50, 100, 100, Images.get("gingerbread-logo")) {
			@Override
			public Priority priority() {
				return Priority.LOW;
			}
		});
		Components.register("powered-by-gingerbread-text", new GLabel(this, 0, 0, this.window.getWidth(), this.window.getHeight() + (this.window.getHeight() / 2), new Text("Powered by Gingerbread", Utilities.buildAgainst(Color.BLACK, 200), Fonts.get("lunchds").deriveFont(72f))));
		Engine.register("splash-fade-in-transition", new FadeInTransition(this, 180, Color.BLACK));
		Engine.register("splash-fade-out-transition", new FadeOutTransition(this, 180, Color.BLACK));
		Scenes.register("gingerbread-splashscreen", new Scene(this) {
			@Override
			public List<String> sceneObjects() {
				return Arrays.asList("splash-background", "gingerbread-logo", "powered-by-gingerbread-text");
			}

			@Override
			public void in() {
				return;
			}

			@Override
			public String inTransition() {
				return "splash-fade-in-transition";
			}

			@Override
			public void out() {
				return;
			}

			@Override
			public String outTransition() {
				return "splash-fade-out-transition";
			}
		});
		Scenes.switchTo("gingerbread-splashscreen");
		Scheduler.scheduleTask("splashscreen-exit", new Task(this, (int) this.refreshRate * 5) {
			@Override
			public void execute() {
				Scenes.switchTo(Application.this.firstScene());
			}
		});
		Binds.registerBind("gingerbread-splashscreen", Utilities.createArray(Keyboard.KEY_SPACE), Utilities.createArray(KeyState.DOWN_IN_CURRENT_FRAME), object -> {
			Scenes.switchTo(Application.this.firstScene());
			Scheduler.cancel("splashscreen-exit", false);
		});
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
				if (this.window.closeRequested()) {
					this.stop();
				}
				this.updateInputDevices();
				delta -= 1;
				shouldRender = true;
			}
			if (shouldRender) {
				frames++;
				this.bufferStrategy = this.window.bufferStrategy();
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
				Engine.parameterize(this.profilerIdentifier(), new String[] { "frames", "updates" }, new Object[] { frames, updates });
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

	/**
	 * Profiler displays fps and ups each cycle.
	 */
	protected synchronized final void useProfiler() {
		Scheduler.scheduleTask(this.profilerIdentifier(), new Task(this, (int) this.refreshRate, (int) this.refreshRate) {
			@Override
			public void execute() {
				final Map<String, Object> argsMap = Engine.parameters(Application.this.profilerIdentifier());
				if (argsMap == null) {
					Logger.log(this.getClass(), LogLevel.WARNING, "Skipping profiler output (null args)");
				} else {
					Logger.log(this.getClass(), LogLevel.DEBUG, "%d frames last second for %d updates", argsMap.get("frames"), argsMap.get("updates"));
				}
			}
		});
	}

	private synchronized final String profilerIdentifier() {
		return this.identifier + "-profiler";
	}

	/**
	 * Updates application logic
	 *
	 * @param delta the delay between the current update and last update
	 */
	protected synchronized void update() {
		Binds.update(this);
		Scenes.update();
	}

	/**
	 * Renders the application
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
	public double refreshRate() {
		return this.refreshRate;
	}

	/**
	 * Self explanatory.
	 */
	public synchronized final void changeKeyboardLayout(final Class<? extends Keyboard> targetKeyboardClass) {
		try {
			this.keyboard = targetKeyboardClass.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException exception) {
			Logger.log(Application.class, LogLevel.FATAL, "Wrong class type for keyboard instantiation: %s", exception.getMessage());
		}
	}
}