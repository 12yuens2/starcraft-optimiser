package game.tree;

import game.Heuristics;
import game.UnitIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import javax.swing.JTextPane;

import data.Datasheet;
import data.UnitNumbers;
import data.UnitData;

/**
 * TimeState represents the simulation of the game. It simulates the game according
 * to a given goal, returning the next TimeState.
 */
public class TimeState {
	ArrayList<TimeState> futureStates;
	
	/**
	 * @see UnitNumbers
	 */
	private UnitNumbers goal;
	private UnitNumbers unitNumbers;
	
	/**
	 * The list with all of the energy of the nexi. Stored as individual doubles,
	 * as the energy pool of nexi is not shared.
	 */
	ArrayList<Double> nexusEnergy;
	
	/**
	 * Instead of differentiating between probes, we use an integer to specify
	 * how many probes are  mining gas. From there we can calculate everything else.
	 */
	private int probesOnGas;
	
	private double totalMinerals;
	private double totalGas;
	private double minerals;
	private double gas;
	private int supply;
	private int maxSupply;

	/**
	 * The final output of the simulation.
	 */
	private StringBuilder buildOrder;
	
	private int time;
	
	/**
	 * The maximum time the simulation should run for.
	 * This changes to become the smallest time for which the goal is achieved.
	 */
	private static int maxTime;
	
	/**
	 * The build queues for each builder type.
	 * @see BuildOrders
	 */
	private HashMap<String,BuildOrders> buildQueues;
	
	/**
	 * The list of active warp gate transformations.
	 */
	private ArrayList<Integer> gatewayTransformations;
	
	/**
	 * The number of gateways which are warp gates. This is used since we don't
	 * keep track of warp gates in unitNumbers.
	 */
	private int numberOfWarpgates;
	private int numberOfTemplar;
	
	private JTextPane output;

	private boolean goalComplete;

	/**
	 * @return The simulation at the next second of game time. Returns null if goal is achieved.
	 */
	public TimeState next(){
		
		if (time > maxTime){
			return null;
		}
		
		ArrayList<Operation> possibleOperations = getPossibleOperations();
		Random generator = new Random();
		
		int index = generator.nextInt(possibleOperations.size());
		
		return new TimeState(this, possibleOperations.get(index));
	}
	
	/**
	 * The initial TimeState of the game. 
	 * @param output the component to output the build order to.
	 * @param goal the goal to achieve.
	 */
	public TimeState(JTextPane output, UnitNumbers goal){
		this.goal = goal;
		this.output = output;
		
		futureStates = new ArrayList<TimeState>();
		
		unitNumbers = new UnitNumbers();
		buildQueues = new HashMap<>();
		gatewayTransformations = new ArrayList<>();
		numberOfWarpgates = 0;
		numberOfTemplar = 0;
		nexusEnergy = new ArrayList<>();
		nexusEnergy.add(new Double(0.0));
		
		for (UnitData data : Datasheet.unitData) {
			unitNumbers.put(data.getName(), 0);
			buildQueues.put(data.getName(), new BuildOrders());
		}
		
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
	}
	
