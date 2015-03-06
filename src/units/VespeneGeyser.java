package units;

import game.Game;
import gameobjects.Bulider;
import gameobjects.GameObject;

public class VespeneGeyser extends Bulider implements GameObject {
	private static final int InitialGas = 2500;
	private int remainingGas = InitialGas;

	public VespeneGeyser(Game game) {
		super(game);
	}

	@Override
	public void passTime() {
		// TODO Auto-generated method stub
		
	}	
}
