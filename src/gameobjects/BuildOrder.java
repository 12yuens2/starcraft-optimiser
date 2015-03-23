package gameobjects;

import game.Datasheet;

public class BuildOrder {
	String unit;
	int buildTime;
	
	public BuildOrder(String unit, int buildTime) {
		super();
		this.unit = unit;
		this.buildTime = -buildTime;
	}
	
	public void increment(){
		this.buildTime++;
	}
	
	public boolean isFinished(){
		return buildTime == Datasheet.getBuildTime(unit);
	}
	
	public Entity getUnit(){
		return Datasheet.getUnit(unit);
	}
	
	public String getUnitName() {
		return unit;
	}
}
