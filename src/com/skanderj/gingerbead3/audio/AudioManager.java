package com.skanderj.gingerbead3.audio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.skanderj.gingerbead3.log.Logger;
import com.skanderj.gingerbead3.log.Logger.LogLevel;

/**
 * A class used for handling all audio purposes. Can't be instantiated, only
 * static methods. Can handle as much audio as you throw at it. Files must be
 * .WAV.
 *
 * @author Skander
 *
 */
public final class AudioManager {
	// Reusable audio streams map - how we can play multiple sounds at once over and
	// over
	private static final Map<String, AudioInputStream> audioMap = new HashMap<String, AudioInputStream>();
	// Clips map to handle pausing, playing, resuming...
	private static final Map<String, Clip> clipsMap = new HashMap<String, Clip>();
	// Threads map, wrappers for each individual clip
	private static final Map<String, Thread> threadsMap = new HashMap<String, Thread>();
	// Keep track of which clips are paused
	private static final Set<String> pausesMap = new HashSet<String>();

	private AudioManager() {
		return;
	}

	/**
	 * Loads an audio from the provided path. File must be .WAV format. Returns true
	 * if the audio was successfully registered, false otherwise.
	 */
	public static boolean registerAudio(String identifier, String path) {
		final File soundFile = new File(path);
		AudioInputStream reusableAudioInputStream;
		try {
			reusableAudioInputStream = AudioManager.createReusableAudioInputStream(soundFile);
			AudioManager.audioMap.put(identifier, reusableAudioInputStream);
			Logger.log(AudioManager.class, LogLevel.INFO, "Successfully registered audio with identifier \"%s\"", identifier);
			return true;
		} catch (IOException | UnsupportedAudioFileException exception) {
			Logger.log(AudioManager.class, LogLevel.SEVERE, "An exception occurred while loading audio from %s: %s", path, exception.getMessage());
			return false;
		}
	}

	/**
	 * Loads all the audio files in the provided directory while adding "_0, _1, _2"
	 * to the identifier. Returns true if successful, false otherwise.
	 */
	public static boolean registerAll(String identifier, String path) {
		final File directory = new File(path);
		if (directory.isDirectory()) {
			int counter = 0;
			boolean success = true;
			for (final File file : directory.listFiles()) {
				if (!AudioManager.registerAudio(String.format(identifier, counter), file.getPath())) {
					success = false;
				}
				counter += 1;
			}
			return success;
		} else {
			Logger.log(AudioManager.class, Logger.LogLevel.SEVERE, "Provided path %s doesn't point to a directory", path);
			return false;
		}
	}

	/**
	 * Self explanatory. Returns true if successful, false otherwise.
	 */
	public static boolean playAudio(String identifier) {
		final AudioInputStream stream = AudioManager.audioMap.get(identifier);
		if (stream == null) {
			Logger.log(AudioManager.class, Logger.LogLevel.SEVERE, "Could not find audio with identifier \"%s\"", identifier);
			return false;
		} else {
			try {
				stream.reset();
				return AudioManager.playAudio(identifier, stream);
			} catch (final IOException exception) {
				Logger.log(AudioManager.class, LogLevel.SEVERE, "An exception occurred while trying to play audio \"%s\": %s", identifier, exception.getMessage());
				return false;
			}
		}
	}

