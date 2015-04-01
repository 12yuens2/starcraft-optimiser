package game.tree;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * The collection of build orders for a specific type
 * of builder.
 */
public class BuildOrders extends ArrayList<Build> {
	
	public BuildOrders(){
		super();
	}
	
	public void increment(){
		for (Build build : this){
			build.increment();
		}
	}
	
	/**
	 * Finds all completed units of this type of builder.
	 * @return An array list with the name of each completed unit.
	 */
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
	
	/**
	 * Removes any completed builds from the queue, freeing more
	 * production space.
	 */
	public void removeCompleted(){
		for (Iterator<Build> iterator = this.iterator(); iterator.hasNext();){
		    Build build = iterator.next();
			if (build.isFinished()){
				iterator.remove();
			}
		}
	}
	
}
