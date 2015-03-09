package units.nexus;

import game.Game;

import java.util.ArrayList;

import logger.SCLogger;
import units.Probe;

public class ExpansionNexus extends Nexus {

	private static final double INITIAL_MINERALS = 9000, INITIAL_GAS = 5000;
	private static final double PROBE_MINING_PER_MINUTE = 41, THIRD_PROBE_MINING_PER_MINUTE = 20;
	private static final double PROBE_MINING_PER_SECOND = PROBE_MINING_PER_MINUTE/60.0;
	private static final double THIRD_PROBE_MINING_PER_SECOND = THIRD_PROBE_MINING_PER_MINUTE/60.0;
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
		double deltaMinerals = 0;
		int mineralProbes = mineralLine.size();
		if (mineralProbes <= 16 ){
			deltaMinerals = mineralLine.size()*PROBE_MINING_PER_SECOND;
		} else if (mineralProbes <= 24){
			deltaMinerals = (mineralLine.size()-16)*THIRD_PROBE_MINING_PER_SECOND + 16*PROBE_MINING_PER_SECOND;
		} else if (mineralProbes > 24){
			deltaMinerals = 8*THIRD_PROBE_MINING_PER_SECOND + 16*PROBE_MINING_PER_SECOND;
		}
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
}
