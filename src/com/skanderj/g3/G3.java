package com.skanderj.g3;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.skanderj.g3.audio.AudioManager;
import com.skanderj.g3.inputdevice.Keyboard;
import com.skanderj.g3.inputdevice.Mouse;
import com.skanderj.g3.log.Logger;
import com.skanderj.g3.log.Logger.LogLevel;
import com.skanderj.g3.util.Utilities;
import com.skanderj.g3.window.Window;

public final class G3 {
	private static final String VERSION = "B.1";
	public static boolean DEBUG = true;

	private static Color backgroundColor = Color.BLACK;

	public static void main(String[] args) {
		Logger.redirectSystemOutput();
		Logger.log(G3.class, LogLevel.INFO, "Gingerbread3 version %s - by SkanderJ", G3.VERSION);
		try {
			AudioManager.registerAudio("theme", "res/silhouette.wav");
		} catch (IOException | UnsupportedAudioFileException exception) {
			exception.printStackTrace();
			System.exit(-1);
		}
		Window window = new Window.Fullscreen(null, "G3", 3, 1);
		Keyboard keyboard = new Keyboard();
		Mouse mouse = new Mouse();
		window.create();
		window.registerInput(keyboard);
		window.registerInput(mouse);
		window.show();
		window.requestFocus();
		try {
			AudioManager.loopAudio("theme", -1);
		} catch (IOException | LineUnavailableException | InterruptedException exception) {
			exception.printStackTrace();
		}
		while (!window.isCloseRequested()) {
			update(window, keyboard, mouse);
			render(window);
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
			backgroundColor = Utilities.randomColor(false);
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
		keyboard.update();
		mouse.update();
	}

	private static final void render(Window window) {
		BufferStrategy bufferStrategy = window.getBufferStrategy();
		Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, window.getWidth(), window.getHeight());
		graphics.setColor(backgroundColor);
		graphics.fillRect(0, 0, window.getWidth(), window.getHeight());
		graphics.dispose();
		bufferStrategy.show();
	}
}
