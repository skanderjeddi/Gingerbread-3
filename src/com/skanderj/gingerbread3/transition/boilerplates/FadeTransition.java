package com.skanderj.gingerbread3.transition.boilerplates;

import java.awt.Color;

import com.skanderj.gingerbread3.core.G3Application;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.display.Screen;
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

	public FadeTransition(final G3Application g3Application, final int duration, final Color color) {
		super(g3Application, duration);
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
		int alpha = 0;
		alpha = (int) Utilities.map(this.timer, 0, this.duration, 255, 0, false);
		final Color newColor = new Color(this.color.getRed(), this.color.getBlue(), this.color.getGreen(), alpha);
		screen.rectangle(newColor, 0, 0, this.g3Application.window().getWidth(), this.g3Application.window().getHeight(), true);
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
		return Registry.identifier(this) + " -> FadeTransition.class(" + this.duration + ", " + this.color + ")";
	}
}
