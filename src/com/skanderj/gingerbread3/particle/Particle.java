package com.skanderj.gingerbread3.particle;

import java.awt.Graphics2D;

import com.skanderj.gingerbread3.math.Vector2;
import com.skanderj.gingerbread3.sprite.Sprite;

/**
 *
 * @author Skander
 *
 */
public class Particle {
	protected int x, y;
	protected Sprite sprite;
	protected Vector2 velocity;

	public Particle(final int x, final int y, final Sprite sprite, final Vector2 velocity) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
		this.velocity = velocity;
	}

	public synchronized void update(final double delta) {
		this.x += this.velocity.getX();
		this.y += this.velocity.getY();
	}

	public synchronized void render(final Graphics2D graphics) {
		this.sprite.render(graphics, this.x, this.y);
	}
}
