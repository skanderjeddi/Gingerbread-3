package com.skanderj.gingerbread3.component;

import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.object.GameObject;

/**
 * Represents a custom graphic component. Placeholder interface for batch
 * updating and rendering.
 *
 * @author Skander
 *
 */
public abstract class Component extends GameObject {
	public Component(final Game game) {
		super(game);
	}

	@Override
	public abstract Priority priority();

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
}
