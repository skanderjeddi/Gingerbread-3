package com.skanderj.gingerbread3.animation;

import java.util.Arrays;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.sprite.Sprite;
import com.skanderj.gingerbread3.util.Utilities;

/**
 * A (very) basic character animation.
 *
 * @author Skander
 *
 */
public class RandomizedAnimation extends Animation {
	private int x, y;
	private final Sprite[] sprites;
	private final int[] timers;
	private int currentSpriteIndex, currentSpriteTimer;

	public RandomizedAnimation(final Application application, final int x, final int y, final Sprite[] sprites, final int[] timers) {
		super(application);
		this.x = x;
		this.y = y;
		this.sprites = sprites;
		this.timers = timers;
		this.currentSpriteIndex = 0;
		this.currentSpriteTimer = 0;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public synchronized void update() {
		this.currentSpriteTimer += 1;
		if (this.currentSpriteTimer >= this.timers[this.currentSpriteIndex]) {
			this.currentSpriteIndex = this.newRandomSprite(this.currentSpriteIndex);
			this.currentSpriteIndex %= this.sprites.length;
			this.currentSpriteTimer = 0;
		}
	}

	/**
	 * Self explanatory.
	 */
	private final int newRandomSprite(final int previous) {
		int r = Utilities.randomInteger(0, this.sprites.length - 1);
		while (r == previous) {
			r = Utilities.randomInteger(0, this.sprites.length - 1);
		}
		return r;
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
		return Registry.identifier(this) + " -> RandomizedAnimation.class(" + this.x + ", " + this.y + ", " + Arrays.deepToString(this.sprites) + ", " + Arrays.deepToString(timersAsObjects) + ")";
	}
}
