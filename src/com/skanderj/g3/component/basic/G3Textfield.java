package com.skanderj.g3.component.basic;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.skanderj.g3.component.ComponentManager;
import com.skanderj.g3.component.Textfield;
import com.skanderj.g3.util.GraphicString;
import com.skanderj.g3.util.TextProperties;
import com.skanderj.g3.util.Utilities;
import com.skanderj.g3.window.Window;
import com.skanderj.g3.window.inputdevice.Keyboard;
import com.skanderj.g3.window.inputdevice.Mouse;

/**
 * A very basic textbox.
 *
 * @author Skander
 *
 */
public final class G3Textfield extends Textfield {
	private int x, y, width, height;
	private Color backgroundColor;
	private final TextProperties textProperties;
	private final int maximumLines;
	private int linesCounter;
	// Does the cursor blink?
	protected boolean cursorBlink;
	// Cursor's blink related
	protected int blinkRate, blinkTimer;

	/**
	 * Background color for rendering a simple box and text properties for the font
	 * and color, by default can only display 1 line.
	 */
	public G3Textfield(int x, int y, int width, Color backgroundColor, TextProperties textProperties) {
		this(x, y, width, backgroundColor, textProperties, 0);
	}

	/**
	 * Background color for rendering a simple box, text properties for the font and
	 * color, and amount maximumLines to display.
	 */
	public G3Textfield(int x, int y, int width, Color backgroundColor, TextProperties textProperties, int lines) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = 0;
		this.backgroundColor = backgroundColor;
		this.textProperties = textProperties;
		this.maximumLines = lines;
		this.multiline = !(lines == 1);
		this.linesCounter = 0;
		// Cursor always blinks by default, I mean why not
		this.cursorBlink = true;
		// Cursor will blink 60/15 times per second (=4)
		this.blinkRate = 5;
		// Timer, pretty basic
		this.blinkTimer = 0;
	}

	@Override
	public void update(double delta, Keyboard keyboard, Mouse mouse, Object... args) {
		super.update(delta, keyboard, mouse, args);
		// Cursor blinking timer
		{
			this.blinkTimer += 1;
			if ((this.blinkTimer % this.blinkRate) == 0) {
				this.cursorBlink = !this.cursorBlink;
			}
		}
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
				this.height = (metrics.getHeight() * this.maximumLines) + (metrics.getHeight() / 3);
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
			// Check if we can spread on multiple maximumLines
			if (this.multiline) {
				// Check if we still have more line space
				if (this.canAddLines) {
					// Case where there's 1 word per line
					if (!this.currentLine.isEmpty() && (this.currentLine.split("\\s+").length == 1)) {
						// Split text until before last character and add hyphen
						String subHyphen = this.currentLine.substring(0, this.currentLine.length() - 1) + "-";
						// Add the first part of the cut text to the maximumLines
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
					// If we can't add more maximumLines
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
				// If we can't add more maximumLines
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
				// Check that we still have enough maximumLines empty - DON'T KNOW WHY IT WORKS
				// BUT IT
				// DOES
				if (((this.linesCounter + 2) * fontHeight) < this.height) {
					// Should give a perfectly spaced text
					graphics.drawString(lineOfText, this.x + 10, this.y + (fontHeight * (this.linesCounter + 1)));
					// Increase maximumLines counter
					this.linesCounter += 1;
				}
			}
			// We exceeded the height limit so we can't add maximumLines anymore
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
			cursorX = this.x + this.stringWidth(metrics, this.currentLine, this.cursorPosition) + (this.textProperties.getFont().getSize() / 5);
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
			cursorX = this.x + this.stringWidth(metrics, this.currentLine, this.cursorPosition) + (this.textProperties.getFont().getSize() / 5);
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
		if (ComponentManager.GRAPHICAL_DEBUG) {
			graphics.setColor(Color.RED);
			graphics.drawRect(this.x, this.y, this.width, this.height);
		}
	}

	@Override
	public boolean containsMouse(int x, int y) {
		return new Rectangle(this.x, this.y, this.width, this.height).contains(x, y);
	}

	/**
	 * Returns a string width up to cursor-characters.
	 */
	private final int stringWidth(FontMetrics metrics, String string, int cursor) {
		return metrics.stringWidth(string.substring(0, cursor));
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public final int getX() {
		return this.x;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public final int getY() {
		return this.y;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public int getWidth() {
		return this.width;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public int getHeight() {
		return this.height;
	}

	/**
	 * Self explanatory.
	 */
	public final Color getBackgroundColor() {
		return this.backgroundColor;
	}

	/**
	 * Self explanatory.
	 */
	public final TextProperties getTextProperties() {
		return this.textProperties;
	}

	/**
	 * Self explanatory.
	 */
	public final int getMaximumLines() {
		return this.maximumLines;
	}

	/**
	 * Self explanatory.
	 */
	public final int getBlinkRate() {
		return this.blinkRate;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public final void setX(int x) {
		this.x = x;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public final void setY(int y) {
		this.y = y;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public final void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public final void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Self explanatory.
	 */
	public final void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}