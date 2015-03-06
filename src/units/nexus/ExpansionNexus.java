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
		mineralPatches.add(new MineralPatch(game));
		mineralPatches.add(new MineralPatch(game));
		mineralPatches.add(new MineralPatch(game));
		mineralPatches.add(new MineralPatch(game));
		mineralPatches.add(new MineralPatch(game));
		mineralPatches.add(new MineralPatch(game));
		mineralPatches.add(new MineralPatch(game));
		mineralPatches.add(new MineralPatch(game));
		vespeneGeysers.add(new VespeneGeyser(game));
		vespeneGeysers.add(new VespeneGeyser(game));
	}
	
	@Override
	public void passTime() {
		for (MineralPatch patch : mineralPatches){
			patch.passTime();
			if (patch.getMinerals() < 0){
				patch.releaseProbes();
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
