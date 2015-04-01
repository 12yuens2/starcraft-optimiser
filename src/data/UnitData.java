package data;

/**
 * Attributes of each individual unit.
 */
public class UnitData {
	String name;
	String dependancy;
	String builtFrom;
	double mineralCost;
	double gasCost;
	int supplyCost;
	double buildTime;

	public UnitData(String name, String dependancy, String buildFrom, double mineralCost, double gasCost,int supplyCost, double buildTime) {
		this.name = name;
		this.dependancy = dependancy;
		this.builtFrom = buildFrom;
		this.mineralCost = mineralCost;
		this.gasCost = gasCost;
		this.supplyCost = supplyCost;
		this.buildTime = buildTime;
	}

	public String getName() {
		return name;
	}
	public String getDependancy(){
		return dependancy;
	}
	public double getMineralCost() {
		return mineralCost;
	}
	public double getGasCost() {
		return gasCost;
	}
	public int getSupplyCost() {
		return supplyCost;
	}
	public double getBuildTime() {
		return buildTime;
	}
	public String getBuiltFrom() {
		return builtFrom;
	}


}
