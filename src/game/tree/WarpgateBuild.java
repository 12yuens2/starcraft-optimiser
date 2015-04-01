package game.tree;

import data.Datasheet;

/**
 * The Warping in of a unit.
 * The warping in is considered finished when the cooldown 
 * of the warp gate expires.
 */
public class WarpgateBuild extends Build{

	double cooldownTime;
	boolean hasProducedUnit;
	
	public WarpgateBuild(String nameOfUnit) {
		super(nameOfUnit);
		this.buildTime = Datasheet.WARPIN_TIME;
		this.cooldownTime = Datasheet.getWarpgateCooldown(nameOfUnit);
		this.hasProducedUnit = false;
	}

	public void setProducedUnit() {
		this.hasProducedUnit = true;
	}

	/**
	 * Checks if the warp gate has finished warping in a unit, but 
	 * might still have a cooldown.
	 */
	public boolean hasProducedUnit(){
		return (this.time >= this.buildTime);
	}
	
	public boolean isFinished(){
		return (this.time > this.cooldownTime + this.buildTime);
	}
}
