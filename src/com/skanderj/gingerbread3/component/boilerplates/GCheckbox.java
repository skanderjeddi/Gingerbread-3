package com.skanderj.gingerbread3.component.boilerplates;

import java.awt.Color;
import java.awt.Rectangle;

import com.skanderj.gingerbread3.component.Checkbox;
import com.skanderj.gingerbread3.component.ComponentLabelPosition;
import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.util.Text;

/**
 * Represents a very basic checkbox with a text on on side.
 *
 * @author Skander
 *
 */
public class GCheckbox extends Checkbox {
	private double x, y;
	private int width, height;
	private Text text;
	private Color backgroundColor, borderColor, crossColor;
	private ComponentLabelPosition labelPosition;

	public GCheckbox(final Application application, final double x, final double y, final int width, final int height, final Text text, final Color backgroundColor, final Color borderColor, final Color crossColor, final ComponentLabelPosition labelPosition) {
		super(application);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
		this.crossColor = crossColor;
		this.labelPosition = labelPosition;
	}

	@Override
	public synchronized void update() {
		super.update();
	}

	/**
	 * Draws a simple rectangle for the checkbox and draws the text on the
	 * appropriate position.
	 */
	@Override
	public synchronized void render(final Screen screen) {
		screen.rectangle(this.backgroundColor, this.x, this.y, this.width, this.height, true, 0, 0);
		if (this.isChecked) {
			screen.rectangle(this.crossColor, this.x + 3, this.y + 3, this.width - 5, this.height - 5, true, 0, 0);
		}
		screen.rectangle(this.borderColor, this.x, this.y, this.width, this.height, false, 0, 0);
		if (!this.text.isEmpty()) {
			switch (this.labelPosition) {
			case TOP:
				this.text.draw(screen, (int) this.x, (int) this.y - this.text.getHeight(screen));
				break;
			case BOTTOM:
				this.text.draw(screen, (int) this.x, (int) this.y + this.height + this.text.getAugmentedHeight(screen));
				break;
			case LEFT:
				this.text.drawCenteredWidthless(screen, (int) this.x - 10 - this.text.getWidth(screen), (int) this.y, this.height);
				break;
			case RIGHT:
				this.text.drawCenteredWidthless(screen, (int) this.x + this.width + 10, (int) this.y, this.height);
				break;
			}
		}
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public boolean containsMouse(final int x, final int y) {
		return new Rectangle((int) this.x, (int) this.y, this.width, this.height).contains(x, y);
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
	public Text text() {
		return this.text;
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
	public Color borderColor() {
		return this.borderColor;
	}

	/**
	 * Self explanatory.
	 */
	public Color crossColor() {
		return this.crossColor;
	}

	/**
	 * Self explanatory.
	 */
	public ComponentLabelPosition labelPosition() {
		return this.labelPosition;
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
	public void setText(final Text text) {
		this.text = text;
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
		return Priority.REGULAR;
	}

	/**
	 * Self explanatory.
	 */
	@Override
	public String description() {
		return Engine.identifier(this) + " -> GCheckBox.class(" + this.x + ", " + this.y + ", " + this.text.toString() + ")";
	}
}
