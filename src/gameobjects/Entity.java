package gameobjects;


public abstract class Entity implements GameObject {
	public int mineralCost;
	public int gasCost;
	public int supplyCost;
	public int buildTime;
	
	public void printResources(){
		System.out.println(this.mineralCost + " " + this.gasCost + " " + this.supplyCost);
	}
	
	public void setResources(int mineralCost, int gasCost, int supplyCost, int buildTime){
		this.mineralCost = mineralCost;
		this.gasCost = gasCost;
		this.supplyCost = supplyCost;
		this.buildTime = buildTime;
	}
}
