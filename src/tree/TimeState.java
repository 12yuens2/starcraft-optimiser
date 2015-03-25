package tree;

import game.Datasheet;
import game.UnitData;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import units.buildings.Nexus;

public class TimeState {
	ArrayList<TimeState> futureStates;
	
	int indexToWalk;

	private HashMap<String,Integer> goal;
	HashMap<String,Integer> unitNumbers;
	
	double minerals;
	double gas;
	int supply;
	int maxSupply;
	final int SUPPLY_LIMIT = 200;

	int time;
	static int MAX_TIME = 3000;
	
	HashMap<String,ArrayList<Build>> buildQueues;
	
	//initial conditions
	public TimeState(HashMap<String,Integer> goal){
		this.goal = goal;
		futureStates = new ArrayList<TimeState>();
		
		unitNumbers = new HashMap<>();
		buildQueues = new HashMap<>();
		
		for (UnitData data : Datasheet.unitData) {
			unitNumbers.put(data.getName(), 0);
			buildQueues.put(data.getName(), new ArrayList<>());
		}
		
		unitNumbers.replace("Nexus", 1);
		unitNumbers.replace("Probe", 6);
		
		this.minerals = 50.0;
		
		ArrayList<Operation> possibleOperations = getPossibleOperations();
		Random generator = new Random();
		
		this.indexToWalk = generator.nextInt(possibleOperations.size());
		
		for (int i = 0; i < possibleOperations.size(); i++){
			if (i == indexToWalk) {
				futureStates.add(new TimeState(this, possibleOperations.get(i)));				
			}
		}
	}
	//branch
	public TimeState(TimeState parent,Operation operation){
		this.time = parent.time + 1;
		this.minerals = parent.minerals;
		this.gas = parent.gas;
		this.supply = parent.supply;
		

		this.unitNumbers = parent.cloneUnitNumbers();
		
		this.buildQueues = parent.cloneBuildQueues();
		
		this.goal = parent.cloneGoal();		
		
		this.executeOperation(operation);
		
		boolean goalComplete = true;
		
		for (Entry<String,Integer> unitGoal : goal.entrySet()){
			if (unitGoal.getValue() != unitNumbers.get(unitGoal.getKey())){
				goalComplete = false;
			}
		}
		
		
		if (goalComplete){
			System.out.println("Goal complete in " + time + " seconds.");
			printMe();
			TimeState.MAX_TIME = time;
		} else {	
//			printMe();
			//System.out.println("\t--\t--\t--\t--");
			if (this.time < MAX_TIME){
				this.futureStates = new ArrayList<>();
				ArrayList<Operation> possibleOperations = getPossibleOperations();
				
				Random generator = new Random();
				
				this.indexToWalk = generator.nextInt(possibleOperations.size());
				
				for (int i = 0; i < possibleOperations.size(); i++){
					if (i == indexToWalk) {
						futureStates.add(new TimeState(this, possibleOperations.get(i)));				
					}
				}
			} else {
				//System.out.println("Goal did not complete in "  + time + " seconds. Shortest time so far is " + time);
				//printMe();
				//System.out.println("\n\n\n");
			}
		}
	}
	
	public HashMap<String, Integer> getGoal() {
		return goal;
	}
	
	private HashMap<String, Integer> cloneUnitNumbers() {
		HashMap<String, Integer>  newNumbers = new HashMap<>();
		for (Entry<String,Integer> entry : unitNumbers.entrySet() ){
			newNumbers.put(entry.getKey(), entry.getValue());
		}
		return newNumbers;
	}
	
	private HashMap<String, ArrayList<Build>> cloneBuildQueues() {
		HashMap<String, ArrayList<Build>> newQueue = new HashMap<>();
		for (Entry<String,ArrayList<Build>> entry : this.buildQueues.entrySet()){
			ArrayList<Build> newBuild = new ArrayList<>();
			for (Build b : entry.getValue()){
				newBuild.add(b.deepClone());
			}
			newQueue.put(entry.getKey(), newBuild);
		}
		return newQueue;
		
	}
	
	private HashMap<String, Integer> cloneGoal() {
		HashMap<String,Integer> newGoal = new HashMap<>();
		for (Entry<String,Integer> entry : this.goal.entrySet()){
			newGoal.put(entry.getKey(), entry.getValue());
		}
		return newGoal;
	}
	
