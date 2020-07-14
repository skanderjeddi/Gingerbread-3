package com.skanderj.gingerbread3.animation;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.object.ApplicationObject;
import com.skanderj.gingerbread3.sprite.Sprite;

/**
 * Animation interace, basis for all other animation classes.
 *
 * @author Skander
 *
 */
public abstract class Animation extends ApplicationObject {
	protected int x, y;
	protected final Sprite[] sprites;
	protected final int[] timers;
	protected int currentSpriteIndex, currentSpriteTimer;
	protected boolean playing;

	public Animation(final Application application, final int x, final int y, final Sprite[] sprites, final int[] timers) {
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
}
