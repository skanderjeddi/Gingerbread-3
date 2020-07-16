package com.skanderj.gingerbread3.particle;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.object.ApplicationObject;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.math.Vector2;

/**
 *
 * @author Skander
 *
 */
public class Particle extends ApplicationObject {
	protected double x, y;
	protected Moveable moveable;
	protected Vector2 velocity;

	public Particle(final Application application, final int x, final int y, final Moveable moveable, final Vector2 velocity) {
		super(application);
		this.x = x;
		this.y = y;
		this.moveable = moveable.copy();
		this.velocity = velocity;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public synchronized void update() {
		final int limit = 40;
		if (Math.abs(this.velocity.x) > limit) {
			this.velocity.x = (Math.signum(this.x) * limit);
		}
		if (Math.abs(this.velocity.y) > limit) {
			this.velocity.y = (Math.signum(this.y) * limit);
		}
		this.x += this.velocity.x;
		this.y += this.velocity.y;
		this.moveable.setX(this.x);
		this.moveable.setY(this.y);
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public synchronized void render(final Screen screen) {
		this.moveable.render(screen);
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public Priority priority() {
		return Priority.REGULAR;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public String description() {
		return Engine.identifier(this) + " -> Particle.class(" + this.x + ", " + this.y + ", " + this.velocity + ")";
	}
}
