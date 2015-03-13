package units.buildings;

import game.Game;
import gameobjects.Builder;

public class RoboticsBay extends Builder {

	public RoboticsBay(Game game) {
		super(game);
		setResources(200, 200, 0, 65);
	}

	public void passTime(){
		super.passTime();
	}
	
}
