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
	
	public GameThread(JTextPane targetOutput,JLabel counter, HashMap<String, Integer> goal){
		this.targetOutput = targetOutput;
		this.goal = goal;
		this.keepRunning = true;
		this.searchedGames = 0;
		this.counter = counter;
	}
	
	public synchronized void run(){
		while (keepRunning){
			new TimeState(targetOutput,goal);
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
		TimeState.MAX_TIME = 1800;
		super.start();
	}

	public void askToStop() {
		keepRunning = false;
	}
}
