package game;

import gameobjects.BuildOrder;
import gameobjects.Entity;
import gameobjects.GameObject;
import gameobjects.Builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

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
}
