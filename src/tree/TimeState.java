package tree;

import game.Datasheet;
import game.UnitData;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

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

	final double probe_shit = 41.0/60.0;
	
	int time;
	static int MAX_TIME = 300;
	
	HashMap<String,ArrayList<Build>> buildQueues;
	
	
	int numberOfProbes;
	BuildOrders probeBuilds;
	
	
	int numberOfNexi;
	BuildOrders nexusBuilds;
	
	int numberOfGateways;
	BuildOrders gatewayBuilds;
	
	BuildOrders stargateBuilds;
	BuildOrders cyberneticsBuilds;
	BuildOrders ForgeBuilds;
	
	int numberOfZealots;

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
		
		this.numberOfNexi = 1;
		this.numberOfGateways = 0;
		this.numberOfProbes = 6;
		this.numberOfZealots = 0;
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
	//rip knorr cube	int[] knorrCube = new int[2000];
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
			TimeState.MAX_TIME = time;
		} else {	
			printMe();
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
		//		System.out.println("Goal did not complete in "  + time + " seconds.");
			}
		}
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

				
				Heuristics.makeProductionBuildings(ops, data, this);

				
				ops.add(new Operation("build", data.getName()));
			}
		}
		
		Heuristics.moreProbes(ops, this);
		Heuristics.expand(ops, this);
		
		
		
		/*rip loop ftw
		//huge list incoming
		if (timeState.minerals >= Datasheet.getMineralCost("Probe") && timeState.nexusBuilds.size() < timeState.numberOfNexi){
			ops.add(new Operation("build", "Probe"));
		}
		if (timeState.minerals >= Datasheet.getMineralCost("Gateway") && timeState.probeBuilds.size() < timeState.numberOfProbes){
			ops.add(new Operation("build", "Gateway"));
		}
		if (timeState.minerals >= Datasheet.getMineralCost("Zealot") && timeState.gatewayBuilds.size() < timeState.numberOfGateways 
				&& timeState.goal.containsKey("Zealot") && timeState.numberOfZealots < timeState.goal.get("Zealot")){
			ops.add(new Operation("build", "Zealot"));
		}*/
		
		
		return ops;
	}
	
	private void executeOperation(Operation op) {
		//pass time first
		this.minerals += unitNumbers.get("Probe")*probe_shit;
		//System.out.println("Operation: " + op.getVerb() + " " + op.getNoun());

		//Increment all build orders.
		for (ArrayList<Build> buildQueue : buildQueues.values()){
			for (Iterator<Build> iterator = buildQueue.iterator(); iterator.hasNext();){
			    Build build = iterator.next();
				build.increment();
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
					this.buildQueues.get(op.getNoun()).add(new Build(op.getNoun()));
				}
			}
			break;
		}
	}
	
	private void printMe(){
				System.out.println("At time " + time + " we have: "
				+ minerals + " Minerals, " 
				+ numberOfProbes + " probes, " 
				+ numberOfGateways + " gateways, " 
				+ numberOfZealots + " zealots" );
	}
	
	public static void main(String[] args) {
		
		final int MAX_NUMBER_OF_TRIALS = 1000000;
		int numberOfTrials = 0;
		
		HashMap<String,Integer> goal = new HashMap<>();
		Datasheet.init();

		goal.put("Zealot", 4);
		
		while (numberOfTrials < MAX_NUMBER_OF_TRIALS){
			TimeState gameTree = new TimeState(goal);	
			numberOfTrials++;
			if (numberOfTrials % 100 == 0){
	//			System.out.println(numberOfTrials);
			}
		}

	}
}

