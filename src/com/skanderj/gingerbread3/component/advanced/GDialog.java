package com.skanderj.gingerbread3.component.advanced;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;

import com.skanderj.gingerbread3.animation.Animation;
import com.skanderj.gingerbread3.component.Component;
import com.skanderj.gingerbread3.component.boilerplates.GLabel;
import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.Moveable;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.resources.Fonts;
import com.skanderj.gingerbread3.util.Text;
import com.skanderj.gingerbread3.util.Utilities;

/**
 * Dialog component that can progressively type text, then clear and change it,
 * is easily hidden and showed with in and out animations and has variable text
 * speeds. Lines will automatically wrap themselves in the provided box defined
 * by startTextRelativeX, startTextRelativeY, endTextRelativeX, endTextRelativeY.
 * You can provide an dialog box opening and closing animations, as well as a
 * waiting animation for when the text is finished displaying.
 *
 * @author Nim
 *
 */
public final class GDialog extends Component {
	private double x, y;
	private final int width, height;
	private final Moveable box;
	private Animation inAnimation;
	private Animation outAnimation;
	private Animation waitAnimation;
	private final int startTextRelativeX, startTextRelativeY, endTextRelativeX, endTextRelativeY;
	private final ArrayList<GLabel> textLabels;
	private int visibleCharacters;
	private double timeBetweenChars;
	private Font font;
	private String text;
	private Color textColor;
	private boolean shouldUpdateVisibleCharacters;
	private long frameCounter;
	private String thingToRender;
	private boolean textVisible;
	private FontMetrics metrics;
	private ArrayList<String> lines;
	private int lineIndexToPrint;
	private int extraSpaceBetweenLines;

	public GDialog(final Application application, final double x, final double y, final int width, final int height, final String boxIdentifier, final String inAnimationIdentifier, final String outAnimationIdentifier, final String waitAnimationIdentifier, final int startTextRelativeX, final int startTextRelativeY, final int endTextRelativeX, final int endTextRelativeY, final String fontIdentifier, final int fontSize) {
		super(application);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.box = (Moveable) Engine.get(boxIdentifier);
		this.box.setX(x);
		this.box.setY(y);
		if (inAnimationIdentifier != null) {
			this.inAnimation = (Animation) Engine.get(inAnimationIdentifier);
			this.inAnimation.setX(x);
			this.inAnimation.setY(y);
		}
		if (outAnimationIdentifier != null) {
			this.outAnimation = (Animation) Engine.get(outAnimationIdentifier);
			this.outAnimation.setX(x);
			this.outAnimation.setY(y);
		}
		if (waitAnimationIdentifier != null) {
			this.waitAnimation = (Animation) Engine.get(waitAnimationIdentifier);
			this.waitAnimation.setX(x);
			this.waitAnimation.setY(y);
		}
		this.startTextRelativeX = startTextRelativeX;
		this.startTextRelativeY = startTextRelativeY;
		this.endTextRelativeX = endTextRelativeX;
		this.endTextRelativeY = endTextRelativeY;
		this.font = Fonts.get(fontIdentifier, fontSize);
		this.textColor = Color.WHITE;
		this.text = Utilities.EMPTY_STRING;
		this.timeBetweenChars = 0.01;
		this.visibleCharacters = 0;
		this.frameCounter = 0;
		this.shouldUpdateVisibleCharacters = false;
		this.thingToRender = Utilities.EMPTY_STRING;
		this.textVisible = true;
		this.textLabels = new ArrayList<>();
		this.extraSpaceBetweenLines = 0;
	}

	@Override
	public synchronized void update() {
		if (this.thingToRender.equals("inAnim")) {
			this.inAnimation.update();
			if (this.inAnimation.sprites().length == this.inAnimation.getCurrentSpriteIndex()) {
				this.thingToRender = "box";
				this.shouldUpdateVisibleCharacters = true;
			}
		}
		if (this.thingToRender.equals("outAnim")) {
			this.outAnimation.update();
			if (this.outAnimation.sprites().length == this.outAnimation.getCurrentSpriteIndex()) {
				this.thingToRender = Utilities.EMPTY_STRING;
				this.shouldUpdateVisibleCharacters = false;
			}
		}
		if (this.thingToRender.equals("waitAnim")) {
			this.waitAnimation.update();
		}
		if (this.thingToRender.equals("box")) {
			if (this.shouldUpdateVisibleCharacters) {
				final double elapsedTime = this.frameCounter / this.application().refreshRate();
				if (elapsedTime >= this.timeBetweenChars) {
					this.visibleCharacters++;
					this.frameCounter = 0;
				}
				final String text = this.lines.get(this.lineIndexToPrint);
				final String displayedText = text.substring(0, this.visibleCharacters);
				this.textLabels.get(this.lineIndexToPrint).text().content = displayedText;
				this.textLabels.get(this.lineIndexToPrint).text().color = this.textColor;
				this.textLabels.get(this.lineIndexToPrint).text().font = this.font;
				if (this.visibleCharacters >= text.length()) {
					this.lineIndexToPrint++;
					if ((this.lineIndexToPrint + 1) > this.lines.size()) {
						this.shouldUpdateVisibleCharacters = false;
						if (this.waitAnimation != null) {
							this.thingToRender = "waitAnim";
						}
					}
					this.visibleCharacters = 0;
				}
				this.frameCounter++;
			}
		}
	}