	/**
	 * Self explanatory. Uses the stream to create a clip and then wrap the clip in
	 * a thread and start it. Registering everything in the corresponding maps.
	 * Returns true if successful, false otherwise.
	 */
	private static boolean playAudio(String identifier, AudioInputStream audioInputStream) {
		class AudioListener implements LineListener {
			private boolean isDone = false;

			@Override
			public synchronized void update(LineEvent event) {
				final Type eventType = event.getType();
				if (eventType == Type.CLOSE) {
					this.isDone = true;
					this.notifyAll();
				}
			}

			public synchronized void waitUntilDone() throws InterruptedException {
				while (!this.isDone) {
					this.wait();
				}
			}
		}
		final AudioListener listener = new AudioListener();
		final Thread thread = new Thread(() -> {
			try {
				final Clip clip = AudioSystem.getClip();
				clip.addLineListener(listener);
				clip.open(audioInputStream);
				try {
					clip.start();
					AudioManager.clipsMap.put(identifier, clip);
					try {
						listener.waitUntilDone();
					} catch (final InterruptedException exception1) {
						Logger.log(AudioManager.class, LogLevel.SEVERE, "An exception occurred while waiting for audio \"%s\" to finish: %s", identifier, exception1.getMessage());
					}
				} finally {
					clip.close();
					AudioManager.clipsMap.remove(identifier);
				}
			} catch (LineUnavailableException | IOException exception2) {
				Logger.log(AudioManager.class, LogLevel.SEVERE, "An exception occurred while trying to play audio \"%s\": %s", identifier, exception2.getMessage());
			} finally {
				try {
					audioInputStream.close();
				} catch (final IOException ioException) {
					Logger.log(AudioManager.class, LogLevel.SEVERE, "An exception occurred while trying to close audio stream \"%s\": %s", identifier, ioException.getMessage());
				}
			}
		});
		thread.start();
		AudioManager.threadsMap.put(identifier, thread);
		return true;
	}

