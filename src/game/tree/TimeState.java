package game.tree;

import game.Chronoboost;
import game.Heuristics;
import game.UnitIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.swing.JTextPane;

import data.Datasheet;
import data.UnitData;

public class TimeState {
	ArrayList<TimeState> futureStates;
	
	int indexToWalk;

	private HashMap<String,Integer> goal;
	HashMap<String,Integer> unitNumbers;
	
	ArrayList<Chronoboost> nexusChronoboosts;
	ArrayList<Double> nexusEnergy;
	
	int probesOnGas;
	
	double totalMinerals;
	double totalGas;
	double minerals;
	double gas;
	int supply;
	int maxSupply;

	
	StringBuilder buildOrder;
	int time;
	public static int MAX_TIME;
	
	HashMap<String,BuildOrders> buildQueues;
	
	ArrayList<Integer> gatewayTransformations;
	int numberOfWarpgates;
	int numberOfTemplar;
	
	JTextPane output;

	private boolean goalComplete;
	
	public void finalize(){
//		System.out.println("COLLECTED");
	}
	
	public TimeState next(){
		if (time > MAX_TIME){
			return null;
		}
		
		ArrayList<Operation> possibleOperations = getPossibleOperations();
		Random generator = new Random();
		
		this.indexToWalk = generator.nextInt(possibleOperations.size());
		
		for (int i = 0; i < possibleOperations.size(); i++){
			if (i == indexToWalk) {
				return new TimeState(this, possibleOperations.get(i));				
			}
		}
		
		
		return new TimeState(this, possibleOperations.get(0));
	}
	
	//initial conditions
	public TimeState(JTextPane output, HashMap<String,Integer> goal){
		this.goal = goal;
		this.output = output;
		
		futureStates = new ArrayList<TimeState>();
		
		unitNumbers = new HashMap<>();
		buildQueues = new HashMap<>();
		gatewayTransformations = new ArrayList<>();
		numberOfWarpgates = 0;
		numberOfTemplar = 0;
		nexusEnergy = new ArrayList<>();
		nexusEnergy.add(new Double(0.0));
		nexusChronoboosts = new ArrayList<>();
		
		for (UnitData data : Datasheet.unitData) {
			unitNumbers.put(data.getName(), 0);
			buildQueues.put(data.getName(), new BuildOrders());
		}
		
		
		
		//ALWAYS RESERACH WARP GATE
		//this.goal.put("Warp Gate", 1);
		
		unitNumbers.replace("Nexus", 1);
		unitNumbers.replace("Probe", 6);
		
		this.totalMinerals = Datasheet.MINS_PER_NEXUS;
		this.totalGas = Datasheet.GAS_PER_NEXUS;
		this.minerals = 50.0;
		this.gas = 0;
		this.supply = 6;
		this.maxSupply = 10;
		this.goalComplete = false;
		this.probesOnGas = 0;
		this.buildOrder = new StringBuilder();
/*		
		ArrayList<Operation> possibleOperations = getPossibleOperations();
		Random generator = new Random();
		
		this.indexToWalk = generator.nextInt(possibleOperations.size());
		
		for (int i = 0; i < possibleOperations.size(); i++){
			if (i == indexToWalk) {
				new TimeState(this, possibleOperations.get(i));				
			}
		} */
	}
	//branch
	public TimeState(TimeState parent,Operation operation){
		this.time = parent.time + 1;
		this.totalMinerals = parent.totalMinerals;
		this.totalGas = parent.totalGas;
		this.minerals = parent.minerals;
		this.gas = parent.gas;
		this.supply = parent.supply;
		this.maxSupply = parent.maxSupply;
		this.probesOnGas = parent.probesOnGas;
		this.goalComplete = parent.goalComplete;
		this.buildOrder = parent.buildOrder;
		
		this.nexusChronoboosts = parent.nexusChronoboosts;
		this.nexusEnergy = parent.nexusEnergy;
		
		this.output = parent.output;
		
		this.gatewayTransformations = parent.gatewayTransformations;
		this.numberOfWarpgates = parent.numberOfWarpgates;
		this.numberOfTemplar = parent.numberOfTemplar;
//		this.unitNumbers = parent.cloneUnitNumbers();
		
//		this.buildQueues = parent.cloneBuildQueues();
		
//		this.goal = parent.cloneGoal();		
		
		this.unitNumbers = parent.unitNumbers;
		this.buildQueues = parent.buildQueues;
		this.nexusEnergy = parent.nexusEnergy;
		this.goal = parent.goal;
		
		this.executeOperation(operation);
		
		goalComplete = true;
		
		for (Entry<String,Integer> unitGoal : goal.entrySet()){
			if (unitGoal.getValue() != unitNumbers.get(unitGoal.getKey())){
				goalComplete = false;
			}
		}
		
		
		if (goalComplete){
			buildOrder.append("Goal complete in " + getTimeStamp() + "\n");
			buildOrder.append("Unit count:\n");
			for (Entry<String,Integer> entry : unitNumbers.entrySet()){
				if (entry.getValue() > 0){
					buildOrder.append(entry.getKey() + " : " + entry.getValue() + "\n");					
				}
			}
			
			//printBuildOrder();
			//printMe();
			if (output != null){
				output.setText(this.buildOrder.toString());				
			}
			TimeState.MAX_TIME = time - 1;				
			
			//System.out.println(TimeState.MAX_TIME);
		} else {	
			//printMe(operation);
			//System.out.println("\t--\t--\t--\t--");
			if (this.time < MAX_TIME){
				/*
				this.futureStates = new ArrayList<>();
				ArrayList<Operation> possibleOperations = getPossibleOperations();
				
				Random generator = new Random();
				
				this.indexToWalk = generator.nextInt(possibleOperations.size());
				
				for (int i = 0; i < possibleOperations.size(); i++){
					if (i == indexToWalk) {
						futureStates.add(new TimeState(this, possibleOperations.get(i)));				
					}
				} */
			} else {
				//System.out.println("Goal did not complete in "  + time + " seconds. Shortest time so far is " + time);
				//printMe();
			//	System.out.println("\n\n\n");
			}
		}
	}
		
