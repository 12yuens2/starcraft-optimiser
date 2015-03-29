package tree;

import game.Datasheet;

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
	
	public boolean hasProducedUnit(){
		return (this.time >= this.buildTime);
	}
	
	public boolean isFinished(){
		return (this.time > this.cooldownTime + this.buildTime);
	}
}
