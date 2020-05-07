package com.skanderj.g3;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;

import com.skanderj.g3.audio.AudioManager;
import com.skanderj.g3.component.Button;
import com.skanderj.g3.component.ButtonState;
import com.skanderj.g3.component.ComponentManager;
import com.skanderj.g3.component.Slider;
import com.skanderj.g3.component.Slider.SliderLabelPosition;
import com.skanderj.g3.component.Textfield;
import com.skanderj.g3.component.action.ButtonAction;
import com.skanderj.g3.inputdevice.Keyboard;
import com.skanderj.g3.inputdevice.Mouse;
import com.skanderj.g3.io.FontManager;
import com.skanderj.g3.log.Logger;
import com.skanderj.g3.log.Logger.LogLevel;
import com.skanderj.g3.translation.TranslationManager;
import com.skanderj.g3.translation.TranslationManager.Language;
import com.skanderj.g3.util.GraphicString;
import com.skanderj.g3.util.Utilities;
import com.skanderj.g3.window.Window;

public final class G3 {
	private static final String VERSION = "A.01";

	private static Textfield topArea, middleArea;
	private static Button playbackButton, randomFartButton;
	private static Slider volumeSlider;

	public static void main(String[] args) {
		Logger.redirectSystemOutput();
		TranslationManager.loadLanguage(Language.ENGLISH);
		Logger.enableDebug();
		Logger.enableDevDebug();
		Logger.log(G3.class, LogLevel.INFO, "Gingerbread3 version %s - by SkanderJ", G3.VERSION);
		FontManager.registerFont("roboto", "res/font/roboto.ttf");
		AudioManager.registerAudio("theme", "res/audio/silhouette.wav");
		AudioManager.registerAll("fart_%d", "res/audio/fart/");
		Window window = new Window.Fullscreen(null, "G3", 3, 1);
		Keyboard keyboard = new Keyboard();
		Mouse mouse = new Mouse();
		window.create();
		window.registerInput(keyboard);
		window.registerInput(mouse);
		window.show();
		G3.topArea = new Textfield(50, 50, window.getWidth() - 100, 50, Color.PINK, Color.BLACK, FontManager.getFont("roboto", 48), false);
		G3.middleArea = new Textfield(50, 125, window.getWidth() - 100, 200, Color.PINK, Color.BLACK, FontManager.getFont("roboto", 48), true);
		G3.playbackButton = new Button.RoundEdge(50, 400, 150, 60, new GraphicString("Pause", Color.WHITE, FontManager.getFont("roboto", 24), Color.DARK_GRAY), Color.WHITE, Color.BLACK, 16);
		G3.randomFartButton = new Button.StraightEdge(250, 400, 250, 60, new GraphicString("Random fart", Color.WHITE, FontManager.getFont("roboto", 24), Color.DARK_GRAY), Color.BLACK, Color.LIGHT_GRAY);
		G3.playbackButton.setButtonAction(ButtonState.IDLE, new ButtonAction() {
			@Override
			public void execute(Object... args) {
				G3.playbackButton.getLabel().setColor(Color.YELLOW);
			}
		});
		G3.playbackButton.setButtonAction(ButtonState.HOVERED, new ButtonAction() {
			@Override
			public void execute(Object... args) {
				G3.playbackButton.getLabel().setColor(Color.CYAN);
			}
		});
		G3.playbackButton.setButtonAction(ButtonState.CLICKED, new ButtonAction() {
			@Override
			public void execute(Object... args) {
				G3.playbackButton.getLabel().setColor(Color.RED);
			}
		});
		G3.playbackButton.setButtonAction(ButtonState.CLICK_FRAME, new ButtonAction() {
			@Override
			public void execute(Object... args) {
				if (G3.playbackButton.getLabel().getContent().equals("Pause")) {
					AudioManager.pauseAudio("theme");
					G3.playbackButton.getLabel().setContent("Resume");
				} else {
					AudioManager.resumeAudio("theme");
					G3.playbackButton.getLabel().setContent("Pause");
				}
			}
		});
		G3.randomFartButton.setButtonAction(ButtonState.CLICK_FRAME, new ButtonAction() {
			@Override
			public void execute(Object... args) {
				AudioManager.playAudio(String.format("fart_%d", Utilities.randomInteger(0, 4)));
			}
		});
		G3.volumeSlider = new Slider(225, 525, 150, 15, 4, 46, 0f, 1f, 0.5f, Color.WHITE, new GraphicString("Volume", Color.WHITE, FontManager.getFont("roboto", 24)), SliderLabelPosition.RIGHT);
		ComponentManager.addComponent("top-text-bar", G3.topArea);
		ComponentManager.addComponent("bottom-text-area", G3.middleArea);
		ComponentManager.addComponent("pause-resume-playbackButton", G3.playbackButton);
		ComponentManager.addComponent("random-fart-button", G3.randomFartButton);
		ComponentManager.addComponent("volume-volumeSlider", G3.volumeSlider);
		window.requestFocus();
		AudioManager.loopAudio("theme", -1);
		while (!window.isCloseRequested()) {
			G3.update(window, keyboard, mouse);
			G3.render(window);
			try {
				Thread.sleep(1000 / 60);
			} catch (InterruptedException interruptedException) {
				interruptedException.printStackTrace();
			}
		}
		window.hide();
		System.exit(0);
	}

	private static final void update(Window window, Keyboard keyboard, Mouse mouse) {
		if (keyboard.isKeyDownInFrame(Keyboard.KEY_ESCAPE)) {
			window.requestClosing();
		}
		ComponentManager.update(0, keyboard, mouse);
		if (mouse.isButtonDown(Mouse.BUTTON_LEFT)) {
			Logger.log(ComponentManager.class, LogLevel.DEBUG, "Current in focus component: %s", ComponentManager.getInFocus());
		}
		AudioManager.setVolume("theme", G3.volumeSlider.getValue());
		keyboard.update();
		mouse.update();
	}

	private static final void render(Window window) {
		BufferStrategy bufferStrategy = window.getBufferStrategy();
		Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, window.getWidth(), window.getHeight());
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		graphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		ComponentManager.render(window, graphics);
		graphics.setColor(Color.WHITE);
		graphics.setFont(FontManager.getFont("roboto", 32));
		graphics.drawString(String.format("Volume: %.2f", AudioManager.getVolume("theme") * 100) + "%", window.getWidth() - 350, window.getHeight() - 40);
		graphics.dispose();
		bufferStrategy.show();
	}
}
