package units.buildings;

import game.Game;
import gameobjects.Builder;

public class TwilightCouncil extends Builder{

	public TwilightCouncil(Game game) {
		super(game);
		setResources(150, 100, 0, 50);
	}

	public void passTime(){
		super.passTime();
	}
	
}
