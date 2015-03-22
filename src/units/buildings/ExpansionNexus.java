package units.buildings;

import game.Game;

import java.util.ArrayList;

import units.nexus.Probe;
import logger.SCLogger;

public class ExpansionNexus extends Nexus {



	private ArrayList<Probe> mineralLine = new ArrayList<>();
	private ArrayList<Probe> gas = new ArrayList<>();
	private double remainingMinerals;
	private double remainingGas;
	
	
	public ExpansionNexus(Game game){
		super(game);
		remainingMinerals = INITIAL_MINERALS;
		remainingGas = INITIAL_GAS;

	}
	
	@Override
	public void passTime() {
		double deltaMinerals = calculateIncome(mineralLine.size());
		if (deltaMinerals > this.remainingMinerals){
			deltaMinerals = this.remainingMinerals;
		}
		this.remainingMinerals -= deltaMinerals;
		if (this.remainingMinerals == 0){
			mineralLine.clear();
		}
		if (deltaMinerals > 0){
			this.getGame().addMinerals(deltaMinerals);
		}
		
		if ((game.goalInvolves(Probe.class) && game.needsMore(Probe.class)) || game.moreProbes()){
				this.build(new Probe(game));
		} 
		super.passTime();
	}

	public double calculateIncome(int mineralProbes) {
		double income = 0;
		if (mineralProbes <= 16 ){
			income = mineralLine.size()*PROBE_MINING_PER_SECOND;
		} else if (mineralProbes <= 24){
			income = (mineralLine.size()-16)*THIRD_PROBE_MINING_PER_SECOND + 16*PROBE_MINING_PER_SECOND;
		} else if (mineralProbes > 24){
			income = 8*THIRD_PROBE_MINING_PER_SECOND + 16*PROBE_MINING_PER_SECOND;
		}
		return income;
	}
	
	public void addMineralProbe(Probe probe){
		mineralLine.add(probe);
	}
	
	public void addGasProbe(Probe probe){
		gas.add(probe);
	}
	
	public double getMinerals(){
		return remainingMinerals;
	}
	
	public double getGas(){
		return remainingGas;
	}
	
	public int getMineralLineSize() {
		return mineralLine.size();
	}
}
