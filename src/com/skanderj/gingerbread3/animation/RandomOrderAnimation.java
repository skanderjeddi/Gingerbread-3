package com.skanderj.gingerbread3.animation;

import java.awt.Graphics2D;

import com.skanderj.gingerbread3.display.Window;
import com.skanderj.gingerbread3.input.Keyboard;
import com.skanderj.gingerbread3.input.Mouse;
import com.skanderj.gingerbread3.sprite.Sprite;
import com.skanderj.gingerbread3.util.Utilities;

/**
 * A (very) basic character animation.
 *
 * @author Skander
 *
 */
public class RandomOrderAnimation implements Animation {
	private int x, y;
	private final Sprite[] sprites;
	private final int[] timers;
	private int currentSpriteIndex, currentSpriteTimer;

	public RandomOrderAnimation(final int x, final int y, final Sprite[] sprites, final int[] timers) {
		this.x = x;
		this.y = y;
		this.sprites = sprites;
		this.timers = timers;
		this.currentSpriteIndex = 0;
		this.currentSpriteTimer = 0;
	}

	@Override
	public void update(final double delta, final Keyboard keyboard, final Mouse mouse, final Object... args) {
		this.currentSpriteTimer += 1;
		if (this.currentSpriteTimer >= this.timers[this.currentSpriteIndex]) {
			this.currentSpriteIndex = this.newRandomSprite(this.currentSpriteIndex);
			this.currentSpriteIndex %= this.sprites.length;
			this.currentSpriteTimer = 0;
		}
	}

	private final int newRandomSprite(final int previous) {
		int r = Utilities.randomInteger(0, this.sprites.length - 1);
		while (r == previous) {
			r = Utilities.randomInteger(0, this.sprites.length - 1);
		}
		return r;
	}

	@Override
	public void render(final Window window, final Graphics2D graphics, final Object... args) {
		graphics.drawImage(this.sprites[this.currentSpriteIndex].getImage(), this.x, this.y, this.sprites[this.currentSpriteIndex].getWidth(), this.sprites[this.currentSpriteIndex].getHeight(), null);
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
	public Sprite[] getSprites() {
		return this.sprites;
	}

	/**
	 * Self explanatory.
	 */
	public int[] getTimers() {
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
}
