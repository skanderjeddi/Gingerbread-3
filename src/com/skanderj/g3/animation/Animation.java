package com.skanderj.g3.animation;

import java.awt.Graphics2D;

import com.skanderj.g3.display.Window;
import com.skanderj.g3.input.Keyboard;
import com.skanderj.g3.input.Mouse;

public interface Animation {
	// Logic happens here
	void update(double delta, Keyboard keyboard, Mouse mouse, Object... args);

	// Rendering happens here
	void render(Window window, Graphics2D graphics, Object... args);
}
