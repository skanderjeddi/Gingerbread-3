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
import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.input.Keyboard;
import com.skanderj.gingerbread3.input.binds.Binds;
import com.skanderj.gingerbread3.input.localized.AZERTYKeyboard;
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
public class G3Demo extends Game {
	/**
	 * Game constants, you don't necessarily need them as constants but it's pretty
	 * nice.
	 */
	public static final String IDENTIFIER = "g3-d", TITLE = "Gingerbread-3 [DEMO]";
	public static final double REFRESH_RATE = 144.0D;
	public static final int WIDTH = 1000, HEIGHT = (G3Demo.WIDTH / 16) * 9, BUFFERS = 2;

	// Constants for button until I implements a better system (how? I don't
	// fucking have a clue)
	public static final int B_WIDTH = 100, B_HEIGHT = (100 / 16) * 9;
	private VisualStringProperties buttonProps;

	// Game scenes
	private final Scene mainMenuScene;
	private final Scene mainGameScene;
	private final Scene settingsScene;

	public G3Demo() {
		super(G3Demo.IDENTIFIER, G3Demo.REFRESH_RATE, G3Demo.TITLE, G3Demo.WIDTH, G3Demo.HEIGHT, G3Demo.BUFFERS, AZERTYKeyboard.class);
		this.mainMenuScene = new Scene(this) {
			@Override
			public List<String> sceneObjects() {
				// Those are the only components which will be rendered/updated during this
				// scene
				return Arrays.asList(G3Demo.this.profilerIdentifier(), "background-clock", "main-menu-background", "play-button", "settings-button", "exit-button", "music-checkbox", "stars-background");
			}

			@Override
			public void present() {
				// Play some audio
				if (((Checkbox) Components.get("music-checkbox")).isChecked()) {
					Audios.loop("background", -1, ((Slider) Components.get("main-menu-music-volume")).getValue() / 100.0F);
				}
			}

			@Override
			public void remove() {
				// Stop the audio
				Audios.stopAll();
			}

			@Override
			public synchronized void update(final double delta, final Object... args) {
				Registry.parameterize("stars-background", args);
				super.update(delta, args);
			}

			@Override
			public synchronized void render(final Screen screen) {
				super.render(screen);
			}
		};
		this.mainGameScene = new Scene(this) {
			@Override
			public void update(final double delta, final Object... args) {
				Registry.parameterize("mouse-position-indicator", this.game.mouse().getX(), this.game.mouse().getY());
				super.update(delta, args);
				// Scene specific keyboard/mouse handling
//				if (G3Demo.this.keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
//					Scenes.setCurrent("main-menu");
//				}
			}

			@Override
			public synchronized void render(final Screen screen) {
				super.render(screen);
			}

			@Override
			public List<String> sceneObjects() {
				return Arrays.asList("main-game-background", "instructions-label", "mouse-position-indicator");
			}

			@Override
			public void present() {
				/**
				 * If you need to ignore a specific component (for special updates), you do it
				 * here.
				 */
				Scenes.transition("fade-transition");
			}

			@Override
			public void remove() {
				Scenes.transition("fade-transition");
			}
		};
		this.settingsScene = new Scene(this) {
			@Override
			public List<String> sceneObjects() {
				return Arrays.asList("main-menu-music-volume", "back-to-main-menu-button");
			}

			@Override
			public void remove() {
				Scenes.transition("fade-transition");
			}

			@Override
			public void present() {
				Scenes.transition("fade-transition");
			}
		};
	}

	@Override
	public void loadResources() {
		// Register some audio and some fonts
		Audios.register("background", "res/audio/background.wav");
		Fonts.register("lunchds", "res/font/lunchds.ttf");
		Images.registerDirectory("ashe_%d", "res/sprite/ashe/");
		Images.registerDirectory("campfire_%d", "res/sprite/campfire");
	}

	@Override
	public void registerGameObjects() {
		Registry.set("campfire-animation", new RandomizedAnimation(this, (G3Demo.WIDTH / 2) - 70, G3Demo.HEIGHT - 140, Sprite.fromImages(this, "campfire_%d", Images.getUniqueID("campfire")), new int[] { 8, 10, 12 }));
		Registry.set("smoke-particles", new Particles(this, G3Demo.WIDTH / 2, (G3Demo.HEIGHT / 2) + (G3Demo.HEIGHT / 3) + 5, 25, 40, 10, Sprite.fromImages(this, "ashe_%d", Images.getUniqueID("ashe")), Vector2.randomVectors(10, -1, 1, 0, -2), 1, 8));
		Registry.set("stars-background", new Particles(this, G3Demo.WIDTH - 100, 0, 10, G3Demo.WIDTH + 10, 500, Sprite.fromImages(this, "ashe_%d", Images.getUniqueID("ashe")), Vector2.randomVectors(500, -1, -1, 1, 1), 5, 4));
		Registry.set("fade-transition", new FadeTransition(this, 300, Color.BLACK));
	}

