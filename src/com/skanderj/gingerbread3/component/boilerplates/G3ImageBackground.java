package com.skanderj.gingerbread3.component.boilerplates;

import java.awt.image.BufferedImage;

import com.skanderj.gingerbread3.component.Background;
import com.skanderj.gingerbread3.component.ComponentPriority;
import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.display.GraphicsWrapper;

public class G3ImageBackground extends Background {
	private int x, y, width, height;
	private BufferedImage image;

	public G3ImageBackground(final Game game, final int x, final int y, final int width, final int height, final BufferedImage image) {
		super(game);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.image = image;
	}

	/**
	 * No need for logic.
	 */
	@Override
	public synchronized void update(final double delta, final Object... args) {
		return;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public synchronized void render(final GraphicsWrapper graphics, final Object... args) {
		graphics.image(this.image, this.x, this.y, this.width, this.height);
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public boolean containsMouse(final int x, final int y) {
		return false;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public int getX() {
		return this.x;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public int getY() {
		return this.y;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setX(final int x) {
		this.x = x;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setY(final int y) {
		this.y = y;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public int getWidth() {
		return this.width;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public int getHeight() {
		return this.height;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setWidth(final int width) {
		this.width = width;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setHeight(final int height) {
		this.height = height;
	}

	/**
	 * Self explanatory.
	 */
	public BufferedImage getBufferedImage() {
		return this.image;
	}

	/**
	 * Self explanatory.
	 */
	public void setBufferedImage(final BufferedImage image) {
		this.image = image;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public ComponentPriority priority() {
		return ComponentPriority.HIGH;
	}
}
