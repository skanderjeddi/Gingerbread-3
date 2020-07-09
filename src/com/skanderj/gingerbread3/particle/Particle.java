package com.skanderj.gingerbread3.particle;

import com.skanderj.gingerbread3.core.G3Application;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.object.G3Object;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.math.Vector2;
import com.skanderj.gingerbread3.sprite.Sprite;

/**
 *
 * @author Skander
 *
 */
public class Particle extends G3Object {
	protected int x, y;
	protected Sprite sprite;
	protected Vector2 velocity;

	public Particle(final G3Application g3Application, final int x, final int y, final Sprite sprite, final Vector2 velocity) {
		super(g3Application);
		this.x = x;
		this.y = y;
		this.sprite = sprite.copy();
		this.velocity = velocity;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public synchronized void update(final double delta) {
		final int limit = 40;
		if (Math.abs(this.velocity.x) > limit) {
			this.velocity.x = (int) (Math.signum(this.x) * limit);
		}
		if (Math.abs(this.velocity.y) > limit) {
			this.velocity.y = (int) (Math.signum(this.y) * limit);
		}
		this.x += this.velocity.x;
		this.y += this.velocity.y;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public synchronized void render(final Screen screen) {
		screen.image(this.sprite.getImage(), this.x, this.y, this.sprite.getWidth(), this.sprite.getHeight());
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public Priority priority() {
		return Priority.REGULAR;
	}
}
