package tree;

import game.Datasheet;

import java.util.HashMap;

public class TreeTest {

	
	public static void main(String[] args) {
		
		final int MAX_NUMBER_OF_TRIALS = 1000000000;
		int numberOfTrials = 0;
		
		TimeState.MAX_TIME = 2000;
		
		HashMap<String,Integer> goal = new HashMap<>();
		Datasheet.init();

		goal.put("Zealot", 4);
		//goal.put("Stalker", 1);
		//goal.put("Immortal", 1);
		//goal.put("Sentry", 2);
		//goal.put("Observer", 1);
		//goal.put("Carrier", 3);
		//goal.put("Blink",1);
		//goal.put("Dark Templar", 2);
		//goal.put("Ground Armor 3", 1);
		
		while (numberOfTrials < MAX_NUMBER_OF_TRIALS){
			TimeState gameTree = new TimeState(goal);	
			numberOfTrials++;
			if (numberOfTrials % 100 == 0){
				//System.out.println(numberOfTrials);
			}
		}

	}
	
}
