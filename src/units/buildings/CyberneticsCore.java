package units.buildings;

import game.Game;
import gameobjects.Builder;
import gameobjects.Entity;

public class CyberneticsCore extends Builder {

	public CyberneticsCore(Game game) {
		super(game);
		setResources(150, 0, 0, 50);
	}
	
	@Override
	public void passTime() {
		super.passTime();
	}

}
