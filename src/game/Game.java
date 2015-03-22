package game;

import gameobjects.BuildOrder;
import gameobjects.Entity;
import gameobjects.GameObject;
import gameobjects.Builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.oracle.xmlns.internal.webservices.jaxws_databinding.ExistingAnnotationsType;

import logger.SCLogger;
import units.buildings.ExpansionNexus;
import units.buildings.Nexus;
import units.nexus.Probe;

public class Game {
	
	private double minerals = 50;
	private double gas = 0;
	private int supply = 0;
	private int maxSupply = 0;
	private int time = 0;
	public static final int SUPPLY_LIMIT = 200;
	
	private HashMap<Class,Integer> goal = new HashMap<>();
	
	
	ArrayList<GameObject> gameObjects  = new ArrayList<>();
	ArrayList<GameObject> tempGameObjects = new ArrayList<>();
	ArrayList<ExpansionNexus> bases = new ArrayList<>();
	ArrayList<LinkedList<BuildOrder>> buildQueues = new ArrayList<>();
	
	public Game(HashMap goal){
		
		this.goal = goal;
		
		//Initial buildings
		gameObjects.add(new ExpansionNexus(this));
		gameObjects.add(new Probe(this));
		gameObjects.add(new Probe(this));
		gameObjects.add(new Probe(this));
		gameObjects.add(new Probe(this));
		gameObjects.add(new Probe(this));
		gameObjects.add(new Probe(this));
	}
	
	public void passTime(){
		setBases();
		
		setBuildQueue();
		
		for (GameObject go : gameObjects){
			go.passTime();
			if( go instanceof Probe){
				//((Probe)go).build(new Probe(this));
			}		
		}
		for (GameObject tempObject : tempGameObjects){
			gameObjects.add(tempObject);
			System.out.println(this.getTimestamp() + " Made new unit " + tempObject);
		}
		tempGameObjects.clear();
		//printResources();
		
		//if everything is done, ripperini.	
		time++;
	}

	private void setBuildQueue() {
		buildQueues.clear();
		for (GameObject go: gameObjects){
			if (go instanceof Builder){
				buildQueues.add(((Builder)go).getQueue());
			}
		}
	}

	public ArrayList<LinkedList<BuildOrder>> getBuildQueue(){
		return buildQueues;
	}
	
	public void printResources() {
		System.out.println(this.minerals + " " + this.gas + " " + this.supply + "/" + this.maxSupply);
	}

	public int getTime() {
		return time;
	}
	
	public void setBases(){
		bases.clear();
		for (GameObject go : gameObjects){
			if (go instanceof ExpansionNexus){
				bases.add((ExpansionNexus) go);
			}
		}
	}
	
	public ArrayList<ExpansionNexus> getBases(){
		return bases;
	}

	public void addMinerals(double minerals) {
		this.minerals += minerals;
	//	SCLogger.log("New mineral count: " + this.minerals, SCLogger.LOG_PARAMS);
	}

	public void addGameObejct(Entity entity) {
		this.tempGameObjects.add(entity);
		this.checkNewUnit(entity.getClass());
	}

	public String getTimestamp(){
		return ""+ (time/60 >=10 ? time/60 : "0" + time/60 ) + ":" + (time%60 >= 10 ? time%60 : "0" + time%60) ;
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
	
	public int getMaxSupply(){
		return maxSupply;
	}

	public void addGas(int gas) {
		this.gas += gas;
	}

	public void addSupply(int supply) {
		this.supply += supply;
	}
	
	public double getIncome(int numberOfProbes) {
		double income = 0;
		
		int numberOfNexi = getNumberOf("ExpansionNexus");
		
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
		Iterator it = this.goal.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<Class, Integer> pair = (Map.Entry<Class,Integer>)it.next();
	        Class unitType = pair.getKey();
	        int numberofBuildingsOfUnit = getNumberOf(Datasheet.getBuiltFrom(unitType.getSimpleName()));
	        numberofBuildingsOfUnit = ( numberofBuildingsOfUnit > goal.get(unitType) ? goal.get(unitType) : numberofBuildingsOfUnit );
	        spending += numberofBuildingsOfUnit*Datasheet.getMineralCost(unitType.getSimpleName());
	        
	    }
		return spending;
	}
	