	/**
	 *
	 */
	@Override
	public synchronized void render(final Screen screen) {
		this.metrics = screen.fontMetrics(this.font);
		if (this.thingToRender.equals("box")) {
			this.box.render(screen);
		} else if (this.thingToRender.equals("inAnim")) {
			this.inAnimation.render(screen);
		} else if (this.thingToRender.equals("outAnim")) {
			this.outAnimation.render(screen);
		} else if (this.thingToRender.equals("waitAnim")) {
			this.waitAnimation.render(screen);
		}
		if (this.textVisible) {
			for (final GLabel label : this.textLabels) {
				label.render(screen);
			}
		}
	}

	private ArrayList<String> getLinesByFontMetrics(final FontMetrics fontMetrics, final String text, final int width) {
		final ArrayList<String> lines = new ArrayList<>();
		int indexOfBeginingOfLineInFinalText = 0;
		String currentText = text;
		while (true) {
			if (currentText.charAt(0) == ' ') {
				currentText = currentText.substring(1, currentText.length());
			}
			// If the text doesn't fit in the given width
			if (fontMetrics.stringWidth(currentText) > width) {
				// We progressively remove words to see when it'll fit
				String lineTry = currentText;
				while(true) {
					String[] txtSplit;
					if (lineTry.contains(" ")) {
						txtSplit = lineTry.split(" ");
					} else {
						lines.add(lineTry);
						break;
					}
					// String[] txtSplit = lineTry.contains(" ") ? lineTry.split(" ") : new String[]{lineTry};
					lineTry = String.join(" ", Arrays.copyOfRange(txtSplit, 0, lineTry.contains(" ") ? txtSplit.length - 1 : txtSplit.length-2));
					if(fontMetrics.stringWidth(lineTry) <= width) {
						break;
					}
				}
				lines.add(lineTry);
				indexOfBeginingOfLineInFinalText = lineTry.length();
			} else {
				lines.add(currentText);
				break;
			}
			if (indexOfBeginingOfLineInFinalText >= currentText.length()) {
				break;
			}
			currentText = currentText.substring(indexOfBeginingOfLineInFinalText, currentText.length());
		}
		return lines;
	}

	public void setText(final String text) {
		this.visibleCharacters = 0;
		this.text = text;
		this.shouldUpdateVisibleCharacters = true;
		this.lines = this.getLinesByFontMetrics(this.metrics, this.text, this.endTextRelativeX - this.startTextRelativeX);
		this.lineIndexToPrint = 0;
		// TODO IMPLEMENT MULTIPLE GLABELS FOR MULTIPLE LINES
		// ALMOSE DONE
		this.textLabels.clear();
		for (int i = 0; i < this.lines.size(); i++) {
			final GLabel label = new GLabel(this.application, this.x + this.startTextRelativeX, this.y + this.startTextRelativeY + (i * (this.metrics.getHeight() + this.extraSpaceBetweenLines)), this.endTextRelativeX - this.startTextRelativeX, this.endTextRelativeY - this.startTextRelativeY, new Text(Utilities.EMPTY_STRING, this.textColor, this.font));
			label.setCentered(false);
			this.textLabels.add(label);
		}
		this.thingToRender = "box";
	}

	public String getText() {
		return this.text;
	}

	public void setVisibleCharacters(final int x) {
		this.visibleCharacters = x;
	}

	public int getVisibleCharacters() {
		return this.visibleCharacters;
	}

	public void setTextColor(final Color color) {
		this.textColor = color;
	}

	public void setTimeBetweenChars(final double timeInSeconds) {
		this.timeBetweenChars = timeInSeconds;
	}

	public void showBox() {
		this.showText();
		if (this.inAnimation != null) {
			this.thingToRender = "inAnim";
		} else {
			this.thingToRender = "box";
		}
	}

	public void hideBox() {
		this.hideText();
		if (this.outAnimation != null) {
			this.thingToRender = "outAnim";
		} else {
			this.thingToRender = Utilities.EMPTY_STRING;
		}
	}

	public void showText() {
		this.textVisible = true;
	}

	public void hideText() {
		this.textVisible = false;
	}

	public void changeFont(final String fontIdentifier, final int size) {
		this.font = Fonts.get(fontIdentifier, size);
	}

	public void setExtraSpaceBetweenLines(final int space) {
		this.extraSpaceBetweenLines = space;
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

	@Override
	public void setWidth(final int width) {
		return;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setHeight(final int height) {
		return;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public boolean containsMouse(final int x, final int y) {
		return new Rectangle((int) this.x, (int) this.y, this.width, this.height).contains(x, y);
	}

	/**
	 * Related to global components management. // TODO
	 */
	@Override
	public void grantFocus() {
		return;
	}

	/**
	 * Related to global components management. // TODO
	 */
	@Override
	public void revokeFocus() {
		return;
	}

	/**
	 * Related to global components management. // TODO
	 */
	@Override
	public boolean canChangeFocus() {
		return true;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public Priority priority() {
		return Priority.EXTREMELY_LOW;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public String description() {
		return Engine.identifier(this) + " -> GDialog.class(" + this.x + ", " + this.y + ", " + this.text + ")";
	}
}
