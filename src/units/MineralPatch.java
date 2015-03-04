package units;

import java.util.ArrayList;

import units.nexus.ExpansionNexus;
import gameobjects.GameObject;

public class MineralPatch implements GameObject {
	private static final double InitialMinerals = 1500;
	public static final double DEPLETION_RATE = 5;
	private double remainingMinerals = InitialMinerals;
	private ArrayList<Probe> probes = new ArrayList<>();
	
	
	
	@Override
	public void passTime() {
		// TODO Auto-generated method stub
		
	}



	public void addProbe(Probe probe) {
		probes.add(probe);
	}
	
	public void removeProbe(Probe probe) {
		probes.remove(probe);
	}


	public void deplete() {
		this.remainingMinerals -= DEPLETION_RATE;
	}

	public double getMinerals(){
		return remainingMinerals;
	}

	public ArrayList<Probe> getProbes() {
		return probes;
	}

	public void releaseProbes() {
		probes.clear();
	}
}
