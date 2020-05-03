package com.skanderj.g3.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.skanderj.g3.inputdevice.Keyboard;
import com.skanderj.g3.inputdevice.Keyboard.KeyState;
import com.skanderj.g3.inputdevice.Mouse;
import com.skanderj.g3.window.Window;

public class Textfield {
	private int x, y, width, height;
	private Color backgroundColor, textColor;
	private Font font;
	private List<String> toDraw = new ArrayList<String>();
	private String currentString = "";
	private boolean hatCarry = false, twoPointsCarry = false, cursorBlink = false;
	private int blinkRate = 30, blinkCounter = 0;
	private boolean canEnter = true;
	private boolean hasFocus = false;

	public Textfield(int x, int y, int width, int height, Color background, Color foreground, Font font, boolean canUseEnter) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.backgroundColor = background;
		this.textColor = foreground;
		this.font = font;
		this.canEnter = canUseEnter;
	}

	public final void giveFocus() {
		this.hasFocus = true;
	}

	public final void removeFocus() {
		this.hasFocus = false;
	}

	public boolean containsMouse(Mouse mouse) {
		return new Rectangle(this.x, this.y, this.width, this.height).contains(mouse.getX(), mouse.getY());
	}

	public synchronized void update(Window window, Keyboard keyboard, Mouse mouse) {
		if (this.hasFocus) {
			for (int keyCode : keyboard.getKeysByState(KeyState.DOWN_IN_FRAME)) {
				if (keyCode == Keyboard.KEY_ENTER) {
					if (this.canEnter) {
						this.toDraw.add(this.currentString);
						this.currentString = "";
						break;
					}
				}
				if (keyCode == Keyboard.KEY_BACK_SPACE) {
					if (!this.currentString.isEmpty()) {
						this.currentString = this.currentString.substring(0, this.currentString.length() - 1);
						break;
					}
				}
				String key = Keyboard.getKeyRepresentation(keyCode, keyboard.isShiftDown(), keyboard.isCapsLocked(), keyboard.isAltGrDown());
				if (key.equals("^") && !this.hatCarry) {
					this.hatCarry = true;
					break;
				}
				if (key.equals("¨") && !this.twoPointsCarry) {
					this.twoPointsCarry = true;
					break;
				}
				if (this.hatCarry) {
					if (key.equals("e")) {
						key = "ê";
					} else if (key.equals("o")) {
						key = "ô";
					} else if (key.equals("E")) {
						key = "Ê";
					} else if (key.equals("O")) {
						key = "Ô";
					} else if (key.equals("u")) {
						key = "û";
					} else if (key.equals("U")) {
						key = "Û";
					}
					this.hatCarry = false;
				}
				if (this.twoPointsCarry) {
					if (key.equals("e")) {
						key = "ë";
					} else if (key.equals("o")) {
						key = "ö";
					} else if (key.equals("E")) {
						key = "Ë";
					} else if (key.equals("O")) {
						key = "Ö";
					} else if (key.equals("u")) {
						key = "ü";
					} else if (key.equals("U")) {
						key = "Ü";
					}
					this.twoPointsCarry = false;
				}
				this.currentString += key;
			}
			this.blinkCounter += 1;
			if ((this.blinkCounter % this.blinkRate) == 0) {
				this.cursorBlink = !this.cursorBlink;
			}
		}
	}

	public synchronized final void render(Window window, Graphics2D graphics) {
		graphics.setColor(this.backgroundColor);
		graphics.fillRect(this.x, this.y, this.width, this.height);
		graphics.setColor(this.textColor);
		graphics.setFont(this.font);
		FontMetrics fontMetrics = graphics.getFontMetrics();
		int counter = 0, cursorX = 0, cursorY = 0, cursorWidth = 0, cursorHeight = 0;
		if (this.canEnter) {
			for (String s : this.toDraw) {
				graphics.drawString(s, this.x + 10, (this.y - (fontMetrics.getHeight() / 10)) + (fontMetrics.getHeight() * (counter + 1)));
				counter += 1;
			}
			graphics.drawString(this.currentString, this.x + 10, (this.y - (fontMetrics.getHeight() / 10)) + (fontMetrics.getHeight() * (counter + 1)));
			cursorY = (this.y - (fontMetrics.getHeight() / 7)) + (((fontMetrics.getHeight() * counter) + (fontMetrics.getHeight() / 2)) - (fontMetrics.getDescent() / 2));
			int width = fontMetrics.stringWidth(this.currentString);
			cursorX = this.x + width + 15;
			cursorWidth = 5;
			cursorHeight = fontMetrics.getAscent() - (fontMetrics.getDescent() / 2);
		} else {
			this.drawCenteredString(graphics, this.currentString, this.x + 10, this.y, this.height, this.font, this.textColor);
			int width = fontMetrics.stringWidth(this.currentString);
			cursorY = this.height + (this.height / 10);
			cursorX = this.x + width + 15;
			cursorWidth = 5;
			cursorHeight = this.height - (2 * (this.height / 10));
		}
		// CURSOR
		if (this.hasFocus) {
			{
				if (this.cursorBlink) {
					graphics.fillRect(cursorX, cursorY, cursorWidth, cursorHeight);
				}
			}
		}
	}

	public final int drawCenteredString(Graphics2D graphics, final String string, final int x0, final int y0, final int height, final Font font, final Color color) {
		graphics.setFont(font);
		graphics.setColor(color);
		final FontMetrics fontMetrics = graphics.getFontMetrics();
		final Rectangle2D rectangle2d = fontMetrics.getStringBounds(string, graphics);
		final int y = ((height - (int) rectangle2d.getHeight()) / 2) + fontMetrics.getAscent();
		graphics.drawString(string, x0, y0 + y);
		return y0 + y;
	}
}
