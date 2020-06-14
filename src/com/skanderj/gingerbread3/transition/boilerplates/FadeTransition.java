package com.skanderj.gingerbread3.transition.boilerplates;

import java.awt.Color;

import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.display.GraphicsWrapper;
import com.skanderj.gingerbread3.transition.Transition;
import com.skanderj.gingerbread3.util.Utilities;

/**
 * A simple fade in transition.
 *
 * @author skand
 *
 */
public class FadeTransition extends Transition {
	private final Color color;

	public FadeTransition(final Game game, final int duration, final Color color) {
		super(game, duration);
		this.color = color;
	}

	@Override
	public void update(final double delta, final Object... args) {
		super.update(delta, args);
	}

	@Override
	public void render(final GraphicsWrapper graphics, final Object... args) {
		int alpha = 0;
		alpha = (int) Utilities.map(this.timer, 0, this.duration, 255, 0, false);
		final Color newColor = new Color(this.color.getRed(), this.color.getBlue(), this.color.getGreen(), alpha);
		graphics.rectangle(newColor, 0, 0, this.game.getWindow().getWidth(), this.game.getWindow().getHeight(), true);
	}

	@Override
	public void sceneChange() {
		super.sceneChange();
	}
}
