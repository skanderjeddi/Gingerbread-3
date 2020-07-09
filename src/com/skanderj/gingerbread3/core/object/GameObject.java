package com.skanderj.gingerbread3.core.object;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.Renderable;
import com.skanderj.gingerbread3.core.Updatable;
import com.skanderj.gingerbread3.display.Screen;

/**
 * Represents a application object, the building blocks of any application.
 *
 * @author Skander
 *
 */
public abstract class GameObject implements Comparable<GameObject> {
	/**
	 * Self explanatory.
	 */
	public static final GameObject constructFromUpdatable(final Application application, final Updatable updatable) {
		return new GameObject(application) {

			@Override
			public void update(final double delta) {
				updatable.update(delta);
			}

			@Override
			public void render(final Screen screen) {
				return;
			}

			@Override
			public Priority priority() {
				return updatable.priority();
			}
		};
	}

	/**
	 * Self explanatory.
	 */
	public static final GameObject constructFromRenderable(final Application application, final Renderable renderable) {
		return new GameObject(application) {

			@Override
			public void update(final double delta) {
				return;
			}

			@Override
			public void render(final Screen screen) {
				renderable.render(screen);
			}

			@Override
			public Priority priority() {
				return renderable.priority();
			}
		};
	}

	protected final Application application;
	protected boolean shouldSkipRegistryChecks;

	public GameObject(final Application application) {
		this.application = application;
		this.shouldSkipRegistryChecks = false;
	}

	/**
	 * Updates the component - called by other classes.
	 */
	public abstract void update(double delta);

	/**
	 * Renders the component - called by other classes.
	 */
	public abstract void render(Screen screen);

	public void sceneChange() {
		return;
	}

	public final Application getApplication() {
		return this.application;
	}

	public abstract Priority priority();

	// Priority comparison, could be nicer but flemme
	@Override
	public int compareTo(final GameObject gameObject) {
		return -(this.priority().priorityIndex - gameObject.priority().priorityIndex);
	}

	/**
	 * Called by other classes.
	 */
	public boolean shouldSkipRegistryChecks() {
		return this.shouldSkipRegistryChecks;
	}

	/**
	 * Called by other classes.
	 */
	public final void setShouldSkipRegistryChecks(final boolean shouldSkipRegistryChecks) {
		this.shouldSkipRegistryChecks = shouldSkipRegistryChecks;
	}
}
