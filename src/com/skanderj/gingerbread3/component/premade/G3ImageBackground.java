package com.skanderj.gingerbread3.component.premade;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.skanderj.gingerbread3.component.ComponentPriority;
import com.skanderj.gingerbread3.component.unit.Background;
import com.skanderj.gingerbread3.display.Window;
import com.skanderj.gingerbread3.input.Keyboard;
import com.skanderj.gingerbread3.input.Mouse;

public class G3ImageBackground extends Background {
	private int x, y, width, height;
	private BufferedImage image;

	public G3ImageBackground(final int x, final int y, final int width, final int height, final BufferedImage image) {
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
	public void update(final double delta, final Keyboard keyboard, final Mouse mouse, final Object... args) {
		return;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void render(final Window window, final Graphics2D graphics, final Object... args) {
		graphics.drawImage(this.image, this.x, this.y, this.width, this.height, null);
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