	private ArrayList<Operation> getPossibleOperations() {
		ArrayList<Operation> ops = new ArrayList<>();
		
		//do nothing
		ops.add(new Operation("wait", ""));
		
		//assigning workers
		if (Heuristics.needMoreGas(this) && probesOnGas < unitNumbers.get("Assimilator")*3) {
			ops.add(new Operation("assign", "gas"));
		} else if (probesOnGas > 0){
			ops.add(new Operation("assign", "minerals"));
		}
				
		//if warp gate is researched, change gateways into warp gates
		if (numberOfWarpgates < unitNumbers.get("Gateway") && unitNumbers.get("Warp Gate") == 1) {
			//if gateway is not building anything
			int gatewaysBuilding = 0;

			for (Build gatewayBuild : buildQueues.get("Gateway")){
				if ( !(gatewayBuild instanceof WarpgateBuild)){
					gatewaysBuilding++;
				}
			}
			if (gatewaysBuilding < (unitNumbers.get("Gateway") - numberOfWarpgates - gatewayTransformations.size() ) ){
				ops.add(new Operation("convert", "Gateway"));				
			}
		}
		
		//Chronoboost
		for (Double energy: nexusEnergy){
			if (energy >= Chronoboost.COST){
				for (Entry<String,BuildOrders> entry : buildQueues.entrySet()){
					for (Build build : entry.getValue()){
						if ((UnitIs.Unit(build.nameOfUnit) && !build.isChronoboosted
								&& UnitIs.Building(Datasheet.getBuiltFrom(build.nameOfUnit))) || UnitIs.Upgrade(build.nameOfUnit)){
							if (build instanceof WarpgateBuild){
								if (((WarpgateBuild)build).hasProducedUnit){
									ops.add(new Operation("chronoboost", build.nameOfUnit));
								}
							} else {
								if (build.buildTime - build.time > 30.0){
									ops.add(new Operation("chronoboost", build.nameOfUnit));
								}
							}
						}
					}
				}
			}			
		}
		
		//Building units and buildings
		for (UnitData data : Datasheet.unitData){
			String unitName = data.getName();
			
			if (Heuristics.canBuild(unitName, this)) {
				
				if(UnitIs.Archon(unitName)){
					if (Heuristics.needsMoreForGoal(unitName, this)){
						if (numberOfTemplar >= 2){
							ops.add(new Operation("build", unitName));							
						}
					}
				}
				
				if (UnitIs.Probe(unitName)){
					if (Heuristics.moreProbes(this)){
						for (int i = 0; i <= unitNumbers.get("Nexus");i++){
							ops.add(new Operation("build", unitName));
							ops.add(new Operation("build", unitName));
							ops.add(new Operation("build", unitName));							
						}
					}
				}
				if (UnitIs.Assimilator(unitName) && Heuristics.needMoreGas(this)){
					if (hasFreeGeysers()) {;
						ops.add(new Operation("build", unitName));
					}
				}
				if (UnitIs.Pylon(unitName)) {
					if (Heuristics.needMoreSupply(this) || Heuristics.supplyBlocked(this)) {
						ops.add(new Operation("build", unitName));
					}
				}
				if (UnitIs.Nexus(unitName)) {
					if (Heuristics.worthExpanding(this)) {
						ops.add(new Operation("build", unitName));
					}
				}
				if (UnitIs.Unit(unitName)){
					if (Heuristics.needsMoreForGoal(unitName, this)){
						if (UnitIs.fromGateway(unitName)) {
							//get number of active warpgates
							int numberOfActiveWarpgates = 0;
							for (Build gatewayBuild : buildQueues.get("Gateway") ){
								if (gatewayBuild instanceof WarpgateBuild){
									numberOfActiveWarpgates++;
								}
							}
							if (numberOfActiveWarpgates < numberOfWarpgates) {
								ops.add(new Operation("warp", unitName));
								
							} else {
								ops.add(new Operation("build", unitName));
							}
						} else if (UnitIs.Archon(unitName)){
							if (numberOfTemplar >= 2){
								ops.add(new Operation("build", unitName));
							}
						} else {
							ops.add(new Operation("build", unitName));	
						}
					}		
				}
				
				if (UnitIs.HighTemplar(unitName)){
					if (Heuristics.canBuild(unitName, this) && goal.containsKey("Archon")){
						if (numberOfTemplar < (goal.get("Archon") - getTotalNumber("Archon"))*2 ){
							ops.add(new Operation("build", unitName));										
						}
					}
				}

				if (UnitIs.Builder(unitName)){
					if (Heuristics.needsMoreFromBuilder(unitName, this) && Heuristics.canSupportFromBuilder(unitName, this)){

						// checking for support
						ops.add(new Operation("build", unitName));						
					}
				}
				if (UnitIs.Dependancy(unitName)){
					if (Heuristics.needsDependancy(unitName, this)){
						ops.add(new Operation("build", unitName));												
					}
				}
				
				if (UnitIs.Upgrade(unitName)) {
					if (getTotalNumber(unitName) < 1 && Heuristics.needsMoreForGoal(unitName, this)) {
						ops.add(new Operation("build", unitName));
					}
				}
				//nexus for expansion

			}
		}
		
		return ops;
	}
	
