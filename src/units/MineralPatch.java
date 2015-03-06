package units;

import java.util.ArrayList;

import logger.SCLogger;
import units.nexus.ExpansionNexus;
import game.Game;
import gameobjects.Bulider;
import gameobjects.GameObject;

public class MineralPatch extends Bulider implements GameObject {
	
	public MineralPatch(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	private final double InitialMinerals = 1500;
	public static final double DEPLETION_RATE = 5;
	private double remainingMinerals = InitialMinerals;
	private ArrayList<Probe> probes = new ArrayList<>();
	
	
	
	@Override
	public void passTime() {
		if (this.remainingMinerals > 0){
			double deltaMinerals = MineralPatch.DEPLETION_RATE*this.probes.size(); //, this.remainingMinerals);
			if (deltaMinerals > this.remainingMinerals){
				deltaMinerals = this.remainingMinerals;
			}
			getGame().addMinerals(deltaMinerals); // change for 3 probes and stuff
			this.remainingMinerals -= deltaMinerals;
			SCLogger.log("Remaining Minerals :" + this.remainingMinerals, SCLogger.LOG_PARAMS);	
			
		}
	
			if (this.getMinerals() == 0){
				this.releaseProbes();
			}
	}


	public void addProbe(Probe probe) {
		probes.add(probe);
	}
	
	public void removeProbe(Probe probe) {
		probes.remove(probe);
	}

	public double getMinerals(){
		return remainingMinerals;
	}

	public ArrayList<Probe> getProbes() {
		return probes;
	}

	public void releaseProbes() {
		probes.forEach( (probe) -> probe.removeFromPatch() );
		probes.clear();
	}
}
