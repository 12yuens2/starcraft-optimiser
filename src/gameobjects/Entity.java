package gameobjects;


public abstract class Entity implements GameObject {
	public int mineralCost;
	public int gasCost;
	public int supplyCost;
	public int buildTime;
	
	public void printResources(){
		System.out.println(this.mineralCost + " " + this.gasCost + " " + this.supplyCost);
	}
	
}