	private void executeOperation(Operation op) {
		//pass time first
		addResources();

		for (int i = 0; i < nexusEnergy.size(); i++){
			nexusEnergy.set(i, nexusEnergy.get(i) + 1);
		}

		for (int i = 0; i < gatewayTransformations.size(); i++){
			gatewayTransformations.set(i, gatewayTransformations.get(i) + 1);
		}
		
		for (Iterator<Integer> iterator = gatewayTransformations.iterator(); iterator.hasNext();){
			Integer i = iterator.next();
			if (i >= Datasheet.WARPGATE_TRANFORMATION_TIME){
				this.numberOfWarpgates++;
				iterator.remove();
			}
		}
		
		//System.out.println(time + ": Operation: " + op.getVerb() + " " + op.getNoun());
		//Increment all build orders.
		for (BuildOrders buildOrder : buildQueues.values()){
			buildOrder.increment();	
			ArrayList<String> newUnits = buildOrder.getProducedUnits();
			if (!newUnits.isEmpty()){
				for (String unit : newUnits){
					unitNumbers.replace(unit, unitNumbers.get(unit) + 1);
					if (unit.equals("Nexus")){
						nexusEnergy.add(new Double(0.0));
						this.totalMinerals += Datasheet.MINS_PER_NEXUS;
						this.totalGas += Datasheet.GAS_PER_NEXUS;
					} else if (unit.equals("Dark Templar") || unit.equals("High Templar")) {
						this.numberOfTemplar++;
					}
					addToMaxSupply(unit);
				}
			}
			buildOrder.removeCompleted();
		}

		//check everything on the server side.
		switch (op.getVerb()){
		case "wait":
			//wait...
			break;
		case "build":
			//archon what a pain D:
			if (op.getNoun().equals("Archon") && Heuristics.enoughTemplarToMakeArchon(this)) {
				buildOrder.append(getTimeStamp() + " " + op.getNoun() + " " + supply + "/" + maxSupply + "\n");
				
				int sacrificedTemplar = 0;
				while (sacrificedTemplar < 2){
					if (unitNumbers.get("High Templar") > 0){
						unitNumbers.replace("High Templar", unitNumbers.get("High Templar") -1 );
						sacrificedTemplar++;
					}
					if (unitNumbers.get("Dark Templar") > 0){
						unitNumbers.replace("Dark Templar", unitNumbers.get("Dark Templar") -1 );
						sacrificedTemplar++;							
					}
				}
				this.numberOfTemplar -= 2;

			}
			
			for (UnitData data : Datasheet.unitData) {

				if (op.getNoun().equals(data.getName())){
					buildOrder.append(getTimeStamp() + " " + data.getName() + " " + supply + "/" + maxSupply + "\n");
					this.minerals -= Datasheet.getMineralCost(op.getNoun());
					this.gas -= Datasheet.getGasCost(op.getNoun());
					this.buildQueues.get(Datasheet.getBuiltFrom(op.getNoun())).add(new Build(op.getNoun()));
					addToSupply(op.getNoun());
				}
			}
			break;
		case "assign":
			switch (op.getNoun()) {
			case "minerals":
				//buildOrder.append(getTimeStamp() + " " + "Assign a Probe to minerals. \n");
				probesOnGas--;
				break;
			case "gas":
				//buildOrder.append(getTimeStamp() + " " + "Assign a Probe to gas. \n");
				probesOnGas++;
				break;
			}
			break;
		case "chronoboost":
			for (Entry<String,BuildOrders> buildQueue : buildQueues.entrySet()){
				for (Build build : buildQueue.getValue()){
					if (build.nameOfUnit.equals(op.getNoun()) && build.buildTime - build.time > 30){
						build.chronoboost();
						//System.out.println("Chronobooosting");
						break;
					}
				}
			}
			for (int i = 0; i < nexusEnergy.size(); i++){
				//System.out.println("At " + getTimeStamp() + " nexus has " + nexusEnergy.get(i) + "energy");
				if (nexusEnergy.get(i) >= Chronoboost.COST){
					nexusEnergy.set(i, nexusEnergy.get(i) - Chronoboost.COST);
					//System.out.println("Reducing nexus energy");
					break;
				}
			}
			buildOrder.append(getTimeStamp() + " Chronoboost " + op.getNoun() + " " + supply + "/" + maxSupply + "\n");
			break;
		case "convert":
			if (op.getNoun().equals("Gateway")){
				buildOrder.append(getTimeStamp() + " Converting Gateway into Warp Gate " + supply + "/" + maxSupply + "\n");
				gatewayTransformations.add(new Integer(0));
			}
			break;
		case "warp":
			buildOrder.append(getTimeStamp() + " Warp in " + op.getNoun() + " " + supply + "/" + maxSupply + "\n");
			this.minerals -= Datasheet.getMineralCost(op.getNoun());
			this.gas -= Datasheet.getGasCost(op.getNoun());
			this.buildQueues.get("Gateway").add(new WarpgateBuild(op.getNoun()));
			
			addToSupply(op.getNoun());
			break;
		}
	}
	
