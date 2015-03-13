package units.buildings;

import game.Game;
import gameobjects.Builder;

public class RoboticsFacility extends Builder {

	public RoboticsFacility(Game game) {
		super(game);
		setResources(200, 100, 0, 65);
	}

	public void passTime(){
		super.passTime();
	}
}
