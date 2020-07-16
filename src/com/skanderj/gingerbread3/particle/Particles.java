package com.skanderj.gingerbread3.particle;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.object.ApplicationObject;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.logging.Logger;
import com.skanderj.gingerbread3.logging.Logger.LogLevel;
import com.skanderj.gingerbread3.math.Vector2;
import com.skanderj.gingerbread3.util.Utilities;

/**
 *
 * @author Skander
 *
 */
public final class Particles extends ApplicationObject {
	private final int centerX, centerY, radius, maxRadius;
	private final Particle[] particles;
	private final double chaosValue;
	private final int updateRate;
	private int updatesCounter;

	/**
	 *
	 * @param centerX        x center of the spawn circle
	 * @param centerY        y center of the spawn circle
	 * @param radius
	 * @param particlesCount
	 * @param moveables
	 * @param accelerations
	 * @param chaosValue
	 * @param updateRate     how many frames before each update
	 */
	public Particles(final Application application, final int centerX, final int centerY, final int radius, final int maxRadius, final int particlesCount, final Moveable[] moveables, final Vector2[] accelerations, final double chaosValue, final int updateRate) {
		super(application);
		if (accelerations.length != particlesCount) {
			Logger.log(Particles.class, LogLevel.FATAL, "Size mismatch between particles count and accelerations array size");
		}
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = radius;
		this.maxRadius = maxRadius;
		this.particles = new Particle[particlesCount];
		for (int i = 0; i < particlesCount; i += 1) {
			final int randomX = centerX + Utilities.randomInteger(-radius, radius);
			final int randomY = centerY + Utilities.randomInteger(-radius, radius);
			final Moveable moveable = moveables[Utilities.randomInteger(0, moveables.length - 1)];
			final Vector2 acceleration = accelerations[i];
			moveable.setX(randomX);
			moveable.setY(randomY);
			this.particles[i] = new Particle(application, randomX, randomY, moveable, acceleration);
		}
		this.chaosValue = chaosValue;
		this.updateRate = updateRate;
		this.updatesCounter = 0;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public synchronized void update() {
		this.updatesCounter += 1;
		if ((this.updatesCounter % this.updateRate) == 0) {
			for (final Particle particle : this.particles) {
				// particle.velocity.x = particle.velocity.x +
				// Utilities.randomInteger(-this.chaosValue, this.chaosValue);
				// particle.velocity.y = particle.velocity.y +
				// Utilities.randomInteger(-this.chaosValue, this.chaosValue);
				particle.velocity.x = particle.velocity.x + Utilities.randomDouble(-this.chaosValue, this.chaosValue);
				particle.velocity.y = particle.velocity.y + Utilities.randomDouble(-this.chaosValue, this.chaosValue);
				particle.update();
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

	/**
	 * Self explanatory.
	 */
	@Override
	public synchronized void render(final Screen screen) {
		for (final Particle particle : this.particles) {
			particle.render(screen);
		}
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public Priority priority() {
		return Priority.EXTREMELY_LOW;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public String description() {
		return Engine.identifier(this) + " -> Particles(" + this.centerX + ", " + this.centerY + ", " + this.radius + ", " + this.maxRadius + ", " + this.particles.length + ", " + this.chaosValue + ")";
	}
}
