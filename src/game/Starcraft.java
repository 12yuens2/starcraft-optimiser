package game;

import gameobjects.GameObject;

import java.util.ArrayList;
import java.util.HashMap;

import units.Probe;
import units.nexus.ExpansionNexus;

public class Starcraft {
public static void main(String[] args) {
		HashMap<Class,Integer> goal = new HashMap<>();
		goal.put(Probe.class, 0);
	
		Game testGame = new Game(goal);
		
		while (testGame.getTime() < 10000000){
			testGame.passTime();
		}
	}
}
