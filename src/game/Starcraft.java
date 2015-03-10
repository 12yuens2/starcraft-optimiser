package game;

import gameobjects.GameObject;

import java.util.ArrayList;
import java.util.HashMap;

import units.Probe;
import units.nexus.ExpansionNexus;

public class Starcraft {
	public static void main(String[] args) {

		HashMap<Class,Integer> goal = new HashMap<>();
		goal.put(Probe.class, 4);

		Game testGame = new Game(goal);

		while (!testGame.achievedGoal() && testGame.getTime() < 1000000){
			testGame.passTime();
		}
	}
}
