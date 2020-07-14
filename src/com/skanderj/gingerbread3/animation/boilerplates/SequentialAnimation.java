package com.skanderj.gingerbread3.animation.boilerplates;

import java.util.Arrays;

import com.skanderj.gingerbread3.animation.Animation;
import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.sprite.Sprite;

/**
 * A (very) basic character animation.
 *
 * @author Nim
 *
 */
public class SequentialAnimation extends Animation {
	private int x, y;
	private final Sprite[] sprites;
	private final int[] timers;
	private int currentSpriteIndex, currentSpriteTimer;
	private boolean playing;

	public SequentialAnimation(final Application application, final int x, final int y, final Sprite[] sprites, final int[] timers) {
		super(application);
		this.x = x;
		this.y = y;
		this.sprites = sprites;
		this.timers = timers;
		this.currentSpriteIndex = 0;
		this.currentSpriteTimer = 0;
		this.playing = true;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public synchronized void update() {
		if (this.playing) {
			this.currentSpriteTimer += 1;
			if (this.currentSpriteTimer >= this.timers[this.currentSpriteIndex]) {
				this.currentSpriteIndex++;
				if (this.currentSpriteIndex == this.sprites.length) {
					this.currentSpriteIndex = 0;
				}
				this.currentSpriteTimer = 0;
			}
		}
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public synchronized void render(final Screen screen) {
		screen.image(this.sprites[this.currentSpriteIndex].image(), this.x, this.y, this.sprites[this.currentSpriteIndex].getWidth(), this.sprites[this.currentSpriteIndex].getHeight());
	}

	/**
	 * Self explanatory.
	 */
	public void pause() {
		this.playing = false;
	}

	/**
	 * Self explanatory.
	 */
	public void play() {
		this.playing = true;
	}

	/**
	 * Self explanatory.
	 */
	public void reset() {
		this.currentSpriteIndex = 0;
	}

	/**
	 * Self explanatory.
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Self explanatory.
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * Self explanatory.
	 */
	public Sprite[] sprites() {
		return this.sprites;
	}

	/**
	 * Self explanatory.
	 */
	public int[] timers() {
		return this.timers;
	}

	/**
	 * Self explanatory.
	 */
	public void setX(final int x) {
		this.x = x;
	}

	/**
	 * Self explanatory.
	 */
	public void setY(final int y) {
		this.y = y;
	}

	/**
	 * Self explanatory.
	 */
	public int getCurrentSpriteIndex() {
		return this.currentSpriteIndex;
	}

	/**
	 * Self explanatory.
	 */
	public void setCurrentSpriteIndex(final int index) {
		this.currentSpriteIndex = index;
	}

	@Override
	public Priority priority() {
		return Priority.LOW;
	}

	@Override
	public String description() {
		final Integer[] timersAsObjects = new Integer[this.timers.length];
		for (int t = 0; t < timersAsObjects.length; t += 1) {
			timersAsObjects[t] = this.timers[t];
		}
		return Engine.identifier(this) + " -> SequentialAnimation.class(" + this.x + ", " + this.y + ", " + Arrays.deepToString(this.sprites) + ", " + Arrays.deepToString(timersAsObjects) + ")";
	}
}
