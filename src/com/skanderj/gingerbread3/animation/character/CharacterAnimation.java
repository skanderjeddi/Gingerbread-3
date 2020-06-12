package com.skanderj.gingerbread3.animation.character;

import java.awt.Graphics2D;

import com.skanderj.gingerbread3.animation.Animation;
import com.skanderj.gingerbread3.display.Window;
import com.skanderj.gingerbread3.input.Keyboard;
import com.skanderj.gingerbread3.input.Mouse;
import com.skanderj.gingerbread3.sprite.Sprite;

/**
 * A (very) basic character animation.
 *
 * @author Skander
 *
 */
public class CharacterAnimation implements Animation {
	private int x, y;
	private final Sprite[] sprites;
	private final int[] timers;
	private int currentSpriteIndex, currentSpriteTimer;

	public CharacterAnimation(final int x, final int y, final Sprite[] sprites, final int[] timers) {
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
			this.currentSpriteIndex += 1;
			this.currentSpriteIndex %= this.sprites.length;
			this.currentSpriteTimer = 0;
		}
	}

	@Override
	public void render(final Window window, final Graphics2D graphics, final Object... args) {
		graphics.drawImage(this.sprites[this.currentSpriteIndex].getImage(), this.x, this.y, this.sprites[this.currentSpriteIndex].getWidth(), this.sprites[this.currentSpriteIndex].getHeight(), null);
	}

	/**
	 * Self explanatory.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Self explanatory.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Self explanatory.
	 */
	public Sprite[] getSprites() {
		return sprites;
	}

	/**
	 * Self explanatory.
	 */
	public int[] getTimers() {
		return timers;
	}

	/**
	 * Self explanatory.
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Self explanatory.
	 */
	public void setY(int y) {
		this.y = y;
	}
}
