package units;

import units.nexus.ExpansionNexus;
import logger.SCLogger;
import game.Game;
import gameobjects.Bulider;
import gameobjects.Entity;

public class Probe extends Bulider{
	
	public Probe(Game game) {
		super(game);
		mineralCost = 50;
		gasCost = 0;
		buildTime = 17;
		supplyCost = 0;
	}
	
	private ExpansionNexus mineralNexus = null;
	private ExpansionNexus gasNexus = null;
	private boolean isBuilding;
	
	public void assignToMinerals(ExpansionNexus nexus){
		if (nexus.getMinerals() > 0){
			this.mineralNexus = nexus;
			nexus.addMineralProbe(this);
		}
	}
	
	public void removeFromNexus(){
		this.mineralNexus = null;
		this.gasNexus = null;
	}
	
	public boolean isMining(){
		return (mineralNexus != null || gasNexus != null);
	}
	
	public void mine(){
		if (mineralNexus != null){
			//SCLogger.log("Probe Mining", SCLogger.LOG_CALLS);
		} else if (gasNexus != null){
			//SCLogger.log("Probe Mining Gas", SCLogger.LOG_CALLS);			
		}
	}
	
	@Override
	public void passTime() {
		if (!isMining() && !isBuilding){
			for (ExpansionNexus nexus : getGame().getBases()){
				if (nexus.getMinerals() > 0){
					nexus.addMineralProbe(this);
					break;
				}
			}
		} else {
			if (isMining()){
				//?
			} else if (isBuilding){
				//?
			}
		}

		for (int i = 0; i < buildQueue.size(); i++){
			if (buildQueue.get(i).isFinished()){
				this.game.addGameObejct(buildQueue.remove(i).getUnit());
			} else {
				buildQueue.get(i).increment();			
			}			
		}
		
	}

}
