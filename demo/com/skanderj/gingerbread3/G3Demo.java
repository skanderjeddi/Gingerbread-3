package com.skanderj.gingerbread3;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import com.skanderj.gingerbread3.animation.RandomizedAnimation;
import com.skanderj.gingerbread3.audio.Audios;
import com.skanderj.gingerbread3.component.Button;
import com.skanderj.gingerbread3.component.Checkbox;
import com.skanderj.gingerbread3.component.ComponentLabelPosition;
import com.skanderj.gingerbread3.component.ComponentState;
import com.skanderj.gingerbread3.component.Components;
import com.skanderj.gingerbread3.component.Slider;
import com.skanderj.gingerbread3.component.boilerplates.GCheckbox;
import com.skanderj.gingerbread3.component.boilerplates.GLabel;
import com.skanderj.gingerbread3.component.boilerplates.GSlider;
import com.skanderj.gingerbread3.component.boilerplates.GSolidColorBackground;
import com.skanderj.gingerbread3.component.boilerplates.GStraightEdgesButton;
import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.input.Binds;
import com.skanderj.gingerbread3.input.Keyboard;
import com.skanderj.gingerbread3.io.Fonts;
import com.skanderj.gingerbread3.io.Images;
import com.skanderj.gingerbread3.logging.Logger;
import com.skanderj.gingerbread3.logging.Logger.DebuggingType;
import com.skanderj.gingerbread3.math.Vector2;
import com.skanderj.gingerbread3.particle.Particles;
import com.skanderj.gingerbread3.scene.Scene;
import com.skanderj.gingerbread3.scene.Scenes;
import com.skanderj.gingerbread3.sprite.Sprite;
import com.skanderj.gingerbread3.transition.boilerplates.FadeTransition;
import com.skanderj.gingerbread3.util.VisualString;
import com.skanderj.gingerbread3.util.VisualStringProperties;

/**
 * Demo class for the G3 engine.
 *
 * @author Skander
 *
 */
public class G3Demo extends Application {
	/**
	 * Application constants, you don't necessarily need them as constants but it's pretty
	 * nice.
	 */
	public static final String IDENTIFIER = "g3-d", TITLE = "Gingerbread-3 [DEMO]";
	public static final double REFRESH_RATE = 60.0D;
	public static final int WIDTH = 1200, HEIGHT = (G3Demo.WIDTH / 16) * 9, BUFFERS = 2;

	public static final int BACKGROUND_PARTICLES = 50;

	// Constants for button until I implements a better system (how? I don't
	// fucking have a clue)
	public static final int B_WIDTH = 120, B_HEIGHT = (100 / 16) * 9;
	private VisualStringProperties buttonProps;

	// Application scenes
	private final Scene mainMenuScene;
	private final Scene mainGameScene;
	private final Scene settingsScene;

	public G3Demo() {
		super(G3Demo.IDENTIFIER, G3Demo.REFRESH_RATE, G3Demo.TITLE, G3Demo.WIDTH, G3Demo.HEIGHT, G3Demo.BUFFERS, Keyboard.AZERTY);
		this.mainMenuScene = new Scene(this) {
			@Override
			public List<String> sceneObjects() {
				// Those are the only components which will be rendered/updated during this
				// scene
				return Arrays.asList("background-clock", "title", "main-menu-background", "play-button", "settings-button", "exit-button", "music-checkbox", "stars-background");
			}

			@Override
			public void enter() {
				// Play some audio
				if (((Checkbox) Components.get("music-checkbox")).isChecked()) {
					Audios.loop("background", -1, ((Slider) Components.get("main-menu-music-volume")).getValue() / 100.0F);
				}
			}

			@Override
			public void exit() {
				// Stop the audio
				Audios.stopAll();
			}

			@Override
			public synchronized void update(final double delta) {
				super.update(delta);
			}

			@Override
			public synchronized void render(final Screen screen) {
				super.render(screen);
			}
		};
		this.mainGameScene = new Scene(this) {
			@Override
			public void update(final double delta) {
				Registry.parameterize("mouse-position-indicator", new String[] { "mouse-x", "mouse-y" }, new Object[] { this.application.mouse().getX(), this.application.mouse().getY() });
				super.update(delta);
			}

			@Override
			public synchronized void render(final Screen screen) {
				super.render(screen);
			}

			@Override
			public List<String> sceneObjects() {
				return Arrays.asList("main-application-background", "instructions-label", "mouse-position-indicator");
			}

			@Override
			public void enter() {
				/**
				 * If you need to ignore a specific component (for special updates), you do it
				 * here.
				 */
				Scenes.transition("fade-transition");
			}

			@Override
			public void exit() {
				Scenes.transition("fade-transition");
			}
		};
		this.settingsScene = new Scene(this) {
			@Override
			public List<String> sceneObjects() {
				return Arrays.asList("main-menu-music-volume", "back-to-main-menu-button");
			}

			@Override
			public void exit() {
				Scenes.transition("fade-transition");
			}

			@Override
			public void enter() {
				Scenes.transition("fade-transition");
			}

			@Override
			public synchronized void update(final double delta) {
				super.update(delta);
			}
		};
	}

