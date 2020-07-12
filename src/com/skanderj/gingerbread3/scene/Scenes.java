package com.skanderj.gingerbread3.scene;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skanderj.gingerbread3.component.Components;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.transition.Transition;

/**
 * Self explanatory.
 *
 * @author Skander
 *
 */
public final class Scenes {
	// Scenes map to quickly retrieve a scene by its identifier
	private static final Map<String, Scene> scenesMap = new HashMap<>();
	private static Scene currentScene;
	private static Transition currentTransition;

	private Scenes() {
		return;
	}

	/**
	 * Self explanatory.
	 */
	public static void register(final String identifier, final Scene scene) {
		Scenes.scenesMap.put(identifier, scene);
		Registry.register(identifier, scene);
	}

	/**
	 * Self explanatory.
	 */
	public static Scene get(final String identifier) {
		return Scenes.scenesMap.get(identifier);
	}

	/**
	 * Self explanatory.
	 */
	public static void switchTo(final String identifier) {
		final Scene previous = Scenes.currentScene;
		Scenes.currentScene = Scenes.get(identifier);
		if (previous != null) {
			previous.exit();
		}
		Registry.newScene();
		final List<String> gameObjects = Scenes.currentScene.sceneObjects();
		Components.considerOnly(gameObjects);
		Scenes.currentScene.enter();
	}

	/**
	 * Self explanatory.
	 */
	public static void update() {
		if (Scenes.currentScene != null) {
			Scenes.currentScene.update();
		}
		if (Scenes.currentTransition != null) {
			Scenes.currentTransition.update();
		}
	}

	/**
	 * Self explanatory.
	 */
	public static void render(final Screen screen) {
		if (Scenes.currentScene != null) {
			Scenes.currentScene.render(screen);
		}
		if (Scenes.currentTransition != null) {
			Scenes.currentTransition.render(screen);
		}
	}

	/**
	 * Self explanatory.
	 */
	public static void transition(final String identifier) {
		Scenes.currentTransition = (Transition) Registry.get(identifier);
	}

	/**
	 * Self explanatory.
	 */
	public static Scene scene() {
		return Scenes.currentScene;
	}

	/**
	 * Self explanatory.
	 */
	public static Transition transition() {
		return Scenes.currentTransition;
	}
}
