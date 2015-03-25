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
			//return true;
	}
	
	public static boolean canSupport(double income, String unitType){
		return (income > Datasheet.getMineralCost(unitType)/Datasheet.getBuildTime(unitType));
	}

	private static boolean timeTaken(int numberOfProbes, TimeState timeState) {
		double income = timeState.getIncome(numberOfProbes);
		double buildTime = 0;
		int buildCost = 0;
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
						buildCost+= Datasheet.getMineralCost(dependancyName);						
					}
				}
				
				//check number of buildings currently available to build unit with
				int numberOfBuildings = timeState.unitNumbers.get((Datasheet.getBuiltFrom(nameOfUnit)));
				if (numberOfBuildings == 0){
					numberOfBuildings++;
					buildTime+= Datasheet.getBuildTime(Datasheet.getBuiltFrom(nameOfUnit));
					buildCost+= Datasheet.getMineralCost(Datasheet.getBuiltFrom(nameOfUnit));
				}
				//if more production can be madem
				double extraSpending = 0;
				while (canSupport(income - timeState.getSpending() - extraSpending, nameOfUnit)) {
					numberOfBuildings++;
					buildTime+= Datasheet.getBuildTime(Datasheet.getBuiltFrom(nameOfUnit));
					buildCost+= Datasheet.getMineralCost(Datasheet.getBuiltFrom(nameOfUnit));
					extraSpending += Datasheet.getMineralCost(nameOfUnit)/(1.0*Datasheet.getBuildTime(nameOfUnit));
					//System.out.println("PLS SUPPORT "+numberOfBuildings+" "+Datasheet.getBuiltFrom(nameOfUnit));
				}
				buildTime+= Datasheet.getBuildTime(nameOfUnit)*unitGoal.getValue()/(1.0*numberOfBuildings);
				buildCost+= Datasheet.getMineralCost(nameOfUnit)*unitGoal.getValue();
				
			}
		}
		double incomeNeeded = (1.0*buildCost)/buildTime;
	//	System.out.println("income needed "+incomeNeeded+" income "+income);
		return (incomeNeeded > income);
		
		//return buildTime;
	}
}
