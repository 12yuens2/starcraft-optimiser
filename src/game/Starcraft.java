package game;

import gameobjects.GameObject;

import java.util.ArrayList;
import java.util.HashMap;

import units.buildings.ExpansionNexus;
import units.gateway.Zealot;
import units.nexus.Probe;

public class Starcraft {
	public static void main(String[] args) {

		HashMap<Class,Integer> goal = new HashMap<>();
		Datasheet.init();
		
//		goal.put(Probe.class, 10);
		goal.put(Zealot.class, 2);

		
		Game testGame = new Game(goal);

		while (!testGame.achievedGoal() && testGame.getTime() < 100000){
			testGame.passTime();
		}
		if (!testGame.achievedGoal()){
			System.out.println("Did not achieve goal.");
		}
	}
}
