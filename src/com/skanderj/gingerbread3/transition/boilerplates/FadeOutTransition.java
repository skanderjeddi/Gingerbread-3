package com.skanderj.gingerbread3.transition.boilerplates;

import java.awt.Color;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.transition.Transition;
import com.skanderj.gingerbread3.util.Utilities;

/**
 * A simple fade in transition.
 *
 * @author skand
 *
 */
public class FadeOutTransition extends Transition {
	private final Color color;

	public FadeOutTransition(final Application application, final int duration, final Color color) {
		super(application, duration);
		this.color = color;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void update() {
		super.update();
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void render(final Screen screen) {
		if (!this.isDone) {
			int alpha = 0;
			alpha = (int) Utilities.map(this.timer, 0, this.duration, 0, 255, true);
			final Color newColor = new Color(this.color.getRed(), this.color.getBlue(), this.color.getGreen(), alpha);
			screen.rectangle(newColor, 0, 0, this.application.window().getWidth(), this.application.window().getHeight(), true);
		}
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void sceneChange() {
		super.sceneChange();
	}

	@Override
	public String description() {
		return Engine.identifier(this) + " -> FadeOutTransition.class(" + this.duration + ", " + this.color + ")";
	}
}
