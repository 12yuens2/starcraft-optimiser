package tree;

import game.Datasheet;

public class Build {
	String nameOfUnit;
	double time;
	double buildTime;

	public Build(String nameOfUnit){
		this.nameOfUnit = nameOfUnit;
		this.time = 0.0;
		this.buildTime = Datasheet.getBuildTime(nameOfUnit);
	}
	
	/**
	 * Used when cloning.
	 * @see Build#deepClone()
	 */
	public Build(String nameOfUnit, double time){
		this.nameOfUnit = nameOfUnit;
		this.time = time;
		this.buildTime = Datasheet.getBuildTime(nameOfUnit);
	}
	
	public Build deepClone() {
		return new Build(this.nameOfUnit,this.time);
	}

	public void increment() {
		this.time++;
		
	}

	public double getTime() {
		return time;
	}
	
	public double getBuildTime() {
		return buildTime;
	}

	public void incrementChronoboost() {
		this.time = this.time + 1.5;
	}
}
