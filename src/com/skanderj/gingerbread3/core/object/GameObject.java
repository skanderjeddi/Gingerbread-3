package com.skanderj.gingerbread3.core.object;

import java.awt.Graphics2D;

import com.skanderj.gingerbread3.core.Game;

public abstract class GameObject {
	protected final Game game;

	public GameObject(final Game game) {
		this.game = game;
	}

	public abstract void update(double delta, Object... args);

	public abstract void render(Graphics2D graphics, Object... args);

	public Game getGame() {
		return this.game;
	}
}
