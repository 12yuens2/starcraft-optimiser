package units.buildings;

import game.Game;
import gameobjects.Builder;

public class Stargate extends Builder {

	public Stargate(Game game) {
		super(game);
		setResources(150, 150, 0, 60);
	}

	public void passTime(){
		super.passTime();
	}
}