	private ArrayList<Operation> getPossibleOperations() {
		ArrayList<Operation> ops = new ArrayList<>();
		
		ops.add(new Operation("wait", ""));
		
		for (UnitData data : Datasheet.unitData){
			if (minerals >= Datasheet.getMineralCost(data.getName()) && buildQueues.get(data.getBuiltFrom()).size() < unitNumbers.get(data.getBuiltFrom())) {
				switch (data.getName()){
				case "Probe" :
					if (Heuristics.moreProbes(this)){
						ops.add(new Operation("build", data.getName()));						
					}
					break;
				case "Assimilator" :
					if (getTotalNumber("Assimilator") < getTotalNumber("Nexus") && needsGas()){
						ops.add(new Operation("build", data.getName()));
					}
					break;
				case "Nexus" :
					
					break;
				case "Zealot" :
					if (needsMoreForGoal("Zealot")){
						ops.add(new Operation("build", data.getName()));						
					}
					break;
				case "Gateway" :
					for (Entry<String,Integer> entry : goal.entrySet()){
						if (Datasheet.getBuiltFrom(entry.getKey()).equals("Gateway")){
							if (Heuristics.canSupport(getIncome(), entry.getKey())){
								ops.add(new Operation("build", data.getName()));								
							}
						}
					}
					break;
				}
			}
//			Heuristics.makeProductionBuildings(ops, data, this);
//			ops.add(new Operation("build", data.getName()));
		}
		
	//	Heuristics.moreProbes(ops, this);
	//	Heuristics.expand(ops, this);
		
		return ops;
	}
	
	private void executeOperation(Operation op) {
		//pass time first
		this.minerals += getIncome(unitNumbers.get("Probe"));
		//System.out.println("Operation: " + op.getVerb() + " " + op.getNoun());

		//Increment all build orders.
		for (ArrayList<Build> buildQueue : buildQueues.values()){
			for (Iterator<Build> iterator = buildQueue.iterator(); iterator.hasNext();){
			    Build build = iterator.next();
				build.increment();
				//System.out.println("Building " + build.nameOfUnit + " " + build.time + "/" + build.buildTime);
				if (build.getTime() >= build.getBuildTime()){
					unitNumbers.replace(build.nameOfUnit, unitNumbers.get(build.nameOfUnit) + 1);
					iterator.remove();
				}
			}
		}
		
		//check everything on the server side.
		switch (op.getVerb()){
		case "wait":
			
			break;
		case "build":
			for (UnitData data : Datasheet.unitData) {
				if (op.getNoun().equals(data.getName())){
					this.minerals -= Datasheet.getMineralCost(op.getNoun());
					this.buildQueues.get(Datasheet.getBuiltFrom(op.getNoun())).add(new Build(op.getNoun()));
				}
			}
			break;
		}
	}
	
	private void printMe(){
		System.out.println("At time " + time + " we have: ");
		System.out.println(minerals + " minerals");
		for (Entry<String,Integer> entry: unitNumbers.entrySet()){
			System.out.print(entry.getKey() + " : " + entry.getValue() + " , ");
		}
		System.out.println();
	}
	
	public double getIncome(){
		return getIncome(unitNumbers.get("Probe"));
	}
	
	public double getIncome(int numberOfProbes) {
		double income = 0;
		
		int numberOfNexi = unitNumbers.get("Nexus");
		
		int bunchesOfSixteen = numberOfProbes/16;
		int remainder = numberOfProbes%16;
		if (bunchesOfSixteen == numberOfNexi){
			income = bunchesOfSixteen*16*Nexus.PROBE_MINING_PER_SECOND 
					+ remainder*Nexus.THIRD_PROBE_MINING_PER_SECOND;
		} else if (bunchesOfSixteen < numberOfNexi){
			income = bunchesOfSixteen*16*Nexus.PROBE_MINING_PER_SECOND 
					+ remainder*Nexus.PROBE_MINING_PER_SECOND;
		} else if (bunchesOfSixteen > numberOfNexi){
			income = numberOfNexi*16*Nexus.PROBE_MINING_PER_SECOND 
					+ (bunchesOfSixteen - numberOfNexi)*16*Nexus.THIRD_PROBE_MINING_PER_SECOND 
					+ remainder*Nexus.THIRD_PROBE_MINING_PER_SECOND;
		}
		return income;
	}
	
