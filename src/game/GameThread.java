package game;

import game.tree.TimeState;

import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

public class GameThread extends Thread {

	JTextPane targetOutput;
	HashMap<String, Integer> goal;
	boolean keepRunning;
	int searchedGames;
	JLabel counter;
	TimeState simulation;
	
	public GameThread(JTextPane targetOutput,JLabel counter, HashMap<String, Integer> goal){
		this.targetOutput = targetOutput;
		this.goal = goal;
		this.keepRunning = true;
		this.searchedGames = 0;
		this.counter = counter;
	}
	
	public synchronized void run(){
		while (keepRunning){
			simulation = new TimeState(targetOutput,goal);
			while (simulation != null && !simulation.isFinished() && simulation.getTime() < TimeState.MAX_TIME){
					simulation = simulation.next();
			}
			
			searchedGames++;
			
			if (searchedGames % 5 == 0){
				SwingUtilities.invokeLater(new Runnable() {
					public void run(){
						counter.setText("Searched " + searchedGames + " games.");				
					}
				});							
			}
		}
	}
	
	public void start(){
		TimeState.MAX_TIME = 22200;
		super.start();
	}

	public void askToStop() {
		keepRunning = false;
	}
}
