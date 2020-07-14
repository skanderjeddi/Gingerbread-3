package com.skanderj.gingerbread3.core.object;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.Registry;
import com.skanderj.gingerbread3.core.Renderable;
import com.skanderj.gingerbread3.core.Updateable;
import com.skanderj.gingerbread3.display.Screen;

/**
 * Represents a application object, the building blocks of any application.
 *
 * @author Skander
 *
 */
public abstract class ApplicationObject implements Comparable<ApplicationObject> {
	/**
	 * Self explanatory.
	 */
	public static final ApplicationObject constructFromUpdateable(final Application application, final Updateable updateable) {
		return new ApplicationObject(application) {

			@Override
			public void update() {
				updateable.update();
			}

			@Override
			public void render(final Screen screen) {
				return;
			}

			@Override
			public Priority priority() {
				return updateable.priority();
			}

			@Override
			public String description() {
				return Registry.identifier(this) + " = Updateable -> ApplicationObject.class()";
			}
		};
	}

	/**
	 * Self explanatory.
	 */
	public static final ApplicationObject constructFromRenderable(final Application application, final Renderable renderable) {
		return new ApplicationObject(application) {

			@Override
			public void update() {
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

			@Override
			public String description() {
				return Registry.identifier(this) + " = Renderable -> ApplicationObject.class()";
			}
		};
	}

	protected final Application application;
	protected boolean shouldSkipRegistryChecks;

	public ApplicationObject(final Application application) {
		this.application = application;
		this.shouldSkipRegistryChecks = false;
	}

	/**
	 * Updates the component - called by other classes.
	 */
	public abstract void update();

	/**
	 * Renders the component - called by other classes.
	 */
	public abstract void render(Screen screen);

	public void sceneChange() {
		return;
	}

	/**
	 * Returns this object's app.
	 */
	public final Application application() {
		return this.application;
	}

	public abstract Priority priority();

	// Priority comparison, could be nicer but flemme
	@Override
	public int compareTo(final ApplicationObject object) {
		return -(this.priority().priorityIndex - object.priority().priorityIndex);
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

	public abstract String description();

	@Override
	public final String toString() {
		return this.description();
	}
}