	@Override
	public void registerScenes() {
		// Register all the scenes in advance to quickly retrieve them later
		Scenes.register("main-menu", this.mainMenuScene);
		Scenes.register("main-game", this.mainGameScene);
		Scenes.register("settings", this.settingsScene);
	}

	@Override
	public void postCreate() {
		super.postCreate();
		// Set the current scene
		Scenes.switchTo("main-menu");
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
	}

	@Override
	public void createComponents() {
		this.buttonProps = new VisualStringProperties(Fonts.get("lunchds", 14), Color.PINK);
		// Register all the components here once and for all then manage them through
		// scenes switching
		Components.register("main-menu-background", new GSolidColorBackground(this, Screen.DEFAULT_ORIGIN_X, Screen.DEFAULT_ORIGIN_Y, G3Demo.WIDTH, G3Demo.HEIGHT, Color.BLACK));
		Components.register("play-button", new GStraightEdgesButton(this, (G3Demo.WIDTH / 2) - (G3Demo.B_WIDTH / 2), (G3Demo.HEIGHT / 2) - 150, G3Demo.B_WIDTH, G3Demo.B_HEIGHT, new VisualString("Play!", this.buttonProps), Color.BLACK, Color.DARK_GRAY));
		Components.register("settings-button", new GStraightEdgesButton(this, (G3Demo.WIDTH / 2) - (G3Demo.B_WIDTH / 2), (G3Demo.HEIGHT / 2) - 50, G3Demo.B_WIDTH, G3Demo.B_HEIGHT, new VisualString("Settings", this.buttonProps), Color.BLACK, Color.DARK_GRAY));
		Components.register("exit-button", new GStraightEdgesButton(this, (G3Demo.WIDTH / 2) - (G3Demo.B_WIDTH / 2), (G3Demo.HEIGHT / 2) + 50, G3Demo.B_WIDTH, G3Demo.B_HEIGHT, new VisualString("Exit...", this.buttonProps), Color.BLACK, Color.DARK_GRAY));
		Components.register("main-game-background", new GSolidColorBackground(this, Screen.DEFAULT_ORIGIN_X, Screen.DEFAULT_ORIGIN_Y, G3Demo.WIDTH, G3Demo.HEIGHT, Color.PINK));
		Components.register("instructions-label", new GLabel(this, Screen.DEFAULT_ORIGIN_X, (G3Demo.HEIGHT / 2) - 50, G3Demo.WIDTH - 1, 100, new VisualString("Press escape to return to the main menu", this.buttonProps.build(28).build(Color.BLACK))));
		Components.register("main-menu-music-volume", new GSlider(this, (G3Demo.WIDTH / 2) - 150, (G3Demo.HEIGHT / 2) - 100, 300, 20, 6, 6, 0, 100, 50, Color.GRAY, new VisualString("Main menu music (%.2f%%)", Color.PINK, Fonts.get("lunchds", 14)), ComponentLabelPosition.TOP));
		Components.register("back-to-main-menu-button", new GStraightEdgesButton(this, (G3Demo.WIDTH / 2) - (G3Demo.B_WIDTH / 2), G3Demo.HEIGHT - (2 * G3Demo.B_HEIGHT), G3Demo.B_WIDTH, G3Demo.B_HEIGHT, new VisualString("Back", this.buttonProps), Color.BLACK, Color.DARK_GRAY));
		Components.register("music-checkbox", new GCheckbox(this, G3Demo.WIDTH - 90, G3Demo.HEIGHT - 45, 20, 20, new VisualString("Music", Color.PINK, Fonts.get("lunchds", 14)), Color.GRAY, Color.DARK_GRAY, Color.PINK.darker(), ComponentLabelPosition.RIGHT));
		Components.register("mouse-position-indicator", new GLabel(this, G3Demo.WIDTH - 175, G3Demo.HEIGHT - 40, 100, 30, new VisualString("Mouse position: (%d ; %d)", this.buttonProps.build(14).build(Color.BLACK))));
		// Button actions
		((Button) Components.get("play-button")).setActionForState(ComponentState.ACTIVE, args -> Scenes.switchTo("main-game"));
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
		Binds.registerBind("main-game", Keyboard.KEY_ESCAPE, Keyboard.KeyState.DOWN, args -> Scenes.switchTo("main-menu"));
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
		Logger.setDebuggingState(DebuggingType.CLASSIC, true);
		Logger.setDebuggingState(DebuggingType.DEVELOPMENT, true);
		new G3Demo().start();
	}
}
