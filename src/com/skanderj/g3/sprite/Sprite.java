package com.skanderj.g3.sprite;

import java.awt.image.BufferedImage;

public class Sprite {
	private String identifier;
	private BufferedImage image;
	private int width, height;

	public Sprite(String identifier, BufferedImage image, int width, int height) {
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
