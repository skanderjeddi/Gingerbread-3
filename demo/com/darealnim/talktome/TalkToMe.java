package com.darealnim.talktome;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import com.skanderj.gingerbread3.animation.boilerplates.SequentialAnimation;
import com.skanderj.gingerbread3.audio.Audios;
import com.skanderj.gingerbread3.component.advanced.GDialog;
import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.input.Binds;
import com.skanderj.gingerbread3.input.Keyboard;
import com.skanderj.gingerbread3.input.Keyboard.KeyState;
import com.skanderj.gingerbread3.logging.Logger;
import com.skanderj.gingerbread3.logging.Logger.DebuggingType;
import com.skanderj.gingerbread3.math.Vector2;
import com.skanderj.gingerbread3.particle.Particles;
import com.skanderj.gingerbread3.resources.Fonts;
import com.skanderj.gingerbread3.resources.Images;
import com.skanderj.gingerbread3.scene.Scene;
import com.skanderj.gingerbread3.scene.Scenes;
import com.skanderj.gingerbread3.sprite.Sprite;
import com.skanderj.gingerbread3.transition.boilerplates.FadeInTransition;
import com.skanderj.gingerbread3.util.OperatingSystem;
import com.skanderj.gingerbread3.util.Utilities;

public class TalkToMe extends Application {
	public static final String IDENTIFIER = "talktome", TITLE = "Talk to Me";
	public static final double REFRESH_RATE = 144.0D;
	public static final int WIDTH = 1920, HEIGHT = (TalkToMe.WIDTH / 16) * 9, BUFFERS = 2;

	// Application scenes
	private final Scene mainGameScene;

	public TalkToMe() {
		super(TalkToMe.IDENTIFIER, TalkToMe.REFRESH_RATE, TalkToMe.TITLE, TalkToMe.BUFFERS, 0, Keyboard.AZERTY);
		this.mainGameScene = new Scene(this) {
			@Override
			public List<String> sceneObjects() {
				// Those are the only components which will be rendered/updated during this
				// scene
				return Arrays.asList("lena", "city", "winddust", "textbox");
			}

			@Override
			public void in() {
				Audios.loop("anxiety", -1, 1);
			}

			@Override
			public void out() {
				Audios.stopAll();
			}

			@Override
			public String inTransition() {
				return "fade-in-transition";
			}
		};
	}

	@Override
	public void loadResources() {
		// Register some audio and some fonts
		Audios.load("anxiety", "res/music/mus_anxiety.wav");
		Fonts.load("8bit", "res/fonts/fnt_8bit.ttf");
		Images.loadAll("lena_%d", "res/sprites/lena/", "lena_%d.png");
		Images.loadAll("winddust_%d", "res/sprites/winddust");
		// Images.load("city", "res/sprites/spr_city.png");
	}

	@Override
	public void registerGameObjects() {
		final Sprite[] lena_sprites = Sprite.fromImages(this, "lena_%d", 320, 320, Sprite.SCALE_NEAREST_NEIGHBOR, Images.getCollectionByID("lena"));
		final Sprite[] winddust = Sprite.fromImages(this, "winddust_%d", 16, 16, Sprite.SCALE_NEAREST_NEIGHBOR, Images.getCollectionByID("winddust"));
		// Vector2[] accelerations = new Vector2[100];
		// Arrays.fill(accelerations, new Vector2(0.3,0.5));
		// Vector2.randomVectors(100, 0.5, 1, 0.3, -0.3)
		Engine.register("winddust", new Particles(this, -TalkToMe.WIDTH, TalkToMe.HEIGHT / 2, TalkToMe.HEIGHT / 2, TalkToMe.WIDTH * 2, 30, winddust, Vector2.randomVectors(30, 5, 10, 0.4, -0.4), 0, 2));
		final int[] timers = new int[15];
		Arrays.fill(timers, 12);
		Engine.register("lena", new SequentialAnimation(this, 820, 608, lena_sprites, timers));
		Engine.register("fade-transition", new FadeInTransition(this, 2000, Color.BLACK));
		Engine.register("city", Sprite.fromImage(this, "city", "res/sprites/spr_city.png", 1920, 1080, Sprite.SCALE_NEAREST_NEIGHBOR));
		((Sprite) Engine.get("city")).setX(0);
		((Sprite) Engine.get("city")).setY(0);
		Engine.register("textboxsprite", Sprite.fromImage(this, "textboxsprite", "res/sprites/textbox.png", 1024, 256, Sprite.SCALE_NEAREST_NEIGHBOR));
		Engine.register("fade-in-transition", new FadeInTransition(this, 1300, Color.BLACK));
	}

	@Override
	public void registerScenes() {
		// Register all the scenes in advance to quickly retrieve them later
		Scenes.register("in-game", this.mainGameScene);
		// Scenes.register("settings", this.settingsScene);
	}

	@Override
	public String firstScene() {
		return "in-game";
	}

	@Override
	public void postCreate() {
		super.postCreate();
		// Enable profiler
		this.useProfiler();
	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
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

	@Override
	public void registerBinds() {
		// Binds.registerBind("in-game", Utilities.createArray(Keyboard.KEY_ESCAPE),
		// Utilities.createArray(KeyState.DOWN), object ->
		// Scenes.switchTo("main-menu"));
		// Binds.registerBind("settings", Utilities.createArray(Keyboard.KEY_ESCAPE),
		// Utilities.createArray(KeyState.DOWN), object ->
		// Scenes.switchTo("main-menu"));
		Binds.registerBind("in-game", Utilities.createArray(Keyboard.KEY_ESCAPE), Utilities.createArray(KeyState.DOWN), object -> this.stop());
		Binds.registerBind("in-game", Utilities.createArray(Keyboard.KEY_H), Utilities.createArray(KeyState.DOWN), object -> ((GDialog) Engine.get("textbox")).hideBox());
		Binds.registerBind("in-game", Utilities.createArray(Keyboard.KEY_S), Utilities.createArray(KeyState.DOWN), object -> ((GDialog) Engine.get("textbox")).showBox());
		Binds.registerBind("in-game", Utilities.createArray(Keyboard.KEY_T), Utilities.createArray(KeyState.DOWN), object -> ((GDialog) Engine.get("textbox")).setText("Yo fuck off mate, you think you can just code a textbox like that?\n Wtf is wrong with you? Do you think we're some kind of slaves or something? Us Letters are disgusted by your actions."));
		// Binds.registerBind("*", Utilities.createArray(Keyboard.KEY_F5),
		// Utilities.createArray(KeyState.DOWN_IN_CURRENT_FRAME), object ->
		// this.screenshot("scr/" + Utilities.fileNameCompatibleDateString() + ".png"));
	}

	@Override
	public void createComponents() {
		Engine.register("textbox", new GDialog(this, 200, 200, 1024, 256, "textboxsprite", null, null, 35, 55, 950, 246, "8bit", 40));
		((GDialog) Engine.get("textbox")).setTimeBetweenChars(0.05);
		((GDialog) Engine.get("textbox")).setExtraSpaceBetweenLines(25);
	}

	public static void main(final String[] args) {
		if (Utilities.getOperatingSystem() == OperatingSystem.WINDOWS) {
			Utilities.disableHiDPI();
		}
		// Set debugging messages
		Logger.toggleLoggingToFile();
		Logger.setStateForDebuggingType(DebuggingType.CLASSIC, true);
		Logger.setStateForDebuggingType(DebuggingType.DEVELOPMENT, false);
		new TalkToMe().start();
	}

}
