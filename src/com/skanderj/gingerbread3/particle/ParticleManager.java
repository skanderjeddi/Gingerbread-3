package com.skanderj.gingerbread3.particle;

import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.core.object.GameObject;
import com.skanderj.gingerbread3.core.object.GameObjectPriority;
import com.skanderj.gingerbread3.display.GraphicsWrapper;
import com.skanderj.gingerbread3.log.Logger;
import com.skanderj.gingerbread3.log.Logger.LogLevel;
import com.skanderj.gingerbread3.math.Vector2;
import com.skanderj.gingerbread3.sprite.Sprite;
import com.skanderj.gingerbread3.util.Utilities;

/**
 *
 * @author Skander
 *
 */
public final class ParticleManager extends GameObject {
	private final int centerX, centerY, radius, maxRadius;
	private final Particle[] particles;
	private final int chaosValue;
	private final int updateRate;
	private int updatesCounter;

	/**
	 *
	 * @param centerX        x center of the spawn circle
	 * @param centerY        y center of the spawn circle
	 * @param radius
	 * @param particlesCount
	 * @param sprites
	 * @param accelerations
	 * @param chaosValue
	 * @param updateRate     how many frames before each update
	 */
	public ParticleManager(final Game game, final int centerX, final int centerY, final int radius, final int maxRadius, final int particlesCount, final Sprite[] sprites, final Vector2[] accelerations, final int chaosValue, final int updateRate) {
		super(game);
		if (accelerations.length != particlesCount) {
			Logger.log(ParticleManager.class, LogLevel.FATAL, "Size mismatch between particles count and accelerations array size");
		}
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = radius;
		this.maxRadius = maxRadius;
		this.particles = new Particle[particlesCount];
		for (int i = 0; i < particlesCount; i += 1) {
			final int randomX = centerX + Utilities.randomInteger(-radius, radius);
			final int randomY = centerY + Utilities.randomInteger(-radius, radius);
			final Sprite randomSprite = sprites[Utilities.randomInteger(0, sprites.length - 1)];
			final Vector2 acceleration = accelerations[i];
			this.particles[i] = new Particle(game, randomX, randomY, randomSprite, acceleration);
		}
		this.chaosValue = chaosValue;
		this.updateRate = updateRate;
		this.updatesCounter = 0;
	}

	@Override
	public synchronized void update(final double delta, final Object... args) {
		this.updatesCounter += 1;
		if ((this.updatesCounter % this.updateRate) == 0) {
			for (final Particle particle : this.particles) {
				particle.velocity.setX(particle.velocity.getX() + Utilities.randomInteger(-this.chaosValue, this.chaosValue));
				particle.velocity.setY(particle.velocity.getY() + Utilities.randomInteger(-this.chaosValue, this.chaosValue));
				particle.update(delta);
				if ((particle.x > (this.centerX + this.maxRadius)) || (particle.x < (this.centerX - this.maxRadius))) {
					final int randomX = this.centerX + Utilities.randomInteger(-this.radius, this.radius);
					particle.x = randomX;
				}
				if ((particle.y > (this.centerY + this.maxRadius)) || (particle.y < (this.centerY - this.maxRadius))) {
					final int randomY = this.centerY + Utilities.randomInteger(-this.radius, this.radius);
					particle.y = randomY;
				}
			}
		}
	}

	@Override
	public synchronized void render(final GraphicsWrapper graphics, final Object... args) {
		for (final Particle particle : this.particles) {
			particle.render(graphics);
		}
	}

	@Override
	public GameObjectPriority priority() {
		return GameObjectPriority.REGULAR;
	}
}