	public int getNumberOf(String unitName) {
		int count = 0;
		for (GameObject go : gameObjects) {
			if (go.getClass().getSimpleName().equals(unitName)) {
				count++;
			}
		}
		return count;
	}
	
	public void checkNewUnit(Class unitClass){
		if (goal.containsKey(unitClass)){
			Integer unitCount = goal.get(unitClass);
			if (unitCount > 0){
				goal.replace(unitClass, unitCount- 1);				
			}
		}
	}

	public boolean achievedGoal() {
		boolean goalMet = true;
		for (Integer i : goal.values()){
			if (i != 0) { goalMet = false; }
		}
		return goalMet;
	}
	
	public boolean goalInvolves(Class unitType){
		return goal.containsKey(unitType);
	}

	public boolean needsMore(Class unitType) {
		int unitsLeft = goal.get(unitType);
		boolean needsMore = false;
		for (LinkedList<BuildOrder> unitQueue : buildQueues){
			for (BuildOrder order : unitQueue){
				if (order.getUnit().getClass().equals(unitType)){
					unitsLeft--;
				}
			}
		}
		if (unitsLeft > 0){
			needsMore = true;
		}
		return needsMore;
	}
	 
	public double timeTakenToReachGoal(double income){
		double buildTime = 0;
		int buildCost = 0;
		HashSet<String> dependancies = new HashSet<>();
		ArrayList<Entity> buildings = new ArrayList<>();
		
		for (Entry<Class, Integer> unitGoal : this.goal.entrySet()){
			if (unitGoal.getValue() > 0){
				String nameOfUnit = unitGoal.getKey().getSimpleName();
				while (nameOfUnit != null && this.getNumberOf(Datasheet.getDependancy(nameOfUnit)) == 0) {
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

				nameOfUnit = unitGoal.getKey().getSimpleName();
				if (this.getNumberOf(Datasheet.getDependancy(nameOfUnit)) == 0){
					String dependancyName = Datasheet.getDependancy(nameOfUnit);
					if (dependancyName != null){
						buildTime+= Datasheet.getBuildTime(dependancyName);
						buildCost+= Datasheet.getMineralCost(dependancyName);						
					}
				}
				int numberOfBuildings = this.getNumberOf(Datasheet.getBuiltFrom(nameOfUnit));
				if (numberOfBuildings == 0){
					numberOfBuildings++;
					buildTime+= Datasheet.getBuildTime(Datasheet.getBuiltFrom(nameOfUnit));
					buildCost+= Datasheet.getMineralCost(Datasheet.getBuiltFrom(nameOfUnit));
				}
				buildTime+= Datasheet.getBuildTime(nameOfUnit)*unitGoal.getValue()/(1.0*numberOfBuildings);
				buildCost+= Datasheet.getMineralCost(nameOfUnit)*unitGoal.getValue();
			}
			System.out.println("income is : " + income + " buildcost : " + buildCost + " buildtime: " + buildTime);
			while (income < buildCost/buildTime){
				buildTime++;
			}
		}
		System.out.println("The bulid time at " + this.getTimestamp() + " is : " + buildTime + " costing " + buildCost + " minerals.");
		return buildTime;
	}
	
	public boolean canSupport(double income, String unitType){
		return (income - getSpending() > Datasheet.getMineralCost(unitType)/Datasheet.getBuildTime(unitType));
	}
	
	public boolean moreProbes () {
		return (timeTakenToReachGoal(getIncome(getNumberOf("Probe"))) > timeTakenToReachGoal(getIncome(getNumberOf("Probe") + 1)));
	}
}
