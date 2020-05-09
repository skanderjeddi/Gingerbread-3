package com.skanderj.g3.testing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;

import com.skanderj.g3.audio.AudioManager;
import com.skanderj.g3.component.Button;
import com.skanderj.g3.component.ButtonState;
import com.skanderj.g3.component.Component;
import com.skanderj.g3.component.ComponentManager;
import com.skanderj.g3.component.Label;
import com.skanderj.g3.component.Selector;
import com.skanderj.g3.component.Slider;
import com.skanderj.g3.component.Slider.Basic.SliderLabelPosition;
import com.skanderj.g3.component.Textbox;
import com.skanderj.g3.component.action.ButtonAction;
import com.skanderj.g3.inputdevice.Keyboard;
import com.skanderj.g3.inputdevice.Mouse;
import com.skanderj.g3.io.FontManager;
import com.skanderj.g3.log.Logger;
import com.skanderj.g3.log.Logger.LogLevel;
import com.skanderj.g3.translation.TranslationManager;
import com.skanderj.g3.util.GraphicString;
import com.skanderj.g3.util.TextProperties;
import com.skanderj.g3.util.Utilities;
import com.skanderj.g3.window.Window;

public final class G3Testing {
	private static G3Testing instance;

	public static final G3Testing getTestingInstance() {
		return G3Testing.instance == null ? G3Testing.instance = new G3Testing() : G3Testing.instance;
	}

	private Window window;
	private Keyboard keyboard;
	private Mouse mouse;

	private Textbox textbox;
	private Button playbackButton, randomFartButton;
	private Slider volumeSlider;
	private Label volumeLabel;
	private Selector selector;
	private Slider redSlider, greenSlider, blueSlider;

	private G3Testing() {
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
		this.registerComponents();
		this.window.requestFocus();
		this.enterLoop();
	}

	private synchronized void registerComponents() {
		this.textbox = new Textbox.Basic(50, 50, this.window.getWidth() - 100, Color.PINK, new TextProperties(FontManager.getFont("lunchtime", 48, Font.BOLD), Color.BLACK), 4);
		this.playbackButton = new Button.RoundEdge(50, 400, 150, 60, new GraphicString(TranslationManager.getKey("key.playbackbutton.pause"), Color.WHITE, FontManager.getFont("lunchtime", 24), Color.DARK_GRAY), Color.WHITE, Color.BLACK, 16);
		this.randomFartButton = new Button.StraightEdge(250, 400, 250, 60, new GraphicString(TranslationManager.getKey("key.fartbutton.label"), Color.WHITE, FontManager.getFont("lunchtime", 24), Color.DARK_GRAY), Color.BLACK, Color.LIGHT_GRAY);
		this.playbackButton.setButtonAction(ButtonState.IDLE, new ButtonAction() {
			@Override
			public void execute(Object... args) {
				G3Testing.this.playbackButton.getLabel().setColor(Color.YELLOW);
			}
		});
		this.playbackButton.setButtonAction(ButtonState.HOVERED, new ButtonAction() {
			@Override
			public void execute(Object... args) {
				G3Testing.this.playbackButton.getLabel().setColor(Color.CYAN);
			}
		});
		this.playbackButton.setButtonAction(ButtonState.CLICKED, new ButtonAction() {
			@Override
			public void execute(Object... args) {
				G3Testing.this.playbackButton.getLabel().setColor(Color.RED);
			}
		});
		this.playbackButton.setButtonAction(ButtonState.ON_ACTUAL_CLICK, new ButtonAction() {
			@Override
			public void execute(Object... args) {
				if (G3Testing.this.playbackButton.getLabel().getContent().equals((TranslationManager.getKey("key.playbackbutton.pause")))) {
					AudioManager.pauseAudio("theme");
					G3Testing.this.playbackButton.getLabel().setContent((TranslationManager.getKey("key.playbackbutton.resume")));
				} else {
					AudioManager.resumeAudio("theme");
					G3Testing.this.playbackButton.getLabel().setContent((TranslationManager.getKey("key.playbackbutton.pause")));
				}
			}
		});
		this.randomFartButton.setButtonAction(ButtonState.ON_ACTUAL_CLICK, new ButtonAction() {
			@Override
			public void execute(Object... args) {
				AudioManager.playAudio(String.format("fart_%d", Utilities.randomInteger(0, 4)));
			}
		});
		this.volumeSlider = new Slider.Basic(225, 525, 150, 15, 4, 46, 0f, 1f, 0.5f, Color.WHITE, new GraphicString((TranslationManager.getKey("key.volumeslider.label")), Color.WHITE, FontManager.getFont("lunchtime", 24)), Slider.Basic.SliderLabelPosition.RIGHT);
		this.volumeLabel = new Label.Basic(50, 800, 200, 60, new GraphicString(TranslationManager.getKey("key.volume.label", 0f), Color.YELLOW, FontManager.getFont("lunchtime", 24)));
		this.selector = new Selector.Basic(150, 900, 100, 70, 50, new TextProperties(FontManager.getFont("lunchtime", 24), Color.DARK_GRAY), new String[] { "Black", "White", "Pink", "Yellow", "Blue", "Red", "Green", "Custom" }, "Black");
		this.redSlider = new Slider.Basic(600, 600, 255, 20, 4, 24, 0f, 255f, 128f, Color.BLACK, new GraphicString("RED (%.0f)", Color.RED, FontManager.getFont("lunchtime", 24)), SliderLabelPosition.RIGHT);
		this.greenSlider = new Slider.Basic(600, 640, 255, 20, 4, 24, 0f, 255f, 128f, Color.BLACK, new GraphicString("GREEN (%.0f)", Color.GREEN, FontManager.getFont("lunchtime", 24)), SliderLabelPosition.RIGHT);
		this.blueSlider = new Slider.Basic(600, 680, 255, 20, 4, 24, 0f, 255f, 128f, Color.BLACK, new GraphicString("BLUE (%.0f)", Color.BLUE, FontManager.getFont("lunchtime", 24)), SliderLabelPosition.RIGHT);
		String[] identifiers = { "text-box-test", "pause-resume-playbackButton", "random-fart-button", "volume-volumeSlider", "volume-label", "color-selector", "red-slider", "green-slider", "blue-slider" };
		Component[] components = { this.textbox, this.playbackButton, this.randomFartButton, this.volumeSlider, this.volumeLabel, this.selector, this.redSlider, this.greenSlider, this.blueSlider };
		ComponentManager.addComponents(identifiers, components);
		ComponentManager.skipComponents("red-slider", "green-slider", "blue-slider");
	}

	private synchronized void loadResources() {
		FontManager.registerFont("lunchtime", "res/font/lunchds.ttf");
		FontManager.registerFont("roboto", "res/font/roboto.ttf");
		AudioManager.registerAudio("theme", "res/audio/silhouette.wav");
		AudioManager.registerAll("fart_%d", "res/audio/fart/");
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
		graphics.dispose();
		bufferStrategy.show();
	}
}
