package com.skanderj.gingerbread3;

import java.awt.Color;

import com.skanderj.gingerbread3.core.Application;
import com.skanderj.gingerbread3.core.Priority;
import com.skanderj.gingerbread3.core.object.ApplicationObject;
import com.skanderj.gingerbread3.display.Screen;
import com.skanderj.gingerbread3.util.Utilities;

public class PetitPD extends ApplicationObject {

	public PetitPD(Application application) {
		super(application);
	}

	@Override
	public void update() {
		return;
	}

	@Override
	public void render(Screen screen) {
		screen.rectangle(Color.YELLOW, Utilities.randomInteger(0, G3Demo.WIDTH), Utilities.randomInteger(0, G3Demo.HEIGHT), 20, 20, true);
	}

	@Override
	public Priority priority() {
		return Priority.REGULAR;
	}

	@Override
	public String description() {
		return "Un petit pd";
	}

}
