package com.skanderj.g3;

import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.skanderj.g3.sound.AudioManager;

public final class G3 {
	private static final String VERSION = "B.1";

	public static void main(String[] args) {
		String point = "point";
		String music = "silhouette";
		System.out.printf("Gingerbread3 version %s - by SkanderJ\n", G3.VERSION);
		try {
			AudioManager.registerSound(point, "res/point.wav");
			AudioManager.registerSound(music, "res/silhouette.wav");
			AudioManager.playSound(music);
			// FontManager.loopSound(point, -1);
			Scanner scanner = new Scanner(System.in);
			while (true) {
				System.out.print("> ");
				String line = scanner.nextLine();
				if (line.equals("q")) {
					scanner.close();
					AudioManager.stopAllSounds();
					System.exit(0);
				} else {
					try {
						float v = Float.valueOf(line);
						AudioManager.setVolume(music, v);
					} catch (Exception exception) {
						continue;
					}
				}
			}
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException exception) {
			exception.printStackTrace();
		}
	}
}
