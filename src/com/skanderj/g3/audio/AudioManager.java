package com.skanderj.g3.audio;

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

import com.skanderj.g3.log.Logger;
import com.skanderj.g3.log.Logger.LogLevel;
import com.skanderj.g3.translation.TranslationManager;

/**
 * A class used for handling all audio purposes. Can't be instantiated, only
 * static methods. Can handle as much audio as you throw at it. Files must be
 * .WAV.
 * 
 * @author Skander
 *
 */
public final class AudioManager {
	// Translations keys
	private static final String KEY_AUDIO_MANAGER_SUCCESS = "key.audiomanager.success";
	private static final String KEY_AUDIO_MANAGER_NOT_DIRECTORY = "key.audiomanager.not_directory";
	private static final String KEY_AUDIO_MANAGER_EXCEPTION_LOADING = "key.audiomanager.exception.loading";
	private static final String KEY_AUDIO_MANAGER_EXCEPTION_PLAYING = "key.audiomanager.exception.playing";
	private static final String KEY_AUDIO_MANAGER_EXCEPTION_CLOSE = "key.audiomanager.exception.close";
	private static final String KEY_AUDIO_MANAGER_EXCEPTION_CLOSE_MULTIPLE = "key.audiomanager.exception.close.multiple";
	private static final String KEY_AUDIO_MANAGER_EXCEPTION_WAIT_FOR = "key.audiomanager.exception.wait_for";
	private static final String KEY_AUDIO_MANAGER_MISSING_AUDIO = "key.audiomanager.missing_audio";
	private static final String KEY_AUDIO_MANAGER_MISSING_AUDIO_STREAM = "key.audiomanager.missing_audio_stream";

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
	public static final boolean registerAudio(String identifier, String path) {
		File soundFile = new File(path);
		AudioInputStream reusableAudioInputStream;
		try {
			reusableAudioInputStream = AudioManager.createReusableAudioInputStream(soundFile);
			AudioManager.audioMap.put(identifier, reusableAudioInputStream);
			Logger.log(AudioManager.class, LogLevel.INFO, TranslationManager.getKey(AudioManager.KEY_AUDIO_MANAGER_SUCCESS, identifier));
			return true;
		} catch (IOException | UnsupportedAudioFileException exception) {
			Logger.log(AudioManager.class, LogLevel.SEVERE, TranslationManager.getKey(AudioManager.KEY_AUDIO_MANAGER_EXCEPTION_LOADING, path, exception.getMessage()));
			return false;
		}
	}

	/**
	 * Loads all the audio files in the provided directory while adding "_0, _1, _2"
	 * to the identifier. Returns true if successful, false otherwise.
	 */
	public static final boolean registerAll(String identifier, String path) {
		File directory = new File(path);
		if (directory.isDirectory()) {
			int counter = 0;
			boolean success = true;
			for (File file : directory.listFiles()) {
				if (!AudioManager.registerAudio(String.format(identifier, counter), file.getPath())) {
					success = false;
				}
				counter += 1;
			}
			return success;
		} else {
			Logger.log(AudioManager.class, Logger.LogLevel.SEVERE, TranslationManager.getKey(AudioManager.KEY_AUDIO_MANAGER_NOT_DIRECTORY));
			return false;
		}
	}

	/**
	 * Self explanatory. Returns true if successful, false otherwise.
	 */
	public static final boolean playAudio(String identifier) {
		AudioInputStream stream = AudioManager.audioMap.get(identifier);
		if (stream == null) {
			Logger.log(AudioManager.class, Logger.LogLevel.SEVERE, TranslationManager.getKey(AudioManager.KEY_AUDIO_MANAGER_MISSING_AUDIO, identifier));
			return false;
		} else {
			try {
				stream.reset();
				return AudioManager.playAudio(identifier, stream);
			} catch (IOException exception) {
				Logger.log(AudioManager.class, LogLevel.SEVERE, TranslationManager.getKey(AudioManager.KEY_AUDIO_MANAGER_EXCEPTION_PLAYING, identifier, exception.getMessage()));
				return false;
			}
		}
	}

