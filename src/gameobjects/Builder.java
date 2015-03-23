package gameobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import logger.SCLogger;
import game.Datasheet;
import game.Game;

public abstract class Builder extends Entity {
	protected Game game;
	protected LinkedList<BuildOrder> buildQueue;
	
	public Builder() {
		buildQueue = new LinkedList<BuildOrder>();	
	}
	
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
				buildQueue.getFirst().increment();;			
			}			
		}
	}
	
	public int getNumberOfInBuildQueue(String entityName) {
		int count = 0;
		for (BuildOrder bo : buildQueue) {
			if (bo.getClass().getSimpleName().equals(entityName)) {
				count++;
			}
		}

		return count;
	}
	
	/*public void build(Entity entity){
		if (buildQueue.isEmpty()) {
			if (game.getMinerals() >= entity.mineralCost && game.getGas() >= entity.gasCost && game.getSupply() <= game.getMaxSupply() + entity.supplyCost){
				buildQueue.add(new BuildOrder(entity, 0));
				System.out.println( game.getSupply() + "\t" + entity.getClass().getSimpleName() + "\t" + game.getTimestamp());
				game.addMinerals(-entity.mineralCost);
				game.addGas(-entity.gasCost);
				game.addSupply(entity.supplyCost);
			} else {

			}
		}
	}*/
	
	public void build(String entityName){
		if (buildQueue.isEmpty()) {
			if (game.getMinerals() >= Datasheet.getMineralCost(entityName) && game.getGas() >= Datasheet.getGasCost(entityName) 
					/*&& game.getSupply() <= game.getMaxSupply() + Datasheet.getSupplyCost(entityName)*/){
				buildQueue.add(new BuildOrder(entityName, Datasheet.getBuildTime(entityName)));
				System.out.println( game.getSupply() + "\t" + entityName + "\t" + game.getTimestamp());
				game.addMinerals(-Datasheet.getMineralCost(entityName));
				game.addGas(-Datasheet.getGasCost(entityName));
				game.addSupply(Datasheet.getSupplyCost(entityName));
			} else {

			}
		}
	}

	public LinkedList<BuildOrder> getQueue() {
		return buildQueue;
	}

	public void assignGame(Game game2) {
		this.game = game2;
	}
}
