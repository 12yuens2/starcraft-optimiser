package entities;

import game.Game;

public abstract class Entity {

	protected int mineralCost;
	protected int gasCost;
	protected double buildTime;
	protected int supplyCost;
	
	public Entity(Game game){
		
	}
	
	public void printResources(){
		System.out.println(this.mineralCost + " " + this.gasCost + " " + this.supplyCost);
	}
}