	public HashMap<String, Integer> getGoal() {
		return goal;
	}
	
	public int getFutureMaxSupply() {
		return (getTotalNumber("Pylon")*8) + (getTotalNumber("Nexus")*10);
	}
	
	public int getSupplyOfGoal() {
		int supply = 0;
		for (Entry<String, Integer> entry : goal.entrySet()) {
			supply+=Datasheet.getSupplyCost(entry.getKey())*entry.getValue();
		}
		return supply;
	}
	
	public double getMineralIncome(){
		return getMineralIncome(unitNumbers.get("Probe"), unitNumbers.get("Nexus"));
	}

	public double getMineralIncome(int probes, int numberOfNexi) {
		double income = 0;
		
		int numberOfProbes = probes - this.probesOnGas;
		numberOfProbes = Math.min(numberOfProbes, numberOfNexi*Datasheet.MAX_PROBES_PER_NEXUS);
				
		int efficientProbeBunches = numberOfProbes/Datasheet.EFFICIENT_PROBES;
		int remainder = numberOfProbes%Datasheet.EFFICIENT_PROBES;
		if (efficientProbeBunches == numberOfNexi){
			income = efficientProbeBunches*Datasheet.EFFICIENT_PROBES*Datasheet.MINS_PER_SECOND
					+ remainder*Datasheet.THIRD_MINS_PER_SECOND;
		} else if (efficientProbeBunches < numberOfNexi){
			income = efficientProbeBunches*Datasheet.EFFICIENT_PROBES*Datasheet.MINS_PER_SECOND 
					+ remainder*Datasheet.MINS_PER_SECOND;
		} else if (efficientProbeBunches > numberOfNexi){
			income = numberOfNexi*Datasheet.EFFICIENT_PROBES*Datasheet.MINS_PER_SECOND 
					+ (efficientProbeBunches - numberOfNexi)*Datasheet.EFFICIENT_PROBES*Datasheet.THIRD_MINS_PER_SECOND
					+ remainder*Datasheet.THIRD_MINS_PER_SECOND;
		}
		//System.out.println(numberOfNexi + " Nexi with  " + numberOfProbes + " probes gives " + income + " income");
		return income;
	}
	
