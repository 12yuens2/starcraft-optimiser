package game;

import game.tree.Operation;
import game.tree.TimeState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map.Entry;

import data.Datasheet;
import data.UnitData;

public class Heuristics {

	public static void makeProductionBuildings(ArrayList<Operation> ops, UnitData data, TimeState timeState) {
		
		//build production building if unit in goal builds from that building
		if (timeState.getUnitNumbers().get(Datasheet.getBuiltFrom(data.getName())) == 0) {
			ops.add(new Operation("build", Datasheet.getBuiltFrom(data.getName())));
		}
		
	}

	public static boolean moreProbes(TimeState timeState) {
	//		return (timeTaken(timeState.unitNumbers.get("Probe"), timeState) 
	//			> timeTaken(timeState.unitNumbers.get("Probe")+1, timeState));
			if (timeTaken(timeState.getUnitNumbers().get("Probe"), timeState.getUnitNumbers().get("Nexus"), timeState) == true){
				//System.out.println(timeState.time + " : IT IS WORTH BUILDING MORE PROBERINOS");
				return true;
			} else {
				return false;
			}
	//		return true;
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
		boolean b = false;
		int numberOfUnits = 0;
		for (Entry<String, Integer> entry: timeState.getGoal().entrySet()){
			if (Datasheet.getBuiltFrom(entry.getKey()).equals(builderName)) {
				numberOfUnits += entry.getValue()-timeState.getTotalNumber(entry.getKey());
				if (needsMoreForGoal(entry.getKey(), timeState)){
					b = true;
				}
			}
		}
		
		//don't make more production than number of units you need from that building
		if (timeState.getTotalNumber(builderName) > numberOfUnits) {
			b = false;
		}
		return b;
	}
	
	public static boolean needsGas(TimeState timeState) {
		for (Entry<String,Integer> entry : timeState.getGoal().entrySet()){
			if (Datasheet.getGasCost(entry.getKey()) > 0){
				return true;
			}
		}
		return false;
	}
	
	public static boolean needMoreGas(TimeState timeState) {
		double gasCost = 0;
		for (Entry<String,Integer> entry : timeState.getGoal().entrySet()){
			gasCost += Datasheet.getGasCost(entry.getKey())*(entry.getValue()-timeState.getUnitNumbers().get(entry.getKey()));
			
		}
		return (gasCost > timeState.getGas());
	}
	
