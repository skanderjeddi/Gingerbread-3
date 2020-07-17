package com.skanderj.gingerbread3.animation.boilerplates;

import java.util.Arrays;

import com.skanderj.gingerbread3.animation.Animation;
import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.particle.Moveable;
import com.skanderj.gingerbread3.sprite.Sprite;
import com.skanderj.gingerbread3.util.Utilities;

/**
 * A (very) basic character animation.
 *
 * @author Skander
 *
 */
public class RandomizedAnimation extends Animation {

	public RandomizedAnimation(final Application application, final double x, final double y, final Sprite[] sprites, final int[] timers) {
		super(application, y, y, sprites, timers);
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public synchronized void update() {
		if (this.playing) {
			this.currentSpriteTimer += 1;
			if (this.currentSpriteTimer >= this.timers[this.currentSpriteIndex]) {
				this.currentSpriteIndex = this.newRandomSprite(this.currentSpriteIndex);
				this.currentSpriteIndex %= this.sprites.length;
				this.currentSpriteTimer = 0;
			}
		}
	}

	/**
	 * Self explanatory.
	 */
	private final int newRandomSprite(final int previous) {
		int randomIndex = Utilities.randomInteger(0, this.sprites.length - 1);
		while (randomIndex == previous) {
			randomIndex = Utilities.randomInteger(0, this.sprites.length - 1);
		}
		return randomIndex;
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
	@Override
	public String description() {
		final Integer[] timersAsObjects = new Integer[this.timers.length];
		for (int t = 0; t < timersAsObjects.length; t += 1) {
			timersAsObjects[t] = this.timers[t];
		}
		return Engine.identifier(this) + " -> RandomizedAnimation.class(" + this.x + ", " + this.y + ", " + Arrays.deepToString(this.sprites) + ", " + Arrays.deepToString(timersAsObjects) + ")";
	}

	@Override
	public double x() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double y() {
		return this.x;
	}

	@Override
	public Moveable copy() {
		return new RandomizedAnimation(this.application, this.x, this.y, this.sprites, this.timers);
	}
}
