package tree;

import game.Datasheet;
import game.UnitData;
import gameobjects.Entity;

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
	//return true;
	//		return (timeTaken(timeState.unitNumbers.get("Probe"), timeState) 
	//			> timeTaken(timeState.unitNumbers.get("Probe")+1, timeState));
			return (timeTaken(timeState.unitNumbers.get("Probe"), timeState));
	//		return true;
	}
	
	private static boolean canSupportMins(double income, String unitType){
		return (income > Datasheet.getMineralCost(unitType)/Datasheet.getBuildTime(unitType));
	}
	
	private static boolean canSupportGas(double gasIncome, String unitType) {
		return (gasIncome > Datasheet.getGasCost(unitType)/Datasheet.getBuildTime(unitType));
	}

	private static boolean timeTaken(int numberOfProbes, TimeState timeState) {
		double income = timeState.getMineralIncome(numberOfProbes - timeState.probesOnGas);
		double gasIncome = timeState.getGasIncome(timeState.probesOnGas);
		double buildTime = 0;
		int mineralCost = 0;
		int gasCost = 0;
		HashSet<String> dependancies = new HashSet<>();
		ArrayList<Entity> buildings = new ArrayList<>();
		
		for (Entry<String, Integer> unitGoal : timeState.getGoal().entrySet()) {
			if (unitGoal.getValue()-timeState.unitNumbers.get(unitGoal.getKey())-timeState.getUnitNumberInBuildQueue(unitGoal.getKey()) > 0) {
			
				//check dependancies and stuff and things ._.
				String nameOfUnit = unitGoal.getKey();
				while (nameOfUnit != null && Datasheet.getDependancy(nameOfUnit) != null 
						&& timeState.unitNumbers.get((Datasheet.getDependancy(nameOfUnit))) == 0) {
					nameOfUnit = Datasheet.getDependancy(nameOfUnit);
					if (nameOfUnit != null){
						dependancies.add(nameOfUnit);						
					}
				}
				
				for (String nameOfDependancy : dependancies){
					String dependancyName = Datasheet.getDependancy(nameOfDependancy);
					if (dependancyName != null){
						buildTime+= Datasheet.getBuildTime(dependancyName);
						mineralCost+= Datasheet.getMineralCost(dependancyName);	
						gasCost+=Datasheet.getGasCost(dependancyName);
					}
				}
				
				//check number of buildings currently available to build unit with
				int numberOfBuildings = timeState.unitNumbers.get((Datasheet.getBuiltFrom(nameOfUnit)));
				if (numberOfBuildings == 0){
					numberOfBuildings++;
					buildTime+= Datasheet.getBuildTime(Datasheet.getBuiltFrom(nameOfUnit));
					mineralCost+= Datasheet.getMineralCost(Datasheet.getBuiltFrom(nameOfUnit));
					gasCost+=Datasheet.getGasCost(Datasheet.getBuiltFrom(nameOfUnit));
				}
				//if more production can be madem
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
