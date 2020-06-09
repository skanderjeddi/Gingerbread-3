package com.skanderj.gingerbead3.util;

import java.awt.Color;
import java.awt.Graphics2D;

import com.skanderj.gingerbead3.display.Window;

public final class GraphicsUtilities {
	public static final int DEFAULT_ORIGIN_X = 0, DEFAULT_ORIGIN_Y = GraphicsUtilities.DEFAULT_ORIGIN_X;

	private GraphicsUtilities() {
		return;
	}

	public static void clear(Window window, Graphics2D graphics, Color color) {
		graphics.setColor(color);
		graphics.fillRect(0, 0, window.getWidth(), window.getHeight());
	}
}