	@Override
	public void loadResources() {
		// Register some audio and some fonts
		Audios.load("background", "res/audio/background.wav");
		Fonts.load("lunchds", "res/font/lunchds.ttf");
		Images.loadAll("ashe_%d", "res/sprite/ashe/");
		Images.loadAll("campfire_%d", "res/sprite/campfire");
	}

	@Override
	public void registerGameObjects() {
		Registry.register("campfire-animation", new RandomizedAnimation(this, (G3Demo.WIDTH / 2) - 70, G3Demo.HEIGHT - 140, Sprite.fromImages(this, "campfire_%d", Images.getCollectionByID("campfire")), new int[] { 8, 10, 12 }));
		Registry.register("smoke-particles", new Particles(this, G3Demo.WIDTH / 2, (G3Demo.HEIGHT / 2) + (G3Demo.HEIGHT / 3) + 5, 25, 40, 10, Sprite.fromImages(this, "ashe_%d", Images.getCollectionByID("ashe")), Vector2.randomVectors(10, -1, 1, 0, -2), 1, 8));
		Registry.register("stars-background", new Particles(this, G3Demo.WIDTH / 2, 0, 10, 2 * G3Demo.HEIGHT, G3Demo.BACKGROUND_PARTICLES, Sprite.fromImages(this, "ashe_%d", Images.getCollectionByID("ashe")), Vector2.randomVectors(G3Demo.BACKGROUND_PARTICLES, -1, 1, 1, 1), 5, 2));
		Registry.register("fade-transition", new FadeTransition(this, 60, Color.BLACK));
	}

	@Override
	public void registerScenes() {
		// Register all the scenes in advance to quickly retrieve them later
		Scenes.register("main-menu", this.mainMenuScene);
		Scenes.register("main-application", this.mainGameScene);
		Scenes.register("settings", this.settingsScene);
	}

	@Override
	public void postCreate() {
		super.postCreate();
		// Enable profiler
		this.useProfiler();
		// Set the current scene
		Scenes.switchTo("main-menu");
	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
	}

