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
	private MineralPatch miningPatch = null;
	private VespeneGeyser miningGeyser = null;
	private boolean isBuilding;
	
	public void assignToPatch(MineralPatch patch){
		if (patch.getMinerals() > 0){
			this.miningPatch = patch;
			patch.addProbe(this);			
		}
	}
	
	public void removeFromPatch(){
		this.miningPatch = null;
	}
	
	public boolean isMining(){
		return (miningPatch != null || miningGeyser != null);
	}
	
	public void mine(){
		if (miningPatch != null){
			SCLogger.log("Probe Mining", SCLogger.LOG_CALLS);
		} else if (miningGeyser != null){
			SCLogger.log("Probe Mining Gas", SCLogger.LOG_CALLS);			
		}
	}
	
	@Override
	public void passTime() {
		if (!isMining() && !isBuilding){
			for (ExpansionNexus nexus : getGame().getBases()){
				for (MineralPatch patch : nexus.getMinerals()){
					if ( patch.getMinerals() > 0){
						assignToPatch(patch);
						return;
					}
				}
			}
		} else {
			if (isMining()){
				mine();
			} else if (isBuilding){
				//
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
