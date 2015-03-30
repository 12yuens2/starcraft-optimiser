package game;

import game.tree.TimeState;

import java.util.HashMap;

import javax.swing.JTextPane;

public class GameThread extends Thread {

	JTextPane targetOutput;
	HashMap<String, Integer> goal;
	

	
	public GameThread(JTextPane targetOutput, HashMap<String, Integer> goal){
		this.targetOutput = targetOutput;
		this.goal = goal;
	}
	
	public void run(){
		TimeState gameTree = new TimeState(targetOutput,goal);			
		yield();
	}
	
	public void start(){
		TimeState.MAX_TIME = 2200;
	}
}