	@Override
	public void createComponents() {
		this.buttonProps = new VisualStringProperties(Fonts.get("lunchds", 16), Color.WHITE);
		// Register all the components here once and for all then manage them through
		// scenes switching
		Components.register("main-menu-background", new GSolidColorBackground(this, Screen.DEFAULT_ORIGIN_X, Screen.DEFAULT_ORIGIN_Y, G3Demo.WIDTH, G3Demo.HEIGHT, Color.PINK));
		Components.register("title", new GLabel(this, 0, 0, G3Demo.WIDTH, G3Demo.HEIGHT / 3, new VisualString("G3DEMO", Color.BLACK, Fonts.get("lunchds", 72))));
		Components.register("play-button", new GStraightEdgesButton(this, (G3Demo.WIDTH / 2) - (G3Demo.B_WIDTH / 2), (G3Demo.HEIGHT / 2) + 50, G3Demo.B_WIDTH, G3Demo.B_HEIGHT, new VisualString("Play!", this.buttonProps), Color.BLACK, Color.WHITE));
		Components.register("settings-button", new GStraightEdgesButton(this, (G3Demo.WIDTH / 2) - (G3Demo.B_WIDTH / 2), (G3Demo.HEIGHT / 2) + 130, G3Demo.B_WIDTH, G3Demo.B_HEIGHT, new VisualString("Settings", this.buttonProps), Color.BLACK, Color.WHITE));
		Components.register("exit-button", new GStraightEdgesButton(this, (G3Demo.WIDTH / 2) - (G3Demo.B_WIDTH / 2), (G3Demo.HEIGHT / 2) + 210, G3Demo.B_WIDTH, G3Demo.B_HEIGHT, new VisualString("Exit", this.buttonProps), Color.BLACK, Color.WHITE));
		Components.register("main-application-background", new GSolidColorBackground(this, Screen.DEFAULT_ORIGIN_X, Screen.DEFAULT_ORIGIN_Y, G3Demo.WIDTH, G3Demo.HEIGHT, Color.PINK));
		Components.register("instructions-label", new GLabel(this, Screen.DEFAULT_ORIGIN_X, (G3Demo.HEIGHT / 2) - 50, G3Demo.WIDTH - 1, 100, new VisualString("Press escape to return to the main menu", this.buttonProps.build(28).build(Color.BLACK))));
		Components.register("main-menu-music-volume", new GSlider(this, (G3Demo.WIDTH / 2) - 150, (G3Demo.HEIGHT / 2) - 100, 300, 20, 6, 6, 0, 100, 50, Color.GRAY, new VisualString("Main menu music (%.2f%%)", Color.PINK, Fonts.get("lunchds", 14)), ComponentLabelPosition.TOP));
		Components.register("back-to-main-menu-button", new GStraightEdgesButton(this, (G3Demo.WIDTH / 2) - (G3Demo.B_WIDTH / 2), G3Demo.HEIGHT - (2 * G3Demo.B_HEIGHT), G3Demo.B_WIDTH, G3Demo.B_HEIGHT, new VisualString("Back", this.buttonProps.build(Color.PINK)), Color.BLACK, Color.DARK_GRAY));
		Components.register("music-checkbox", new GCheckbox(this, G3Demo.WIDTH - 90, G3Demo.HEIGHT - 45, 20, 20, new VisualString("Music", Color.BLACK, Fonts.get("lunchds", 14)), Color.GRAY, Color.DARK_GRAY, Color.PINK.darker(), ComponentLabelPosition.RIGHT));
		Components.register("mouse-position-indicator", new GLabel(this, G3Demo.WIDTH - 175, G3Demo.HEIGHT - 40, 100, 30, new VisualString("Mouse position: (%d ; %d)", this.buttonProps.build(14).build(Color.BLACK))));
		// Button actions
		((Button) Components.get("play-button")).setActionForState(ComponentState.ACTIVE, args -> Scenes.switchTo("main-application"));
		((Button) Components.get("settings-button")).setActionForState(ComponentState.ACTIVE, args -> Scenes.switchTo("settings"));
		((Button) Components.get("back-to-main-menu-button")).setActionForState(ComponentState.ACTIVE, args -> Scenes.switchTo("main-menu"));
		((Button) Components.get("exit-button")).setActionForState(ComponentState.ACTIVE, args -> this.stop());
		((Checkbox) Components.get("music-checkbox")).setOnSwitchAction(args -> {
			final boolean state = (boolean) args[0];
			if (state) {
				Audios.stop("background");
				Audios.loop("background", -1, ((Slider) Components.get("main-menu-music-volume")).getValue() / 100.0F);
			} else {
				Audios.stop("background");
			}
		});
	}

	@Override
	public void registerBinds() {
		Binds.registerBind("main-application", new Integer[] { Keyboard.KEY_ESCAPE }, new Keyboard.KeyState[] { Keyboard.KeyState.DOWN }, args -> Scenes.switchTo("main-menu"));
		Binds.registerBind("settings", new Integer[] { Keyboard.KEY_ESCAPE }, new Keyboard.KeyState[] { Keyboard.KeyState.DOWN }, args -> Scenes.switchTo("main-menu"));
		Binds.registerBind("main-menu", new Integer[] { Keyboard.KEY_ESCAPE, Keyboard.KEY_SPACE }, new Keyboard.KeyState[] { Keyboard.KeyState.DOWN, Keyboard.KeyState.DOWN }, args -> this.stop());
	}

	@Override
	public synchronized void update(final double delta) {
		// VERY IMPORTANT TO CALL
		super.update(delta);
		// Scene-independent updating --- not recommended, but flexibility
	}

	@Override
	public synchronized void render(final Screen screen) {
		screen.focusOnQuality();
		// VERY IMPORTANT TO CALL
		super.render(screen);
		// Scene-independent rendering --- not recommended, but flexibility
	}

	public static void main(final String[] args) {
		// Set debugging messages
		Logger.toggleLoggingToFile();
		Logger.setDebuggingState(DebuggingType.CLASSIC, true);
		Logger.setDebuggingState(DebuggingType.DEVELOPMENT, true);
		new G3Demo().start();
	}
}
