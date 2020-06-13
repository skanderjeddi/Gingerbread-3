package com.skanderj.gingerbread3.particle;

import java.awt.Graphics2D;

import com.skanderj.gingerbread3.log.Logger;
import com.skanderj.gingerbread3.log.Logger.LogLevel;
import com.skanderj.gingerbread3.math.Vector2D;
import com.skanderj.gingerbread3.sprite.Sprite;
import com.skanderj.gingerbread3.util.Utilities;

/**
 *
 * @author Skander
 *
 */
public final class ParticleManager {
	private final int centerX, centerY, radius;
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
	public ParticleManager(final int centerX, final int centerY, final int radius, final int particlesCount, final Sprite[] sprites, final Vector2D[] accelerations, final int chaosValue, final int updateRate) {
		if (accelerations.length != particlesCount) {
			Logger.log(ParticleManager.class, LogLevel.FATAL, "Size mismatch between particles count and accelerations array size");
		}
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = radius;
		this.particles = new Particle[particlesCount];
		for (int i = 0; i < particlesCount; i += 1) {
			final int randomX = centerX + Utilities.randomInteger(-radius, radius);
			final int randomY = centerY + Utilities.randomInteger(-radius, radius);
			final Sprite randomSprite = sprites[Utilities.randomInteger(0, sprites.length - 1)];
			final Vector2D acceleration = accelerations[i];
			this.particles[i] = new Particle(randomX, randomY, randomSprite, acceleration);
		}
		this.chaosValue = chaosValue;
		this.updateRate = updateRate;
		this.updatesCounter = 0;
	}

	public synchronized void update(final double delta) {
		this.updatesCounter += 1;
		if ((this.updatesCounter % this.updateRate) == 0) {
			for (final Particle particle : this.particles) {
				particle.velocity.setX(particle.velocity.getX() + Utilities.randomInteger(0, this.chaosValue));
				particle.velocity.setY(particle.velocity.getY() + Utilities.randomInteger(0, this.chaosValue));
				particle.update(delta);
				if ((particle.x > (this.centerX + this.radius)) || (particle.x < (this.centerX - this.radius))) {
					final int randomX = this.centerX + Utilities.randomInteger(-this.radius, this.radius);
					particle.x = randomX;
				}
				if ((particle.y > (this.centerY + this.radius)) || (particle.y < (this.centerY - this.radius))) {
					final int randomY = this.centerY + Utilities.randomInteger(-this.radius, this.radius);
					particle.y = randomY;
				}
			}
		}
	}

	public synchronized void render(final Graphics2D graphics) {
		for (final Particle particle : this.particles) {
			particle.render(graphics);
		}
	}
}
