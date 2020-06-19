package com.skanderj.gingerbread3;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import com.skanderj.gingerbread3.animation.RandomOrderAnimation;
import com.skanderj.gingerbread3.audio.AudioManager;
import com.skanderj.gingerbread3.component.Button;
import com.skanderj.gingerbread3.component.Checkbox;
import com.skanderj.gingerbread3.component.ComponentLabelPosition;
import com.skanderj.gingerbread3.component.ComponentManager;
import com.skanderj.gingerbread3.component.ComponentState;
import com.skanderj.gingerbread3.component.Slider;
import com.skanderj.gingerbread3.component.boilerplates.G3Checkbox;
import com.skanderj.gingerbread3.component.boilerplates.G3Label;
import com.skanderj.gingerbread3.component.boilerplates.G3Slider;
import com.skanderj.gingerbread3.component.boilerplates.G3SolidColorBackground;
import com.skanderj.gingerbread3.component.boilerplates.G3StraightEdgesButton;
import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.display.GraphicsWrapper;
import com.skanderj.gingerbread3.input.Keyboard;
import com.skanderj.gingerbread3.io.FontManager;
import com.skanderj.gingerbread3.io.ImageManager;
import com.skanderj.gingerbread3.logging.Logger;
import com.skanderj.gingerbread3.logging.Logger.DebuggingType;
import com.skanderj.gingerbread3.math.Vector2;
import com.skanderj.gingerbread3.particle.ParticleManager;
import com.skanderj.gingerbread3.scene.Scene;
import com.skanderj.gingerbread3.scene.SceneManager;
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
	public static final int WIDTH = 1400, HEIGHT = (G3Demo.WIDTH / 16) * 9, BUFFERS = 3;

	// Constants for button until I implements a better system (how? I don't
	// fucking have a clue)
	public static final int B_WIDTH = 100, B_HEIGHT = (100 / 16) * 9;
	private VisualStringProperties buttonProps;

	// Game scenes
	private final Scene mainMenuScene;
	private final Scene mainGameScene;
	private final Scene settingsScene;

	public G3Demo() {
		super(G3Demo.IDENTIFIER, G3Demo.REFRESH_RATE, G3Demo.TITLE, G3Demo.WIDTH, G3Demo.HEIGHT, G3Demo.BUFFERS);
		this.mainMenuScene = new Scene(this) {
			@Override
			public List<String> sceneObjects() {
				// Those are the only components which will be rendered/updated during this
				// scene
				return Arrays.asList(G3Demo.this.refreshMarker(), "background-clock", "main-menu-background", "play-button", "settings-button", "exit-button", "music-checkbox", "stars-background");
			}

			@Override
			public void present() {
				// Play some audio
				if (((Checkbox) ComponentManager.get("music-checkbox")).isChecked()) {
					AudioManager.loop("background", -1, ((Slider) ComponentManager.get("main-menu-music-volume")).getValue() / 100.0F);
				}
			}

			@Override
			public void remove() {
				// Stop the audio
				AudioManager.stopAll();
			}

			@Override
			public synchronized void update(final double delta, final Object... args) {
				Registry.parameterize("stars-background", args);
				super.update(delta, args);
			}

			@Override
			public synchronized void render(final GraphicsWrapper graphics) {
				super.render(graphics);
			}
		};
		this.mainGameScene = new Scene(this) {
			@Override
			public void update(final double delta, final Object... args) {
				Registry.parameterize("mouse-position-indicator", this.game.getMouse().getX(), this.game.getMouse().getY());
				super.update(delta, args);
				// Scene specific keyboard/mouse handling
				if (G3Demo.this.keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
					SceneManager.setCurrent("main-menu");
				}
			}

			@Override
			public synchronized void render(final GraphicsWrapper graphics) {
				super.render(graphics);
			}

			@Override
			public List<String> sceneObjects() {
				return Arrays.asList(G3Demo.this.refreshMarker(), "main-game-background", "instructions-label", "mouse-position-indicator");
			}

			@Override
			public void present() {
				/**
				 * If you need to ignore a specific component (for special updates), you do it
				 * here.
				 */
				SceneManager.transition("fade-transition");
			}

			@Override
			public void remove() {
				SceneManager.transition("fade-transition");
			}
		};
		this.settingsScene = new Scene(this) {
			@Override
			public List<String> sceneObjects() {
				return Arrays.asList(G3Demo.this.refreshMarker(), "main-menu-music-volume", "back-to-main-menu-button");
			}

			@Override
			public void remove() {
				SceneManager.transition("fade-transition");
			}

			@Override
			public void present() {
				SceneManager.transition("fade-transition");
			}
		};
	}

	@Override
	public void loadResources() {
		// Register some audio and some fonts
		AudioManager.register("background", "res/audio/background.wav");
		FontManager.register("lunchds", "res/font/lunchds.ttf");
		ImageManager.registerDirectory("ashe_%d", "res/sprite/ashe/");
		ImageManager.registerDirectory("campfire_%d", "res/sprite/campfire");
	}

	@Override
	public void registerGameObjects() {
		Registry.set("campfire-animation", new RandomOrderAnimation(this, (G3Demo.WIDTH / 2) - 70, G3Demo.HEIGHT - 140, Sprite.fromImages(this, "campfire_%d", ImageManager.getUniqueID("campfire")), new int[] { 8, 10, 12 }));
		Registry.set("smoke-particles", new ParticleManager(this, G3Demo.WIDTH / 2, (G3Demo.HEIGHT / 2) + (G3Demo.HEIGHT / 3) + 5, 25, 40, 10, Sprite.fromImages(this, "ashe_%d", ImageManager.getUniqueID("ashe")), Vector2.randomVectors(10, -1, 1, 0, -2), 1, 8));
		Registry.set("stars-background", new ParticleManager(this, G3Demo.WIDTH - 100, 0, 10, G3Demo.WIDTH + 10, 500, Sprite.fromImages(this, "ashe_%d", ImageManager.getUniqueID("ashe")), Vector2.randomVectors(500, -1, -1, 1, 1), 5, 4));
		Registry.set("fade-transition", new FadeTransition(this, 300, Color.BLACK));
	}

	@Override
	public void registerScenes() {
		// Register all the scenes in advance to quickly retrieve them later
		SceneManager.register("main-menu", this.mainMenuScene);
		SceneManager.register("main-game", this.mainGameScene);
		SceneManager.register("settings", this.settingsScene);
	}

	@Override
	public void postCreate() {
		super.postCreate();
		// Set the current scene
		SceneManager.setCurrent("main-menu");
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
	}

	@Override
	public void createComponents() {
		this.buttonProps = new VisualStringProperties(FontManager.get("lunchds", 14), Color.PINK);
		// Register all the components here once and for all then manage them through
		// scenes switching
		ComponentManager.register("main-menu-background", new G3SolidColorBackground(this, GraphicsWrapper.DEFAULT_ORIGIN_X, GraphicsWrapper.DEFAULT_ORIGIN_Y, G3Demo.WIDTH, G3Demo.HEIGHT, Color.BLACK));
		ComponentManager.register("play-button", new G3StraightEdgesButton(this, (G3Demo.WIDTH / 2) - (G3Demo.B_WIDTH / 2), (G3Demo.HEIGHT / 2) - 150, G3Demo.B_WIDTH, G3Demo.B_HEIGHT, new VisualString("Play!", this.buttonProps), Color.BLACK, Color.DARK_GRAY));
		ComponentManager.register("settings-button", new G3StraightEdgesButton(this, (G3Demo.WIDTH / 2) - (G3Demo.B_WIDTH / 2), (G3Demo.HEIGHT / 2) - 50, G3Demo.B_WIDTH, G3Demo.B_HEIGHT, new VisualString("Settings", this.buttonProps), Color.BLACK, Color.DARK_GRAY));
		ComponentManager.register("exit-button", new G3StraightEdgesButton(this, (G3Demo.WIDTH / 2) - (G3Demo.B_WIDTH / 2), (G3Demo.HEIGHT / 2) + 50, G3Demo.B_WIDTH, G3Demo.B_HEIGHT, new VisualString("Exit...", this.buttonProps), Color.BLACK, Color.DARK_GRAY));
		ComponentManager.register("main-game-background", new G3SolidColorBackground(this, GraphicsWrapper.DEFAULT_ORIGIN_X, GraphicsWrapper.DEFAULT_ORIGIN_Y, G3Demo.WIDTH, G3Demo.HEIGHT, Color.PINK));
		ComponentManager.register("instructions-label", new G3Label(this, GraphicsWrapper.DEFAULT_ORIGIN_X, (G3Demo.HEIGHT / 2) - 50, G3Demo.WIDTH - 1, 100, new VisualString("Press escape to return to the main menu", this.buttonProps.build(28).build(Color.BLACK))));
		ComponentManager.register("main-menu-music-volume", new G3Slider(this, (G3Demo.WIDTH / 2) - 150, (G3Demo.HEIGHT / 2) - 100, 300, 20, 6, 6, 0, 100, 50, Color.GRAY, new VisualString("Main menu music (%.2f%%)", Color.PINK, FontManager.get("lunchds", 14)), ComponentLabelPosition.TOP));
		ComponentManager.register("back-to-main-menu-button", new G3StraightEdgesButton(this, (G3Demo.WIDTH / 2) - (G3Demo.B_WIDTH / 2), G3Demo.HEIGHT - (2 * G3Demo.B_HEIGHT), G3Demo.B_WIDTH, G3Demo.B_HEIGHT, new VisualString("Back", this.buttonProps), Color.BLACK, Color.DARK_GRAY));
		ComponentManager.register("music-checkbox", new G3Checkbox(this, G3Demo.WIDTH - 90, G3Demo.HEIGHT - 45, 20, 20, new VisualString("Music", Color.PINK, FontManager.get("lunchds", 14)), Color.GRAY, Color.DARK_GRAY, Color.PINK.darker(), ComponentLabelPosition.RIGHT));
		ComponentManager.register("mouse-position-indicator", new G3Label(this, G3Demo.WIDTH - 175, G3Demo.HEIGHT - 40, 100, 30, new VisualString("Mouse position: (%d ; %d)", this.buttonProps.build(14).build(Color.BLACK))));
		// Button actions
		((Button) ComponentManager.get("play-button")).setActionForState(ComponentState.ON_CLICK, args -> SceneManager.setCurrent("main-game"));
		((Button) ComponentManager.get("settings-button")).setActionForState(ComponentState.ON_CLICK, args -> SceneManager.setCurrent("settings"));
		((Button) ComponentManager.get("back-to-main-menu-button")).setActionForState(ComponentState.ON_CLICK, args -> SceneManager.setCurrent("main-menu"));
		((Button) ComponentManager.get("exit-button")).setActionForState(ComponentState.ON_CLICK, args -> this.stop());
		((Checkbox) ComponentManager.get("music-checkbox")).setOnSwitchAction(args -> {
			final boolean state = (boolean) args[0];
			if (state) {
				AudioManager.stop("background");
				AudioManager.loop("background", -1, ((Slider) ComponentManager.get("main-menu-music-volume")).getValue() / 100.0F);
			} else {
				AudioManager.stop("background");
			}
		});
	}

	@Override
	public synchronized void update(final double delta) {
		// VERY IMPORTANT TO CALL
		super.update(delta);
		// Scene-independent updating --- not recommended, but flexibility
	}

	@Override
	public synchronized void render(final GraphicsWrapper graphics) {
		// Clear the screen here -- ideally done through a black background component
		graphics.clear(this.window, Color.BLACK);
		// VERY IMPORTANT TO CALL
		super.render(graphics);
		// Scene-independent rendering --- not recommended, but flexibility
	}

	public static void main(final String[] args) {
		// Set debugging messages
		Logger.setDebuggingState(DebuggingType.CLASSIC, true);
		Logger.setDebuggingState(DebuggingType.DEVELOPMENT, true);
		new G3Demo().start();
	}
}
