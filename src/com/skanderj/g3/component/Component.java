package com.skanderj.g3.component;

import java.awt.Graphics2D;

import com.skanderj.g3.inputdevice.Keyboard;
import com.skanderj.g3.inputdevice.Mouse;
import com.skanderj.g3.window.Window;

public interface Component {
	public void update(double delta, Keyboard keyboard, Mouse mouse, Object... args);

	public void render(Window window, Graphics2D graphics, Object... args);

	public boolean canChangeFocus();

	public void grantFocus();

	public void revokeFocus();

	public boolean containsMouse(int x, int y);
}
