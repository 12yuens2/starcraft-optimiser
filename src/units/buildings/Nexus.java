package units.buildings;

import game.Game;
import gameobjects.Builder;
import gameobjects.Entity;

public class Nexus extends Builder {

	public static final double INITIAL_MINERALS = 9000;
	public static final double INITIAL_GAS = 5000;
	public static final double PROBE_MINING_PER_MINUTE = 41, THIRD_PROBE_MINING_PER_MINUTE = 20;
	public static final double PROBE_MINING_PER_SECOND = PROBE_MINING_PER_MINUTE/60.0;
	public static final double THIRD_PROBE_MINING_PER_SECOND = THIRD_PROBE_MINING_PER_MINUTE/60.0;
	
	public Nexus(Game game) {
		super(game);
	}

	public void passTime(){
		super.passTime();
	}
}
