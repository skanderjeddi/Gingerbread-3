package com.skanderj.g3.component;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.skanderj.g3.inputdevice.Keyboard;
import com.skanderj.g3.inputdevice.Keyboard.KeyState;
import com.skanderj.g3.inputdevice.Mouse;
import com.skanderj.g3.util.GraphicString;
import com.skanderj.g3.util.TextProperties;
import com.skanderj.g3.util.Utilities;
import com.skanderj.g3.window.Window;

/**
 * Represents an abstract textbox, basic for other textbox classes which can
 * implement their rendering the way they please. See Textbox#Basic for a basic
 * but detailed example.
 * 
 * @author Skander
 *
 */
public abstract class Textbox implements Component {
	// Basic properties
	protected int x, y, width, height;
	protected boolean multiline;
	protected boolean hasFocus;
	protected List<String> text;
	protected String currentLine;
	protected boolean hatCarry, twoPointsCarry, cursorBlink, canAddLines;
	protected int blinkRate, blinkTimer, cursorPosition, linesCounter;

	// Basic constructor: position and width
	public Textbox(int x, int y, int width) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = 0;
		this.multiline = true;
		this.hasFocus = false;
		this.hatCarry = false;
		this.twoPointsCarry = false;
		this.cursorBlink = true;
		this.canAddLines = true;
		this.blinkRate = 15;
		this.blinkTimer = 0;
		this.cursorPosition = 0;
		this.linesCounter = 0;
		this.currentLine = new String();
		this.text = new ArrayList<String>();
	}

	@Override
	public void update(double delta, Keyboard keyboard, Mouse mouse, Object... args) {
		// Check if the component has global focus
		if (this.hasFocus) {
			// Go through every keyboard key and retain those which are pressed at the
			// current frame
			for (int keyCode : keyboard.getKeysByState(KeyState.DOWN_IN_FRAME)) {
				// Left key handling
				if (keyCode == Keyboard.KEY_LEFT) {
					this.cursorPosition -= 1;
					if (this.cursorPosition < 0) {
						this.cursorPosition = 0;
					}
					break;
				}
				// Right key handling
				if (keyCode == Keyboard.KEY_RIGHT) {
					this.cursorPosition += 1;
					if (this.cursorPosition >= this.currentLine.length()) {
						this.cursorPosition = this.currentLine.length();
					}
					break;
				}
				// Newline
				if (keyCode == Keyboard.KEY_ENTER) {
					if (this.multiline) {
						if (this.canAddLines) {
							this.text.add(this.currentLine);
							this.currentLine = new String();
							this.cursorPosition = 0;
							break;
						}
					}
				}
				// Deleting (backspace)
				if (keyCode == Keyboard.KEY_BACK_SPACE) {
					if (!this.currentLine.isEmpty() && (this.cursorPosition != 0)) {
						char[] newChar = new char[this.currentLine.length() - 1];
						boolean hasSkipped = false;
						for (int index = 0; index < (this.currentLine.length() - 1); index += 1) {
							if (index == (this.cursorPosition - 1)) {
								hasSkipped = true;
							}
							newChar[index] = this.currentLine.toCharArray()[hasSkipped ? index + 1 : index];
						}
						this.currentLine = new String(newChar);
						this.cursorPosition -= 1;
						break;
					}
				}
				// Deleting (delete)
				if (keyCode == Keyboard.KEY_DELETE) {
					if (this.cursorPosition == this.currentLine.length()) {
						break;
					} else {
						char[] newChar = new char[this.currentLine.length() - 1];
						boolean hasSkipped = false;
						for (int index = 0; index < (this.currentLine.length() - 1); index += 1) {
							if (index == this.cursorPosition) {
								hasSkipped = true;
							}
							newChar[index] = this.currentLine.toCharArray()[hasSkipped ? index + 1 : index];
						}
						this.currentLine = new String(newChar);
						break;
					}
				}
				// See Keyboard.getKeyRepresentation(), pretty self explanatory
				String key = Keyboard.getKeyRepresentation(keyCode, keyboard.isShiftDown(), keyboard.isCapsLocked(), keyboard.isAltGrDown());
				{
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
						} else if (key.equals("a")) {
							key = "â";
						} else if (key.equals("A")) {
							key = "Â";
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
				}
				// new char to add
				char[] newChar = null;
				// Handles TAB in particular, adds any other key
				{
					if (key.equals("    ")) {
						newChar = new char[this.currentLine.toCharArray().length + 4];
						newChar[this.cursorPosition] = key.charAt(0);
						newChar[this.cursorPosition + 1] = key.charAt(0);
						newChar[this.cursorPosition + 2] = key.charAt(0);
						newChar[this.cursorPosition + 3] = key.charAt(0);
						for (int index = 0; index < this.cursorPosition; index += 1) {
							newChar[index] = this.currentLine.toCharArray()[index];
						}
						for (int index = this.cursorPosition + 4; index < newChar.length; index += 1) {
							newChar[index] = this.currentLine.toCharArray()[index - 4];
						}
					} else {
						if (!key.isEmpty()) {
							newChar = new char[this.currentLine.toCharArray().length + 1];
							newChar[this.cursorPosition] = key.charAt(0);
							for (int index = 0; index < this.cursorPosition; index += 1) {
								newChar[index] = this.currentLine.toCharArray()[index];
							}
							for (int index = this.cursorPosition + 1; index < newChar.length; index += 1) {
								newChar[index] = this.currentLine.toCharArray()[index - 1];
							}
						}
					}
				}
				// I don't know anymore but it works
				{
					if (newChar != null) {
						this.currentLine = new String(newChar);
						if (key.equals("    ")) {
							this.cursorPosition += 4;
						} else {
							this.cursorPosition += 1;
						}
					}
				}
			}
			// Cursor blinking timer
			{
				this.blinkTimer += 1;
				if ((this.blinkTimer % this.blinkRate) == 0) {
					this.cursorBlink = !this.cursorBlink;
				}
			}
		}
	}

	/**
	 * Can alwaus move focus away from textbox.
	 */
	@Override
	public boolean canChangeFocus() {
		return true;
	}

	/**
	 * Gives global focus.
	 */
	@Override
	public void grantFocus() {
		this.hasFocus = true;
	}

	/**
	 * Removes global focus.
	 */
	@Override
	public void revokeFocus() {
		this.hasFocus = false;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public boolean containsMouse(int x, int y) {
		return new Rectangle(this.x, this.y, this.width, this.height).contains(x, y);
	}

	/**
	 * Self explanatory.
	 */
	public final int getX() {
		return x;
	}

	/**
	 * Self explanatory.
	 */
	public final int getY() {
		return y;
	}

	/**
	 * Self explanatory.
	 */
	public final int getWidth() {
		return width;
	}

	/**
	 * Self explanatory.
	 */
	public final int getHeight() {
		return height;
	}

	/**
	 * Self explanatory.
	 */
	public final boolean isMultiline() {
		return multiline;
	}

	/**
	 * Self explanatory.
	 */
	public final List<String> getText() {
		return text;
	}

	/**
	 * Self explanatory.
	 */
	public final String getCurrentLine() {
		return currentLine;
	}

	/**
	 * Self explanatory.
	 */
	public final int getBlinkRate() {
		return blinkRate;
	}

	/**
	 * Self explanatory.
	 */
	public final int getCursorPosition() {
		return cursorPosition;
	}

	/**
	 * Self explanatory.
	 */
	public final void setX(int x) {
		this.x = x;
	}

	/**
	 * Self explanatory.
	 */
	public final void setY(int y) {
		this.y = y;
	}

	/**
	 * Self explanatory.
	 */
	public final void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Self explanatory.
	 */
	public final void setBlinkRate(int blinkRate) {
		this.blinkRate = blinkRate;
	}

	/**
	 * Self explanatory.
	 */
	public final void setCursorPosition(int cursorPosition) {
		this.cursorPosition = cursorPosition;
	}

	/**
	 * A very basic textbox.
	 * 
	 * @author Skander
	 *
	 */
	public static class Basic extends Textbox {
		private Color backgroundColor;
		private TextProperties textProperties;
		private int lines;

		/**
		 * Background color for rendering a simple box and text properties for the font
		 * and color, by default can only display 1 line.
		 */
		public Basic(int x, int y, int width, Color backgroundColor, TextProperties textProperties) {
			this(x, y, width, backgroundColor, textProperties, 0);
		}

		/**
		 * Background color for rendering a simple box, text properties for the font and
		 * color, and amount lines to display.
		 */
		public Basic(int x, int y, int width, Color backgroundColor, TextProperties textProperties, int lines) {
			super(x, y, width);
			this.backgroundColor = backgroundColor;
			this.textProperties = textProperties;
			this.lines = lines;
			this.multiline = !(lines == 1);
		}

		/**
		 * The rendering routine.
		 */
		@Override
		public void render(Window window, Graphics2D graphics, Object... args) {
			graphics.setFont(this.textProperties.getFont());
			FontMetrics metrics = graphics.getFontMetrics();
			int fontHeight = metrics.getHeight();
			// Determine height if not done before (= 0)
			{
				if (this.height == 0) {
					this.height = (metrics.getHeight() * this.lines) + (metrics.getHeight() / 2);
				}
			}
			// Background
			{
				graphics.setColor(this.backgroundColor);
				graphics.fillRect(this.x, this.y, this.width, this.height);
			}
			// Border
			{
				graphics.setColor(this.backgroundColor.darker().darker());
				graphics.drawRect(this.x, this.y, this.width, this.height);
			}
			// Text color & font
			{
				graphics.setColor(this.textProperties.getColor());
				graphics.setFont(this.textProperties.getFont());
			}
			// Line counter
			this.linesCounter = 0;
			// Cursor tracking variables
			int cursorX = 0, cursorY = 0, cursorWidth = 0, cursorHeight = 0;
			// Check if width of current line exceeds box width
			if ((metrics.stringWidth(this.currentLine) + this.x + 10) > this.width) {
				// Check if we can spread on multiple lines
				if (this.multiline) {
					// Check if we still have more line space
					if (this.canAddLines) {
						// Case where there's 1 word per line
						if (!this.currentLine.isEmpty() && (this.currentLine.split("\\s+").length == 1)) {
							// Split text until before last character and add hyphen
							String subHyphen = this.currentLine.substring(0, this.currentLine.length() - 1) + "-";
							// Add the first part of the cut text to the lines
							this.text.add(subHyphen);
							// Make a new line with the last character of the previous full string
							this.currentLine = this.currentLine.substring(this.currentLine.length() - 1, this.currentLine.length());
							// Reset cursor position
							this.cursorPosition = 1;
						} else { // If there are more than 1 word per line
							String sub = new String();
							// Separate the string at each space
							String[] parts = this.currentLine.split("\\s+");
							// Append the parts to a new string adding spaces accordingly
							for (int i = 0; i < (parts.length - 1); i += 1) {
								sub += parts[i] + (i == (parts.length - 2) ? "" : " ");
							}
							// Add the new string to the text
							this.text.add(sub);
							// Use the last word of the original string as beginning of the newline
							this.currentLine = parts[parts.length - 1];
							// Reset cursor position
							this.cursorPosition = this.currentLine.length();
						}
					} else {
						// If we can't add more lines
						int maxSize = 0;
						// Count how many characters we need to exceed the width
						while ((metrics.stringWidth(this.currentLine.substring(0, maxSize)) + this.x + 10) < this.width) {
							maxSize += 1;
						}
						// Take that amount of characters to display
						this.currentLine = this.currentLine.substring(0, maxSize);
						// Reset cursor position so it doesn't go on indefinitely without text
						if (this.cursorPosition >= this.currentLine.length()) {
							this.cursorPosition = this.currentLine.length();
						}
					}
				} else {
					// If we can't add more lines
					int maxSize = 0;
					// Count how many characters we need to exceed the width
					while ((metrics.stringWidth(this.currentLine.substring(0, maxSize)) + this.x + 10) < this.width) {
						maxSize += 1;
					}
					// Take that amount of characters to display
					this.currentLine = this.currentLine.substring(0, maxSize);
					// Reset cursor position so it doesn't go on indefinitely without text
					if (this.cursorPosition >= this.currentLine.length()) {
						this.cursorPosition = this.currentLine.length();
					}
				}
			}
			// Drawing when multi-line is enabled
			if (this.multiline) {
				// Loop through each line
				for (String lineOfText : this.text) {
					// Check that we still have enough lines empty - DON'T KNOW WHY IT WORKS BUT IT
					// DOES
					if (((this.linesCounter + 2) * fontHeight) < this.height) {
						// Should give a perfectly spaced text
						graphics.drawString(lineOfText, this.x + 10, this.y + (fontHeight * (this.linesCounter + 1)));
						// Increase lines counter
						this.linesCounter += 1;
					}
				}
				// We exceeded the height limit so we can't add lines anymore
				if (((this.linesCounter + 2) * fontHeight) > this.height) {
					this.canAddLines = false;
				}
				// Draw current line if we have the space
				if (((this.linesCounter + 1) * fontHeight) < this.height) {
					graphics.drawString(this.currentLine, this.x + 10, this.y + (fontHeight * (this.linesCounter + 1)));
				}
				// Compute cursor position
				cursorY = this.y + (fontHeight * this.linesCounter) + (metrics.getAscent() / 2) + (metrics.getDescent() / 2);
				// Here is a fixed x-offset - need to change that to scale with the font
				cursorX = this.x + this.stringWidth(metrics, this.currentLine, this.cursorPosition) + 15;
				// Constant width, 2/4 looks good IMO
				if (this.textProperties.getFont().isBold()) {
					cursorWidth = 6;
				} else {
					cursorWidth = 2;
				}
				// Cursor height - again, don't know why it works but it does
				cursorHeight = (int) ((metrics.getDescent() / 2) + (metrics.getAscent() / 2) + Utilities.map(this.textProperties.getFont().getSize(), 0, 144, 0, 4, true));
			} else {
				// easy - maybe too memory heavy? might need a cache
				new GraphicString(this.currentLine, this.textProperties.getColor(), this.textProperties.getFont()).drawCenteredAbsolute(graphics, this.x + 10, this.y, this.height);
				// FIXED!
				cursorY = this.y + 5;
				// Here is a fixed x-offset - need to change that to scale with the font
				cursorX = this.x + this.stringWidth(metrics, this.currentLine, this.cursorPosition) + 15;
				// Constant width, 2/4 looks good IMO
				if (this.textProperties.getFont().isBold()) {
					cursorWidth = 6;
				} else {
					cursorWidth = 2;
				}
				// FIXED!
				cursorHeight = this.height - 10;
			}
			// Cursor draw routine
			if (this.hasFocus) {
				{
					if (this.cursorBlink) {
						graphics.fillRect(cursorX, cursorY, cursorWidth, cursorHeight);
					}
				}
			}
		}

		/**
		 * Returns a string width up to cursor-characters.
		 */
		private final int stringWidth(FontMetrics metrics, String string, int cursor) {
			return metrics.stringWidth(string.substring(0, cursor));
		}
	}
}
