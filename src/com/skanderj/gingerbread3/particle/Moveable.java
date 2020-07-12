package com.skanderj.gingerbread3.particle;

import com.skanderj.gingerbread3.core.Renderable;

public interface Moveable extends Renderable {
	void setX(int x);

	void setY(int y);

	int x();

	int y();

	Moveable copy();
}
