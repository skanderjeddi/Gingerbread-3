package com.skanderj.gingerbread3.core.object;

import com.skanderj.gingerbread3.core.G3Application;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.Renderable;
import com.skanderj.gingerbread3.core.Updatable;
import com.skanderj.gingerbread3.display.Screen;

/**
 * Represents a g3Application object, the building blocks of any g3Application.
 *
 * @author Skander
 *
 */
public abstract class G3Object implements Comparable<G3Object> {
	/**
	 * Self explanatory.
	 */
	public static final G3Object constructFromUpdatable(final G3Application g3Application, final Updatable updatable) {
		return new G3Object(g3Application) {

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
	public static final G3Object constructFromRenderable(final G3Application g3Application, final Renderable renderable) {
		return new G3Object(g3Application) {

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

	protected final G3Application g3Application;
	protected boolean shouldSkipRegistryChecks;

	public G3Object(final G3Application g3Application) {
		this.g3Application = g3Application;
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

	/**
	 * Returns this object's app.
	 */
	public final G3Application application() {
		return this.g3Application;
	}

	public abstract Priority priority();

	// Priority comparison, could be nicer but flemme
	@Override
	public int compareTo(final G3Object g3Object) {
		return -(this.priority().priorityIndex - g3Object.priority().priorityIndex);
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
