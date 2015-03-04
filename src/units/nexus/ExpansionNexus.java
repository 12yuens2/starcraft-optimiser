package units.nexus;

import game.Game;

import java.util.ArrayList;

import logger.SCLogger;
import units.MineralPatch;
import units.VespeneGeyser;

public class ExpansionNexus extends Nexus {

	private ArrayList<MineralPatch> mineralPatches = new ArrayList<>();
	private ArrayList<VespeneGeyser> vespeneGeysers = new ArrayList<>();
	
	public ExpansionNexus(Game game){
		super(game);
		mineralPatches.add(new MineralPatch());
		mineralPatches.add(new MineralPatch());
		mineralPatches.add(new MineralPatch());
		mineralPatches.add(new MineralPatch());
		mineralPatches.add(new MineralPatch());
		mineralPatches.add(new MineralPatch());
		mineralPatches.add(new MineralPatch());
		mineralPatches.add(new MineralPatch());
		vespeneGeysers.add(new VespeneGeyser());
		vespeneGeysers.add(new VespeneGeyser());
	}
	
	@Override
	public void passTime() {
		for (MineralPatch patch : mineralPatches){
			if (patch.getMinerals() == 0){
				SCLogger.log("Deleting Mineral patch",SCLogger.LOG_DEBUG);
				patch.releaseProbes();
				mineralPatches.remove(patch);
			}
		}
	}

	public ArrayList<MineralPatch> getMinerals(){
		return mineralPatches;
	}
	
	public ArrayList<VespeneGeyser> getGeysers(){
		return vespeneGeysers;
	}
}