	public static boolean needMoreSupply(TimeState timeState) {
		if (timeState.getMaxSupply() < timeState.getSupply() - 3 || willBeSupplyBlocked(timeState)) {
			if (timeState.getMaxSupply() < timeState.getSupplyOfGoal()+timeState.getUnitNumbers().get("Probe")) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean supplyBlocked(TimeState timeState) {
		return (timeState.getSupply() == timeState.getMaxSupply());
	}
	
	public static boolean canBuild(String unitName, TimeState timeState){
		String dependancy = Datasheet.getDependancy(unitName);
		String builtFrom = Datasheet.getBuiltFrom(unitName);
		if (dependancy == null || timeState.getUnitNumbers().get(dependancy) > 0){
			if( timeState.getBuildQueues().get(builtFrom).size() < timeState.getUnitNumbers().get(builtFrom)){
				if(timeState.getMinerals() >= Datasheet.getMineralCost(unitName) && timeState.getGas() >= Datasheet.getGasCost(unitName)){
					if (timeState.getSupply() + timeState.getSupplyInBuildQueues() + Datasheet.getSupplyCost(unitName) <= timeState.getMaxSupply()){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean canSupportFromBuilder(String name, TimeState timeState) {

		double excessMinerals = timeState.getMineralIncome() - timeState.getMineralSpending();
		double excessGas = timeState.getGasIncome() - timeState.getGasSpending();
				
		for (Entry<String,Integer> entry : timeState.getGoal().entrySet()){

			double mineralRateCost = Datasheet.getMineralCost(entry.getKey())/(1.0*Datasheet.getBuildTime(entry.getKey()));
			double gasRateCost = Datasheet.getGasCost(entry.getKey())/(1.0*Datasheet.getBuildTime(entry.getKey()));
			
			if (Datasheet.getBuiltFrom(entry.getKey()).equals(name)){
				if (excessMinerals >= mineralRateCost && excessGas  >= gasRateCost){
					return true;
				}
			}
		}
	
		return false;
	}

	public static boolean worthExpanding(TimeState timeState) {
		boolean b = timeState.getTotalNumber("Nexus") <= Datasheet.MAX_NUMBER_OF_NEXI 
				&& Heuristics.timeTaken(timeState.getUnitNumbers().get("Probe"), timeState.getTotalNumber("Nexus"), timeState);
		//System.out.println(b);
		return b;
	}
	
	public static boolean timeTaken(int numberOfProbes, int numberOfNexi, TimeState timeState) {
		int deltaNexi = numberOfNexi-timeState.getUnitNumbers().get("Nexus");
		double income = timeState.getMineralIncome(numberOfProbes - timeState.getProbesOnGas(), numberOfNexi);
		double gasIncome = timeState.getGasIncome(timeState.getProbesOnGas()+deltaNexi);
		double buildTime = 0;
		int mineralCost = 0;
		int gasCost = 0;
		HashSet<String> dependancies = new HashSet<>();
		
		buildTime += deltaNexi*Datasheet.getBuildTime("Nexus");
		mineralCost += deltaNexi*Datasheet.getMineralCost("Nexus");
		
		for (Entry<String, Integer> unitGoal : timeState.getGoal().entrySet()) {
			if (unitGoal.getValue()-timeState.getUnitNumbers().get(unitGoal.getKey())-timeState.getUnitNumberInBuildQueue(unitGoal.getKey()) > 0) {
			
				//check dependancies and stuff and things ._.
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
						buildTime+= Datasheet.getBuildTime(dependancyName);
						mineralCost+= Datasheet.getMineralCost(dependancyName);	
						gasCost+=Datasheet.getGasCost(dependancyName);
					}
				}
				
				//check number of buildings currently available to build unit with
				int numberOfBuildings = 0;
				if (unitGoal.getValue()-timeState.getTotalNumber(nameOfUnit) > 0) {
					numberOfBuildings = timeState.getTotalNumber((Datasheet.getBuiltFrom(nameOfUnit)));
					if (numberOfBuildings == 0){
						numberOfBuildings++;
						buildTime+= Datasheet.getBuildTime(Datasheet.getBuiltFrom(nameOfUnit));
						mineralCost+= Datasheet.getMineralCost(Datasheet.getBuiltFrom(nameOfUnit));
						gasCost+=Datasheet.getGasCost(Datasheet.getBuiltFrom(nameOfUnit));
					}
					
					//if more production can be made
					double extraMineralSpending = 0;
					double extraGasSpending = 0;
					while (canSupportMins(income - timeState.getMineralSpending() - extraMineralSpending, nameOfUnit) 
							&& canSupportGas(gasIncome - timeState.getGasSpending() - extraGasSpending, nameOfUnit)) {
						numberOfBuildings++;
						buildTime+= Datasheet.getBuildTime(Datasheet.getBuiltFrom(nameOfUnit));
						mineralCost+= Datasheet.getMineralCost(Datasheet.getBuiltFrom(nameOfUnit));
						gasCost+= Datasheet.getGasCost(nameOfUnit);
						extraMineralSpending += Datasheet.getMineralCost(nameOfUnit)/(1.0*Datasheet.getBuildTime(nameOfUnit));
						extraGasSpending += Datasheet.getGasCost(nameOfUnit)/(1.0*Datasheet.getBuildTime(nameOfUnit));
						//System.out.println("PLS SUPPORT "+numberOfBuildings+" "+Datasheet.getBuiltFrom(nameOfUnit));
					}
				}
				
				buildTime+= Datasheet.getBuildTime(nameOfUnit)*unitGoal.getValue()/(1.0*numberOfBuildings);
				mineralCost+= Datasheet.getMineralCost(nameOfUnit)*unitGoal.getValue();
				gasCost+= Datasheet.getGasCost(nameOfUnit)*unitGoal.getValue();
				
			}
		}
		double incomeNeeded = (1.0*mineralCost)/buildTime;
		double gasIncomeNeeded = (1.0*gasCost)/buildTime;
	//	System.out.println("income needed "+incomeNeeded+" income "+income);
		return (incomeNeeded > income || gasIncomeNeeded > gasIncome);
		
		//return buildTime;
	}
	
	private static boolean canSupportMins(double income, String unitName){
		return (income > Datasheet.getMineralCost(unitName)/Datasheet.getBuildTime(unitName));
	}
	
	private static boolean canSupportGas(double gasIncome, String unitName) {
		return (gasIncome > Datasheet.getGasCost(unitName)/Datasheet.getBuildTime(unitName));
	}
	
	private static boolean willBeSupplyBlocked(TimeState timeState) {
		int tempSupply = timeState.getSupply();
		for (Entry<String, Integer> entry : timeState.getGoal().entrySet()) {
			if (canBuild(entry.getKey(), timeState)) {
				tempSupply += Datasheet.getSupplyCost(entry.getKey());
			}
		}
		return (tempSupply + 1 >= timeState.getFutureMaxSupply());
	}
	
	private static boolean worthToBuild(String buildingName, String unitName, double mineralIncome, double gasIncome, TimeState timeState) {
		double totalTime = 0;
		double futureTime = 0;
		double totalMinCost = 0;
		double totalGasCost = 0;
		int numberOfUnitsNeeded = timeState.getGoal().get(unitName)-timeState.getTotalNumber(unitName);
		totalTime = numberOfUnitsNeeded*Datasheet.getBuildTime(unitName);
		totalMinCost = numberOfUnitsNeeded*Datasheet.getMineralCost(unitName);
		totalGasCost = numberOfUnitsNeeded*Datasheet.getGasCost(unitName);
		
		totalTime = ((totalMinCost/mineralIncome)+(totalGasCost/gasIncome))/(1.0*timeState.getTotalNumber(buildingName));
		return futureTime > totalTime;
	}
}
