package com.skanderj.gingerbread3.animation.boilerplates;

import java.util.Arrays;

import com.skanderj.gingerbread3.animation.Animation;
import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.Moveable;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.sprite.Sprite;

/**
 * A (very) basic character animation.
 *
 * @author Nim
 *
 */
public class SequentialAnimation extends Animation {
	public SequentialAnimation(final Application application, final double x, final double y, final Sprite[] sprites, final int[] timers) {
		super(application, x, y, sprites, timers);
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
	@Override
	public String description() {
		final Integer[] timersAsObjects = new Integer[this.timers.length];
		for (int t = 0; t < timersAsObjects.length; t += 1) {
			timersAsObjects[t] = this.timers[t];
		}
		return Engine.identifier(this) + " -> SequentialAnimation.class(" + this.x + ", " + this.y + ", " + Arrays.deepToString(this.sprites) + ", " + Arrays.deepToString(timersAsObjects) + ")";
	}

	@Override
	public double x() {
		return this.x;
	}

	@Override
	public double y() {
		return this.y;
	}

	@Override
	public Moveable copy() {
		return new SequentialAnimation(this.application, this.x, this.y, this.sprites, this.timers);
	}
}
