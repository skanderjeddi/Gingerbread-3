package com.skanderj.g3;

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

	public static void main(String[] args) {
		Logger.redirectSystemOutput();
		Logger.log(G3.class, LogLevel.INFO, "Gingerbread3 version %s - by SkanderJ", G3.VERSION);
		try {
			AudioManager.registerAudio("theme", "res/silhouette.wav");
		} catch (IOException | UnsupportedAudioFileException exception) {
			exception.printStackTrace();
			System.exit(-1);
		}
		Window window = new Window.Regular(null, "G3", 800, 600, 3);
		Keyboard keyboard = new Keyboard();
		Mouse mouse = new Mouse();
		//window.create();
		window.registerInput(keyboard);
		window.registerInput(mouse);
		window.show();
		try {
			AudioManager.loopAudio("theme", -1);
		} catch (IOException | LineUnavailableException | InterruptedException exception) {
			exception.printStackTrace();
		}
		while (!window.isCloseRequested()) {
			if (keyboard.isKeyDownInFrame(Keyboard.KEY_SPACE)) {
				System.out.println("space");
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
			}
			float volume = Utilities.map(mouse.getX(), 0, 800, 0, 1.0f, true);
			AudioManager.setVolume("theme", volume);
			keyboard.update();
			mouse.update();
			try {
				Thread.sleep(1000 / 60);
			} catch (InterruptedException interruptedException) {
				interruptedException.printStackTrace();
			}
		}
		window.hide();
		System.exit(0);
	}
}
