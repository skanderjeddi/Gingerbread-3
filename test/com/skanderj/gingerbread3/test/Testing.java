package com.skanderj.gingerbread3.test;

import java.awt.Color;
import java.awt.Graphics2D;

import com.skanderj.gingerbead3.audio.AudioManager;
import com.skanderj.gingerbead3.component.ComponentManager;
import com.skanderj.gingerbead3.component.basic.G3SolidBackground;
import com.skanderj.gingerbead3.core.Game;
import com.skanderj.gingerbead3.input.Keyboard;
import com.skanderj.gingerbead3.input.Mouse;
import com.skanderj.gingerbead3.log.Logger;
import com.skanderj.gingerbead3.log.Logger.DebuggingType;
import com.skanderj.gingerbead3.util.GraphicsUtilities;

public class Testing extends Game {
	public static final String IDENTIFIER = "g-3t", TITLE = "Gingerbread-3 [TESTING]";
	public static final double REFRESH_RATE = 60.0D;
	public static final int WIDTH = 1200, HEIGHT = Testing.WIDTH / 16 * 9, BUFFERS = 3;

	public Testing() {
		super(Testing.IDENTIFIER, Testing.REFRESH_RATE, Testing.TITLE, Testing.WIDTH, Testing.HEIGHT, Testing.BUFFERS);
		this.keyboard = new Keyboard();
		this.mouse = new Mouse();
	}

	@Override
	public void loadResources() {
		AudioManager.registerAudio("silhouette-kana_boon", "res/audio/silhouette.wav");
	}

	@Override
	public void postCreate() {
		super.postCreate();
		this.displayRefreshRate = false;
		AudioManager.loopAudio("silhouette-kana_boon", -1);
	}

	@Override
	public void registerComponents() {
		ComponentManager.addComponent("main-menu-background", new G3SolidBackground(GraphicsUtilities.DEFAULT_ORIGIN_X, GraphicsUtilities.DEFAULT_ORIGIN_Y, Testing.WIDTH, Testing.HEIGHT, Color.WHITE));
	}

	@Override
	public void update(double delta) {
		ComponentManager.update(delta, this.keyboard, this.mouse);
	}

	@Override
	public void render(Graphics2D graphics) {
		GraphicsUtilities.clear(this.window, graphics, Color.BLACK);
		ComponentManager.render(this.window, graphics);
	}

	public static void main(String[] args) {
		Logger.setDebuggingState(DebuggingType.CLASSIC, true);
		Logger.setDebuggingState(DebuggingType.DEVELOPMENT, true);
		new Testing().start();
	}
}
