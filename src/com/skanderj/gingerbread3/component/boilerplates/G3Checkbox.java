package com.skanderj.gingerbread3.component.boilerplates;

import java.awt.Color;
import java.awt.Rectangle;

import com.skanderj.gingerbread3.component.Checkbox;
import com.skanderj.gingerbread3.component.ComponentLabelPosition;
import com.skanderj.gingerbread3.core.Game;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.display.GraphicsWrapper;
import com.skanderj.gingerbread3.util.VisualString;

/**
 * Represents a very basic checkbox with a label on on side.
 *
 * @author Skander
 *
 */
public class G3Checkbox extends Checkbox {
	private int x, y, width, height;
	private VisualString label;
	private Color backgroundColor, borderColor, crossColor;
	private ComponentLabelPosition labelPosition;

	public G3Checkbox(final Game game, final int x, final int y, final int width, final int height, final VisualString label, final Color backgroundColor, final Color borderColor, final Color crossColor, final ComponentLabelPosition labelPosition) {
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
	public synchronized void update(final double delta, final Object... args) {
		super.update(delta, args);
	}

	/**
	 * Draws a simple rectangle for the checkbox and draws the label on the
	 * appropriate position.
	 */
	@Override
	public synchronized void render(final GraphicsWrapper graphics) {
		graphics.rectangle(this.backgroundColor, this.x, this.y, this.width, this.height, true, 0, 0);
		if (this.isChecked) {
			graphics.rectangle(this.crossColor, this.x + 3, this.y + 3, this.width - 5, this.height - 5, true, 0, 0);
		}
		graphics.rectangle(this.borderColor, this.x, this.y, this.width, this.height, false, 0, 0);
		if (!this.label.isEmpty()) {
			switch (this.labelPosition) {
			case TOP:
				this.label.draw(graphics, this.x, this.y - this.label.getHeight(graphics));
				break;
			case BOTTOM:
				this.label.draw(graphics, this.x, this.y + this.height + this.label.getAugmentedHeight(graphics));
				break;
			case LEFT:
				this.label.drawCenteredWidthless(graphics, this.x - 10 - this.label.getWidth(graphics), this.y, this.height);
				break;
			case RIGHT:
				this.label.drawCenteredWidthless(graphics, this.x + this.width + 10, this.y, this.height);
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
