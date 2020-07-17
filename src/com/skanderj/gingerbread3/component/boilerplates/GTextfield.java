package com.skanderj.gingerbread3.component.boilerplates;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Rectangle;

import com.skanderj.gingerbread3.component.Components;
import com.skanderj.gingerbread3.component.Textfield;
import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.util.Label;
import com.skanderj.gingerbread3.util.LabelProperties;
import com.skanderj.gingerbread3.util.Utilities;

/**
 * A very basic textbox.
 *
 * @author Skander
 *
 */
public final class GTextfield extends Textfield {
	private double x, y;
	private int width, height;
	private Color backgroundColor;
	private final LabelProperties textProperties;
	private final int maximumLines;
	private int linesCounter;
	// Does the cursor blink?
	protected boolean cursorBlink;
	// Cursor's blink related
	protected int blinkDelay, blinkTimer;

	/**
	 * Background color for rendering a simple box and text properties for the font
	 * and color, by default can only display 1 line.
	 */
	public GTextfield(final Application application, final double x, final double y, final int width, final Color backgroundColor, final LabelProperties textProperties) {
		this(application, x, y, width, backgroundColor, textProperties, 0);
	}

	/**
	 * Background color for rendering a simple box, text properties for the font and
	 * color, and amount maximumLines to display.
	 */
	public GTextfield(final Application application, final double x, final double y, final int width, final Color backgroundColor, final LabelProperties textProperties, final int lines) {
		super(application);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = 0;
		this.backgroundColor = backgroundColor;
		this.textProperties = textProperties;
		this.maximumLines = lines;
		this.multiline = lines != 1;
		this.linesCounter = 0;
		// Cursor always blinks by default, I mean why not
		this.cursorBlink = true;
		// Cursor will blink 60/15 times per second (=4)
		this.blinkDelay = 5;
		// Timer, pretty basic
		this.blinkTimer = 0;
	}

	@Override
	public synchronized void update() {
		super.update();
		// Cursor blinking timer
		{
			this.blinkTimer += 1;
			if ((this.blinkTimer % this.blinkDelay) == 0) {
				this.cursorBlink = !this.cursorBlink;
			}
		}
	}

	/**
	 * The rendering routine.
	 */
	@Override
	public synchronized void render(final Screen screen) {
		screen.font(this.textProperties.font);
		final FontMetrics metrics = screen.fontMetrics();
		final int fontHeight = metrics.getHeight();
		// Determine height if not done before (= 0)
		{
			if (this.height == 0) {
				this.height = (metrics.getHeight() * this.maximumLines) + (metrics.getHeight() / 3);
			}
		}
		// Background
		{
			screen.rectangle(this.backgroundColor, this.x, this.y, this.width, this.height, true, 0, 0);
		}
		// Border
		{
			screen.rectangle(this.backgroundColor.darker().darker(), this.x, this.y, this.width, this.height, false, 0, 0);
		}
		// Text color & font
		{
			screen.color(this.textProperties.color);
			screen.font(this.textProperties.font);
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
						final String subHyphen = this.currentLine.substring(0, this.currentLine.length() - 1) + "-";
						// Add the first part of the cut text to the maximumLines
						this.text.add(subHyphen);
						// Make a new line with the last character of the previous full string
						this.currentLine = this.currentLine.substring(this.currentLine.length() - 1, this.currentLine.length());
						// Reset cursor position
						this.cursorPosition = 1;
					} else { // If there are more than 1 word per line
						String sub = new String();
						// Separate the string at each space
						final String[] parts = this.currentLine.split("\\s+");
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
			for (final String lineOfText : this.text) {
				// Check that we still have enough maximumLines empty - DON'T KNOW WHY IT WORKS
				// BUT IT
				// DOES
				if (((this.linesCounter + 2) * fontHeight) < this.height) {
					// Should give a perfectly spaced text
					screen.string(lineOfText, this.x + 10, this.y + (fontHeight * (this.linesCounter + 1)));
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
				screen.string(this.currentLine, this.x + 10, this.y + (fontHeight * (this.linesCounter + 1)));
			}
			// Compute cursor position
			cursorY = (int) this.y + (fontHeight * this.linesCounter) + (metrics.getAscent() / 2) + (metrics.getDescent() / 2);
			// Here is a fixed x-offset - need to change that to scale with the font
			cursorX = (int) this.x + this.stringWidth(metrics, this.currentLine, this.cursorPosition) + (this.textProperties.font.getSize() / 5);
			// Constant width, 2/4 looks good IMO
			if (this.textProperties.font.isBold()) {
				cursorWidth = 6;
			} else {
				cursorWidth = 2;
			}
			// Cursor height - again, don't know why it works but it does
			cursorHeight = (int) ((metrics.getDescent() / 2) + (metrics.getAscent() / 2) + Utilities.map(this.textProperties.font.getSize(), 0, 144, 0, 4, true));
		} else {
			// easy - maybe too memory heavy? might need a cache
			new Label(this.currentLine, this.textProperties.color, this.textProperties.font).drawCenteredAbsolute(screen, (int) this.x + 10, (int) this.y, this.height);
			// FIXED!
			cursorY = (int) this.y + 5;
			// Here is a fixed x-offset - need to change that to scale with the font
			cursorX = (int) this.x + this.stringWidth(metrics, this.currentLine, this.cursorPosition) + (this.textProperties.font.getSize() / 5);
			// Constant width, 2/4 looks good IMO
			if (this.textProperties.font.isBold()) {
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
					screen.rectangle(Color.BLACK, cursorX, cursorY, cursorWidth, cursorHeight, true, 0, 0);
				}
			}
		}
		if (Components.GRAPHICAL_DEBUG) {
			screen.rectangle(Color.RED, this.x, this.y, this.width, this.height, false, 0, 0);
		}
	}

	@Override
	public boolean containsMouse(final int x, final int y) {
		return new Rectangle((int) this.x, (int) this.y, this.width, this.height).contains(x, y);
	}

	/**
	 * Returns a string width up to cursor-characters.
	 */
	private int stringWidth(final FontMetrics metrics, final String string, final int cursor) {
		return metrics.stringWidth(string.substring(0, cursor));
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public double getX() {
		return this.x;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public double getY() {
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
	public Color backgroundColor() {
		return this.backgroundColor;
	}

	/**
	 * Self explanatory.
	 */
	public LabelProperties textProperties() {
		return this.textProperties;
	}

	/**
	 * Self explanatory.
	 */
	public int maximumLines() {
		return this.maximumLines;
	}

	/**
	 * Self explanatory.
	 */
	public int blinkDelay() {
		return this.blinkDelay;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setX(final double x) {
		this.x = x;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setY(final double y) {
		this.y = y;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setWidth(final int width) {
		this.width = width;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setHeight(final int height) {
		this.height = height;
	}

	/**
	 * Self explanatory.
	 */
	public void setBackgroundColor(final Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public Priority priority() {
		return Priority.LOW;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public String description() {
		return Engine.identifier(this) + " -> GTextField.class(" + this.x + ", " + this.y + ", " + this.width + ", " + this.height + ")";
	}
}