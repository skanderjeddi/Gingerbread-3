package com.skanderj.g3.component;

import java.awt.Graphics2D;

import com.skanderj.g3.window.Window;
import com.skanderj.g3.window.inputdevice.Keyboard;
import com.skanderj.g3.window.inputdevice.Mouse;

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

	// Self explanatory, implementation is child-component dependent
	public int getX();

	// Self explanatory, implementation is child-component dependent
	public int getY();

	// Self explanatory, implementation is child-component dependent
	public void setX(int x);

	// Self explanatory, implementation is child-component dependent
	public void setY(int y);

	// Self explanatory, implementation is child-component dependent
	public int getWidth();

	// Self explanatory, implementation is child-component dependent
	public int getHeight();

	// Self explanatory, implementation is child-component dependent
	public void setWidth(int width);

	// Self explanatory, implementation is child-component dependent
	public void setHeight(int height);
}
