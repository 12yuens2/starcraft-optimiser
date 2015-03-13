package gameobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import logger.SCLogger;
import game.Game;

public abstract class Builder extends Entity {
	protected Game game;
	protected LinkedList<BuildOrder> buildQueue;
	
	public Builder(Game game) {
		this.game = game;
		buildQueue = new LinkedList<BuildOrder>();
	}
	
	public Game getGame(){
		return game;
	}
	
	public void passTime(){
		if (!buildQueue.isEmpty()){
			if (buildQueue.getFirst().isFinished()){
				this.game.addGameObejct(buildQueue.removeFirst().getUnit());
			} else {
				buildQueue.getFirst().increment();			
			}			
		}
	}
	
	public void build(Entity entity){
		if (game.getMinerals() >= entity.mineralCost && game.getGas() >= entity.gasCost && game.getSupply() <= game.getMaxSupply() + entity.supplyCost){
			buildQueue.add(new BuildOrder(entity, 0));
			System.out.println( game.getSupply() + "\t" + entity.getClass().getSimpleName() + "\t" + game.getTimestamp());
			game.addMinerals(-entity.mineralCost);
			game.addGas(-entity.gasCost);
			game.addSupply(entity.supplyCost);
		} else {

		}
	}

	public LinkedList<BuildOrder> getQueue() {
		return buildQueue;
	}
}