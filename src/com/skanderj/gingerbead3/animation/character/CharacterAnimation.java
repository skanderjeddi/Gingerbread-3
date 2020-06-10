package com.skanderj.gingerbead3.animation.character;

import java.awt.Graphics2D;

import com.skanderj.gingerbead3.animation.Animation;
import com.skanderj.gingerbead3.display.Window;
import com.skanderj.gingerbead3.input.Keyboard;
import com.skanderj.gingerbead3.input.Mouse;
import com.skanderj.gingerbead3.sprite.Sprite;

/**
 * A (very) basic character animation.
 * 
 * @author Skander
 *
 */
public class CharacterAnimation implements Animation {
	private final int x, y;
	private final Sprite[] sprites;
	private final int[] timers;
	private int currentSpriteIndex, currentSpriteTimer;

	public CharacterAnimation(int x, int y, Sprite[] sprites, int[] timers) {
		this.x = x;
		this.y = y;
		this.sprites = sprites;
		this.timers = timers;
		this.currentSpriteIndex = 0;
		this.currentSpriteTimer = 0;
	}

	@Override
	public void update(double delta, Keyboard keyboard, Mouse mouse, Object... args) {
		this.currentSpriteTimer += 1;
		if (this.currentSpriteTimer >= this.timers[this.currentSpriteIndex]) {
			this.currentSpriteIndex += 1;
			this.currentSpriteIndex %= this.sprites.length;
			this.currentSpriteTimer = 0;
		}
	}

	@Override
	public void render(Window window, Graphics2D graphics, Object... args) {
		graphics.drawImage(this.sprites[this.currentSpriteIndex].getImage(), this.x, this.y, this.sprites[this.currentSpriteIndex].getWidth(), this.sprites[this.currentSpriteIndex].getHeight(), null);
	}
}