	/**
	 * The simulation from a parent TimeState. Adds one second of game time.
	 * @param parent the parent TimeState.
	 * @param operation the Operation to perform on that second.
	 * @see Operation
	 */
	private TimeState(TimeState parent,Operation operation){
		
		//Take information from parent.
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
		this.nexusEnergy = parent.nexusEnergy;
		this.output = parent.output;
		this.gatewayTransformations = parent.gatewayTransformations;
		this.numberOfWarpgates = parent.numberOfWarpgates;
		this.numberOfTemplar = parent.numberOfTemplar;	
		this.unitNumbers = parent.unitNumbers;
		this.buildQueues = parent.buildQueues;
		this.nexusEnergy = parent.nexusEnergy;
		this.goal = parent.goal;
		
		this.executeOperation(operation);
		
		goalComplete = true;
		
		for (Entry<String,Integer> unitGoal : goal.entrySet()){
			if (unitGoal.getValue() != unitNumbers.get(unitGoal.getKey())){
				goalComplete = false;
				break;
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
			
			if (output != null){
				output.setText(this.buildOrder.toString());				
			}
			
			/**
			 * Since a solution has been found, the next solution should be faster.
			 */
			TimeState.maxTime = time - 1;
		}
	}
	
	/**
	 * Uses the current game state to see what operations can be performed.
	 * @return the list of possible Operations.
	 */
	private ArrayList<Operation> getPossibleOperations() {
		ArrayList<Operation> ops = new ArrayList<>();
		
		//Do nothing
		ops.add(new Operation("wait", ""));
		
		//Assign workers to minerals and gas
		if (Heuristics.needMoreGas(this) && probesOnGas < unitNumbers.get("Assimilator")*3 && Heuristics.getGas(this)) {
			ops.add(new Operation("assign", "gas"));
		} else if (probesOnGas > 0){
			ops.add(new Operation("assign", "minerals"));
		}
		
		//Change gateways into warp gates if they are idle
		if (numberOfWarpgates < unitNumbers.get("Gateway") && unitNumbers.get("Warp Gate") == 1) {
			
			int gatewaysBuilding = 0;

			for (Build gatewayBuild : buildQueues.get("Gateway")){
				if ( !(gatewayBuild instanceof WarpgateBuild)){
					gatewaysBuilding++;
				}
			}
			if (gatewaysBuilding < (unitNumbers.get("Gateway") - numberOfWarpgates - gatewayTransformations.size() ) ){
				for (int i=0; i<20; i++) {
					ops.add(new Operation("convert", "Gateway"));		
				}
			}
		}
		
		//Chronoboost
		for (Double energy: nexusEnergy){
			if (energy >= Datasheet.CHRONOBOOST_COST){
				for (Entry<String,BuildOrders> entry : buildQueues.entrySet()){
					for (Build build : entry.getValue()){
						if ((UnitIs.Unit(build.nameOfUnit) && UnitIs.Building(Datasheet.getBuiltFrom(build.nameOfUnit)) || (UnitIs.Upgrade(build.nameOfUnit))) 
								&& !build.isChronoboosted){
							if (build instanceof WarpgateBuild){
								if (((WarpgateBuild)build).hasProducedUnit){
									ops.add(new Operation("chronoboost", build.nameOfUnit));
								}
							} else {
								if (build.buildTime - build.time > Datasheet.CHRONOBOST_MIN_TIME){
									ops.add(new Operation("chronoboost", build.nameOfUnit));
								}
							}
						}
					}
				}
			}
		}
		
		//Build units and buildings
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
					if (Heuristics.moreProbes(this) || goal.containsKey(unitName)){
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
						} else if (UnitIs.Interceptor(unitName)){
							if (unitNumbers.get("Carrier")*8 > unitNumbers.get("Interceptor")){
								ops.add(new Operation("build", unitName));
							}
						} else {
							ops.add(new Operation("build", unitName));	
						}
					}
				}
				
				if (UnitIs.Carrier(unitName) && goal.containsKey("Interceptor")){
					if (goal.get("Interceptor") > getTotalNumber("Carrier")*8){
						ops.add(new Operation("build", unitName));
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
				
				if (UnitIs.Warpgate(unitName)) {
					if (!goal.containsKey(unitName) && Heuristics.worthWarpgate(this)) {
						goal.put("Warp Gate", 1);
					}
				}

			}
		}
		
