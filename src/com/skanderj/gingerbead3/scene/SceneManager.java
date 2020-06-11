package com.skanderj.gingerbead3.scene;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skanderj.gingerbead3.component.ComponentManager;
import com.skanderj.gingerbead3.core.Game;

/**
 * Self explanatory.
 *
 * @author Skander
 *
 */
public class SceneManager {
	// Scenes map to quickly retrieve a scene by its identifier
	private static final Map<String, Scene> scenesMap = new HashMap<String, Scene>();

	private static Scene currentScene;

	private SceneManager() {
		return;
	}

	/**
	 * Self explanatory.
	 */
	public static void registerScene(final String identifier, final Scene scene) {
		SceneManager.scenesMap.put(identifier, scene);
	}

	/**
	 * Self explanatory.
	 */
	public static Scene retrieveScene(final String identifier) {
		return SceneManager.scenesMap.get(identifier);
	}

	/**
	 * Self explanatory.
	 */
	public static void setCurrentScene(final String identifier) {
		if (SceneManager.currentScene != null) {
			SceneManager.currentScene.remove();
		}
		SceneManager.currentScene = SceneManager.retrieveScene(identifier);
		final List<String> components = SceneManager.currentScene.sceneComponents();
		ComponentManager.onlyConsider(components);
		SceneManager.currentScene.present();
	}

	/**
	 * Self explanatory.
	 */
	public static void updateScene(final Game game, final double delta) {
		if (SceneManager.currentScene != null) {
			SceneManager.currentScene.update(delta, game.getKeyboard(), game.getMouse());
		}
	}

	/**
	 * Self explanatory.
	 */
	public static void renderScene(final Game game, final Graphics2D graphics) {
		if (SceneManager.currentScene != null) {
			SceneManager.currentScene.render(game.getWindow(), graphics);
		}
	}
}
