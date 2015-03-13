package units.buildings;

import units.gateway.Zealot;
import game.Game;
import gameobjects.Builder;

public class Gateway extends Builder{

	public Gateway(Game game) {
		super(game);
		setResources(150, 0, 0, 65);
	}
	
	public void passTime(){
		super.passTime();
		if (game.goalInvolves(Zealot.class) && game.needsMore(Zealot.class)){
			this.build(new Zealot());
		}
	}
}
