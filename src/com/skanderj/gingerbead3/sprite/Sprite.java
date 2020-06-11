package com.skanderj.gingerbead3.sprite;

import java.awt.image.BufferedImage;

public class Sprite {
	private final String identifier;
	private final BufferedImage image;
	private final int width, height;

	public Sprite(final String identifier, final BufferedImage image, final int width, final int height) {
		this.identifier = identifier;
		this.image = image;
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public String getIdentifier() {
		return this.identifier;
	}

	public BufferedImage getImage() {
		return this.image;
	}
}
