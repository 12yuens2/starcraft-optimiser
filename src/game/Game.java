package game;

import gameobjects.Entity;
import gameobjects.GameObject;

import java.util.ArrayList;

import logger.SCLogger;
import units.MineralPatch;
import units.Probe;
import units.nexus.ExpansionNexus;
import units.nexus.Nexus;

public class Game {
	
	private int minerals = 0;
	private int gas = 0;
	private int supply = 0;
	private int maxSupply = 0;
	private int time = 0;
	public static final int SUPPLY_LIMIT = 200;
	
	ArrayList<GameObject> gameObjects  = new ArrayList<>();
	ArrayList<GameObject> tempGameObjects = new ArrayList<>();
	ArrayList<ExpansionNexus> bases = new ArrayList<>();
	
	public Game(){

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
	
		for (GameObject go : gameObjects){
			go.passTime();
			if( go instanceof Probe){
				((Probe)go).build(new Probe(this));
			}		
		}
		for (GameObject tempObject : tempGameObjects){
			gameObjects.add(tempObject);
		//	SCLogger.log("Adding new " + tempObject + " to game.",SCLogger.LOG_PARAMS);
		}
		tempGameObjects.clear();
		printResources();
		time++;
	}

	private void printResources() {
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
		SCLogger.log("New mineral count: " + this.minerals, SCLogger.LOG_PARAMS);
	}

	public void addGameObejct(Entity entity) {
		this.tempGameObjects.add(entity);
	}

	public int getMinerals() {
		return minerals;
	}

	public int getGas() {
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
	
}
