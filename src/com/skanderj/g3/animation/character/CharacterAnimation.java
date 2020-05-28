package com.skanderj.g3.animation.character;

import java.awt.Graphics2D;

import com.skanderj.g3.animation.Animation;
import com.skanderj.g3.sprite.Sprite;
import com.skanderj.g3.window.Window;
import com.skanderj.g3.window.inputdevice.Keyboard;
import com.skanderj.g3.window.inputdevice.Mouse;

public class CharacterAnimation implements Animation {
	private int x, y;
	private Sprite[] sprites;
	private int[] timers;
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
		graphics.drawImage(this.sprites[this.currentSpriteIndex].getImage(), x, y, this.sprites[this.currentSpriteIndex].getWidth(), this.sprites[this.currentSpriteIndex].getHeight(), null);
	}
}