	/**
	 * Self explanatory. Uses the stream to create a clip and then wrap the clip in
	 * a thread and start it. Registering everything in the corresponding maps.
	 * Returns true if successful, false otherwise.
	 */
	private static final boolean playAudio(String identifier, AudioInputStream audioInputStream) {
		class AudioListener implements LineListener {
			private boolean isDone = false;

			@Override
			public synchronized void update(LineEvent event) {
				Type eventType = event.getType();
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
		AudioListener listener = new AudioListener();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					clip.addLineListener(listener);
					clip.open(audioInputStream);
					try {
						clip.start();
						AudioManager.clipsMap.put(identifier, clip);
						try {
							listener.waitUntilDone();
						} catch (InterruptedException exception) {
							Logger.log(AudioManager.class, LogLevel.SEVERE, TranslationManager.getKey(AudioManager.KEY_AUDIO_MANAGER_EXCEPTION_WAIT_FOR, identifier, exception.getMessage()));
						}
					} finally {
						clip.close();
						AudioManager.clipsMap.remove(identifier);
					}
				} catch (LineUnavailableException | IOException exception) {
					Logger.log(AudioManager.class, LogLevel.SEVERE, TranslationManager.getKey(AudioManager.KEY_AUDIO_MANAGER_EXCEPTION_PLAYING, identifier, exception.getMessage()));
				} finally {
					try {
						audioInputStream.close();
					} catch (IOException ioException) {
						Logger.log(AudioManager.class, LogLevel.SEVERE, TranslationManager.getKey(AudioManager.KEY_AUDIO_MANAGER_EXCEPTION_CLOSE, identifier, ioException.getMessage()));
					}
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
	public static final boolean isAudioPaused(String identifier) {
		if (AudioManager.pausesMap.contains(identifier)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Self explanatory.
	 */
	public static final float getVolume(String identifier) {
		Clip clip = AudioManager.clipsMap.get(identifier);
		if (clip == null) {
			Logger.log(AudioManager.class, Logger.LogLevel.SEVERE, TranslationManager.getKey(AudioManager.KEY_AUDIO_MANAGER_MISSING_AUDIO, identifier));
			return -1;
		} else {
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			return (float) Math.pow(10f, gainControl.getValue() / 20f);
		}
	}

	/**
	 * Sets the volume for a running clip. Volume must be between 0f and 1f.
	 */
	public static final void setVolume(String identifier, float volume) {
		Clip clip = AudioManager.clipsMap.get(identifier);
		if (clip == null) {
			Logger.log(AudioManager.class, Logger.LogLevel.SEVERE, TranslationManager.getKey(AudioManager.KEY_AUDIO_MANAGER_MISSING_AUDIO, identifier));
		} else {
			if ((volume < 0f) || (volume > 1f)) {
				throw new IllegalArgumentException("Volume not valid: " + volume);
			}
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(20f * (float) Math.log10(volume));
		}
	}

	/**
	 * Self explanatory. Returns true if successful, false otherwise.
	 */
	public static final boolean pauseAudio(String identifier) {
		Clip clip = AudioManager.clipsMap.get(identifier);
		if (clip == null) {
			Logger.log(AudioManager.class, Logger.LogLevel.SEVERE, TranslationManager.getKey(AudioManager.KEY_AUDIO_MANAGER_MISSING_AUDIO, identifier));
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
	public static final boolean resumeAudio(String identifier) {
		Clip clip = AudioManager.clipsMap.get(identifier);
		if (clip == null) {
			Logger.log(AudioManager.class, Logger.LogLevel.SEVERE, TranslationManager.getKey(AudioManager.KEY_AUDIO_MANAGER_MISSING_AUDIO, identifier));
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
	public static final boolean stopAudio(String identifier) {
		Clip clip = AudioManager.clipsMap.get(identifier);
		if (clip == null) {
			Logger.log(AudioManager.class, Logger.LogLevel.SEVERE, TranslationManager.getKey(AudioManager.KEY_AUDIO_MANAGER_MISSING_AUDIO, identifier));
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
	public static final boolean stopAllAudio() {
		for (AudioInputStream audioInputStream : AudioManager.audioMap.values()) {
			try {
				audioInputStream.reset();
				audioInputStream.close();
			} catch (IOException ioException) {
				Logger.log(AudioManager.class, LogLevel.SEVERE, TranslationManager.getKey(AudioManager.KEY_AUDIO_MANAGER_EXCEPTION_CLOSE_MULTIPLE, ioException.getMessage()));
				return false;
			}
		}
		for (Clip clip : AudioManager.clipsMap.values()) {
			clip.stop();
		}
		for (Thread thread : AudioManager.threadsMap.values()) {
			thread.interrupt();
		}
		return true;
	}

	/**
	 * Self explanatory. Returns true if successful, false otherwise.
	 */
	public static final boolean loopAudio(String identifier, int count) {
		AudioInputStream stream = AudioManager.audioMap.get(identifier);
		if (stream == null) {
			Logger.log(AudioManager.class, Logger.LogLevel.SEVERE, TranslationManager.getKey(AudioManager.KEY_AUDIO_MANAGER_MISSING_AUDIO_STREAM, identifier));
			return false;
		} else {
			try {
				stream.reset();
			} catch (IOException ioException) {
				Logger.log(AudioManager.class, LogLevel.SEVERE, TranslationManager.getKey(AudioManager.KEY_AUDIO_MANAGER_EXCEPTION_PLAYING, identifier, ioException.getMessage()));
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
	private static final boolean loopAudio(String identifier, int count, AudioInputStream audioInputStream) {
		class AudioListener implements LineListener {
			private boolean isDone = false;

			@Override
			public synchronized void update(LineEvent event) {
				Type eventType = event.getType();
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
		AudioListener listener = new AudioListener();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					clip.addLineListener(listener);
					clip.open(audioInputStream);
					try {
						clip.loop(count == -1 ? -1 : count - 1);
						AudioManager.clipsMap.put(identifier, clip);
						listener.waitUntilDone();
					} catch (InterruptedException exception) {
						Logger.log(AudioManager.class, LogLevel.SEVERE, TranslationManager.getKey(AudioManager.KEY_AUDIO_MANAGER_EXCEPTION_WAIT_FOR, identifier, exception.getMessage()));
					} finally {
						clip.close();
						AudioManager.clipsMap.remove(identifier);
					}
				} catch (LineUnavailableException | IOException exception) {
					Logger.log(AudioManager.class, LogLevel.SEVERE, TranslationManager.getKey(AudioManager.KEY_AUDIO_MANAGER_EXCEPTION_PLAYING, identifier, exception.getMessage()));
				} finally {
					try {
						audioInputStream.close();
					} catch (IOException ioException) {
						Logger.log(AudioManager.class, LogLevel.SEVERE, TranslationManager.getKey(AudioManager.KEY_AUDIO_MANAGER_EXCEPTION_CLOSE, identifier, ioException.getMessage()));
					}
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
