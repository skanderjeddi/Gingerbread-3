package com.skanderj.gingerbread3.animation;

import java.awt.Graphics2D;

import com.skanderj.gingerbread3.display.Window;
import com.skanderj.gingerbread3.input.Keyboard;
import com.skanderj.gingerbread3.input.Mouse;

/**
 * Animation interace, basis for all other animation classes.
 *
 * @author Skander
 *
 */
public interface Animation {
	// Logic happens here
	void update(double delta, Keyboard keyboard, Mouse mouse, Object... args);

	// Rendering happens here
	void render(Window window, Graphics2D graphics, Object... args);
}
