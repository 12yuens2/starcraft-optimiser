package units.buildings;

import game.Game;
import gameobjects.Builder;

public class FleetBeacon extends Builder {

	public FleetBeacon(Game game) {
		super(game);
		setResources(300, 200, 0, 60);
	}

	public void passTime(){
		super.passTime();
	}
	
}
