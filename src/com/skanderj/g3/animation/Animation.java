package com.skanderj.g3.animation;

import java.awt.Graphics2D;

import com.skanderj.g3.window.Window;
import com.skanderj.g3.window.inputdevice.Keyboard;
import com.skanderj.g3.window.inputdevice.Mouse;

public interface Animation {
	// Logic happens here
	public void update(double delta, Keyboard keyboard, Mouse mouse, Object... args);

	// Rendering happens here
	public void render(Window window, Graphics2D graphics, Object... args);
}
