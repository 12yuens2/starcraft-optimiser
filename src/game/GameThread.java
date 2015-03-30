package game;

import game.tree.TimeState;

import java.util.HashMap;

import javax.swing.JTextPane;

public class GameThread extends Thread {

	JTextPane targetOutput;
	HashMap<String, Integer> goal;
	boolean keepRunning;
	
	public GameThread(JTextPane targetOutput, HashMap<String, Integer> goal){
		this.targetOutput = targetOutput;
		this.goal = goal;
		this.keepRunning = true;
	}
	
	public synchronized void run(){
		while (keepRunning){
			new TimeState(targetOutput,goal);
		}
	}
	
	public void start(){
		TimeState.MAX_TIME = 2200;
		super.start();
	}

	public void askToStop() {
		keepRunning = false;
	}
}
