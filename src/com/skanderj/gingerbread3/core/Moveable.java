package com.skanderj.gingerbread3.core;

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
