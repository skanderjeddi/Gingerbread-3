package com.skanderj.gingerbread3.particle;

import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.object.GameObject;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.math.Vector2;
import com.skanderj.gingerbread3.sprite.Sprite;

/**
 *
 * @author Skander
 *
 */
public class Particle extends GameObject {
	protected int x, y;
	protected Sprite sprite;
	protected Vector2 velocity;

	public Particle(final Game game, final int x, final int y, final Sprite sprite, final Vector2 velocity) {
		super(game);
		this.x = x;
		this.y = y;
		this.sprite = sprite.copy();
		this.velocity = velocity;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public synchronized void update(final double delta, final Object... args) {
		final int limit = 40;
		if (Math.abs(this.velocity.x) > limit) {
			this.velocity.x = (int) (Math.signum(this.x) * limit);
		}
		if (Math.abs(this.velocity.y) > limit) {
			this.velocity.y = (int) (Math.signum(this.y) * limit);
		}
		this.x += this.velocity.x;
		this.y += this.velocity.y;
		this.sprite.update(delta, this.x, this.y);
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public synchronized void render(final Screen screen) {
		this.sprite.render(screen);
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public Priority priority() {
		return Priority.REGULAR;
	}
}
