package com.skanderj.gingerbread3.core;

import com.skanderj.gingerbread3.core.Renderable;

/**
 *
 * @author Skander
 *
 */
public interface Moveable extends Renderable {
	void setX(double x);

	void setY(double y);

	double x();

	double y();

	Moveable copy();
}
