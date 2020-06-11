package com.skanderj.gingerbread3.demo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.List;

import com.skanderj.gingerbead3.audio.AudioManager;
import com.skanderj.gingerbead3.component.Button;
import com.skanderj.gingerbead3.component.Button.ButtonState;
import com.skanderj.gingerbead3.component.ComponentManager;
import com.skanderj.gingerbead3.component.basic.G3Label;
import com.skanderj.gingerbead3.component.basic.G3SolidBackground;
import com.skanderj.gingerbead3.component.basic.G3StraightEdgesButton;
import com.skanderj.gingerbead3.core.Game;
import com.skanderj.gingerbead3.input.Keyboard;
import com.skanderj.gingerbead3.input.Mouse;
import com.skanderj.gingerbead3.io.FontManager;
import com.skanderj.gingerbead3.log.Logger;
import com.skanderj.gingerbead3.log.Logger.DebuggingType;
import com.skanderj.gingerbead3.scene.Scene;
import com.skanderj.gingerbead3.scene.SceneManager;
import com.skanderj.gingerbead3.util.GraphicsUtilities;
import com.skanderj.gingerbead3.util.VisualString;
import com.skanderj.gingerbead3.util.VisualStringProperties;

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
	public static final int WIDTH = 1200, HEIGHT = G3Demo.WIDTH / 16 * 9, BUFFERS = 3;

	// Constants for button sizes until I implements a better system (how? I don't
	// fucking have a clue)
	public static final int B_WIDTH = 100, B_HEIGHT = 100 / 16 * 9;

	// Main menu scene - first scene of the game
	public static final Scene mainMenuScene = new Scene() {
		@Override
		public List<String> sceneComponents() {
			// Those are the only components which will be rendered/updated during this
			// scene
			return Arrays.asList("main-menu-background", "play-button", "settings-button", "exit-button");
		}

		@Override
		public void present() {
			// Play some audio
			AudioManager.loopAudio("silhouette-kana_boon", -1);
		}

		@Override
		public void remove() {
			// Stop the audio
			AudioManager.stopAllAudio();
		}
	};

	public static final Scene mainGameScene = new Scene() {
		@Override
		public void update(double delta, Keyboard keyboard, Mouse mouse, Object... args) {
			super.update(delta, keyboard, mouse, args);
			// Scene specific keyboard/mouse handling
			if (keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				SceneManager.setCurrentScene("main-menu");
			}
		}

		@Override
		public List<String> sceneComponents() {
			return Arrays.asList("main-game-background", "instructions-label");
		}

		@Override
		public void present() {
			return;
		}

		@Override
		public void remove() {
			return;
		}
	};

	public G3Demo() {
		super(G3Demo.IDENTIFIER, G3Demo.REFRESH_RATE, G3Demo.TITLE, G3Demo.WIDTH, G3Demo.HEIGHT, G3Demo.BUFFERS);
		// Initialize the keyboard/mouse and they will get polled and updated
		// automatically
		this.keyboard = new Keyboard();
		this.mouse = new Mouse();
	}

	@Override
	public void loadResources() {
		// Register some audio and some fonts
		AudioManager.registerAudio("silhouette-kana_boon", "res/audio/silhouette.wav");
		FontManager.registerFont("lunchds", "res/font/lunchds.ttf");
	}

	@Override
	public void postCreate() {
		super.postCreate();
		// Part of debugging - should be linked to the debugging constant but I'm lazy
		this.displayRefreshRate = false;
		// Register all the scenes in advance to quickly retrieve them later
		SceneManager.registerScene("main-menu", G3Demo.mainMenuScene);
		SceneManager.registerScene("main-game", G3Demo.mainGameScene);
		// Set the current scene
		SceneManager.setCurrentScene("main-menu");
	}

	@Override
	public void registerComponents() {
		// Register all the components here once and for all then manage them through
		// scenes switching
		ComponentManager.addComponent("main-menu-background", new G3SolidBackground(GraphicsUtilities.DEFAULT_ORIGIN_X, GraphicsUtilities.DEFAULT_ORIGIN_Y, G3Demo.WIDTH, G3Demo.HEIGHT, Color.BLACK));
		ComponentManager.addComponent("play-button", new G3StraightEdgesButton(G3Demo.WIDTH / 2 - G3Demo.B_WIDTH / 2, G3Demo.HEIGHT / 2 - 150, G3Demo.B_WIDTH, G3Demo.B_HEIGHT, new VisualString("Play!", Color.RED, FontManager.getFont("lunchds", 14)), Color.BLACK, Color.DARK_GRAY));
		ComponentManager.addComponent("settings-button", new G3StraightEdgesButton(G3Demo.WIDTH / 2 - G3Demo.B_WIDTH / 2, G3Demo.HEIGHT / 2 - 50, G3Demo.B_WIDTH, G3Demo.B_HEIGHT, new VisualString("Settings", Color.RED, FontManager.getFont("lunchds", 14)), Color.BLACK, Color.DARK_GRAY));
		ComponentManager.addComponent("exit-button", new G3StraightEdgesButton(G3Demo.WIDTH / 2 - G3Demo.B_WIDTH / 2, G3Demo.HEIGHT / 2 + 50, G3Demo.B_WIDTH, G3Demo.B_HEIGHT, new VisualString("Exit...", Color.RED, FontManager.getFont("lunchds", 14)), Color.BLACK, Color.DARK_GRAY));
		((Button) ComponentManager.getComponent("play-button")).setButtonAction(ButtonState.ON_CLICK, args -> SceneManager.setCurrentScene("main-game"));
		ComponentManager.addComponent("main-game-background", new G3SolidBackground(GraphicsUtilities.DEFAULT_ORIGIN_X, GraphicsUtilities.DEFAULT_ORIGIN_Y, G3Demo.WIDTH, G3Demo.HEIGHT, Color.PINK));
		ComponentManager.addComponent("instructions-label", new G3Label(GraphicsUtilities.DEFAULT_ORIGIN_X, G3Demo.HEIGHT / 2 - 50, G3Demo.WIDTH - 1, 100, new VisualString("Press escape to return to the main menu", new VisualStringProperties(FontManager.getFont("lunchds", 28), Color.BLACK))));
	}

	@Override
	public void update(double delta) {
		SceneManager.updateScene(this, delta);
		// Scene-independant updating --- not recommanded, but flexibility
	}

	@Override
	public void render(Graphics2D graphics) {
		GraphicsUtilities.clear(this.window, graphics, Color.BLACK);
		SceneManager.renderScene(this, graphics);
		// Scene-independant rendering --- not recommanded, but flexibility
	}

	public static void main(String[] args) {
		// Set debugging messages
		Logger.setDebuggingState(DebuggingType.CLASSIC, true);
		Logger.setDebuggingState(DebuggingType.DEVELOPMENT, true);
		new G3Demo().start();
	}
}
