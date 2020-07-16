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
		final Scene previous = Scenes.currentScene, next = Scenes.get(identifier);
		if (next == null) {
			Logger.log(Scenes.class, LogLevel.SEVERE, "Can't switch to non existent scene %s", identifier);
			return;
		}
		if (previous != null) {
			final Transition exitingTransition = (Transition) Engine.get(previous.exitingTransition());
			final Transition enteringTransition = (Transition) Engine.get(next.enteringTransition());
			if (exitingTransition != null) {
				Engine.register(previous.exitingTransition(), exitingTransition);
				Engine.get(previous.exitingTransition()).setShouldSkipRegistryChecks(true);
				Scheduler.scheduleTask(exitingTransition.application(), new RecurrentTask(previous.application(), previous.exitingTransition() + "-recurr", 0) {
					@Override
					public Priority priority() {
						return Priority.EXTREMELY_HIGH;
					}

					@Override
					public Application application() {
						return exitingTransition.application();
					}

					@Override
					public void execute(final ApplicationObject object) {
						if (exitingTransition.isDone()) {
							Engine.get(previous.exitingTransition()).setShouldSkipRegistryChecks(false);
							Engine.skip(previous.exitingTransition());
							Scheduler.delete(previous.exitingTransition() + "-recurr");
							Scenes.currentScene = next;
							Scenes.newScene();
							if (enteringTransition != null) {
								Engine.register(next.enteringTransition(), enteringTransition);
								Engine.get(next.enteringTransition()).setShouldSkipRegistryChecks(true);
							}
						}
					}
				});
			}
			previous.exit();
		} else {
			Scenes.currentScene = next;
			Scenes.newScene();
			final Transition enteringTransition = (Transition) Engine.get(next.enteringTransition());
			if (enteringTransition != null) {
				Engine.register(next.enteringTransition(), enteringTransition);
				Engine.get(next.enteringTransition()).setShouldSkipRegistryChecks(true);
				Scheduler.scheduleTask(enteringTransition.application(), new DelayedTask(next.application(), next.enteringTransition() + "-delayed", enteringTransition.duration()) {
					@Override
					public Priority priority() {
						return Priority.EXTREMELY_HIGH;
					}

					@Override
					public Application application() {
						return enteringTransition.application();
					}

					@Override
					public void execute(final ApplicationObject object) {
						if (enteringTransition.isDone()) {
							Engine.get(next.enteringTransition()).setShouldSkipRegistryChecks(false);
							Engine.skip(next.enteringTransition());
							Scheduler.delete(next.enteringTransition() + "-recurr");
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
		Scenes.currentScene.enter();
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
