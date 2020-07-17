package com.skanderj.gingerbread3.component.boilerplates;

import java.awt.Font;
import java.awt.Color;

import com.skanderj.gingerbread3.component.boilerplates.GText;
import com.skanderj.gingerbread3.component.Component;
import com.skanderj.gingerbread3.component.Text;
import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.Moveable;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.util.Label;
import com.skanderj.gingerbread3.animation.Animation;
import com.skanderj.gingerbread3.resources.Fonts;



/**
 * Textbox component that can progressively type text, then clear and change it,
 * is easily hidden and showed with in and out animations, and has variable text
 * speeds,
 *
 * @author Nim
 *
 */
public final class GTextbox extends Component {
    private double x, y;
    private int width, height;
    private Moveable box;
    private Animation inAnimation;
    private Animation outAnimation;
    private int startTextRelativeX, startTextRelativeY, endTextRelativeX, endTextRelativeY;
    private GText textLabel;
    private int visibleCharacters;
    private double timeBetweenChars;
    private Font font;
    private String text;
    private String displayedText;
    private Color textColor;
    private boolean shouldUpdateVisibleCharacters;
    private long frameCounter;
    private String thingToRender;
    private boolean textVisible;

    public GTextbox(final Application application, final double x, final double y, final int width, final int height, final String boxIdentifier, final String inAnimationIdentifier, final String outAnimationIdentifier, final int startTextRelativeX, final int startTextRelativeY, final int endTextRelativeX, final int endTextRelativeY, final String fontIdentifier, final int fontSize, final String text) {
        super(application);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.box = (Moveable) Engine.get(boxIdentifier);
        if (inAnimationIdentifier != null) this.inAnimation = (Animation) Engine.get(inAnimationIdentifier);
        if (outAnimationIdentifier != null) this.outAnimation = (Animation) Engine.get(outAnimationIdentifier);
        this.startTextRelativeX = startTextRelativeX;
        this.startTextRelativeY = startTextRelativeY;
        this.endTextRelativeX = endTextRelativeX;
        this.endTextRelativeY = endTextRelativeY;
        this.font = Fonts.get(fontIdentifier, fontSize);
        this.textColor = Color.WHITE;
        Label label = new Label("", this.textColor, this.font);
        this.textLabel = new GText(application, this.x+startTextRelativeX, this.x+startTextRelativeY, this.endTextRelativeX-this.startTextRelativeX, this.endTextRelativeY-this.startTextRelativeY, label);
        this.text = text;
        this.displayedText = "";
        this.timeBetweenChars = 0.003;
        this.visibleCharacters = 0;
        this.frameCounter = 0;
        this.shouldUpdateVisibleCharacters = false;
        this.thingToRender = "";
        this.textVisible = true;
    }

    @Override
	public synchronized void update() {
        this.textLabel.update();

        if (this.thingToRender == "inAnim") {
            if (this.inAnimation.sprites().length == this.inAnimation.getCurrentSpriteIndex()) {
                this.thingToRender = "box";
                this.shouldUpdateVisibleCharacters = true;
            }
        }
        if (this.thingToRender == "outAnim") {
            if (this.outAnimation.sprites().length == this.outAnimation.getCurrentSpriteIndex()) {
                this.thingToRender = "";
                this.shouldUpdateVisibleCharacters = false;
            }
        }
        if (this.thingToRender == "box") {
            if (this.shouldUpdateVisibleCharacters) {
                double elapsedTime = this.frameCounter/this.application().refreshRate();
                if (elapsedTime >= this.timeBetweenChars) {
                    this.visibleCharacters++;
                    this.frameCounter = 0;
                }
                this.displayedText = this.text.substring(0,this.visibleCharacters);
                this.textLabel.label().content = this.displayedText;
                this.textLabel.label().color = this.textColor;
                this.textLabel.label().font = this.font;
                if (this.visibleCharacters >= this.text.length()) {
                    this.shouldUpdateVisibleCharacters = false;
                }
                this.frameCounter++;
            }
        }
		return;
	}

    /**
     *
     */
    @Override
    public synchronized void render(final Screen screen) {
        System.out.println(this.thingToRender);
        if (this.thingToRender == "box") {
            this.box.render(screen);
        } else if (this.thingToRender == "inAnim") {
            this.inAnimation.render(screen);
        } else if (this.thingToRender == "outAnim") {
            this.outAnimation.render(screen);
        }

        if (this.textVisible) {
            this.textLabel.render(screen);
        }

    }

    public void setText(String text) {
        this.visibleCharacters = 0;
        this.text = text;
        this.shouldUpdateVisibleCharacters = true;
    }

    public String getText() {
        return this.text;
    }

    public void setVisibleCharacters(int x) {
        this.visibleCharacters = x;
    }

    public int getVisibleCharacters() {
        return this.visibleCharacters;
    }

    public void setTextColor(Color color) {
        this.textColor = color;
    }

    public void setTimeBetweenChars(double timeInSeconds) {
        this.timeBetweenChars = timeInSeconds;
    }

    public void showBox() {
        if (this.inAnimation != null) {
            this.thingToRender = "inAnim";
        } else {
            this.thingToRender = "box";
        }
    }

    public void hideBox() {
        if (this.outAnimation != null) {
            this.thingToRender = "outAnim";
        } else {
            this.thingToRender = "";
        }
    }

    public void showText() {
        this.textVisible = true;
    }

    public void hideText() {
        this.textVisible = false;
    }

    public void changeFont(String fontIdentifier, int size) {
        this.font = Fonts.get(fontIdentifier, size);
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
    }

    /**
     * Self explanatory.
     */
    @Override
    public void setHeight(final int height) {
    }

	/**
	 * Self explanatory.
	 */
	@Override
	public boolean containsMouse(final int x, final int y) {
		return false;
	}

    /**
     * Related to global components management. Focus management is different for
     * buttons so these do nothing.
     */
    @Override
    public final void grantFocus() {
        return;
    }

    /**
     * Related to global components management. Focus management is different for
     * buttons so these do nothing.
     */
    @Override
    public final void revokeFocus() {
        return;
    }

    /**
     * Related to global components management. We can only switch focus out of the
     * button if it's completely idle.
     */
    @Override
    public final boolean canChangeFocus() {
        return false;
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
		// return Engine.identifier(this) + " -> GTextbox.class(" + this.x + ", " + this.y + ", " + this.label.toString() + ")";
        return "";
    }
}
