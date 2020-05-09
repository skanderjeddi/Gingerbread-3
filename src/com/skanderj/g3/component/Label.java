package com.skanderj.g3.component;

import java.awt.Graphics2D;

import com.skanderj.g3.inputdevice.Keyboard;
import com.skanderj.g3.inputdevice.Mouse;
import com.skanderj.g3.util.GraphicString;
import com.skanderj.g3.window.Window;

/**
 * Represents an abstract label, basis for other button classes which can
 * implement their rendering the way they please. See Label#Basic for a very
 * basic example.
 *
 * @author Skander
 *
 */
public abstract class Label implements Component {
	protected int x, y;
	protected GraphicString graphicString;

	/**
	 * Very basic constructor.
	 */
	public Label(int x, int y, GraphicString graphicString) {
		this.x = x;
		this.y = y;
		this.graphicString = graphicString;
	}

	/**
	 * Can always give focus to other components as we don't need to be part of the
	 * focus handling system.
	 */
	@Override
	public boolean canChangeFocus() {
		return true;
	}

	/**
	 * Don't need mouse detection for a label, can be implemented in subclasses if
	 * need be.
	 */
	@Override
	public boolean containsMouse(int x, int y) {
		return false;
	}

	/**
	 * No effects of the focus system.
	 */
	@Override
	public void grantFocus() {
		return;
	}

	/**
	 * No effects of the focus system.
	 */
	@Override
	public void revokeFocus() {
		return;
	}

	/**
	 * Self explanatory.
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Self explanatory.
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * Self explanatory.
	 */
	public GraphicString getGraphicString() {
		return this.graphicString;
	}

	/**
	 * Self explanatory.
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Self explanatory.
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Self explanatory.
	 */
	public void setGraphicString(GraphicString graphicString) {
		this.graphicString = graphicString;
	}

	/**
	 * Represents a simple label centered inside a rectangle.
	 *
	 * @author Skander
	 *
	 */
	public static class Basic extends Label {
		private int width, height;

		public Basic(int x, int y, int width, int height, GraphicString graphicString) {
			super(x, y, graphicString);
			this.width = width;
			this.height = height;
		}

		/**
		 * No need for logic.
		 */
		@Override
		public void update(double delta, Keyboard keyboard, Mouse mouse, Object... args) {
			return;
		}

		/**
		 * Just draw the string.
		 */
		@Override
		public void render(Window window, Graphics2D graphics, Object... args) {
			this.graphicString.drawCentered(graphics, this.x, this.y, this.width, this.height);
		}

		/**
		 * Self explanatory.
		 */
		public final int getWidth() {
			return this.width;
		}

		/**
		 * Self explanatory.
		 */
		public final int getHeight() {
			return this.height;
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
		public final void setHeight(int height) {
			this.height = height;
		}
	}
}
