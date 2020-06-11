package com.skanderj.gingerbread3.test;

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

public class Testing extends Game {
	public static final String IDENTIFIER = "g-3t", TITLE = "Gingerbread-3 [TESTING]";
	public static final double REFRESH_RATE = 60.0D;
	public static final int WIDTH = 1200, HEIGHT = Testing.WIDTH / 16 * 9, BUFFERS = 3;

	public static final int B_WIDTH = 100, B_HEIGHT = 100 / 16 * 9;

	public static final Scene mainMenuScene = new Scene() {
		@Override
		public List<String> sceneComponents() {
			return Arrays.asList("main-menu-background", "play-button", "settings-button", "exit-button");
		}
	};

	public static final Scene mainGameScene = new Scene() {
		@Override
		public void update(double delta, Keyboard keyboard, Mouse mouse, Object... args) {
			super.update(delta, keyboard, mouse, args);
			if (keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				SceneManager.setCurrentScene("main-menu");
			}
		}

		@Override
		public List<String> sceneComponents() {
			return Arrays.asList("main-game-background", "instructions-label");
		}
	};

	public Testing() {
		super(Testing.IDENTIFIER, Testing.REFRESH_RATE, Testing.TITLE, Testing.WIDTH, Testing.HEIGHT, Testing.BUFFERS);
		this.keyboard = new Keyboard();
		this.mouse = new Mouse();
	}

	@Override
	public void loadResources() {
		AudioManager.registerAudio("silhouette-kana_boon", "res/audio/silhouette.wav");
		FontManager.registerFont("lunchds", "res/font/lunchds.ttf");
	}

	@Override
	public void postCreate() {
		super.postCreate();
		this.displayRefreshRate = false;
		SceneManager.registerScene("main-menu", Testing.mainMenuScene);
		SceneManager.registerScene("main-game", Testing.mainGameScene);
		AudioManager.loopAudio("silhouette-kana_boon", -1);
		SceneManager.setCurrentScene("main-menu");
	}

	@Override
	public void registerComponents() {
		ComponentManager.addComponent("main-menu-background", new G3SolidBackground(GraphicsUtilities.DEFAULT_ORIGIN_X, GraphicsUtilities.DEFAULT_ORIGIN_Y, Testing.WIDTH, Testing.HEIGHT, Color.BLACK));
		ComponentManager.addComponent("play-button", new G3StraightEdgesButton(Testing.WIDTH / 2 - Testing.B_WIDTH / 2, Testing.HEIGHT / 2 - 150, Testing.B_WIDTH, Testing.B_HEIGHT, new VisualString("Play!", Color.RED, FontManager.getFont("lunchds", 14)), Color.BLACK, Color.DARK_GRAY));
		ComponentManager.addComponent("settings-button", new G3StraightEdgesButton(Testing.WIDTH / 2 - Testing.B_WIDTH / 2, Testing.HEIGHT / 2 - 50, Testing.B_WIDTH, Testing.B_HEIGHT, new VisualString("Settings", Color.RED, FontManager.getFont("lunchds", 14)), Color.BLACK, Color.DARK_GRAY));
		ComponentManager.addComponent("exit-button", new G3StraightEdgesButton(Testing.WIDTH / 2 - Testing.B_WIDTH / 2, Testing.HEIGHT / 2 + 50, Testing.B_WIDTH, Testing.B_HEIGHT, new VisualString("Exit...", Color.RED, FontManager.getFont("lunchds", 14)), Color.BLACK, Color.DARK_GRAY));
		((Button) ComponentManager.getComponent("play-button")).setButtonAction(ButtonState.ON_CLICK, args -> SceneManager.setCurrentScene("main-game"));
		ComponentManager.addComponent("main-game-background", new G3SolidBackground(GraphicsUtilities.DEFAULT_ORIGIN_X, GraphicsUtilities.DEFAULT_ORIGIN_Y, Testing.WIDTH, Testing.HEIGHT, Color.PINK));
		ComponentManager.addComponent("instructions-label", new G3Label(0, Testing.HEIGHT / 2 - 50, Testing.WIDTH - 1, 100, new VisualString("Press escape to return to the main menu", new VisualStringProperties(FontManager.getFont("lunchds", 28), Color.BLACK))));
	}

	@Override
	public void update(double delta) {
		SceneManager.updateScene(this, delta);
	}

	@Override
	public void render(Graphics2D graphics) {
		GraphicsUtilities.clear(this.window, graphics, Color.BLACK);
		SceneManager.renderScene(this, graphics);
	}

	public static void main(String[] args) {
		Logger.setDebuggingState(DebuggingType.CLASSIC, true);
		Logger.setDebuggingState(DebuggingType.DEVELOPMENT, true);
		new Testing().start();
	}
}
