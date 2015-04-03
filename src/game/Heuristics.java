package game;

import game.tree.TimeState;

import java.util.HashSet;
import java.util.Map.Entry;

import data.Datasheet;

/**
 * A helper class that handles various aspects of in-game logic.
 */
public class Heuristics {

	/**
	 * Static resources to simulate resources required in the game.
	 * Used in calculations to work out the total costs of all the units and their dependencies in the goal.
	 */
	private static int totalMineralCost;
	private static int totalGasCost;
	private static double totalBuildTime;

	public static boolean moreProbes(TimeState timeState) {
			return (needMoreIncome(timeState.getTotalNumber("Probe"), timeState.getTotalNumber("Nexus"), timeState));
	}
	
	public static boolean needsMoreForGoal(String unitName, TimeState timeState){
		return (timeState.getGoal().containsKey(unitName) && timeState.getTotalNumber(unitName) < timeState.getGoal().get(unitName));
	}

	public static boolean needsDependancy(String dependancyName, TimeState timeState){
		if (timeState.getTotalNumber(dependancyName) > 0){
			return false;
		}
		for (Entry<String, Integer> entry: timeState.getGoal().entrySet()){
			String dependancy = Datasheet.getDependancy(entry.getKey());
			while (dependancy != null){
				if (dependancy.equals(dependancyName) && needsMoreForGoal(entry.getKey(), timeState)){
					return true;
				}
				dependancy = Datasheet.getDependancy(dependancy);
			}
			String builtFrom = Datasheet.getBuiltFrom(entry.getKey());
			dependancy = Datasheet.getDependancy(builtFrom);
			while (dependancy != null){
				if (dependancy.equals(dependancyName) && needsMoreForGoal(entry.getKey(), timeState)){
					return true;
				}
				dependancy = Datasheet.getDependancy(dependancy);
			}
		}
		return false;		
	}
	
	public static boolean needsMoreFromBuilder(String builderName, TimeState timeState){
		boolean needsMore = false;
		int numberOfUnits = 0;
		for (Entry<String, Integer> entry: timeState.getGoal().entrySet()){
			if (Datasheet.getBuiltFrom(entry.getKey()).equals(builderName)) {
				numberOfUnits += entry.getValue()-timeState.getTotalNumber(entry.getKey());
				if (needsMoreForGoal(entry.getKey(), timeState)){
					needsMore = true;
				}
			}
		}
		
		//Don't make more production than number of units you need from that building
		if (timeState.getTotalNumber(builderName) >= numberOfUnits) {
			needsMore = false;
		}
		return needsMore;
	}
	
	public static boolean needMoreGas(TimeState timeState) {
		calculateTotalResources(timeState);
		return (totalGasCost > timeState.getGas());
	}
	
