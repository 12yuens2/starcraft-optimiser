package tree;

import game.Datasheet;

import java.util.HashMap;

public class TreeTest {

	
	public static void main(String[] args) {
		
		final int MAX_NUMBER_OF_TRIALS = 1000000;
		int numberOfTrials = 0;
		
		HashMap<String,Integer> goal = new HashMap<>();
		Datasheet.init();

		goal.put("Zealot", 10);
		
		while (numberOfTrials < MAX_NUMBER_OF_TRIALS){
			TimeState gameTree = new TimeState(goal);	
			numberOfTrials++;
			if (numberOfTrials % 100 == 0){
	//			System.out.println(numberOfTrials);
			}
		}

	}
	
}
