package com.skanderj.g3.util;

import java.awt.Color;
import java.util.Random;

import com.skanderj.g3.inputdevice.Keyboard;

public final class Utilities {
	private static final Random random = new Random();

	private Utilities() {
		return;
	}

	public static final int randomInteger(int a, int b) {
		if (Math.min(a, b) < 0) {
			return -random.nextInt(Math.abs(Math.min(a, b)) + 1) + random.nextInt(Math.max(a, b) + 1);
		} else {
			return Math.min(a, b) + Utilities.random.nextInt(Math.max(a, b));
		}
	}

	public static final Color randomColor(boolean useAlpha) {
		return new Color(Utilities.random.nextInt(255), Utilities.random.nextInt(255), Utilities.random.nextInt(255), useAlpha ? Utilities.random.nextInt(255) : 255);
	}

	public static final float map(float value, float valueMin, float valueMax, float targetMin, float targetMax, boolean withinBounds) {
		float newval = (((value - valueMin) / (valueMax - valueMin)) * (targetMax - targetMin)) + targetMin;
		if (!withinBounds) {
			return newval;
		}
		if (targetMin < targetMax) {
			return Utilities.constrain(newval, targetMin, targetMax);
		} else {
			return Utilities.constrain(newval, targetMax, targetMin);
		}
	}

	private static float constrain(float value, float minimum, float maximum) {
		return Math.max(Math.min(value, maximum), minimum);
	}

	public static final String keyName(int keycode) {
		switch (keycode) {
		case Keyboard.KEY_0:
			return "0";
		case Keyboard.KEY_1:
			return "1";
		case Keyboard.KEY_2:
			return "2";
		case Keyboard.KEY_3:
			return "3";
		case Keyboard.KEY_4:
			return "4";
		case Keyboard.KEY_5:
			return "5";
		case Keyboard.KEY_6:
			return "6";
		case Keyboard.KEY_7:
			return "7";
		case Keyboard.KEY_8:
			return "8";
		case Keyboard.KEY_9:
			return "9";
		case Keyboard.KEY_SPACE:
			return "[SPACE]";
		case Keyboard.KEY_ENTER:
			return "[ENTER]";
		case Keyboard.KEY_BACK_SPACE:
			return "[BACKSPACE]";
		case Keyboard.KEY_ESCAPE:
			return "[ESCAPE]";
		case Keyboard.KEY_A:
			return "A";
		case Keyboard.KEY_Z:
			return "Z";
		case Keyboard.KEY_E:
			return "E";
		case Keyboard.KEY_R:
			return "R";
		case Keyboard.KEY_T:
			return "T";
		case Keyboard.KEY_Y:
			return "Y";
		case Keyboard.KEY_U:
			return "U";
		case Keyboard.KEY_I:
			return "I";
		case Keyboard.KEY_O:
			return "O";
		case Keyboard.KEY_P:
			return "P";
		case Keyboard.KEY_Q:
			return "Q";
		case Keyboard.KEY_S:
			return "S";
		case Keyboard.KEY_D:
			return "D";
		case Keyboard.KEY_F:
			return "F";
		case Keyboard.KEY_G:
			return "G";
		case Keyboard.KEY_H:
			return "H";
		case Keyboard.KEY_J:
			return "J";
		case Keyboard.KEY_K:
			return "K";
		case Keyboard.KEY_L:
			return "L";
		case Keyboard.KEY_M:
			return "M";
		case Keyboard.KEY_W:
			return "W";
		case Keyboard.KEY_X:
			return "X";
		case Keyboard.KEY_C:
			return "C";
		case Keyboard.KEY_V:
			return "V";
		case Keyboard.KEY_B:
			return "B";
		case Keyboard.KEY_N:
			return "N";
		case Keyboard.KEY_COLON:
			return ":";
		case Keyboard.KEY_SEMICOLON:
			return ";";
		case Keyboard.KEY_COMMA:
			return ",";
		case Keyboard.KEY_EXCLAMATION_MARK:
			return "!";
		default:
			return "?";
		}
	}
}
