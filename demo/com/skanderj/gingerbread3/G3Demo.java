package com.skanderj.gingerbread3;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.List;

import com.skanderj.gingerbread3.animation.Animation;
import com.skanderj.gingerbread3.animation.RandomOrderAnimation;
import com.skanderj.gingerbread3.audio.AudioManager;
import com.skanderj.gingerbread3.component.ComponentLabelPosition;
import com.skanderj.gingerbread3.component.ComponentManager;
import com.skanderj.gingerbread3.component.ComponentState;
import com.skanderj.gingerbread3.component.premade.G3Checkbox;
import com.skanderj.gingerbread3.component.premade.G3Label;
import com.skanderj.gingerbread3.component.premade.G3Slider;
import com.skanderj.gingerbread3.component.premade.G3SolidBackground;
import com.skanderj.gingerbread3.component.premade.G3StraightEdgesButton;
import com.skanderj.gingerbread3.component.unit.Button;
import com.skanderj.gingerbread3.component.unit.Checkbox;
import com.skanderj.gingerbread3.component.unit.Slider;
import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.input.Keyboard;
import com.skanderj.gingerbread3.input.Mouse;
import com.skanderj.gingerbread3.io.FontManager;
import com.skanderj.gingerbread3.io.ImageManager;
import com.skanderj.gingerbread3.log.Logger;
import com.skanderj.gingerbread3.log.Logger.DebuggingType;
import com.skanderj.gingerbread3.math.Vector2D;
import com.skanderj.gingerbread3.particle.ParticleManager;
import com.skanderj.gingerbread3.scene.Scene;
import com.skanderj.gingerbread3.scene.SceneManager;
import com.skanderj.gingerbread3.sprite.Sprite;
import com.skanderj.gingerbread3.util.GraphicsUtilities;
import com.skanderj.gingerbread3.util.Utilities;
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
	public static final double REFRESH_RATE = 60.0D;
	public static final int WIDTH = 1200, HEIGHT = (G3Demo.WIDTH / 16) * 9, BUFFERS = 3;

	// Constants for button until I implements a better system (how? I don't
	// fucking have a clue)
	public static final int B_WIDTH = 100, B_HEIGHT = (100 / 16) * 9;
	private VisualStringProperties buttonProps;

	// Main menu scene - first scene of the game
	public static final Scene mainMenuScene = new Scene() {
		private Animation campfireAnimation;
		private ParticleManager manager;

		@Override
		public List<String> sceneComponents() {
			// Those are the only components which will be rendered/updated during this
			// scene
			return Arrays.asList("main-menu-background", "play-button", "settings-button", "exit-button", "music-checkbox");
		}

		@Override
		public void present() {
			final Sprite[] ashes = Sprite.fromImages("ashe_%d", ImageManager.retrieveImages("ashe"));
			final Sprite[] campfire = Sprite.fromImages("campfire_%d", ImageManager.retrieveImages("campfire"));
			this.campfireAnimation = new RandomOrderAnimation((G3Demo.WIDTH / 2) - 70, G3Demo.HEIGHT - 140, campfire, new int[] { 8, 10, 12 });
			this.manager = new ParticleManager(G3Demo.WIDTH / 2, (G3Demo.HEIGHT / 2) + (G3Demo.HEIGHT / 3) + 5, 25, 40, 50, ashes, Vector2D.randomVectors(50, -1, 1, 0, -2), 1, 8);
			// Play some audio
			if (((Checkbox) ComponentManager.getComponent("music-checkbox")).isChecked()) {
				AudioManager.loopAudio("background", -1, ((Slider) ComponentManager.getComponent("main-menu-music-volume")).getValue() / 100.0F);
			}
		}

		@Override
		public void remove() {
			// Stop the audio
			AudioManager.stopAllAudio();
		}

		@Override
		public synchronized void update(final double delta, final Keyboard keyboard, final Mouse mouse) {
			super.update(delta, keyboard, mouse);
			this.campfireAnimation.update(delta, keyboard, mouse);
			this.manager.update(delta);
		}

		@Override
		public synchronized void render(final com.skanderj.gingerbread3.display.Window window, final Graphics2D graphics) {
			super.render(window, graphics);
			this.campfireAnimation.render(window, graphics);
			this.manager.render(graphics);
		}
	};

	public static final Scene mainGameScene = new Scene() {
		@Override
		public void update(final double delta, final Keyboard keyboard, final Mouse mouse) {
			super.update(delta, keyboard, mouse);
			/**
			 * Don't forget to update your skipped components accordingly.
			 */
			ComponentManager.updateSpecific("mouse-position-indicator", delta, keyboard, mouse, mouse.getX(), mouse.getY());
			// Scene specific keyboard/mouse handling
			if (keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				SceneManager.setCurrentScene("main-menu");
			}
			if (keyboard.isKeyDownInFrame(Keyboard.KEY_SPACE)) {
				AudioManager.playAudio("fart_" + Utilities.randomInteger(0, 4));
			}
		}

		@Override
		public synchronized void render(final com.skanderj.gingerbread3.display.Window window, final Graphics2D graphics) {
			super.render(window, graphics);
			/**
			 * Don't forget to manually render any ignored components.
			 */
			ComponentManager.renderSpecific("mouse-position-indicator", window, graphics);
		}

		@Override
		public List<String> sceneComponents() {
			return Arrays.asList("main-game-background", "instructions-label", "mouse-position-indicator");
		}

		@Override
		public void present() {
			/**
			 * If you need to ignore a specific component (for special updates), you do it
			 * here.
			 */
			ComponentManager.skipComponent("mouse-position-indicator");
		}

		@Override
		public void remove() {
			return;
		}
	};

	public static final Scene settingsScene = new Scene() {
		@Override
		public List<String> sceneComponents() {
			return Arrays.asList("main-menu-music-volume", "back-to-main-menu-button");
		}

		@Override
		public void remove() {
			return;
		}

		@Override
		public void present() {
			return;
		}
	};

	public G3Demo() {
		super(G3Demo.IDENTIFIER, G3Demo.REFRESH_RATE, G3Demo.TITLE, G3Demo.WIDTH, G3Demo.HEIGHT, G3Demo.BUFFERS);
	}

	@Override
	public void loadResources() {
		// Register some audio and some fonts
		AudioManager.registerAudio("background", "res/audio/background.wav");
		FontManager.registerFont("lunchds", "res/font/lunchds.ttf");
		ImageManager.registerAll("ashe_%d", "res/sprite/ashe/");
		ImageManager.registerAll("campfire_%d", "res/sprite/campfire");
	}

	@Override
	public void registerScenes() {
		// Register all the scenes in advance to quickly retrieve them later
		SceneManager.registerScene("main-menu", G3Demo.mainMenuScene);
		SceneManager.registerScene("main-game", G3Demo.mainGameScene);
		SceneManager.registerScene("settings", G3Demo.settingsScene);
	}

	@Override
	public void postCreate() {
		super.postCreate();
		// Part of debugging - should be linked to the debugging constant but I'm lazy
		this.displayRefreshRate = true;
		// Set the current scene
		SceneManager.setCurrentScene("main-menu");
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
	}

	@Override
	public void createComponents() {
		this.buttonProps = new VisualStringProperties(FontManager.getFont("lunchds", 14), Color.PINK);
		// Register all the components here once and for all then manage them through
		// scenes switching
		ComponentManager.addComponent("main-menu-background", new G3SolidBackground(GraphicsUtilities.DEFAULT_ORIGIN_X, GraphicsUtilities.DEFAULT_ORIGIN_Y, G3Demo.WIDTH, G3Demo.HEIGHT, Color.BLACK));
		ComponentManager.addComponent("play-button", new G3StraightEdgesButton((G3Demo.WIDTH / 2) - (G3Demo.B_WIDTH / 2), (G3Demo.HEIGHT / 2) - 150, G3Demo.B_WIDTH, G3Demo.B_HEIGHT, new VisualString("Play!", this.buttonProps), Color.BLACK, Color.DARK_GRAY));
		ComponentManager.addComponent("settings-button", new G3StraightEdgesButton((G3Demo.WIDTH / 2) - (G3Demo.B_WIDTH / 2), (G3Demo.HEIGHT / 2) - 50, G3Demo.B_WIDTH, G3Demo.B_HEIGHT, new VisualString("Settings", this.buttonProps), Color.BLACK, Color.DARK_GRAY));
		ComponentManager.addComponent("exit-button", new G3StraightEdgesButton((G3Demo.WIDTH / 2) - (G3Demo.B_WIDTH / 2), (G3Demo.HEIGHT / 2) + 50, G3Demo.B_WIDTH, G3Demo.B_HEIGHT, new VisualString("Exit...", this.buttonProps), Color.BLACK, Color.DARK_GRAY));
		ComponentManager.addComponent("main-game-background", new G3SolidBackground(GraphicsUtilities.DEFAULT_ORIGIN_X, GraphicsUtilities.DEFAULT_ORIGIN_Y, G3Demo.WIDTH, G3Demo.HEIGHT, Color.PINK));
		ComponentManager.addComponent("instructions-label", new G3Label(GraphicsUtilities.DEFAULT_ORIGIN_X, (G3Demo.HEIGHT / 2) - 50, G3Demo.WIDTH - 1, 100, new VisualString("Press escape to return to the main menu", this.buttonProps.build(28).build(Color.BLACK))));
		ComponentManager.addComponent("main-menu-music-volume", new G3Slider((G3Demo.WIDTH / 2) - 150, (G3Demo.HEIGHT / 2) - 100, 300, 20, 6, 6, 0, 100, 50, Color.GRAY, new VisualString("Main menu music (%.2f%%)", Color.PINK, FontManager.getFont("lunchds", 14)), ComponentLabelPosition.TOP));
		ComponentManager.addComponent("back-to-main-menu-button", new G3StraightEdgesButton((G3Demo.WIDTH / 2) - (G3Demo.B_WIDTH / 2), G3Demo.HEIGHT - (2 * G3Demo.B_HEIGHT), G3Demo.B_WIDTH, G3Demo.B_HEIGHT, new VisualString("Back", this.buttonProps), Color.BLACK, Color.DARK_GRAY));
		ComponentManager.addComponent("music-checkbox", new G3Checkbox(G3Demo.WIDTH - 90, G3Demo.HEIGHT - 45, 20, 20, new VisualString("Music", Color.PINK, FontManager.getFont("lunchds", 14)), Color.GRAY, Color.DARK_GRAY, Color.PINK.darker(), ComponentLabelPosition.RIGHT));
		ComponentManager.addComponent("mouse-position-indicator", new G3Label(G3Demo.WIDTH - 175, G3Demo.HEIGHT - 40, 100, 30, new VisualString("Mouse position: (%d ; %d)", this.buttonProps.build(14).build(Color.BLACK))));
		// Button actions
		((Button) ComponentManager.getComponent("play-button")).setActionForState(ComponentState.ON_CLICK, args -> SceneManager.setCurrentScene("main-game"));
		((Button) ComponentManager.getComponent("settings-button")).setActionForState(ComponentState.ON_CLICK, args -> SceneManager.setCurrentScene("settings"));
		((Button) ComponentManager.getComponent("back-to-main-menu-button")).setActionForState(ComponentState.ON_CLICK, args -> SceneManager.setCurrentScene("main-menu"));
		((Button) ComponentManager.getComponent("exit-button")).setActionForState(ComponentState.ON_CLICK, args -> this.stop());
		((Checkbox) ComponentManager.getComponent("music-checkbox")).setOnSwitchAction(args -> {
			final boolean state = (boolean) args[0];
			if (state) {
				AudioManager.stopAudio("background");
				AudioManager.loopAudio("background", -1, ((Slider) ComponentManager.getComponent("main-menu-music-volume")).getValue() / 100.0F);
			} else {
				AudioManager.stopAudio("background");
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
	public synchronized void render(final Graphics2D graphics) {
		// Clear the screen here -- ideally done through a black background component
		GraphicsUtilities.clear(this.window, graphics, Color.BLACK);
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
