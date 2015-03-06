package gameobjects;

import java.util.ArrayList;
import java.util.HashMap;

import logger.SCLogger;
import entities.buildings.Building;
import game.Game;

public abstract class Bulider extends Entity {
	protected Game game;
	protected HashMap<Entity, Integer> buildQueue;
	
	public Bulider(Game game) {
		this.game = game;
		buildQueue = new HashMap<>();
	}
	
	public Game getGame(){
		return game;
	}
	
	public void build(Entity entity){
		if (game.getMinerals() >= entity.mineralCost && game.getGas() >= entity.gasCost && game.getSupply() <= game.getMaxSupply() + entity.supplyCost){
			buildQueue.put(entity,0);
			game.addMinerals(-entity.mineralCost);
			game.addGas(-entity.gasCost);
			game.addSupply(entity.supplyCost);
			
		} else {
			SCLogger.log("FAILED TO BUILDERINO",SCLogger.LOG_SEVERE);
		}
	}
}
