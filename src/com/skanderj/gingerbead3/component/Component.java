package com.skanderj.gingerbead3.component;

import java.awt.Graphics2D;

import com.skanderj.gingerbead3.display.Window;
import com.skanderj.gingerbead3.input.Keyboard;
import com.skanderj.gingerbead3.input.Mouse;

/**
 * Represents a custom graphic component. Placeholder interface for batch
 * updating and rendering.
 *
 * @author Skander
 *
 */
public interface Component {
	// Logic happens here
	void update(double delta, Keyboard keyboard, Mouse mouse, Object... args);

	// Rendering happens here
	void render(Window window, Graphics2D graphics, Object... args);

	// Focus related methods
	boolean canChangeFocus();

	void grantFocus();

	void revokeFocus();

	// Self explanatory, implementation is child-component dependent
	boolean containsMouse(int x, int y);

	// Self explanatory, implementation is child-component dependent
	int getX();

	// Self explanatory, implementation is child-component dependent
	int getY();

	// Self explanatory, implementation is child-component dependent
	void setX(int x);

	// Self explanatory, implementation is child-component dependent
	void setY(int y);

	// Self explanatory, implementation is child-component dependent
	int getWidth();

	// Self explanatory, implementation is child-component dependent
	int getHeight();

	// Self explanatory, implementation is child-component dependent
	void setWidth(int width);

	// Self explanatory, implementation is child-component dependent
	void setHeight(int height);
}
