package com.skanderj.g3.sound;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public final class AudioManager {
	private static final Map<String, AudioInputStream> soundsMap = new HashMap<String, AudioInputStream>();
	private static final Map<String, Clip> clipsMap = new HashMap<String, Clip>();
	private static final Map<String, Thread> threadsMap = new HashMap<String, Thread>();

	private AudioManager() {
		return;
	}

	public static final void registerSound(String identifier, String path) throws IOException, UnsupportedAudioFileException {
		File soundFile = new File(path);
		AudioInputStream reusableAudioInputStream = AudioManager.createReusableAudioInputStream(soundFile);
		AudioManager.soundsMap.put(identifier, reusableAudioInputStream);
	}

	public static final void playSound(String identifier) throws LineUnavailableException, IOException, InterruptedException {
		AudioInputStream stream = AudioManager.soundsMap.get(identifier);
		stream.reset();
		AudioManager.playSound(identifier, stream);
	}

	private static final void playSound(String identifier, AudioInputStream audioInputStream) throws LineUnavailableException, IOException, InterruptedException {
		class AudioListener implements LineListener {
			private boolean isDone = false;

			@Override
			public synchronized void update(LineEvent event) {
				Type eventType = event.getType();
				if ((eventType == Type.CLOSE)) {
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
						} catch (InterruptedException interruptedException) {
							interruptedException.printStackTrace();
						}
					} finally {
						clip.close();
						AudioManager.clipsMap.remove(identifier);
					}
				} catch (LineUnavailableException | IOException exception) {
					exception.printStackTrace();
				} finally {
					try {
						audioInputStream.close();
					} catch (IOException ioException) {
						ioException.printStackTrace();
					}
				}
			}
		});
		thread.start();
		AudioManager.threadsMap.put(identifier, thread);
	}

	public static final float getVolume(String identifier) {
		Clip clip = AudioManager.clipsMap.get(identifier);
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		return (float) Math.pow(10f, gainControl.getValue() / 20f);
	}

	public static final void setVolume(String identifier, float volume) {
		Clip clip = AudioManager.clipsMap.get(identifier);
		if ((volume < 0f) || (volume > 1f)) {
			throw new IllegalArgumentException("Volume not valid: " + volume);
		}
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(20f * (float) Math.log10(volume));
	}

	public static final void pauseSound(String identifier) {
		AudioManager.clipsMap.get(identifier).stop();
	}

	public static final void resumeSound(String identifier) {
		AudioManager.clipsMap.get(identifier).start();
	}

	public static final void stopSound(String identifier) {
		AudioManager.clipsMap.get(identifier).stop();
		AudioManager.threadsMap.get(identifier).interrupt();
	}

	public static final void stopAllSounds() {
		for (AudioInputStream audioInputStream : AudioManager.soundsMap.values()) {
			try {
				audioInputStream.reset();
				audioInputStream.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
		for (Clip clip : AudioManager.clipsMap.values()) {
			clip.stop();
		}
		for (Thread thread : AudioManager.threadsMap.values()) {
			thread.interrupt();
		}
	}

	public static final void loopSound(String identifier, int count) throws IOException, LineUnavailableException, InterruptedException {
		AudioInputStream stream = AudioManager.soundsMap.get(identifier);
		stream.reset();
		AudioManager.loopSound(identifier, count, stream);
	}

	private static final void loopSound(String identifier, int count, AudioInputStream audioInputStream) throws LineUnavailableException, IOException, InterruptedException {
		class AudioListener implements LineListener {
			private boolean isDone = false;

			@Override
			public synchronized void update(LineEvent event) {
				Type eventType = event.getType();
				if ((eventType == Type.STOP) || (eventType == Type.CLOSE)) {
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
					} catch (InterruptedException interruptedException) {
						interruptedException.printStackTrace();
					} finally {
						clip.close();
						AudioManager.clipsMap.remove(identifier);
					}
				} catch (LineUnavailableException | IOException exception) {
					exception.printStackTrace();
				} finally {
					try {
						audioInputStream.close();
					} catch (IOException ioException) {
						ioException.printStackTrace();
					}
				}
			}
		});
		thread.start();
		AudioManager.threadsMap.put(identifier, thread);
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
