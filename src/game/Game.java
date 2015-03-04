package game;

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
		}
		time++;
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
	
}
