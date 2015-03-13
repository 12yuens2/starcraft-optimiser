package units.buildings;

import game.Game;
import gameobjects.Builder;

public class Forge extends Builder {

	public Forge(Game game) {
		super(game);
		setResources(150, 0, 0, 45);
	}

	public void passTime(){
		super.passTime();
	}
}
