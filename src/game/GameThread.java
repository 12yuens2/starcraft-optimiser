package game;

import game.tree.TimeState;

import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import data.UnitNumbers;
import utils.Timer;

/**
 * Thread that controls the simulation.
 */
public class GameThread extends Thread {

	JTextPane targetOutput;
	UnitNumbers goal;
	boolean keepRunning;
	int searchedGames;
	JLabel counter;
	TimeState simulation;
	
	public GameThread(JTextPane targetOutput,JLabel counter, UnitNumbers goal){
		this.targetOutput = targetOutput;
		this.goal = goal;
		this.keepRunning = true;
		this.searchedGames = 0;
		this.counter = counter;
	}
	
	public void run(){
		while (keepRunning){
			simulation = new TimeState(targetOutput,goal);

			Timer.start("Playing single game");
			while (simulation != null && !simulation.isFinished() && simulation.getTime() < TimeState.getMaxTime()){
					simulation = simulation.next();
			}
			Timer.stop();
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
		TimeState.setMaxTime(2000);
		super.start();
	}

	public void askToStop() {
		keepRunning = false;
	}
}