	public int getSpending() {
		int spending = 0;
		Iterator it = this.buildQueues.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<String, ArrayList<Build>> pair = (Map.Entry<String,ArrayList<Build>>)it.next();
	        String unitType = pair.getKey();
	        spending += pair.getValue().size()*Datasheet.getMineralCost(unitType)/Datasheet.getBuildTime(unitType);
	        
	    }
		return spending;
	}
	

	public int getUnitNumberInBuildQueue(String name) {
		int unitCount = 0;
		for (Entry<String, ArrayList<Build>> entry : buildQueues.entrySet()){
			for (Build build : entry.getValue()){
				if (build.nameOfUnit.equals(name)){
					unitCount++;
				}
			}
		}
		return unitCount;
	}
	
	public int getTotalNumber(String unitType){
		return unitNumbers.get(unitType) + getUnitNumberInBuildQueue(unitType);
	}
	
	public boolean needsMoreForGoal(String unitName){
		return (goal.containsKey(unitName) && getTotalNumber(unitName) < goal.get(unitName));
	}

	public boolean needsDependancy(String dependancyName){
		if (unitNumbers.get(dependancyName) > 0){
			return false;
		}
		for (Entry<String, Integer> entry: goal.entrySet()){
			if (Datasheet.getDependancy(entry.getKey()).equals(dependancyName) && needsMoreForGoal(entry.getKey())){
				return true;
			}
		}
		return false;		
	}
	
	public boolean needsMoreFromBuilder(String builderName){
		for (Entry<String, Integer> entry: goal.entrySet()){
			if (Datasheet.getBuiltFrom(entry.getKey()).equals(builderName) && needsMoreForGoal(entry.getKey())){
				return true;
			}
		}
		return false;
	}
	
	private boolean needsGas() {
		for (Entry<String,Integer> entry : goal.entrySet()){
			if (Datasheet.getGasCost(entry.getKey()) > 0){
				return true;
			}
		}
		return false;
	}
	
	public boolean isProbe(String unitName){
		return unitName.equals("Probe");
	}

	public boolean isAssimilator(String unitName){
		return unitName.equals("Assimilator");
	}
	
	public boolean canBuild(String unitName){
		String dependancy = Datasheet.getDependancy(unitName);
		String builtFrom = Datasheet.getBuiltFrom(unitName);
		if (unitNumbers.get(dependancy) > 0 && buildQueues.get(builtFrom).size() < unitNumbers.get(builtFrom)){
			if (minerals >= Datasheet.getMineralCost(unitName)){
				return true;				
			}
		}
		return false;
	}
	
	public boolean isUnit(String unitName){
		return (
				unitName.equals("Probe") || unitName.equals("Zealot") || unitName.equals("Stalker") ||
				unitName.equals("Observer") || unitName.equals("Sentry") || unitName.equals("High Templar") ||
				unitName.equals("Immortal") || unitName.equals("Phoenix") || unitName.equals("Void Ray") ||
				unitName.equals("Oracle") || unitName.equals("Warp Prism") || unitName.equals("Colossus") ||
				unitName.equals("Tempest") || unitName.equals("Dark Templar") || unitName.equals("Archon") ||
				unitName.equals("Carrier") || unitName.equals("Interceptor") || unitName.equals("Mothership Core") ||
				unitName.equals("Mothership")
				);
	}
	
	public boolean isBuilder(String unitName){
		return (
				unitName.equals("Nexus") || unitName.equals("Gateway") || unitName.equals("Robotics Facility") ||
				unitName.equals("Stargate") || unitName.equals("Mothership Core") || unitName.equals("High Templar") ||
				unitName.equals("Probe") || unitName.equals("Carrier")
		);
	}
	
	public boolean isDependancy(String unitName) {
		return (
			unitName.equals("Pylon") || unitName.equals("Gateway") || unitName.equals("Cybernetics Core") ||
			unitName.equals("Twilight Council") || unitName.equals("Robotics Facility") ||
			unitName.equals("Stargate") || unitName.equals("Forge") || unitName.equals("Robotics Bay") ||
			unitName.equals("Fleet Beacon") || unitName.equals("Templar Archives") || unitName.equals("Dark Shrine")
		);
	}
}

