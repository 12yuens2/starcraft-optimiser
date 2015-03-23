package units.buildings;

import units.gateway.Zealot;
import game.Game;
import gameobjects.Builder;

public class Gateway extends Builder{

	public Gateway(Game game) {
		super(game);
		setResources(150, 0, 0, 65);
	}
	
	public Gateway() {
		// TODO Auto-generated constructor stub
	}

	public void passTime(){
		super.passTime();
		if (game.goalInvolves("Zealot") && game.needsMore("Zealot")){
			this.build("Zealot");
		}
	}
}
