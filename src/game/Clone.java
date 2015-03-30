package game;

import game.tree.Build;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Clone {

	public static HashMap<String, Integer> unitNumbers(HashMap<String, Integer> unitNumbers) {
		HashMap<String, Integer>  newNumbers = new HashMap<>();
		for (Entry<String,Integer> entry : unitNumbers.entrySet() ){
			newNumbers.put(entry.getKey(), entry.getValue());
		}
		return newNumbers;
	}
	
	public static HashMap<String, ArrayList<Build>> buildQueues(HashMap<String, ArrayList<Build>> buildQueues) {
		HashMap<String, ArrayList<Build>> newQueue = new HashMap<>();
		for (Entry<String,ArrayList<Build>> entry : buildQueues.entrySet()){
			ArrayList<Build> newBuild = new ArrayList<>();
			for (Build b : entry.getValue()){
			//	newBuild.add(b.deepClone());
			}
			newQueue.put(entry.getKey(), newBuild);
		}
		return newQueue;
		
	}
	
	public static HashMap<String, Integer> goal(HashMap<String, Integer> goal) {
		HashMap<String,Integer> newGoal = new HashMap<>();
		for (Entry<String,Integer> entry : goal.entrySet()){
			newGoal.put(entry.getKey(), entry.getValue());
		}
		return newGoal;
	}

	
	
}
