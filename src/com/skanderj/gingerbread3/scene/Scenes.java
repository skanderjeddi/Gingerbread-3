package com.skanderj.gingerbread3.scene;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skanderj.gingerbread3.component.Components;
import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Engine;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.object.ApplicationObject;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.logging.Logger;
import com.skanderj.gingerbread3.logging.Logger.LogLevel;
import com.skanderj.gingerbread3.scheduler.Scheduler;
import com.skanderj.gingerbread3.scheduler.tasks.DelayedTask;
import com.skanderj.gingerbread3.scheduler.tasks.RecurrentTask;
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
				Scheduler.scheduleTask(outTransition.application(), new RecurrentTask(previous.application(), previous.outTransition() + "-recurr", 0) {
					@Override
					public Priority priority() {
						return Priority.EXTREMELY_HIGH;
					}

					@Override
					public Application application() {
						return outTransition.application();
					}

					@Override
					public void execute(final ApplicationObject object) {
						if (outTransition.isDone()) {
							Engine.get(previous.outTransition()).setShouldSkipRegistryChecks(false);
							Engine.skip(previous.outTransition());
							Scheduler.delete(previous.outTransition() + "-recurr");
							Scenes.currentScene = next;
							Scenes.newScene();
							if (inTransition != null) {
								Engine.register(next.inTransition(), inTransition);
								Engine.get(next.inTransition()).setShouldSkipRegistryChecks(true);
							}
						}
					}
				});
			}
			previous.out();
		} else {
			Scenes.currentScene = next;
			Scenes.newScene();
			final Transition inTransition = (Transition) Engine.get(next.inTransition());
			if (inTransition != null) {
				Engine.register(next.inTransition(), inTransition);
				Engine.get(next.inTransition()).setShouldSkipRegistryChecks(true);
				Scheduler.scheduleTask(inTransition.application(), new DelayedTask(next.application(), next.inTransition() + "-delayed", inTransition.duration()) {
					@Override
					public Priority priority() {
						return Priority.EXTREMELY_HIGH;
					}

					@Override
					public Application application() {
						return inTransition.application();
					}

					@Override
					public void execute(final ApplicationObject object) {
						if (inTransition.isDone()) {
							Engine.get(next.inTransition()).setShouldSkipRegistryChecks(false);
							Engine.skip(next.inTransition());
							Scheduler.delete(next.inTransition() + "-recurr");
						}
					}
				});
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