	public static boolean needMoreSupply(TimeState timeState) {
		if (timeState.getFutureMaxSupply() -timeState.getTotalSupply() < 3 || willBeSupplyBlocked(timeState)) {
			if (timeState.getFutureMaxSupply()+8 <= Datasheet.MAX_SUPPLY 
					|| timeState.getMaxSupply() <= timeState.getSupplyOfGoal()+timeState.getTotalNumber("Probe")) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean needMoreIncome(int numberOfProbes, int numberOfNexi, TimeState timeState) {
		calculateTotalResources(numberOfProbes, numberOfNexi, timeState);
		
		double totalTime = totalBuildTime;
		
		calculateTotalResources(numberOfProbes+1, numberOfNexi, timeState);
		addTotalResources("Probe");
		
		int deltaNexi = numberOfNexi-timeState.getUnitNumbers().get("Nexus");
		double income = timeState.getMineralIncome(numberOfProbes+1, numberOfNexi);
		double gasIncome = timeState.getGasIncome(timeState.getProbesOnGas()+deltaNexi);
		double incomeNeeded = (1.0*totalMineralCost)/totalBuildTime;
		double gasIncomeNeeded = (1.0*totalGasCost)/totalBuildTime;

		return (totalTime <= totalBuildTime && (incomeNeeded > income || gasIncomeNeeded > gasIncome));
	}
	
	public static boolean getGas(TimeState timeState) {
		calculateTotalResources(timeState);
		return (timeState.getGasIncome()*totalBuildTime < totalGasCost);
	}
	
	public static boolean supplyBlocked(TimeState timeState) {
		return (timeState.getSupply() == timeState.getMaxSupply() && timeState.getFutureMaxSupply()+8<=200 
				&& timeState.getFutureMaxSupply() <= timeState.getSupplyOfGoal() + timeState.getTotalNumber("Probe"));
	}
	
	/**
	 * Checks whether or not a unit can be built at a current time.
	 * @param unitName the unit name.
	 */
	public static boolean canBuild(String unitName, TimeState timeState){
		String dependancy = Datasheet.getDependancy(unitName);
		String builtFrom = Datasheet.getBuiltFrom(unitName);
		if (dependancy == null || timeState.getUnitNumbers().get(dependancy) > 0){
			//If there are more builders of the unit than builders building the units.
			//In other words, if there are free builders of the unit.
			if( timeState.getBuildQueues().get(builtFrom).size() < timeState.getUnitNumbers().get(builtFrom)){
				if(timeState.getMinerals() >= Datasheet.getMineralCost(unitName) && timeState.getGas() >= Datasheet.getGasCost(unitName)){
					if (timeState.getTotalSupply() + Datasheet.getSupplyCost(unitName) <= timeState.getMaxSupply()){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks whether or not a builder can support more units from the goal at a given time.
	 * @param name the name of the builder.
	 */
	public static boolean canSupportFromBuilder(String name, TimeState timeState) {

		double excessMinerals = timeState.getMineralIncome() - timeState.getMineralSpending();
		double excessGas = timeState.getGasIncome() - timeState.getGasSpending();

		for (Entry<String,Integer> entry : timeState.getGoal().entrySet()){
			if (Datasheet.getBuiltFrom(entry.getKey()).equals(name)){
				double mineralRateCost = Datasheet.getMineralCost(entry.getKey())/(1.0*Datasheet.getBuildTime(entry.getKey()));
				double gasRateCost = Datasheet.getGasCost(entry.getKey())/(1.0*Datasheet.getBuildTime(entry.getKey()));
				
				//if there are excess resources that means we can make more production
				if (excessMinerals >= mineralRateCost && excessGas  >= gasRateCost){
					return true;
				}
			}
		}
		return false;
	}

	public static boolean worthExpanding(TimeState timeState) {
		return (timeState.getTotalNumber("Nexus") <= Datasheet.MAX_NUMBER_OF_NEXI 
				&& Heuristics.needMoreIncome(timeState.getUnitNumbers().get("Probe"), timeState.getTotalNumber("Nexus")+1, timeState));
	}

	public static boolean enoughTemplarToMakeArchon(TimeState timeState) {
		int darkTemplars = timeState.getUnitNumbers().get("Dark Templar");
		int highTemplars = timeState.getUnitNumbers().get("High Templar");
		return (darkTemplars + highTemplars >=2);
	}
	
	/**
	 * Checks if it is worth it to research Warpgate as it builds gateway units faster.
	 * The mineral and gas cost of Warpgate is negligible here.
	 */
	public static boolean worthWarpgate(TimeState timeState) {
		double totalBuildTimeGateway = 0;
		double totalBuildTimeWarpgate = Datasheet.WARPGATE_TRANFORMATION_TIME*timeState.getTotalNumber("Gateway");
		for (Entry<String, Integer> entry: timeState.getGoal().entrySet()) {
			String unitName = entry.getKey();
			if (UnitIs.fromGateway(unitName)) {
				totalBuildTimeGateway += Datasheet.getBuildTime(unitName) * (entry.getValue()-timeState.getTotalNumber(unitName));
				totalBuildTimeWarpgate += (Datasheet.WARPIN_TIME+Datasheet.getWarpgateCooldown(unitName)) 
						* (entry.getValue() -timeState.getTotalNumber(unitName));
			} else if (UnitIs.Archon(unitName)) {
				totalBuildTimeGateway += 2*Datasheet.getBuildTime("High Templar")*(entry.getValue());
				totalBuildTimeWarpgate += 2*(Datasheet.WARPIN_TIME+Datasheet.getWarpgateCooldown("High Templar")) 
						* (entry.getValue() -timeState.getTotalNumber(unitName));
			}
		}
		return (totalBuildTimeGateway > totalBuildTimeWarpgate);
	}
	
	private static boolean canSupportMins(double income, String unitName){
		return (income > Datasheet.getMineralCost(unitName)/Datasheet.getBuildTime(unitName));
	}
	
	private static boolean canSupportGas(double gasIncome, String unitName) {
		return (gasIncome > Datasheet.getGasCost(unitName)/Datasheet.getBuildTime(unitName));
	}
	
	private static void resetTotalResources() {
		totalBuildTime = 0;
		totalMineralCost = 0;
		totalGasCost = 0;
	}
	
	private static void addTotalResources(String unitName) {
		totalBuildTime += Datasheet.getBuildTime(unitName);
		totalMineralCost += Datasheet.getMineralCost(unitName);
		totalGasCost += Datasheet.getGasCost(unitName);
	}
	
	/**
	 * Calculate total resources with current number of probes and nexi in given TimeState.
	 * @see Heuristics#calculateTotalResources(int, int, TimeState)
	 */
	private static void calculateTotalResources(TimeState timeState) {
		calculateTotalResources(timeState.getUnitNumbers().get("Probe"), timeState.getTotalNumber("Nexus"), timeState);
	}
	
	/**
	 * Calulates the total resources needed to achieve the current goal.
	 * @param numberOfProbes Number of mining probes. Changes affects resources needed.
	 * @param numberOfNexi Number of bases. Affects resource gather rate.
	 * @param timeState Current TimeState.
	 */
	private static void calculateTotalResources(int numberOfProbes, int numberOfNexi, TimeState timeState) {
		int deltaNexi = numberOfNexi-timeState.getUnitNumbers().get("Nexus");
		double income = timeState.getMineralIncome(numberOfProbes, numberOfNexi);
		double gasIncome = timeState.getGasIncome(timeState.getProbesOnGas()+deltaNexi);
		resetTotalResources();
		HashSet<String> dependancies = new HashSet<>();
		
		totalBuildTime += deltaNexi*Datasheet.getBuildTime("Nexus");
		totalMineralCost += deltaNexi*Datasheet.getMineralCost("Nexus");
		
		for (Entry<String, Integer> unitGoal : timeState.getGoal().entrySet()) {
			int numberOfUnits = unitGoal.getValue() - timeState.getTotalNumber(unitGoal.getKey());
			if (numberOfUnits > 0) {
			
				//check dependancies of units
				String nameOfUnit = unitGoal.getKey();
				while (nameOfUnit != null && Datasheet.getDependancy(nameOfUnit) != null 
						&& timeState.getTotalNumber((Datasheet.getDependancy(nameOfUnit))) == 0) {
					nameOfUnit = Datasheet.getDependancy(nameOfUnit);
					if (nameOfUnit != null){
						dependancies.add(nameOfUnit); 
					}
				}
				
				//add total buildtime and costs of dependancies from goal
				for (String nameOfDependancy : dependancies){
					String dependancyName = Datasheet.getDependancy(nameOfDependancy);
					if (dependancyName != null){
						addTotalResources(dependancyName);
					}
				}
				
				//check number of buildings currently available to build unit with
				int numberOfBuildings = 0;
				numberOfBuildings = timeState.getTotalNumber((Datasheet.getBuiltFrom(nameOfUnit)));
				if (numberOfBuildings == 0){
					numberOfBuildings++;
					addTotalResources(Datasheet.getBuiltFrom(nameOfUnit));
				}

				//if more production can be made
				double extraMineralSpending = 0;
				double extraGasSpending = 0;
				while (!unitGoal.getKey().equals("Archon") && canSupportMins(income - timeState.getMineralSpending() - extraMineralSpending, nameOfUnit) 
						&& canSupportGas(gasIncome - timeState.getGasSpending() - extraGasSpending, nameOfUnit)) {
					numberOfBuildings++;
					addTotalResources(Datasheet.getBuiltFrom(nameOfUnit));
					extraMineralSpending += Datasheet.getMineralCost(nameOfUnit)/(1.0*Datasheet.getBuildTime(nameOfUnit));
					extraGasSpending += Datasheet.getGasCost(nameOfUnit)/(1.0*Datasheet.getBuildTime(nameOfUnit));
				}

				totalBuildTime += (Datasheet.getBuildTime(nameOfUnit)*numberOfUnits)/(1.0*numberOfBuildings);
				totalMineralCost += Datasheet.getMineralCost(nameOfUnit)*numberOfUnits;
				totalGasCost += Datasheet.getGasCost(nameOfUnit)*numberOfUnits;
				
			}
		}
	}
	
	private static boolean willBeSupplyBlocked(TimeState timeState) {
		int tempSupply = timeState.getTotalSupply();
		for (Entry<String, Integer> entry : timeState.getGoal().entrySet()) {
			if (canBuild(entry.getKey(), timeState)) {
				tempSupply += Datasheet.getSupplyCost(entry.getKey());
			}
		}
		return (tempSupply > timeState.getFutureMaxSupply());
	}

}
