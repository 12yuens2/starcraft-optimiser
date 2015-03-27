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

public class TimeState {
	ArrayList<TimeState> futureStates;
	
	int indexToWalk;

	private HashMap<String,Integer> goal;
	HashMap<String,Integer> unitNumbers;
	
	int probesOnGas;
	
	double minerals;
	double gas;
	int supply;
	int maxSupply;

	StringBuilder buildOrder;
	int time;
	static int MAX_TIME;
	
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
		this.gas = 0;
		this.supply = 6;
		this.maxSupply = 10;
		
		this.probesOnGas = 0;
		this.buildOrder = new StringBuilder();
		
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
		this.maxSupply = parent.maxSupply;
		this.probesOnGas = parent.probesOnGas;
		
		this.buildOrder = parent.buildOrder;
		
//		this.unitNumbers = parent.cloneUnitNumbers();
		
//		this.buildQueues = parent.cloneBuildQueues();
		
//		this.goal = parent.cloneGoal();		
		
		this.unitNumbers = parent.unitNumbers;
		this.buildQueues = parent.buildQueues;
		this.goal = parent.goal;
		
		this.executeOperation(operation);
		
		boolean goalComplete = true;
		
		for (Entry<String,Integer> unitGoal : goal.entrySet()){
			if (unitGoal.getValue() != unitNumbers.get(unitGoal.getKey())){
				goalComplete = false;
			}
		}
		
		
		if (goalComplete){
			System.out.println("Goal complete in " + time + " seconds.");
			printBuildOrder();
			printMe();
			TimeState.MAX_TIME = time;
		} else {	
			//printMe(operation);
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
			//	System.out.println("Goal did not complete in "  + time + " seconds. Shortest time so far is " + time);
			//	printMe();
			//	System.out.println("\n\n\n");
			}
		}
	}
	
	public HashMap<String, Integer> getGoal() {
		return goal;
	}
		
	private ArrayList<Operation> getPossibleOperations() {
		ArrayList<Operation> ops = new ArrayList<>();
		
		//do nothing
		ops.add(new Operation("wait", ""));
		
		//assigning workers
		if (needMoreGas() && probesOnGas<unitNumbers.get("Assimilator")*3) {
			ops.add(new Operation("assign", "gas"));
		} else if (probesOnGas > 0){
			ops.add(new Operation("assign", "minerals"));
		}
		
		//Building units and buildings
		for (UnitData data : Datasheet.unitData){
			String unitName = data.getName();
			
			if (canBuild(unitName)) {
				
				if (UnitIs.Probe(unitName)){
					if (Heuristics.moreProbes(this)){
						ops.add(new Operation("build", unitName));
						ops.add(new Operation("build", unitName));
						ops.add(new Operation("build", unitName));
					}
				}
				if (UnitIs.Assimilator(unitName) && needsGas()){
					if (hasFreeGeysers()) {
						ops.add(new Operation("build", unitName));
					}
				}
				if (UnitIs.Pylon(unitName)) {
					if (needMoreSupply()) {
						ops.add(new Operation("build", unitName));
					}
				}
				if (UnitIs.Nexus(unitName)) {
					if (worthExpanding()) {
						ops.add(new Operation("build", unitName));
					}
				}
				if (UnitIs.Unit(unitName)){
					if (needsMoreForGoal(unitName)){
						ops.add(new Operation("build", unitName));
					}
				}
				if (UnitIs.Builder(unitName)){
					if (needsMoreFromBuilder(unitName) && canSupportFromBuilder(unitName)){
						// checking for support
						ops.add(new Operation("build", unitName));						
					}
				}
				if (UnitIs.Dependancy(unitName)){
					if (needsDependancy(unitName)){
						ops.add(new Operation("build", unitName));												
					}
				}
				
				if (UnitIs.Upgrade(unitName)) {
					if (getTotalNumber(unitName) < 1 && needsMoreForGoal(unitName)) {
						ops.add(new Operation("build", unitName));
					}
				}
				//nexus for expansion

			}
//			Heuristics.makeProductionBuildings(ops, data, this);
//			ops.add(new Operation("build", data.getName()));
		}
		
	//	Heuristics.moreProbes(ops, this);
	//	Heuristics.expand(ops, this);
		
		return ops;
	}

	private boolean hasFreeGeysers() {
		return (unitNumbers.get("Nexus")*2 > getTotalNumber("Assimilator"));
	}
	
	private boolean canSupportFromBuilder(String name) {

		double excessMinerals = getMineralIncome() - getMineralSpending();
		double excessGas = getGasIncome() - getGasSpending();
		
		for (Entry<String,Integer> entry : goal.entrySet()){

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
	
	private void executeOperation(Operation op) {
		//pass time first
		this.minerals += getMineralIncome();
		this.gas += getGasIncome();
		//System.out.println(time + ": Operation: " + op.getVerb() + " " + op.getNoun());

		//Increment all build orders.
		for (ArrayList<Build> buildQueue : buildQueues.values()){
			for (Iterator<Build> iterator = buildQueue.iterator(); iterator.hasNext();){
			    Build build = iterator.next();
				build.increment();
				//System.out.println("Building " + build.nameOfUnit + " " + build.time + "/" + build.buildTime);
				if (build.getTime() >= build.getBuildTime()){
					unitNumbers.replace(build.nameOfUnit, unitNumbers.get(build.nameOfUnit) + 1);
					addSupply(build.nameOfUnit);
					iterator.remove();
				}
			}
		}
		
		//check everything on the server side.
		switch (op.getVerb()){
		case "wait":
			//wait...
			break;
		case "build":
			for (UnitData data : Datasheet.unitData) {
				if (op.getNoun().equals(data.getName())){
					buildOrder.append(getTimeStamp() + " " + data.getName() + " " + supply + "/" + maxSupply + "\n");
					supplyBlocked();
					this.minerals -= Datasheet.getMineralCost(op.getNoun());
					this.gas -= Datasheet.getGasCost(op.getNoun());
					this.buildQueues.get(Datasheet.getBuiltFrom(op.getNoun())).add(new Build(op.getNoun()));
				}
			}
			break;
		case "assign":
			switch (op.getNoun()) {
			case "minerals":
				probesOnGas--;
				break;
			case "gas":
				probesOnGas++;
				break;
			}
			break;
		}
	}
	
	private boolean needMoreSupply() {
		if (maxSupply < supply - 3 || willBeSupplyBlocked()) {
			if (maxSupply < getSupplyOfGoal()+unitNumbers.get("Probe")) {
				return true;
			}
		}
		return false;
	}
	
	private boolean willBeSupplyBlocked() {
		int tempSupply = supply;
		for (Entry<String, Integer> entry : goal.entrySet()) {
			if (canBuild(entry.getKey())) {
				tempSupply += Datasheet.getSupplyCost(entry.getKey());
			}
		}
		return (tempSupply + 1 >= getFutureMaxSupply());
	}
	
	private void supplyBlocked() {
		if (supply == maxSupply) {
			buildOrder.append("Supply blocked :( \n");
		}
	}
	
	private int getFutureMaxSupply() {
		return (getTotalNumber("Pylon")*8) + (getTotalNumber("Nexus")*10);
	}
	
	private int getSupplyOfGoal() {
		int supply = 0;
		for (Entry<String, Integer> entry : goal.entrySet()) {
			supply+=Datasheet.getSupplyCost(entry.getKey())*entry.getValue();
		}
		return supply;
	}
	
	private void addSupply(String nameOfUnit) {
		if (nameOfUnit.equals("Nexus")){
			this.maxSupply += 10;
		} else if (nameOfUnit.equals("Pylon")){
			this.maxSupply += 8;
		} else {
			this.supply += Datasheet.getSupplyCost(nameOfUnit);
		}
		this.maxSupply = Math.min(maxSupply, Datasheet.MAX_SUPPLY);
	}
	
	private void printMe(){
		System.out.print("At time " + time + " we have: ");
		System.out.print(minerals + " minerals ," + gas + " gas ," + supply + "/" + maxSupply + " : ");
		for (Entry<String,Integer> entry: unitNumbers.entrySet()){
			if (entry.getValue() > 0){
				System.out.print(entry.getKey() + " : " + entry.getValue() + " , ");
			}
		}
		System.out.println();
	}
	
	private void printMe(Operation op){
		System.out.print("At time " + time + " we have: ");
		System.out.print(minerals + " minerals ," + gas + " gas ," + supply + "/" + maxSupply + " : ");
		for (Entry<String,Integer> entry: unitNumbers.entrySet()){
			if (entry.getValue() > 0){
				System.out.print(entry.getKey() + " : " + entry.getValue() + " , ");
			}
		}
		System.out.print(" Operation " + op.getNoun() + "  " + op.getVerb());
		System.out.println();
	}
	
	
	private void printBuildOrder() {
		System.out.println(buildOrder.toString());
	}
	
	
	public double getMineralIncome(){
		return getMineralIncome(unitNumbers.get("Probe"), unitNumbers.get("Nexus"));
	}

	public double getMineralIncome(int probes, int numberOfNexi) {
		double income = 0;
		
		int numberOfProbes = probes - this.probesOnGas;
		numberOfProbes = Math.min(numberOfProbes, numberOfNexi*Datasheet.MAX_PROBES_PER_NEXUS);
				
		int efficientProbeBunches = numberOfProbes/16;
		int remainder = numberOfProbes%16;
		if (efficientProbeBunches == numberOfNexi){
			income = efficientProbeBunches*16*Datasheet.MINS_PER_SECOND
					+ remainder*Datasheet.THIRD_MINS_PER_SECOND;
		} else if (efficientProbeBunches < numberOfNexi){
			income = efficientProbeBunches*16*Datasheet.MINS_PER_SECOND 
					+ remainder*Datasheet.MINS_PER_SECOND;
		} else if (efficientProbeBunches > numberOfNexi){
			income = numberOfNexi*16*Datasheet.MINS_PER_SECOND 
					+ (efficientProbeBunches - numberOfNexi)*16*Datasheet.THIRD_MINS_PER_SECOND
					+ remainder*Datasheet.THIRD_MINS_PER_SECOND;
		}
		return income;
	}
	
	public double getGasIncome(){
		return getGasIncome(probesOnGas);
	}
	
	public double getGasIncome(int probes){
		return probes*Datasheet.GAS_PER_SECOND;
	}
	
	public double getMineralSpending() {
		int spending = 0;
		Iterator it = this.buildQueues.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<String, ArrayList<Build>> pair = (Map.Entry<String,ArrayList<Build>>)it.next();
	        String unitType = pair.getKey();
	        spending += pair.getValue().size()*Datasheet.getMineralCost(unitType)/Datasheet.getBuildTime(unitType);
	        
	    }
		return spending;
	}
	
	public int getGasSpending(){
		int spending = 0;
		Iterator it = this.buildQueues.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<String, ArrayList<Build>> pair = (Map.Entry<String,ArrayList<Build>>)it.next();
	        String unitType = pair.getKey();
	        spending += pair.getValue().size()*Datasheet.getGasCost(unitType)/Datasheet.getBuildTime(unitType);
	        
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
	
	public int getSupplyInBuildQueues(){
		int tempSupply = 0;
		for (Entry<String, ArrayList<Build>> entry : buildQueues.entrySet()){
			for (Build build : entry.getValue()){
				tempSupply += Datasheet.getSupplyCost(build.nameOfUnit);
			}
		}
		return tempSupply;
	}
	
	public int getTotalNumber(String unitType){
		return unitNumbers.get(unitType) + getUnitNumberInBuildQueue(unitType);
	}
	
	public String getTimeStamp() {
		int mins = time/60;
		int secs = time%60;
		return ( mins < 10 ? "0" + mins : mins ) + ":" + ( secs < 10 ? "0" + secs : secs);
	}
	
	public boolean needsMoreForGoal(String unitName){
		return (goal.containsKey(unitName) && getTotalNumber(unitName) < goal.get(unitName));
	}

	public boolean needsDependancy(String dependancyName){
		if (getTotalNumber(dependancyName) > 0){
			return false;
		}
		for (Entry<String, Integer> entry: goal.entrySet()){
			String dependancy = Datasheet.getDependancy(entry.getKey());
			while (dependancy != null){
				if (dependancy.equals(dependancyName) && needsMoreForGoal(entry.getKey())){
					return true;
				}
				dependancy = Datasheet.getDependancy(dependancy);
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
	
	private boolean needMoreGas() {
		double gasCost = 0;
		for (Entry<String,Integer> entry : goal.entrySet()){
			gasCost += Datasheet.getGasCost(entry.getKey())*(entry.getValue()-unitNumbers.get(entry.getKey()));
			
		}
		return (gasCost > gas);
	}
	
	public boolean canBuild(String unitName){
		String dependancy = Datasheet.getDependancy(unitName);
		String builtFrom = Datasheet.getBuiltFrom(unitName);
		if (dependancy == null || unitNumbers.get(dependancy) > 0){
			if( buildQueues.get(builtFrom).size() < unitNumbers.get(builtFrom)){
				if(minerals >= Datasheet.getMineralCost(unitName) && gas >= Datasheet.getGasCost(unitName)){
					if (supply + getSupplyInBuildQueues() + Datasheet.getSupplyCost(unitName) <= maxSupply){
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean worthExpanding() {
		boolean b = Heuristics.timeTaken(unitNumbers.get("Probe"), unitNumbers.get("Nexus"), this);
		//System.out.println(b);
		return b;
	}
	
}

