package game;

import gameobjects.GameObject;

import java.util.ArrayList;
import java.util.HashMap;

import units.buildings.ExpansionNexus;
import units.gateway.Zealot;
import units.nexus.Probe;

public class Starcraft {
	public static void main(String[] args) {

		HashMap<String,Integer> goal = new HashMap<>();
		Datasheet.init();
		
//		goal.put(Probe.class, 10);
		goal.put("Zealot", 1);

		
		Game testGame = new Game(goal);

		while (!testGame.achievedGoal() && testGame.getTime() < 10000000){
			testGame.passTime();
		}
		if (!testGame.achievedGoal()){
			System.out.println("Did not achieve goal.");
		} else {
			System.out.println("Goal achieved.");
		}
	}
}
