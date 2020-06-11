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
public abstract class Component implements Comparable<Component> {
	public abstract ComponentPriority priority();

	// Logic happens here
	public abstract void update(double delta, Keyboard keyboard, Mouse mouse, Object... args);

	// Rendering happens here
	public abstract void render(Window window, Graphics2D graphics, Object... args);

	// Focus related methods
	public abstract boolean canChangeFocus();

	public abstract void grantFocus();

	public abstract void revokeFocus();

	// Self explanatory, implementation is child-component dependent
	public abstract boolean containsMouse(int x, int y);

	// Self explanatory, implementation is child-component dependent
	public abstract int getX();

	// Self explanatory, implementation is child-component dependent
	public abstract int getY();

	// Self explanatory, implementation is child-component dependent
	public abstract void setX(int x);

	// Self explanatory, implementation is child-component dependent
	public abstract void setY(int y);

	// Self explanatory, implementation is child-component dependent
	public abstract int getWidth();

	// Self explanatory, implementation is child-component dependent
	public abstract int getHeight();

	// Self explanatory, implementation is child-component dependent
	public abstract void setWidth(int width);

	// Self explanatory, implementation is child-component dependent
	public abstract void setHeight(int height);

	// Priority comparison, could be nicer but flemme
	@Override
	public int compareTo(Component o) {
		return -(this.priority().priorityIndex - o.priority().priorityIndex);
	}
}
