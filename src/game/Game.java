package game;

import gui.SCWindow;
import utils.Timer;
import data.Datasheet;

public class Game {
	public static void main(String[] args) {
		Timer.isLogging = true;
		Datasheet.init();
		new SCWindow();
	}
}
