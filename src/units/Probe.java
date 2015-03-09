package units;

import units.nexus.ExpansionNexus;
import logger.SCLogger;
import game.Game;
import gameobjects.Bulider;
import gameobjects.Entity;

public class Probe extends Bulider{

	public Probe(Game game) {
		super(game);
	}

	public int mineralCost = 50;
	public int gasCost = 0;
	public int buildTime = 17;
	public int supplyCost = 0;
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

		
		this.buildQueue.forEach( (entity,buildTime) -> {
			if (buildTime == entity.buildTime){
				this.game.addGameObejct(entity);
				//this.buildQueue.remove(entity);
			} else {
				//buildTime++;
			}
		});
	}

}
