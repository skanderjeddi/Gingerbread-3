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
	private Color backgroundColor, foregroundColor;
	private Font font;
	private List<String> text = new ArrayList<String>();
	private String currentString;
	private boolean hatCarry, twoPointsCarry, cursorBlink;
	private int blinkRate, blinkTimer;
	private boolean multiline, hasFocus;
	private int cursor, lineCounter;

	public Textfield(int x, int y, int width, int height, Color background, Color foreground, Font font, boolean multiline) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.backgroundColor = background;
		this.foregroundColor = foreground;
		this.font = font;
		this.currentString = new String();
		this.multiline = multiline;
		this.hatCarry = false;
		this.twoPointsCarry = false;
		this.cursorBlink = false;
		this.blinkRate = 15;
		this.blinkTimer = 0;
		this.multiline = multiline;
		this.hasFocus = false;
		this.cursor = 0;
		this.lineCounter = 0;
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
				if (keyCode == Keyboard.KEY_LEFT) {
					this.cursor -= 1;
					if (this.cursor < 0) {
						this.cursor = 0;
					}
					break;
				}
				if (keyCode == Keyboard.KEY_RIGHT) {
					this.cursor += 1;
					if (this.cursor >= this.currentString.length()) {
						this.cursor = this.currentString.length();
					}
					break;
				}
				if (keyCode == Keyboard.KEY_ENTER) {
					if (this.multiline) {
						this.text.add(this.currentString);
						this.currentString = "";
						this.cursor = 0;
						break;
					}
				}
				if (keyCode == Keyboard.KEY_BACK_SPACE) {
					if (!this.currentString.isEmpty() && (this.cursor != 0)) {
						char[] newChar = new char[this.currentString.length() - 1];
						boolean hasSkipped = false;
						for (int index = 0; index < (this.currentString.length() - 1); index += 1) {
							if (index == (this.cursor - 1)) {
								hasSkipped = true;
							}
							newChar[index] = this.currentString.toCharArray()[hasSkipped ? index + 1 : index];
						}
						this.currentString = new String(newChar);
						this.cursor -= 1;
						break;
					}
				}
				if (keyCode == Keyboard.KEY_DELETE) {
					if (this.cursor == this.currentString.length()) {
						break;
					} else {
						char[] newChar = new char[this.currentString.length() - 1];
						boolean hasSkipped = false;
						for (int index = 0; index < (this.currentString.length() - 1); index += 1) {
							if (index == this.cursor) {
								hasSkipped = true;
							}
							newChar[index] = this.currentString.toCharArray()[hasSkipped ? index + 1 : index];
						}
						this.currentString = new String(newChar);
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
				char[] newChar = null;
				if (key.equals("    ")) {
					newChar = new char[this.currentString.toCharArray().length + 4];
					newChar[this.cursor] = key.charAt(0);
					newChar[this.cursor + 1] = key.charAt(0);
					newChar[this.cursor + 2] = key.charAt(0);
					newChar[this.cursor + 3] = key.charAt(0);
					for (int index = 0; index < this.cursor; index += 1) {
						newChar[index] = this.currentString.toCharArray()[index];
					}
					for (int index = this.cursor + 4; index < newChar.length; index += 1) {
						newChar[index] = this.currentString.toCharArray()[index - 4];
					}
				} else {
					if (!key.isEmpty()) {
						newChar = new char[this.currentString.toCharArray().length + 1];
						newChar[this.cursor] = key.charAt(0);
						for (int index = 0; index < this.cursor; index += 1) {
							newChar[index] = this.currentString.toCharArray()[index];
						}
						for (int index = this.cursor + 1; index < newChar.length; index += 1) {
							newChar[index] = this.currentString.toCharArray()[index - 1];
						}
					}
				}
				if (newChar != null) {
					this.currentString = new String(newChar);
					if (key.equals("    ")) {
						this.cursor += 4;
					} else {
						this.cursor += 1;
					}
				}
			}
			this.blinkTimer += 1;
			if ((this.blinkTimer % this.blinkRate) == 0) {
				this.cursorBlink = !this.cursorBlink;
			}
		}
	}

	public synchronized final void render(Window window, Graphics2D graphics) {
		graphics.setColor(this.backgroundColor);
		graphics.fillRect(this.x, this.y, this.width, this.height);
		graphics.setColor(this.backgroundColor.darker().darker());
		graphics.drawRect(this.x, this.y, this.width, this.height);
		graphics.setColor(this.foregroundColor);
		graphics.setFont(this.font);
		FontMetrics fontMetrics = graphics.getFontMetrics();
		this.lineCounter = 0;
		int cursorX = 0, cursorY = 0, cursorWidth = 0, cursorHeight = 0;
		if (this.multiline) {
			for (String line : this.text) {
				graphics.drawString(line, this.x + 10, (this.y - (fontMetrics.getHeight() / 10)) + (fontMetrics.getHeight() * (this.lineCounter + 1)));
				this.lineCounter += 1;
			}
			graphics.drawString(this.currentString, this.x + 10, (this.y - (fontMetrics.getHeight() / 10)) + (fontMetrics.getHeight() * (this.lineCounter + 1)));
			cursorY = (this.y - (fontMetrics.getHeight() / 7)) + (((fontMetrics.getHeight() * this.lineCounter) + (fontMetrics.getHeight() / 2)) - (fontMetrics.getDescent() / 2));
			cursorX = this.x + this.stringWidth(fontMetrics, this.currentString, this.cursor) + 9;
			cursorWidth = 2;
			cursorHeight = fontMetrics.getAscent() - (fontMetrics.getDescent() / 2);
		} else {
			this.drawCenteredString(graphics, this.currentString, this.x + 10, this.y, this.height, this.font, this.foregroundColor);
			cursorY = this.height + (this.height / 10);
			cursorX = this.x + this.stringWidth(fontMetrics, this.currentString, this.cursor) + 9;
			cursorWidth = 2;
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

	private final int stringWidth(FontMetrics metrics, String string, int cursor) {
		int finalWidth = 0;
		int counter = 0;
		for (char character : string.toCharArray()) {
			if (counter >= cursor) {
				break;
			}
			finalWidth += metrics.charWidth(character);
			counter += 1;
		}
		return finalWidth;
	}

	public final int drawCenteredString(Graphics2D graphics, String string, int x0, int y0, int height, Font font, Color color) {
		graphics.setFont(font);
		graphics.setColor(color);
		final FontMetrics fontMetrics = graphics.getFontMetrics();
		final Rectangle2D rectangle2d = fontMetrics.getStringBounds(string, graphics);
		final int y = ((height - (int) rectangle2d.getHeight()) / 2) + fontMetrics.getAscent();
		graphics.drawString(string, x0, y0 + y);
		return y0 + y;
	}
}
