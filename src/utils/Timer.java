package utils;

import java.util.Date;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Utility class included for testing and optimisation.
 * Implements a FILO structure.
 */
public class Timer {
	private static LinkedList<Date> now = new LinkedList<>();
	private static LinkedList<String> operation = new LinkedList<>();
	
	public static boolean isLogging = false;
	
	/**
	 * Starts a Timer.
	 * @param s the Text to display in the console.
	 */
	public static void start(String s){
		if (isLogging){
			now.add( new Date() );
			operation.add( s );
			System.out.println("'" + operation.getLast() + "' starting...");
		}
	}
	
	/**
	 * Stops the most recent Timer started, displaying the time taken.
	 */
	public static void stop(){
		if (isLogging){
			try{
				long deltaT = (new Date().getTime() - now.removeLast().getTime());
				System.out.println("'" + operation.removeLast() + "' ended in " + deltaT + " milliseconds.");
			} catch (NullPointerException e){
				System.err.println("Timer was never started.");
			} catch (NoSuchElementException e){
				System.err.println("Timer was stopped too many times.");
			}			
		}
	}
	
}
