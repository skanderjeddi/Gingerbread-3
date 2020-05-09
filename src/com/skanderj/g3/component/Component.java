package com.skanderj.g3.component;

import java.awt.Graphics2D;

import com.skanderj.g3.inputdevice.Keyboard;
import com.skanderj.g3.inputdevice.Mouse;
import com.skanderj.g3.window.Window;

/**
 * Represents a custom graphic component. Placeholder interface for batch
 * updating and rendering.
 *
 * @author Skander
 *
 */
public interface Component {
	// Logic happens here
	public void update(double delta, Keyboard keyboard, Mouse mouse, Object... args);

	// Rendering happens here
	public void render(Window window, Graphics2D graphics, Object... args);

	// Focus related methods
	public boolean canChangeFocus();

	public void grantFocus();

	public void revokeFocus();

	// Self explanatory, implementation is child-component dependent
	public boolean containsMouse(int x, int y);
}
