package tree;

import game.Datasheet;
import game.UnitData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map.Entry;

public class Heuristics {

	public static void makeProductionBuildings(ArrayList<Operation> ops, UnitData data, TimeState timeState) {
		
		//build production building if unit in goal builds from that building
		if (timeState.unitNumbers.get(Datasheet.getBuiltFrom(data.getName())) == 0) {
			ops.add(new Operation("build", Datasheet.getBuiltFrom(data.getName())));
		}
		
	}

	public static boolean moreProbes(TimeState timeState) {
	//		return (timeTaken(timeState.unitNumbers.get("Probe"), timeState) 
	//			> timeTaken(timeState.unitNumbers.get("Probe")+1, timeState));
			if (timeTaken(timeState.unitNumbers.get("Probe"), timeState.unitNumbers.get("Nexus"), timeState) == true){
				//System.out.println(timeState.time + " : IT IS WORTH BUILDING MORE PROBERINOS");
				return true;
			} else {
				return false;
			}
	//		return true;
	}
	
	private static boolean canSupportMins(double income, String unitName){
		return (income > Datasheet.getMineralCost(unitName)/Datasheet.getBuildTime(unitName));
	}
	
	private static boolean canSupportGas(double gasIncome, String unitName) {
		return (gasIncome > Datasheet.getGasCost(unitName)/Datasheet.getBuildTime(unitName));
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

	public static boolean timeTaken(int numberOfProbes, int numberOfNexi, TimeState timeState) {
		int deltaNexi = numberOfNexi-timeState.unitNumbers.get("Nexus");
		double income = timeState.getMineralIncome(numberOfProbes - timeState.probesOnGas, numberOfNexi);
		double gasIncome = timeState.getGasIncome(timeState.probesOnGas+deltaNexi);
		double buildTime = 0;
		int mineralCost = 0;
		int gasCost = 0;
		HashSet<String> dependancies = new HashSet<>();
		
		buildTime += deltaNexi*Datasheet.getBuildTime("Nexus");
		mineralCost += deltaNexi*Datasheet.getMineralCost("Nexus");
		
		for (Entry<String, Integer> unitGoal : timeState.getGoal().entrySet()) {
			if (unitGoal.getValue()-timeState.unitNumbers.get(unitGoal.getKey())-timeState.getUnitNumberInBuildQueue(unitGoal.getKey()) > 0) {
			
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
}
