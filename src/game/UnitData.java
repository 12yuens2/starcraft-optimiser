package game;

import gameobjects.Entity;

public class UnitData {
	String name;
	String dependancy;
	String builtFrom;
	int mineralCost;
	int gasCost;
	int supplyCost;
	int buildTime;
	Entity unit;

	public UnitData(String name, String dependancy, String buildFrom, int mineralCost, int gasCost, int supplyCost, int buildTime, Entity unit) {
		this.name = name;
		this.dependancy = dependancy;
		this.builtFrom = buildFrom;
		this.mineralCost = mineralCost;
		this.gasCost = gasCost;
		this.supplyCost = supplyCost;
		this.buildTime = buildTime;
		this.unit = unit;
	}

	public String getName() {
		return name;
	}
	public String getDependancy(){
		return dependancy;
	}
	public int getMineralCost() {
		return mineralCost;
	}
	public int getGasCost() {
		return gasCost;
	}
	public int getSupplyCost() {
		return supplyCost;
	}
	public int getBuildTime() {
		return buildTime;
	}
	public String getBuiltFrom() {
		return builtFrom;
	}
	public Entity getUnit() {
		return unit;
	}


}
