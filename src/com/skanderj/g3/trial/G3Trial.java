package com.skanderj.g3.trial;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;

import com.skanderj.g3.animation.character.CharacterAnimation;
import com.skanderj.g3.audio.AudioManager;
import com.skanderj.g3.component.Button;
import com.skanderj.g3.component.Button.ButtonState;
import com.skanderj.g3.component.Component;
import com.skanderj.g3.component.ComponentManager;
import com.skanderj.g3.component.Label;
import com.skanderj.g3.component.Selector;
import com.skanderj.g3.component.Slider;
import com.skanderj.g3.component.Textfield;
import com.skanderj.g3.component.action.ButtonAction;
import com.skanderj.g3.component.basic.G3Label;
import com.skanderj.g3.component.basic.G3REButton;
import com.skanderj.g3.component.basic.G3Selector;
import com.skanderj.g3.component.basic.G3Slider;
import com.skanderj.g3.component.basic.G3Slider.SliderLabelPosition;
import com.skanderj.g3.component.basic.G3Textfield;
import com.skanderj.g3.io.FontManager;
import com.skanderj.g3.io.ImageManager;
import com.skanderj.g3.log.Logger;
import com.skanderj.g3.log.Logger.LogLevel;
import com.skanderj.g3.sprite.Sprite;
import com.skanderj.g3.translation.Key;
import com.skanderj.g3.translation.TranslationManager;
import com.skanderj.g3.util.GraphicString;
import com.skanderj.g3.util.TextProperties;
import com.skanderj.g3.window.Window;
import com.skanderj.g3.window.inputdevice.Keyboard;
import com.skanderj.g3.window.inputdevice.Mouse;

public final class G3Trial {
	private static G3Trial instance;

	public static final G3Trial getTrialInstance() {
		return G3Trial.instance == null ? G3Trial.instance = new G3Trial() : G3Trial.instance;
	}

	private Window window;
	private Keyboard keyboard;
	private Mouse mouse;

	private Textfield textfield;
	private Button playbackButton;
	private Slider volumeSlider;
	private Label volumeLabel;
	private Selector selector;
	private Slider redSlider, greenSlider, blueSlider;

	private TextProperties buttonTextStyle;
	private int leftAlignment, middleAlignment;

	private CharacterAnimation robertIdleAnimation, jessicaIdleAnimation;

	private G3Trial() {
		this.window = new Window.Fullscreen(null, "G3T", Window.TRIPLE_BUFFERING, Window.Fullscreen.isDeviceIDAvailable(0), Window.Fullscreen.DEFAULT_FALLBACK_DEVICE_ID);
		this.keyboard = new Keyboard();
		this.mouse = new Mouse();
	}

	public synchronized final void run() {
		this.create();
	}

	private synchronized final void create() {
		this.loadResources();
		this.window.create();
		this.window.registerInput(this.keyboard);
		this.window.registerInput(this.mouse);
		this.window.show();
		{
			this.buttonTextStyle = new TextProperties(FontManager.getFont("lunchtime", 24), Color.WHITE);
			this.leftAlignment = this.window.getWP(1f / 44f);
			this.middleAlignment = this.window.getWP(1f / 2f);
		}
		this.registerComponents();
		this.window.requestFocus();
		this.enterLoop();
	}

