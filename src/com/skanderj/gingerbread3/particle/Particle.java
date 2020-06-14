package com.skanderj.gingerbread3.particle;

import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.core.object.GameObject;
import com.skanderj.gingerbread3.display.GraphicsWrapper;
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
		this.sprite = sprite;
		this.velocity = velocity;
	}

	@Override
	public synchronized void update(final double delta, final Object... args) {
		this.x += this.velocity.getX();
		this.y += this.velocity.getY();
	}

	@Override
	public synchronized void render(final GraphicsWrapper graphics, final Object... args) {
		this.sprite.render(graphics, this.x, this.y);
	}
}
