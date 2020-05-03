package com.skanderj.g3;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.skanderj.g3.audio.AudioManager;
import com.skanderj.g3.inputdevice.Keyboard;
import com.skanderj.g3.inputdevice.Keyboard.KeyState;
import com.skanderj.g3.inputdevice.Mouse;
import com.skanderj.g3.io.FontManager;
import com.skanderj.g3.log.Logger;
import com.skanderj.g3.log.Logger.LogLevel;
import com.skanderj.g3.util.Utilities;
import com.skanderj.g3.window.Window;

public final class G3 {
	private static final String VERSION = "A.01";
	public static boolean DEBUG = true;

	private static Color backgroundColor = Color.BLACK;

	private static List<String> toDraw = new ArrayList<String>();
	private static String currentString = "";

	public static void main(String[] args) {
		Logger.redirectSystemOutput();
		Logger.log(G3.class, LogLevel.INFO, "Gingerbread3 version %s - by SkanderJ", G3.VERSION);
		try {
			FontManager.registerFont("main_font", "res/fonts/main_font.ttf");
		} catch (FontFormatException | IOException exception) {
			exception.printStackTrace();
			System.exit(-1);
		}
		AudioManager.registerAudio("theme", "res/audios/silhouette.wav");
		Window window = new Window.Fullscreen(null, "G3", 3, 1);
		Keyboard keyboard = new Keyboard();
		Mouse mouse = new Mouse();
		window.create();
		window.registerInput(keyboard);
		window.registerInput(mouse);
		window.show();
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
		if (keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			G3.backgroundColor = Utilities.randomColor(false);
		}
		if (keyboard.isKeyDownInFrame(Keyboard.KEY_ESCAPE)) {
			window.requestClosing();
		}
		if (mouse.isButtonDownInFrame(Mouse.BUTTON_LEFT)) {
			if (AudioManager.isAudioPaused("theme")) {
				AudioManager.resumeAudio("theme");
			} else {
				AudioManager.pauseAudio("theme");
			}
			System.out.println(mouse.getX());
		}
		if (mouse.isButtonDownInFrame(Mouse.BUTTON_RIGHT)) {
			Logger.log(mouse.getClass(), LogLevel.INFO, "A random number between -10 and 10: %d", Utilities.randomInteger(-10, 10));
		}
		float volume = Utilities.map(mouse.getX(), 0, window.getWidth(), 0, 1.0f, true);
		AudioManager.setVolume("theme", volume);
		for (int keyCode : keyboard.getKeysByState(KeyState.DOWN_IN_FRAME)) {
			if (keyCode == Keyboard.KEY_ENTER) {
				G3.toDraw.add(G3.currentString);
				G3.currentString = "";
				break;
			}
			if (keyCode == Keyboard.KEY_BACK_SPACE) {
				if (!G3.currentString.isEmpty()) {
					G3.currentString = G3.currentString.substring(0, G3.currentString.length() - 1);
					break;
				}
			}
			String key = Keyboard.getKeyRepresentation(keyCode, keyboard.isShiftDown(), keyboard.isCapsLocked(), keyboard.isAltGrDown());
			G3.currentString += key;
		}
		keyboard.update();
		mouse.update();
	}

	private static final void render(Window window) {
		BufferStrategy bufferStrategy = window.getBufferStrategy();
		Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, window.getWidth(), window.getHeight());
		graphics.setColor(G3.backgroundColor);
		graphics.fillRect(0, 0, window.getWidth(), window.getHeight());
		graphics.setColor(Color.WHITE);
		graphics.setFont(new Font("Times New Roman", Font.PLAIN, 48));
		int counter = 0;
		for (String s : G3.toDraw) {
			graphics.drawString(s, 20, 40 + (40 * counter));
			counter += 1;
		}
		graphics.drawString(G3.currentString, 20, 40 + (40 * (counter + 1)));
		graphics.drawString(String.format("Volume: %.2f", AudioManager.getVolume("theme") * 100) + "%", window.getWidth() - 350, window.getHeight() - 40);
		graphics.dispose();
		bufferStrategy.show();
	}
}
