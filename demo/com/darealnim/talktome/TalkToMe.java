package com.darealnim.talktome

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import java.awt.image.AffineTransformOp;

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
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.Priority;
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
import com.skanderj.gingerbread3.animation.boilerplates.SequentialAnimation;



public class TalkToMe extends Application {
    public static final String IDENTIFIER = "talktome", TITLE = "Talk to Me";
	public static final double REFRESH_RATE = 100.0D;
	public static final int WIDTH = 1920, HEIGHT = (TalkToMe.WIDTH / 16) * 9, BUFFERS = 2;

    // Application scenes
	private final Scene mainGameScene;

    public TalkToMe() {
        super(TalkToMe.IDENTIFIER, TalkToMe.REFRESH_RATE, TalkToMe.TITLE, TalkToMe.BUFFERS, 0, Keyboard.AZERTY);
        this.mainGameScene = new Scene(this) {
            public List<String> sceneObjects() {
				// Those are the only components which will be rendered/updated during this
				// scene
				return Arrays.asList("lena", "city");
			}

			@Override
			public void enter() {
                Scenes.transition("fade-transition");
				Audios.loop("anxiety", -1, 1);
			}

			@Override
			public void exit() {
				// Stop the audio
				Audios.stopAll();
                Scenes.transition("fade-transition");
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
    }

    @Override
	public void loadResources() {
		// Register some audio and some fonts
		Audios.load("anxiety", "res/music/mus_anxiety.wav");
		Fonts.load("8bit", "res/fonts/fnt_8bit.ttf");
        Images.loadAll("lena_%d", "res/sprites/lena/");
        // Images.load("city", "res/sprites/spr_city.png");
	}

    @Override
	public void registerGameObjects() {
		final Sprite[] lena_sprites = Sprite.fromImages(this, "lena_%d", Images.getCollectionByID("lena"));
        int[] timers = new int[15];
        Arrays.fill(timers, 12);
        Engine.register("lena", new SequentialAnimation(this, 820, 608, lena_sprites, timers));
        Engine.register("fade-transition", new FadeTransition(this, 1000, Color.BLACK));
        Engine.register("city", Sprite.fromImage(this, "city", "res/sprites/spr_city.png", 1920, 1080, AffineTransformOp.TYPE_NEAREST_NEIGHBOR));
        ((Sprite)Engine.get("city")).setX(1);
        ((Sprite)Engine.get("city")).setY(1);
	}

    @Override
	public void registerScenes() {
		// Register all the scenes in advance to quickly retrieve them later
		Scenes.register("in-game", this.mainGameScene);
		// Scenes.register("settings", this.settingsScene);
		// Set the current scene
		Scenes.switchTo("in-game");
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
		// Binds.registerBind("in-game", Utilities.createArray(Keyboard.KEY_ESCAPE), Utilities.createArray(KeyState.DOWN), object -> Scenes.switchTo("main-menu"));
		// Binds.registerBind("settings", Utilities.createArray(Keyboard.KEY_ESCAPE), Utilities.createArray(KeyState.DOWN), object -> Scenes.switchTo("main-menu"));
		Binds.registerBind("in-game", Utilities.createArray(Keyboard.KEY_ESCAPE, Keyboard.KEY_SPACE), Utilities.createArray(KeyState.DOWN, KeyState.DOWN), object -> this.stop());
		// Binds.registerBind("*", Utilities.createArray(Keyboard.KEY_F5), Utilities.createArray(KeyState.DOWN_IN_CURRENT_FRAME), object -> this.screenshot("scr/" + Utilities.fileNameCompatibleDateString() + ".png"));
	}

    public void createComponents() {}

    public static void main(final String[] args) {
		// Set debugging messages
		Logger.toggleLoggingToFile();
		Logger.setStateForDebuggingType(DebuggingType.CLASSIC, true);
		Logger.setStateForDebuggingType(DebuggingType.DEVELOPMENT, false);
		new TalkToMe().start();
	}



}
