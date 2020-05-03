package com.skanderj.g3;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;

import com.skanderj.g3.audio.AudioManager;
import com.skanderj.g3.component.Textfield;
import com.skanderj.g3.inputdevice.Keyboard;
import com.skanderj.g3.inputdevice.Mouse;
import com.skanderj.g3.io.FontManager;
import com.skanderj.g3.log.Logger;
import com.skanderj.g3.log.Logger.LogLevel;
import com.skanderj.g3.window.Window;

public final class G3 {
	private static final String VERSION = "A.01";
	public static boolean DEBUG = true;

	private static Textfield smallArea, largeArea;

	public static void main(String[] args) {
		Logger.redirectSystemOutput();
		Logger.log(G3.class, LogLevel.INFO, "Gingerbread3 version %s - by SkanderJ", G3.VERSION);
		FontManager.registerFont("roboto", "res/fonts/roboto.ttf");
		AudioManager.registerAudio("theme", "res/audios/silhouette.wav");
		Window window = new Window.Fullscreen(null, "G3", 3, 0);
		Keyboard keyboard = new Keyboard();
		Mouse mouse = new Mouse();
		window.create();
		window.registerInput(keyboard);
		window.registerInput(mouse);
		window.show();
		G3.smallArea = new Textfield(50, 50, window.getWidth() - 100, 50, Color.PINK, Color.BLACK, FontManager.getFont("roboto", 48), false);
		G3.largeArea = new Textfield(50, 125, window.getWidth() - 100, 200, Color.PINK, Color.BLACK, FontManager.getFont("roboto", 48), true);
		window.requestFocus();
		// AudioManager.loopAudio("theme", -1);
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
		if (mouse.isButtonDownInFrame(Mouse.BUTTON_LEFT)) {
			if (G3.smallArea.containsMouse(mouse)) {
				G3.smallArea.giveFocus();
			} else {
				G3.smallArea.removeFocus();
			}
			if (G3.largeArea.containsMouse(mouse)) {
				G3.largeArea.giveFocus();
			} else {
				G3.largeArea.removeFocus();
			}
		}
		// float volume = Utilities.map(mouse.getX(), 0, window.getWidth(), 0, 1.0f,
		// true);
		// AudioManager.setVolume("theme", volume);
		G3.smallArea.update(window, keyboard, mouse);
		G3.largeArea.update(window, keyboard, mouse);
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
		G3.smallArea.render(window, graphics);
		G3.largeArea.render(window, graphics);
//		graphics.setColor(Color.WHITE);
//		graphics.drawString(String.format("Volume: %.2f", AudioManager.getVolume("theme") * 100) + "%", window.getWidth() - 350, window.getHeight() - 40);
		graphics.dispose();
		bufferStrategy.show();
	}
}