	private synchronized void registerComponents() {
		this.textfield = new G3Textfield(this.leftAlignment, this.window.getHP((float) 1 / 30), this.window.getWP((float) 42 / 44), Color.PINK, new TextProperties(FontManager.getFont("lunchtime", 36, Font.BOLD), Color.BLACK), 4);
		this.playbackButton = new G3REButton(this.leftAlignment, this.window.getHP(1f / 4.8f), this.window.getWP(1f / 20f), this.window.getHP(1f / 20f), new GraphicString(Key.getKey("key.playbackbutton.pause"), this.buttonTextStyle, Color.DARK_GRAY), new Color(100, 100, 100), Color.BLACK, 16);
		this.playbackButton.setButtonAction(ButtonState.IDLE, new ButtonAction() {
			@Override
			public void execute(Object... args) {
				((G3REButton) G3Trial.this.playbackButton).getLabel().setColor(Color.YELLOW);
			}
		});
		this.playbackButton.setButtonAction(ButtonState.HOVERED, new ButtonAction() {
			@Override
			public void execute(Object... args) {
				((G3REButton) G3Trial.this.playbackButton).getLabel().setColor(Color.CYAN);
			}
		});
		this.playbackButton.setButtonAction(ButtonState.CLICKED, new ButtonAction() {
			@Override
			public void execute(Object... args) {
				((G3REButton) G3Trial.this.playbackButton).getLabel().setColor(Color.RED);
			}
		});
		this.playbackButton.setButtonAction(ButtonState.ON_ACTUAL_CLICK, new ButtonAction() {
			@Override
			public void execute(Object... args) {
				if (((G3REButton) G3Trial.this.playbackButton).getLabel().getContent().equals((TranslationManager.getKey("key.playbackbutton.pause")))) {
					AudioManager.pauseAudio("theme");
					((G3REButton) G3Trial.this.playbackButton).getLabel().setContent((TranslationManager.getKey("key.playbackbutton.resume")));
				} else {
					AudioManager.resumeAudio("theme");
					((G3REButton) G3Trial.this.playbackButton).getLabel().setContent((TranslationManager.getKey("key.playbackbutton.pause")));
				}
			}
		});
		this.volumeSlider = new G3Slider(this.leftAlignment, this.window.getHP(1f / 3.5f), 150, 15, 4, 46, 0f, 1f, 0.5f, Color.WHITE, new GraphicString((TranslationManager.getKey("key.volumeslider.label")), Color.WHITE, FontManager.getFont("lunchtime", 24)), G3Slider.SliderLabelPosition.RIGHT);
		this.volumeLabel = new G3Label(this.leftAlignment, this.window.getHP(1f / 3f), 250, 60, new GraphicString(TranslationManager.getKey("key.volume.label", 0f), Color.YELLOW, FontManager.getFont("lunchtime", 24)));
		this.selector = new G3Selector(this.leftAlignment, this.window.getHP(1f / 2.4f), 100, 70, 50, new TextProperties(FontManager.getFont("lunchtime", 24), Color.DARK_GRAY), new String[] { "Black", "White", "Pink", "Yellow", "Blue", "Red", "Green", "Custom" }, "Black");
		this.redSlider = new G3Slider(this.middleAlignment, this.window.getHP(1f / 4f), 255, 20, 4, 24, 0f, 255f, 128f, Color.BLACK, new GraphicString("RED (%.0f)", Color.RED, FontManager.getFont("lunchtime", 24)), SliderLabelPosition.RIGHT);
		this.greenSlider = new G3Slider(this.middleAlignment, this.window.getHP(1f / 3.4f), 255, 20, 4, 24, 0f, 255f, 128f, Color.BLACK, new GraphicString("GREEN (%.0f)", Color.GREEN, FontManager.getFont("lunchtime", 24)), SliderLabelPosition.RIGHT);
		this.blueSlider = new G3Slider(this.middleAlignment, this.window.getHP(1f / 2.8f), 255, 20, 4, 24, 0f, 255f, 128f, Color.BLACK, new GraphicString("BLUE (%.0f)", Color.BLUE, FontManager.getFont("lunchtime", 24)), SliderLabelPosition.RIGHT);
		String[] identifiers = { "text-box-test", "playback-button", "volume-volumeSlider", "volume-label", "color-selector", "red-slider", "green-slider", "blue-slider" };
		Component[] components = { this.textfield, this.playbackButton, this.volumeSlider, this.volumeLabel, this.selector, this.redSlider, this.greenSlider, this.blueSlider };
		ComponentManager.addComponents(identifiers, components);
		ComponentManager.skipComponents("red-slider", "green-slider", "blue-slider");
		Sprite[] robertSprites = new Sprite[] { new Sprite("idle_0", ImageManager.retrieveImage("idle_0"), 256, 256), new Sprite("idle_0", ImageManager.retrieveImage("idle_1"), 256, 256) };
		Sprite[] jessicaSprites = new Sprite[] { new Sprite("jessica_idle_0", ImageManager.retrieveImage("jessica_idle_0"), 256, 256), new Sprite("jessica_idle_1", ImageManager.retrieveImage("jessica_idle_1"), 256, 256) };
		this.robertIdleAnimation = new CharacterAnimation(this.leftAlignment, this.window.getHeight() - 292, robertSprites, new int[] { 60, 60 });
		this.jessicaIdleAnimation = new CharacterAnimation(this.leftAlignment + 256 + 64, this.window.getHeight() - 292, jessicaSprites, new int[] { 30, 30 });
	}