	/**
	 * Self explanatory.
	 */
	public static boolean isAudioPaused(String identifier) {
		if (AudioManager.pausesMap.contains(identifier)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Self explanatory.
	 */
	public static float getVolume(String identifier) {
		final Clip clip = AudioManager.clipsMap.get(identifier);
		if (clip == null) {
			Logger.log(AudioManager.class, Logger.LogLevel.SEVERE, "Could not find audio with identifier \"%s\"", identifier);
			return -1;
		} else {
			final FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			return (float) Math.pow(10f, gainControl.getValue() / 20f);
		}
	}

	/**
	 * Sets the volume for a running clip. Volume must be between 0f and 1f.
	 */
	public static void setVolume(String identifier, float volume) {
		final Clip clip = AudioManager.clipsMap.get(identifier);
		if (clip == null) {
			Logger.log(AudioManager.class, Logger.LogLevel.SEVERE, "Could not find audio with identifier \"%s\"", identifier);
		} else {
			if (volume < 0f || volume > 1f) {
				throw new IllegalArgumentException("Volume not valid: " + volume);
			}
			final FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(20f * (float) Math.log10(volume));
		}
	}

	/**
	 * Self explanatory. Returns true if successful, false otherwise.
	 */
	public static boolean pauseAudio(String identifier) {
		final Clip clip = AudioManager.clipsMap.get(identifier);
		if (clip == null) {
			Logger.log(AudioManager.class, Logger.LogLevel.SEVERE, "Could not find audio with identifier \"%s\"", identifier);
			return false;
		} else {
			clip.stop();
			AudioManager.pausesMap.add(identifier);
			return true;
		}
	}

	/**
	 * Self explanatory. Returns true if successful, false otherwise.
	 */
	public static boolean resumeAudio(String identifier) {
		final Clip clip = AudioManager.clipsMap.get(identifier);
		if (clip == null) {
			Logger.log(AudioManager.class, Logger.LogLevel.SEVERE, "Could not find audio with identifier \"%s\"", identifier);
			return false;
		} else {
			clip.start();
			AudioManager.pausesMap.remove(identifier);
			return true;
		}
	}

	/**
	 * Self explanatory. Returns true if successful, false otherwise.
	 */
	public static boolean stopAudio(String identifier) {
		final Clip clip = AudioManager.clipsMap.get(identifier);
		if (clip == null) {
			Logger.log(AudioManager.class, Logger.LogLevel.SEVERE, "Could not find audio with identifier \"%s\"", identifier);
			return false;
		} else {
			clip.stop();
			AudioManager.threadsMap.get(identifier).interrupt();
			return true;
		}
	}

	/**
	 * Self explanatory. Returns true if successful, false otherwise.
	 */
	public static boolean stopAllAudio() {
		for (final AudioInputStream audioInputStream : AudioManager.audioMap.values()) {
			try {
				audioInputStream.reset();
				audioInputStream.close();
			} catch (final IOException ioException) {
				Logger.log(AudioManager.class, LogLevel.SEVERE, "An exception occurred while trying to close audio streams \"%s\": %s", ioException.getMessage());
				return false;
			}
		}
		for (final Clip clip : AudioManager.clipsMap.values()) {
			clip.stop();
		}
		for (final Thread thread : AudioManager.threadsMap.values()) {
			thread.interrupt();
		}
		return true;
	}

	/**
	 * Self explanatory. Returns true if successful, false otherwise. -1 to loop
	 * indefinitely.
	 */
	public static boolean loopAudio(String identifier, int count) {
		final AudioInputStream stream = AudioManager.audioMap.get(identifier);
		if (stream == null) {
			Logger.log(AudioManager.class, Logger.LogLevel.SEVERE, "Could not find audio stream with identifier \"%s\"", identifier);
			return false;
		} else {
			try {
				stream.reset();
			} catch (final IOException ioException) {
				Logger.log(AudioManager.class, LogLevel.SEVERE, "An exception occurred while trying to play audio \"%s\": %s", identifier, ioException.getMessage());
				return false;
			}
			return AudioManager.loopAudio(identifier, count, stream);
		}
	}

	/**
	 * Self explanatory. Uses the stream to create a clip and then wrap the clip in
	 * a thread and loop it. Registering everything in the corresponding maps.
	 * Returns true if successful, false otherwise.
	 */
	private static boolean loopAudio(String identifier, int count, AudioInputStream audioInputStream) {
		class AudioListener implements LineListener {
			private boolean isDone = false;

			@Override
			public synchronized void update(LineEvent event) {
				final Type eventType = event.getType();
				if (eventType == Type.CLOSE) {
					this.isDone = true;
					this.notifyAll();
				}
			}

			public synchronized void waitUntilDone() throws InterruptedException {
				while (!this.isDone) {
					this.wait();
				}
			}
		}
		final AudioListener listener = new AudioListener();
		final Thread thread = new Thread(() -> {
			try {
				final Clip clip = AudioSystem.getClip();
				clip.addLineListener(listener);
				clip.open(audioInputStream);
				try {
					clip.loop(count == -1 ? -1 : count - 1);
					AudioManager.clipsMap.put(identifier, clip);
					listener.waitUntilDone();
				} catch (final InterruptedException exception1) {
					Logger.log(AudioManager.class, LogLevel.SEVERE, "An exception occurred while waiting for audio \"%s\" to finish: %s", identifier, exception1.getMessage());
				} finally {
					clip.close();
					AudioManager.clipsMap.remove(identifier);
				}
			} catch (LineUnavailableException | IOException exception2) {
				Logger.log(AudioManager.class, LogLevel.SEVERE, "An exception occurred while trying to loop audio \"%s\": %s", identifier, exception2.getMessage());
			} finally {
				try {
					audioInputStream.close();
				} catch (final IOException ioException) {
					Logger.log(AudioManager.class, LogLevel.SEVERE, "An exception occurred while trying to close audio stream \"%s\": %s", identifier, ioException.getMessage());
				}
			}
		});
		thread.start();
		AudioManager.threadsMap.put(identifier, thread);
		return true;
	}

	/**
	 * This is where the magic - and the reusable audio streams creation happens.
	 */
	private static AudioInputStream createReusableAudioInputStream(File file) throws IOException, UnsupportedAudioFileException {
		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(file);
			final byte[] buffer = new byte[1024 * 32];
			int read = 0;
			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(buffer.length);
			while ((read = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
				outputStream.write(buffer, 0, read);
			}
			final AudioInputStream reusableAudioInputStream = new AudioInputStream(new ByteArrayInputStream(outputStream.toByteArray()), audioInputStream.getFormat(), AudioSystem.NOT_SPECIFIED);
			return reusableAudioInputStream;
		} finally {
			if (audioInputStream != null) {
				audioInputStream.close();
			}
		}
	}
}
