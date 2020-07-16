package com.skanderj.gingerbread3.component.boilerplates;

import java.awt.image.BufferedImage;

import com.skanderj.gingerbread3.component.Background;
import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.display.Screen;

/**
 * Represents a basic image background.
 *
 * @author Skander
 *
 */
public class GBackgroundImage extends Background {
	private int x, y, width, height;
	private BufferedImage image;

	public GBackgroundImage(final Application application, final int x, final int y, final int width, final int height, final BufferedImage image) {
		super(application);
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
	public void update() {
		return;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void render(final Screen screen) {
		screen.image(this.image, this.x, this.y, this.width, this.height);
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
	public BufferedImage image() {
		return this.image;
	}

	/**
	 * Self explanatory.
	 */
	public void setImage(final BufferedImage image) {
		this.image = image;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public Priority priority() {
		return Priority.HIGH;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public String description() {
		return Engine.identifier(this) + " -> GBackgroundImage.class(" + this.x + ", " + this.y + ", " + this.image + ")";
	}
}
