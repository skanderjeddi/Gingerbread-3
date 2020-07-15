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
import com.skanderj.gingerbread3.scheduler.Scheduler;
import com.skanderj.gingerbread3.scheduler.tasks.DelayedTask;
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
		Engine.register(identifier, scene);
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
			if (((Transition) Engine.get(previous.exitingTransition()) != null)) {
				final Transition exit = (Transition) Engine.get(previous.exitingTransition());
				Scenes.currentTransition = exit;
				Scheduler.scheduleTask(previous.application(), new DelayedTask("transition-time", exit.duration()) {
					@Override
					public Priority priority() {
						return Priority.MONITOR;
					}

					@Override
					public Application application() {
						return previous.application();
					}

					@Override
					public void execute(final ApplicationObject object) {
						if (Scenes.currentScene.enteringTransition() != null) {
							Scenes.currentTransition = (Transition) Engine.get(Scenes.currentScene.enteringTransition());
							Scenes.newScene();
						} else {
							Scenes.currentTransition = null;
							Scenes.newScene();
						}
					}
				});
			} else {
				Scenes.currentTransition = (Transition) Engine.get(Scenes.currentScene.enteringTransition());
				Scenes.newScene();
			}
		} else {
			Scenes.currentTransition = (Transition) Engine.get(Scenes.currentScene.enteringTransition());
			Scenes.newScene();
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
