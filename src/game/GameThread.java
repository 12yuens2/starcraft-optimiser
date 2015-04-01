package game;

import game.tree.TimeState;

import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

/**
 * Thread that controls the simulation.
 */
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
	
	public void run(){
		while (keepRunning){
			simulation = new TimeState(targetOutput,goal);
			while (simulation != null && !simulation.isFinished() && simulation.getTime() < TimeState.maxTime){
					simulation = simulation.next();
			}

			//Update GUI on the AWT thread.
			SwingUtilities.invokeLater(new Runnable() {
				public void run(){
					counter.setText("Searched " + ++searchedGames + " games.");				
				}
			});	
		}
	}
	
	public void start(){
		//Reset the max time for the build.
		TimeState.maxTime = 2000;
		super.start();
	}

	public void askToStop() {
		keepRunning = false;
	}
}
