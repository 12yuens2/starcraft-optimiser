package game;

public class Chronoboost {
	public static final int MAX_TIME = 20;
	
	private String name;
	private double time;
	private boolean used;
	
	public Chronoboost(String name) {
		this.name = name;
		this.time = 0;
		this.used = false;
	}

	public String getName() {
		return name;
	}

	public double getTime() {
		return time;
	}

	public void incrementTime() {
		this.time++;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed() {
		this.used = true;
	}
	
	public void setUnused() {
		this.used = false;
	}
}
