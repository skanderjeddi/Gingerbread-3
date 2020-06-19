package com.skanderj.gingerbread3.scene;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skanderj.gingerbread3.component.ComponentManager;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.display.GraphicsWrapper;
import com.skanderj.gingerbread3.transition.Transition;

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
	private static Transition currentTransition;

	private SceneManager() {
		return;
	}

	/**
	 * Self explanatory.
	 */
	public static void register(final String identifier, final Scene scene) {
		SceneManager.scenesMap.put(identifier, scene);
	}

	/**
	 * Self explanatory.
	 */
	public static Scene get(final String identifier) {
		return SceneManager.scenesMap.get(identifier);
	}

	/**
	 * Self explanatory.
	 */
	public static void setCurrent(final String identifier) {
		if (SceneManager.currentScene != null) {
			SceneManager.currentScene.remove();
		}
		SceneManager.currentScene = SceneManager.get(identifier);
		Registry.newScene();
		final List<String> gameObjects = SceneManager.currentScene.sceneObjects();
		ComponentManager.considerOnly(gameObjects);
		SceneManager.currentScene.present();
	}

	/**
	 * Self explanatory.
	 */
	public static void update(final double delta, final Object... args) {
		if (SceneManager.currentScene != null) {
			SceneManager.currentScene.update(delta, args);
		}
		if (SceneManager.currentTransition != null) {
			SceneManager.currentTransition.update(delta, args);
		}
	}

	/**
	 * Self explanatory.
	 */
	public static void render(final GraphicsWrapper graphics) {
		if (SceneManager.currentScene != null) {
			SceneManager.currentScene.render(graphics);
		}
		if (SceneManager.currentTransition != null) {
			SceneManager.currentTransition.render(graphics);
		}
	}

	public static void transition(final String identifier) {
		SceneManager.currentTransition = (Transition) Registry.get(identifier);
	}

	public static Scene scene() {
		return SceneManager.currentScene;
	}

	public static Transition getCurrentTransition() {
		return SceneManager.currentTransition;
	}
}
