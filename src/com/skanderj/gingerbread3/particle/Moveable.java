package com.skanderj.gingerbread3.particle;

import com.skanderj.gingerbread3.core.Renderable;

public interface Moveable extends Renderable {
	void setX(double x);

	void setY(double y);

	double x();

	double y();

	Moveable copy();
}
