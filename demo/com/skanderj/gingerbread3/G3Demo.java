package com.skanderj.gingerbread3;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import com.skanderj.gingerbread3.audio.Audios;
import com.skanderj.gingerbread3.component.Button;
import com.skanderj.gingerbread3.component.Checkbox;
import com.skanderj.gingerbread3.component.ComponentLabelPosition;
import com.skanderj.gingerbread3.component.ComponentState;
import com.skanderj.gingerbread3.component.Components;
import com.skanderj.gingerbread3.component.Slider;
import com.skanderj.gingerbread3.component.boilerplates.GBackgroundColor;
import com.skanderj.gingerbread3.component.boilerplates.GButton;
import com.skanderj.gingerbread3.component.boilerplates.GCheckbox;
import com.skanderj.gingerbread3.component.boilerplates.GSlider;
import com.skanderj.gingerbread3.component.boilerplates.GText;
import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.input.Binds;
import com.skanderj.gingerbread3.input.Keyboard;
import com.skanderj.gingerbread3.input.Keyboard.KeyState;
import com.skanderj.gingerbread3.lighting.boilerplates.OmnidirectionalLighting;
import com.skanderj.gingerbread3.logging.Logger;
import com.skanderj.gingerbread3.logging.Logger.DebuggingType;
import com.skanderj.gingerbread3.math.Vector2;
import com.skanderj.gingerbread3.particle.Particles;
import com.skanderj.gingerbread3.resources.Fonts;
import com.skanderj.gingerbread3.resources.Images;
import com.skanderj.gingerbread3.scene.Scene;
import com.skanderj.gingerbread3.scene.Scenes;
import com.skanderj.gingerbread3.sprite.Sprite;
import com.skanderj.gingerbread3.transition.boilerplates.FadeTransition;
import com.skanderj.gingerbread3.util.Label;
import com.skanderj.gingerbread3.util.LabelProperties;
import com.skanderj.gingerbread3.util.Utilities;

/**
 * Demo class for the G3 engine.
 *
 * @author Skander
 *
 */
public class G3Demo extends Application {
	/**
	 * Application constants, you don't necessarily need them as constants but it's
	 * pretty nice.
	 */
	public static final String IDENTIFIER = "g3-d", TITLE = "Gingerbread-3 [DEMO]";
	public static final double REFRESH_RATE = 60.0D;
	public static final int WIDTH = 1200, HEIGHT = (G3Demo.WIDTH / 16) * 9, BUFFERS = 2;

	public static final int BACKGROUND_PARTICLES = 150;

	// Constants for button until I implements a better system (how? I don't
	// fucking have a clue)
	public static final int BUTTONS_WIDTH = 120, BUTTONS_HEIGHT = (100 / 16) * 10;
	private LabelProperties buttonProps;

	// Application scenes
	private final Scene mainMenuScene, mainGameScene, settingsScene;

