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
	public static void registerScene(String identifier, Scene scene) {
		SceneManager.scenesMap.put(identifier, scene);
	}

	/**
	 * Self explanatory.
	 */
	public static Scene retrieveScene(String identifier) {
		return SceneManager.scenesMap.get(identifier);
	}

	/**
	 * Self explanatory.
	 */
	public static void setCurrentScene(String identifier) {
		if (currentScene != null) {
			currentScene.remove();
		}
		SceneManager.currentScene = SceneManager.retrieveScene(identifier);
		final List<String> components = SceneManager.currentScene.sceneComponents();
		ComponentManager.onlyConsider(components);
		currentScene.present();
	}

	/**
	 * Self explanatory.
	 */
	public static void updateScene(Game game, double delta) {
		if (SceneManager.currentScene != null) {
			SceneManager.currentScene.update(delta, game.getKeyboard(), game.getMouse());
		}
	}

	/**
	 * Self explanatory.
	 */
	public static void renderScene(Game game, Graphics2D graphics) {
		if (SceneManager.currentScene != null) {
			SceneManager.currentScene.render(game.getWindow(), graphics);
		}
	}
}