		return ops;
	}
	
	/**
	 * Pass time in the simulation and execute the given Operation.
	 * @param operation the Operation to be executed.
	 */
	private void executeOperation(Operation operation) {
		//Pass time
		
		addResources();

		//Increment nexus energies.
		for (int i = 0; i < nexusEnergy.size(); i++){
			if (nexusEnergy.get(i) >= Datasheet.MAX_ENERGY_IN_NEXUS) {
				//do nothing as energy reached the maximum in the nexus.
			} else {
				nexusEnergy.set(i, nexusEnergy.get(i) + 1);
			}
		}

		for (int i = 0; i < gatewayTransformations.size(); i++){
			gatewayTransformations.set(i, gatewayTransformations.get(i) + 1);
		}
		
		//Increment gateway transformations.
		for (Iterator<Integer> iterator = gatewayTransformations.iterator(); iterator.hasNext();){
			Integer i = iterator.next();
			if (i >= Datasheet.WARPGATE_TRANFORMATION_TIME){
				this.numberOfWarpgates++;
				iterator.remove();
			}
		}
		
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
					} else if (unit.equals("Carrier")){
						unitNumbers.replace("Interceptor", unitNumbers.get("Interceptor") + 4);
					}
					addToMaxSupply(unit);
				}
			}
			buildOrder.removeCompleted();
		}

		switch (operation.getVerb()){
		case "wait":
			//wait...
			break;
		case "build":
			//Morphing Archons
			if (operation.getNoun().equals("Archon") && Heuristics.enoughTemplarToMakeArchon(this)) {
				buildOrder.append(getTimeStamp() + " " + operation.getNoun() + " " + supply + "/" + maxSupply + "\n");
				
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
			
			//Building units
			for (UnitData data : Datasheet.unitData) {
				if (operation.getNoun().equals(data.getName())){
					buildOrder.append(getTimeStamp() + " " + data.getName() + " " + supply + "/" + maxSupply + "\n");
					this.minerals -= Datasheet.getMineralCost(operation.getNoun());
					this.gas -= Datasheet.getGasCost(operation.getNoun());
					this.buildQueues.get(Datasheet.getBuiltFrom(operation.getNoun())).add(new Build(operation.getNoun()));
					addToSupply(operation.getNoun());
					break;
				}
			}
			break;
		case "assign":
			switch (operation.getNoun()) {
			case "minerals":
				probesOnGas--;
				break;
			case "gas":
				probesOnGas++;
				break;
			}
			break;
		case "chronoboost":
			for (Entry<String,BuildOrders> buildQueue : buildQueues.entrySet()){
				for (Build build : buildQueue.getValue()){
					if (build.nameOfUnit.equals(operation.getNoun()) && build.buildTime - build.time > Datasheet.CHRONOBOST_MIN_TIME 
							&& !build.isChronoboosted){
						build.chronoboost();
						break;
					}
				}
			}
			for (int i = 0; i < nexusEnergy.size(); i++){
				if (nexusEnergy.get(i) >= Datasheet.CHRONOBOOST_COST){
					nexusEnergy.set(i, nexusEnergy.get(i) - Datasheet.CHRONOBOOST_COST);
					break;
				}
			}
			buildOrder.append(getTimeStamp() + " Chronoboost " + Datasheet.getBuiltFrom(operation.getNoun()) + " " + supply + "/" + maxSupply + "\n");
			break;
		case "convert":
			if (operation.getNoun().equals("Gateway")){
				buildOrder.append(getTimeStamp() + " Convert Gateway into Warp Gate " + supply + "/" + maxSupply + "\n");
				gatewayTransformations.add(new Integer(0));
			}
			break;
		case "warp":
			buildOrder.append(getTimeStamp() + " Warp in " + operation.getNoun() + " " + supply + "/" + maxSupply + "\n");
			this.minerals -= Datasheet.getMineralCost(operation.getNoun());
			this.gas -= Datasheet.getGasCost(operation.getNoun());
			this.buildQueues.get("Gateway").add(new WarpgateBuild(operation.getNoun()));
			
			addToSupply(operation.getNoun());
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
		for (Entry<String, BuildOrders> buildQueue : buildQueues.entrySet()){
			for (Build build : buildQueue.getValue()){
				if (UnitIs.Unit(build.nameOfUnit)){
					double unitCostRate = Datasheet.getMineralCost(build.nameOfUnit)/Datasheet.getBuildTime(build.nameOfUnit);
					if (build.isChronoboosted){
						unitCostRate *= 1.5;
					}
					spending += unitCostRate;
				}
			}
		}
		return spending;
	}
	
	public int getGasSpending(){
		int spending = 0;
		for (Entry<String, BuildOrders> buildQueue : buildQueues.entrySet()){
			for (Build build : buildQueue.getValue()){
				if (UnitIs.Unit(build.nameOfUnit)){
					double unitCostRate = Datasheet.getGasCost(build.nameOfUnit)/Datasheet.getBuildTime(build.nameOfUnit);
					if (build.isChronoboosted){
						unitCostRate *= 1.5;
					}
					spending += unitCostRate;
				}
			}
		}
		return spending;
	}
	
	private int getUnitNumberInBuildQueue(String name) {
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
	
	public int getTotalSupply(){
		return ( getSupply() + getSupplyInBuildQueues() );
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
		
	public synchronized static void setMaxTime(int time){
		TimeState.maxTime = time;
	}

	public synchronized static int getMaxTime() {
		return TimeState.maxTime;
	}
}

