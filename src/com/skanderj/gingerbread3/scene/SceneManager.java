package com.skanderj.gingerbread3.scene;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skanderj.gingerbread3.component.ComponentManager;

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
		final List<String> gameObjects = SceneManager.currentScene.sceneObjects();
		ComponentManager.onlyConsider(gameObjects);
		SceneManager.currentScene.present();
	}

	/**
	 * Self explanatory.
	 */
	public static void updateScene(final double delta, final Object... args) {
		if (SceneManager.currentScene != null) {
			SceneManager.currentScene.update(delta, args);
		}
	}

	/**
	 * Self explanatory.
	 */
	public static void renderScene(final Graphics2D graphics, final Object... args) {
		if (SceneManager.currentScene != null) {
			SceneManager.currentScene.render(graphics, args);
		}
	}

	public static Scene getCurrentScene() {
		return SceneManager.currentScene;
	}
}
