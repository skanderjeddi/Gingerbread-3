package com.skanderj.g3.io;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public final class IOHandler {
	private IOHandler() {
		return;
	}

	private static final Map<String, BufferedImage> imagesMap = new HashMap<String, BufferedImage>();
	private static final Map<String, Font> fontsMap = new HashMap<String, Font>();
	private static final Map<String, AudioInputStream> soundsMap = new HashMap<String, AudioInputStream>();

	public static final void registerImage(String identifier, String path) throws IOException {
		BufferedImage image = ImageIO.read(new File(path));
		IOHandler.imagesMap.put(identifier, image);
	}

	public static final BufferedImage retrieveImage(String identifier) {
		return IOHandler.imagesMap.get(identifier);
	}

	public static final void registerFont(String identifier, String path) throws FontFormatException, IOException {
		File fontFile = new File(path);
		FileInputStream fileInputStream = new FileInputStream(fontFile);
		Font font = Font.createFont(Font.TRUETYPE_FONT, fileInputStream);
		IOHandler.fontsMap.put(identifier, font);
	}

	public static final Font getFont(String identifier) {
		return IOHandler.fontsMap.get(identifier);
	}

	public static final Font getFont(String identifier, int size) {
		return IOHandler.fontsMap.get(identifier).deriveFont((float) size);
	}

	public static final void registerSound(String identifier, String path) throws IOException, UnsupportedAudioFileException {
		File soundFile = new File(path);
		AudioInputStream reusableAudioInputStream = IOHandler.createReusableAudioInputStream(soundFile);
		IOHandler.soundsMap.put(identifier, reusableAudioInputStream);
	}

	public static final void playSound(String identifier) throws LineUnavailableException, IOException {
		AudioInputStream stream = IOHandler.soundsMap.get(identifier);
		stream.reset();
		Clip clip = AudioSystem.getClip();
		clip.open(stream);
		clip.start();
	}

	private static final AudioInputStream createReusableAudioInputStream(File file) throws IOException, UnsupportedAudioFileException {
		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(file);
			byte[] buffer = new byte[1024 * 32];
			int read = 0;
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(buffer.length);
			while ((read = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
				outputStream.write(buffer, 0, read);
			}
			AudioInputStream reusableAudioInputStream = new AudioInputStream(new ByteArrayInputStream(outputStream.toByteArray()), audioInputStream.getFormat(), AudioSystem.NOT_SPECIFIED);
			return reusableAudioInputStream;
		} finally {
			if (audioInputStream != null) {
				audioInputStream.close();
			}
		}
	}
}
