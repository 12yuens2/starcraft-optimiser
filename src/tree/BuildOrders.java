package tree;

import java.util.ArrayList;

public class BuildOrders extends ArrayList<Build> {
	
	public BuildOrders deepClone(){
		BuildOrders clone = new BuildOrders();
		if (!this.isEmpty()){
			for (Build build : this){
				clone.add(build.deepClone());
			}			
		}
		return clone;
	}
}
