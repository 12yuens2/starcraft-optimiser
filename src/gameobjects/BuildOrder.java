package gameobjects;

public class BuildOrder {
	Entity unit;
	int buildTime;
	
	public BuildOrder(Entity unit, int buildTime) {
		super();
		this.unit = unit;
		this.buildTime = buildTime;
	}
	
	public void increment(){
		this.buildTime++;
	}
	
	public boolean isFinished(){
		return buildTime == unit.buildTime;
	}
	
	public Entity getUnit(){
		return unit;
	}
}
