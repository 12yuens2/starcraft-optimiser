package tree;

public class Build {
	String nameOfUnit;
	int time;
	int buildTime;

	public Build(String nameOfUnit, int buildTime){
		this.nameOfUnit = nameOfUnit;
		this.time = 0;
		this.buildTime = buildTime;
	}
	
	public Build(String nameOfUnit,int time, int buildTime){
		this.nameOfUnit = nameOfUnit;
		this.time = 0;
		this.buildTime = buildTime;
	}
	
	public Build deepClone() {
		return new Build(this.nameOfUnit,this.time,this.buildTime);
	}
}