	private synchronized void loadResources() {
		FontManager.registerFont("lunchtime", "res/font/lunchds.ttf");
		FontManager.registerFont("roboto", "res/font/roboto.ttf");
		AudioManager.registerAudio("theme", "res/audio/silhouette.wav");
		AudioManager.registerAll("fart_%d", "res/audio/fart/");
		ImageManager.registerImage("idle_0", "res/sprite/main/idle_0.png");
		ImageManager.registerImage("idle_1", "res/sprite/main/idle_1.png");
		ImageManager.registerImage("jessica_idle_0", "res/sprite/main/jessica_idle_0.png");
		ImageManager.registerImage("jessica_idle_1", "res/sprite/main/jessica_idle_1.png");
	}

	private synchronized final void enterLoop() {
		AudioManager.loopAudio("theme", -1);
		while (!this.window.isCloseRequested()) {
			this.update();
			this.render();
			try {
				Thread.sleep(1000 / 60);
			} catch (InterruptedException interruptedException) {
				interruptedException.printStackTrace();
			}
		}
		this.destroy();
	}

	private synchronized final void destroy() {
		this.window.hide();
		System.exit(0);
	}

	private synchronized final void update() {
		if (this.keyboard.isKeyDownInFrame(Keyboard.KEY_ESCAPE)) {
			this.window.requestClosing();
		}
		ComponentManager.update(0, this.keyboard, this.mouse);
		if (this.mouse.isButtonDown(Mouse.BUTTON_LEFT)) {
			Logger.log(ComponentManager.class, LogLevel.DEV_DEBUG, TranslationManager.getKey("key.component.infocus", ComponentManager.getInFocus()));
		}
		AudioManager.setVolume("theme", this.volumeSlider.getValue());
		((Label) ComponentManager.getComponent("volume-label")).getGraphicString().setContent(TranslationManager.getKey("key.volume.label", AudioManager.getVolume("theme") * 100f));
		if (this.selector.getCurrentOption().equalsIgnoreCase("custom")) {
			ComponentManager.unskipComponents("red-slider", "green-slider", "blue-slider");
		} else {
			ComponentManager.skipComponents("red-slider", "green-slider", "blue-slider");
		}
		this.robertIdleAnimation.update(0, keyboard, mouse);
		this.jessicaIdleAnimation.update(0, keyboard, mouse);
		this.keyboard.update();
		this.mouse.update();
	}

	private synchronized final void render() {
		BufferStrategy bufferStrategy = this.window.getBufferStrategy();
		Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, this.window.getWidth(), this.window.getHeight());
		{
			switch (this.selector.getCurrentOption().toLowerCase()) {
			case "black":
				graphics.setColor(Color.BLACK);
				break;
			case "white":
				graphics.setColor(Color.WHITE);
				break;
			case "pink":
				graphics.setColor(Color.PINK);
				break;
			case "yellow":
				graphics.setColor(Color.YELLOW);
				break;
			case "blue":
				graphics.setColor(Color.BLUE);
				break;
			case "red":
				graphics.setColor(Color.RED);
				break;
			case "green":
				graphics.setColor(Color.GREEN);
				break;
			case "custom":
				int red = (int) this.redSlider.getValue();
				int green = (int) this.greenSlider.getValue();
				int blue = (int) this.blueSlider.getValue();
				Color color = new Color(red, green, blue);
				graphics.setColor(color);
				break;
			}
			graphics.fillRect(0, 0, this.window.getWidth(), this.window.getHeight());
		}
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		graphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		ComponentManager.render(this.window, graphics);
		this.robertIdleAnimation.render(window, graphics);
		this.jessicaIdleAnimation.render(window, graphics);
		graphics.dispose();
		bufferStrategy.show();
	}
}