	public double getGasIncome(){
		return getGasIncome(probesOnGas);
	}
	
	public double getGasIncome(int probes){
		return probes*Datasheet.GAS_PER_SECOND;
	}
	
	public double getMineralSpending() {
		double spending = 0;
		Iterator<Entry<String,BuildOrders>> it = this.buildQueues.entrySet().iterator();
	    while (it.hasNext()) {
	    	Entry<String, BuildOrders> pair = it.next();
	        String unitType = pair.getKey();
	        for (Build b : pair.getValue()){
	    	    if (UnitIs.Unit(b.nameOfUnit)){
		        	spending += Datasheet.getMineralCost(b.nameOfUnit)/Datasheet.getBuildTime(b.nameOfUnit);	    	    	
	    	    }
	        }	        
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
		for (Entry<String, BuildOrders> entry : buildQueues.entrySet()){
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
		for (Entry<String, BuildOrders> entry : buildQueues.entrySet()){
			for (Build build : entry.getValue()){
				tempSupply += Datasheet.getSupplyCost(build.nameOfUnit);
			}
		}
		return tempSupply;
	}
	
	public int getTotalNumber(String unitType){
		return 0+unitNumbers.get(unitType) + getUnitNumberInBuildQueue(unitType);
	}
	
	public String getTimeStamp() {
		int mins = time/60;
		int secs = time%60;
		return ( mins < 10 ? "0" + mins : mins ) + ":" + ( secs < 10 ? "0" + secs : secs);
	}
	
	private boolean hasFreeGeysers() {
		return (unitNumbers.get("Nexus")*2 > getTotalNumber("Assimilator"));
	}
	
	private void addToMaxSupply(String nameOfUnit) {
		if (nameOfUnit.equals("Nexus")){
			this.maxSupply += Datasheet.NEXUS_SUPPLY;
		} else if (nameOfUnit.equals("Pylon")){
			this.maxSupply += Datasheet.PYLON_SUPPLY;
		}
		this.maxSupply = Math.min(maxSupply, Datasheet.MAX_SUPPLY);
	}
	
	private void addToSupply(String nameOfUnit) {
		this.supply += Datasheet.getSupplyCost(nameOfUnit);
	}
	
	private void addResources() {
		double mineralsEarned = getMineralIncome();
		if (mineralsEarned > this.totalMinerals) {
			this.totalMinerals = 0;
			this.minerals += this.totalMinerals;
		} else {
			this.totalMinerals -= mineralsEarned;
			this.minerals += mineralsEarned;
		}
		
		double gasEarned = getGasIncome();
		if (gasEarned > this.totalGas) {
			this.totalGas = 0;
			this.gas += this.totalGas;
		} else {
			this.totalGas -= gasEarned;
			this.gas += gasEarned;
		}
	}
	
	private void printMe(){
		System.out.print("At time " + time + " we have: ");
		System.out.print(minerals + " minerals ," + gas + " gas ," + supply + "/" + maxSupply + " : ");
		for (Entry<String,Integer> entry: unitNumbers.entrySet()){
			if (entry.getValue() > 0){
				System.out.print(entry.getKey() + " : " + entry.getValue() + " , ");
			}
		}
		System.out.print("Warp gates : " + numberOfWarpgates);
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
		System.out.print("Warp gates : " + numberOfWarpgates);
		System.out.print(" Operation " + op.getNoun() + "  " + op.getVerb());
		System.out.println();
	}
	
	private void printBuildOrder() {
		System.out.println("PLS PRINT BUILD");
		System.out.println(buildOrder.toString());
	}
	public HashMap<String, Integer> getUnitNumbers() {
		return unitNumbers;
	}
	public ArrayList<Double> getNexusEnergy() {
		return nexusEnergy;
	}
	public int getProbesOnGas() {
		return probesOnGas;
	}
	public double getTotalMinerals() {
		return totalMinerals;
	}
	public double getTotalGas() {
		return totalGas;
	}
	public double getMinerals() {
		return minerals;
	}
	public double getGas() {
		return gas;
	}
	public int getSupply() {
		return supply;
	}
	public int getMaxSupply() {
		return maxSupply;
	}
	public StringBuilder getBuildOrder() {
		return buildOrder;
	}
	public int getTime() {
		return time;
	}
	public HashMap<String, BuildOrders> getBuildQueues() {
		return buildQueues;
	}

	public boolean isFinished() {
		return goalComplete;
	}
		
}

