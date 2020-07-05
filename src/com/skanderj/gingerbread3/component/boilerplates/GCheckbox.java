package com.skanderj.gingerbread3.component.boilerplates;

import java.awt.Color;
import java.awt.Rectangle;

import com.skanderj.gingerbread3.component.Checkbox;
import com.skanderj.gingerbread3.component.ComponentLabelPosition;
import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.util.VisualString;

/**
 * Represents a very basic checkbox with a label on on side.
 *
 * @author Skander
 *
 */
public class GCheckbox extends Checkbox {
	private int x, y, width, height;
	private VisualString label;
	private Color backgroundColor, borderColor, crossColor;
	private ComponentLabelPosition labelPosition;

	public GCheckbox(final Game game, final int x, final int y, final int width, final int height, final VisualString label, final Color backgroundColor, final Color borderColor, final Color crossColor, final ComponentLabelPosition labelPosition) {
		super(game);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.label = label;
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
		this.crossColor = crossColor;
		this.labelPosition = labelPosition;
	}

	@Override
	public synchronized void update(final double delta) {
		super.update(delta);
	}

	/**
	 * Draws a simple rectangle for the checkbox and draws the label on the
	 * appropriate position.
	 */
	@Override
	public synchronized void render(final Screen screen) {
		screen.rectangle(this.backgroundColor, this.x, this.y, this.width, this.height, true, 0, 0);
		if (this.isChecked) {
			screen.rectangle(this.crossColor, this.x + 3, this.y + 3, this.width - 5, this.height - 5, true, 0, 0);
		}
		screen.rectangle(this.borderColor, this.x, this.y, this.width, this.height, false, 0, 0);
		if (!this.label.isEmpty()) {
			switch (this.labelPosition) {
			case TOP:
				this.label.draw(screen, this.x, this.y - this.label.getHeight(screen));
				break;
			case BOTTOM:
				this.label.draw(screen, this.x, this.y + this.height + this.label.getAugmentedHeight(screen));
				break;
			case LEFT:
				this.label.drawCenteredWidthless(screen, this.x - 10 - this.label.getWidth(screen), this.y, this.height);
				break;
			case RIGHT:
				this.label.drawCenteredWidthless(screen, this.x + this.width + 10, this.y, this.height);
				break;
			}
		}
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public boolean containsMouse(final int x, final int y) {
		return new Rectangle(this.x, this.y, this.width, this.height).contains(x, y);
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public int getX() {
		return this.x;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public int getY() {
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
	public VisualString getLabel() {
		return this.label;
	}

	/**
	 * Self explanatory.
	 */
	public Color getBackgroundColor() {
		return this.backgroundColor;
	}

	/**
	 * Self explanatory.
	 */
	public Color getBorderColor() {
		return this.borderColor;
	}

	/**
	 * Self explanatory.
	 */
	public Color getCrossColor() {
		return this.crossColor;
	}

	/**
	 * Self explanatory.
	 */
	public ComponentLabelPosition getLabelPosition() {
		return this.labelPosition;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setX(final int x) {
		this.x = x;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public void setY(final int y) {
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
	public void setLabel(final VisualString label) {
		this.label = label;
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
	public void setBorderColor(final Color borderColor) {
		this.borderColor = borderColor;
	}

	/**
	 * Self explanatory.
	 */
	public void setCrossColor(final Color crossColor) {
		this.crossColor = crossColor;
	}

	/**
	 * Self explanatory.
	 */
	public void setLabelPosition(final ComponentLabelPosition labelPosition) {
		this.labelPosition = labelPosition;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public Priority priority() {
		return Priority.LOW;
	}
}
