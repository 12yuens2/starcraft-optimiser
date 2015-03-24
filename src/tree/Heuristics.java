package tree;

import game.Datasheet;
import game.UnitData;

import java.util.ArrayList;

public class Heuristics {

	public static void makeProductionBuildings(ArrayList<Operation> ops, UnitData data, TimeState ts) {
		
		//build production building if unit in goal builds from that building
		if (ts.unitNumbers.get(Datasheet.getBuiltFrom(data.getName())) == 0) {
			ops.add(new Operation("build", Datasheet.getBuiltFrom(data.getName())));
		}
		
	}

	
}
