package units.nexus;

import units.buildings.ExpansionNexus;
import units.buildings.Gateway;
import units.gateway.Zealot;
import logger.SCLogger;
import game.Game;
import gameobjects.BuildOrder;
import gameobjects.Builder;
import gameobjects.Entity;
import gameobjects.GameObject;

public class Probe extends Builder{
	
	public Probe(Game game) {
		super(game);
		setResources(50, 0, 0, 17);
	}
	
	public Probe() {

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

		//Move this to another place when things are good and stuff ._.
		if ( game.needsMore("Zealot") && game.canSupport(game.getIncome(game.getNumberOf("Probe")), "Zealot")){
			this.build("Gateway");
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
