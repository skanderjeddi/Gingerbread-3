package com.skanderj.gingerbread3.scene;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skanderj.gingerbread3.component.Components;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.logging.Logger;
import com.skanderj.gingerbread3.logging.Logger.LogLevel;
import com.skanderj.gingerbread3.scheduler.Scheduler;
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

	private Scenes() {
		return;
	}

	/**
	 * Self explanatory.
	 */
	public static void register(final String identifier, final Scene scene) {
		Scenes.scenesMap.put(identifier, scene);
		Engine.register(identifier, scene);
	}

	/**
	 * Self explanatory.
	 */
	public static Scene get(final String identifier) {
		return Scenes.scenesMap.get(identifier);
	}

	public static void switchTo(final String identifier) {
		// Get the possible previous and next scenes
		final Scene previous = Scenes.currentScene, next = Scenes.get(identifier);
		// If next is null, no scenes to switch to
		if (next == null) {
			Logger.log(Scenes.class, LogLevel.SEVERE, "Can't switch to non existent scene %s", identifier);
			return;
		}
		// If there is actually a previous scene
		if (previous != null) {
			// Try to get the previous scene's exiting transition
			final Transition outTransition = (Transition) Engine.get(previous.outTransition());
			// Try to get the next scene's transition
			final Transition inTransition = (Transition) Engine.get(next.inTransition());
			// If there is actually an exiting transition
			if (outTransition != null) {
				// Register it
				Engine.register(previous.outTransition(), outTransition);
				// Make it renderable
				Engine.get(previous.outTransition()).setShouldSkipRegistryChecks(true);
				// Schedule a task to remove it and play the potential next transition
				Scheduler.schedule(previous.outTransition() + "-recurr", new Runnable() {
					@Override
					public void run() {
						if (outTransition.isDone()) {
							Engine.get(previous.outTransition()).setShouldSkipRegistryChecks(false);
							Engine.skip(previous.outTransition());
							Scheduler.cancel(previous.outTransition() + "-recurr", true);
							Scenes.currentScene = next;
							Scenes.newScene();
							if (inTransition != null) {
								Engine.register(next.inTransition(), inTransition);
								Engine.get(next.inTransition()).setShouldSkipRegistryChecks(true);
							}
						}
					}
				}, 0, true, 1);
			}
			previous.out();
		} else {
			Scenes.currentScene = next;
			Scenes.newScene();
			final Transition inTransition = (Transition) Engine.get(next.inTransition());
			if (inTransition != null) {
				Engine.register(next.inTransition(), inTransition);
				Engine.get(next.inTransition()).setShouldSkipRegistryChecks(true);
				Scheduler.schedule(next.inTransition() + "-delayed", new Runnable() {
					@Override
					public void run() {
						if (inTransition.isDone()) {
							Engine.get(next.inTransition()).setShouldSkipRegistryChecks(false);
							Engine.skip(next.inTransition());
							Scheduler.cancel(next.inTransition() + "-recurr", true);
						}
					}
				}, inTransition.duration(), false, 0);
			}
		}
	}

	public static void newScene() {
		Engine.newScene();
		final List<String> gameObjects = Scenes.currentScene.sceneObjects();
		Components.considerOnly(gameObjects);
		Scenes.currentScene.in();
	}

	/**
	 * Self explanatory.
	 */
	public static void update() {
		if (Scenes.currentScene != null) {
			Scenes.currentScene.update();
		}
	}

	/**
	 * Self explanatory.
	 */
	public static void render(final Screen screen) {
		if (Scenes.currentScene != null) {
			Scenes.currentScene.render(screen);
		}
	}

	/**
	 * Self explanatory.
	 */
	public static Scene scene() {
		return Scenes.currentScene;
	}
}
