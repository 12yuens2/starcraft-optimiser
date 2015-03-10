package gameobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import logger.SCLogger;
import entities.buildings.Building;
import game.Game;

public abstract class Bulider extends Entity {
	protected Game game;
	protected LinkedList<BuildOrder> buildQueue;
	
	public Bulider(Game game) {
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
}
