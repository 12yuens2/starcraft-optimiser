package tree;

import game.Datasheet;

import java.util.ArrayList;
import java.util.Iterator;

public class BuildOrders extends ArrayList<Build> {
	
	public BuildOrders(){
		super();
	}
	
	public void increment(){
		for (Build build : this){
			build.increment();
		}
	}
		
	public ArrayList<String> getProducedUnits(){
		ArrayList<String> completedUnits = new ArrayList<>();
		for (Build build : this){
			if (build.hasProducedUnit()){
				if (build instanceof WarpgateBuild){
					if (((WarpgateBuild)build).hasProducedUnit == false){
						((WarpgateBuild)build).setProducedUnit();
						completedUnits.add(build.nameOfUnit);
					}
				} else {
					completedUnits.add(build.nameOfUnit);
				}
			}
		}
		return completedUnits;
	}
	
	public void removeCompleted(){
		for (Iterator<Build> iterator = this.iterator(); iterator.hasNext();){
		    Build build = iterator.next();
			if (build.isFinished()){
				iterator.remove();
			}
		}
	}
	
}