	public G3Demo() {
		super(G3Demo.IDENTIFIER, G3Demo.REFRESH_RATE, G3Demo.TITLE, G3Demo.WIDTH, G3Demo.HEIGHT, G3Demo.BUFFERS, Keyboard.AZERTY);
		this.mainMenuScene = new Scene(this) {
			@Override
			public List<String> sceneObjects() {
				// Those are the only components which will be rendered/updated during this
				// scene
				return Arrays.asList("pink-source", "title", "play-button", "settings-button", "exit-button", "music-checkbox", "flowers");
			}

			@Override
			public void enter() {
				// Play some audio
				if (((Checkbox) Components.get("music-checkbox")).isChecked()) {
					Audios.loop("background", -1, ((Slider) Components.get("main-menu-music-volume")).value() / 100.0F);
				}
			}

			@Override
			public void exit() {
				// Stop the audio
				Audios.stopAll();
			}

			@Override
			public synchronized void update() {
				super.update();
			}

			@Override
			public synchronized void render(final Screen screen) {
				super.render(screen);
			}
		};
		this.mainGameScene = new Scene(this) {
			@Override
			public void update() {
				super.update();
				Registry.parameterize("mouse-position-indicator", new String[] { "mouse-x", "mouse-y" }, new Object[] { this.application.mouse().getX(), this.application.mouse().getY() });
			}

			@Override
			public synchronized void render(final Screen screen) {
				super.render(screen);
			}

			@Override
			public List<String> sceneObjects() {
				return Arrays.asList("pink-source", "instructions-label", "mouse-position-indicator");
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
			public synchronized void update() {
				super.update();
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
		Registry.register("pink-source", new OmnidirectionalLighting(this, new Color(Color.PINK.getRed(), Color.PINK.getGreen(), Color.PINK.getBlue(), 255), G3Demo.WIDTH / 2, G3Demo.HEIGHT / 2, G3Demo.WIDTH + (G3Demo.WIDTH / 2), Priority.REGULAR));
		final Sprite[] ashes = Sprite.fromImages(this, "ashe_%d", Images.getCollectionByID("ashe"));
		Registry.register("flowers", new Particles(this, G3Demo.WIDTH / 2, 0, 10, 2 * G3Demo.HEIGHT, G3Demo.BACKGROUND_PARTICLES, ashes, Vector2.randomVectors(G3Demo.BACKGROUND_PARTICLES, -1, 1, 1, 1), 5, 2));
		Registry.register("fade-transition", new FadeTransition(this, 60, Color.BLACK));
	}

	@Override
	public void registerScenes() {
		// Register all the scenes in advance to quickly retrieve them later
		Scenes.register("main-menu", this.mainMenuScene);
		Scenes.register("in-game", this.mainGameScene);
		Scenes.register("settings", this.settingsScene);
		// Set the current scene
		Scenes.switchTo("main-menu");
	}

	@Override
	public void postCreate() {
		super.postCreate();
		// Enable profiler
		this.useProfiler();
		this.window.enableOpenGL();
	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
	}

	/**
	 * Register all the components here once and for all then manage them through
	 * scenes switching
	 **/
	@Override
	public void createComponents() {
		this.buttonProps = new LabelProperties(Fonts.get("lunchds", 16), Color.WHITE);
		// Backgrounds
		{
			Components.register("main-menu-background", new GBackgroundColor(this, Screen.DEFAULT_ORIGIN_X, Screen.DEFAULT_ORIGIN_Y, G3Demo.WIDTH, G3Demo.HEIGHT, Color.WHITE));
			Components.register("in-game-background", new GBackgroundColor(this, Screen.DEFAULT_ORIGIN_X, Screen.DEFAULT_ORIGIN_Y, G3Demo.WIDTH, G3Demo.HEIGHT, Color.PINK));
		}
		// Labels
		{
			Components.register("title", new GText(this, 0, 0, G3Demo.WIDTH, G3Demo.HEIGHT / 3, new Label("G3DEMO", new Color(0f, 0f, 0f, 0.75f), Fonts.get("lunchds", 72))));
			Components.register("instructions-label", new GText(this, Screen.DEFAULT_ORIGIN_X, (G3Demo.HEIGHT / 2) - 50, G3Demo.WIDTH - 1, 100, new Label("Press escape to return to the main menu", this.buttonProps.build(28).build(Color.BLACK))));
			Components.register("mouse-position-indicator", new GText(this, G3Demo.WIDTH - 175, G3Demo.HEIGHT - 40, 100, 30, new Label("Mouse position: (%d ; %d)", this.buttonProps.build(14).build(Color.BLACK))));
		}
		// Main scene
		{
			Components.register("music-checkbox", new GCheckbox(this, G3Demo.WIDTH - 90, G3Demo.HEIGHT - 45, 20, 20, new Label("Music", Color.BLACK, Fonts.get("lunchds", 14)), Color.GRAY, Color.DARK_GRAY, Color.PINK.darker(), ComponentLabelPosition.RIGHT));
		}
		// Settings scene
		{
			Components.register("main-menu-music-volume", new GSlider(this, (G3Demo.WIDTH / 2) - 150, (G3Demo.HEIGHT / 2) - 100, 300, 20, 6, 6, 0, 100, 50, Color.GRAY, new Label("Main menu music (%.2f%%)", Color.PINK, Fonts.get("lunchds", 14)), ComponentLabelPosition.TOP));
		}
		// Buttons
		{
			// Back to main menu button
			{
				Components.register("back-to-main-menu-button", new GButton(this, (G3Demo.WIDTH / 2) - (G3Demo.BUTTONS_WIDTH / 2), G3Demo.HEIGHT - (2 * G3Demo.BUTTONS_HEIGHT), G3Demo.BUTTONS_WIDTH, G3Demo.BUTTONS_HEIGHT, new Label("Back", this.buttonProps.build(Color.BLACK)), Color.BLACK, Color.DARK_GRAY, 12));
				((Button) Components.get("back-to-main-menu-button")).mapActionToState(ComponentState.IDLE, object -> {
					final GButton button = (GButton) object;
					button.setBackgroundColor(new Color(1f, 1f, 1f, 0.3f));
					button.label().color = Color.BLACK;
				});
				((Button) Components.get("back-to-main-menu-button")).mapActionToState(ComponentState.HOVERED, object -> ((GButton) object).setBackgroundColor(new Color(1f, 1f, 1f, 0.7f)));
				((Button) Components.get("back-to-main-menu-button")).mapActionToState(ComponentState.HELD, object -> ((GButton) object).label().color = Color.PINK);
				((Button) Components.get("back-to-main-menu-button")).mapActionToState(ComponentState.ACTIVE, object -> Scenes.switchTo("main-menu"));
			}
			((Checkbox) Components.get("music-checkbox")).onSwitch(object -> {
				final Checkbox checkbox = (Checkbox) object;
				final boolean state = checkbox.isChecked();
				if (state) {
					Audios.stop("background");
					Audios.loop("background", -1, ((Slider) Components.get("main-menu-music-volume")).value() / 100.0F);
				} else {
					Audios.stop("background");
				}
			});
			{
				// Play button
				{
					Components.register("play-button", new GButton(this, (G3Demo.WIDTH / 2) - (G3Demo.BUTTONS_WIDTH / 2), (G3Demo.HEIGHT / 2) + 50, G3Demo.BUTTONS_WIDTH, G3Demo.BUTTONS_HEIGHT, new Label("Play!", this.buttonProps), new Color(0f, 0f, 0f, 0.7f), Color.GRAY, 12));
					((Button) Components.get("play-button")).mapActionToState(ComponentState.IDLE, object -> {
						final GButton button = (GButton) object;
						button.setBackgroundColor(new Color(0f, 0f, 0f, 0.7f));
						button.label().color = Color.WHITE;
					});
					((Button) Components.get("play-button")).mapActionToState(ComponentState.HOVERED, object -> ((GButton) object).setBackgroundColor(new Color(0f, 0f, 0f, 0.9f)));
					((Button) Components.get("play-button")).mapActionToState(ComponentState.HELD, object -> ((GButton) object).label().color = Color.PINK);
					((Button) Components.get("play-button")).mapActionToState(ComponentState.ACTIVE, object -> Scenes.switchTo("in-game"));
				}
				// Settings button
				{
					Components.register("settings-button", new GButton(this, (G3Demo.WIDTH / 2) - (G3Demo.BUTTONS_WIDTH / 2), (G3Demo.HEIGHT / 2) + 130, G3Demo.BUTTONS_WIDTH, G3Demo.BUTTONS_HEIGHT, new Label("Settings", this.buttonProps), new Color(0f, 0f, 0f, 0.7f), Color.GRAY, 12));
					((Button) Components.get("settings-button")).mapActionToState(ComponentState.IDLE, object -> {
						final GButton button = (GButton) object;
						button.setBackgroundColor(new Color(0f, 0f, 0f, 0.7f));
						button.label().color = Color.WHITE;
					});
					((Button) Components.get("settings-button")).mapActionToState(ComponentState.HOVERED, object -> ((GButton) object).setBackgroundColor(new Color(0f, 0f, 0f, 0.9f)));
					((Button) Components.get("settings-button")).mapActionToState(ComponentState.HELD, object -> ((GButton) object).label().color = Color.PINK);
					((Button) Components.get("settings-button")).mapActionToState(ComponentState.ACTIVE, object -> Scenes.switchTo("settings"));
				}
				// Exit button
				{
					Components.register("exit-button", new GButton(this, (G3Demo.WIDTH / 2) - (G3Demo.BUTTONS_WIDTH / 2), (G3Demo.HEIGHT / 2) + 210, G3Demo.BUTTONS_WIDTH, G3Demo.BUTTONS_HEIGHT, new Label("Exit", this.buttonProps), new Color(0f, 0f, 0f, 0.7f), Color.GRAY, 12));
					((Button) Components.get("exit-button")).mapActionToState(ComponentState.IDLE, object -> {
						final GButton button = (GButton) object;
						button.setBackgroundColor(new Color(0f, 0f, 0f, 0.7f));
						button.label().color = Color.WHITE;
					});
					((Button) Components.get("exit-button")).mapActionToState(ComponentState.HOVERED, object -> ((GButton) object).setBackgroundColor(new Color(0f, 0f, 0f, 0.9f)));
					((Button) Components.get("exit-button")).mapActionToState(ComponentState.HELD, object -> ((GButton) object).label().color = Color.PINK);
					((Button) Components.get("exit-button")).mapActionToState(ComponentState.ACTIVE, object -> this.stop());
				}
			}
		}
	}

	@Override
	public void registerBinds() {
		Binds.registerBind("in-game", new Integer[] { Keyboard.KEY_ESCAPE }, new Keyboard.KeyState[] { Keyboard.KeyState.DOWN }, object -> Scenes.switchTo("main-menu"));
		Binds.registerBind("settings", new Integer[] { Keyboard.KEY_ESCAPE }, new Keyboard.KeyState[] { Keyboard.KeyState.DOWN }, object -> Scenes.switchTo("main-menu"));
		Binds.registerBind("main-menu", new Integer[] { Keyboard.KEY_ESCAPE, Keyboard.KEY_SPACE }, new Keyboard.KeyState[] { Keyboard.KeyState.DOWN, Keyboard.KeyState.DOWN }, object -> this.stop());
		Binds.registerBind("*", new Integer[] { Keyboard.KEY_F5 }, new KeyState[] { KeyState.DOWN_IN_CURRENT_FRAME }, object -> this.screenshot("scr/" + Utilities.fileNameCompatibleDateString() + ".png"));
	}

	@Override
	public synchronized void update() {
		// VERY IMPORTANT TO CALL
		super.update();
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
		Logger.setStateForDebuggingType(DebuggingType.CLASSIC, true);
		Logger.setStateForDebuggingType(DebuggingType.DEVELOPMENT, false);
		new G3Demo().start();
	}
}
