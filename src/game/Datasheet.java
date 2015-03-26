package game;

import gameobjects.Entity;

import java.util.ArrayList;

import units.buildings.*;
import units.gateway.Zealot;
import units.nexus.Probe;

public class Datasheet {

	public static final int MAX_SUPPLY = 200;
	public static final double MINS_PER_NEXUS = 9000;
	public static final double GAS_PER_NEXUS = 5000;
	
	public static final double MINS_PER_MINUTE = 41, THIRD_MINS_PER_MINUTE = 20;
	public static final double MINS_PER_SECOND = MINS_PER_MINUTE/60.0;
	public static final double THIRD_MINS_PER_SECOND = THIRD_MINS_PER_MINUTE/60.0;
	
	public static final double GAS_PER_MINUTE = 38;
	public static final double GAS_PER_SECOND = GAS_PER_MINUTE/60.0;
	
	
	public static ArrayList<UnitData> unitData;
	
	
	public static void init(){
		unitData = new ArrayList<>();

		//Buildings
		unitData.add(new UnitData("Nexus", null,"Probe", 400, 0, 0, 100));
		unitData.add(new UnitData("Pylon", null,"Probe", 100, 0, 0, 25));
		unitData.add(new UnitData("Assimilator", null,"Probe", 75, 0, 0, 30));
		unitData.add(new UnitData("Gateway", "Pylon", "Probe", 150, 0, 0, 65));
		unitData.add(new UnitData("Cybernetics Core", "Gateway","Probe", 150, 0, 0, 50));
		unitData.add(new UnitData("Robotics Facility", "Cybernetics Core","Probe", 200, 100, 0, 65));
		unitData.add(new UnitData("Stargate", "Cybernetics Core","Probe", 150, 150, 0, 60));
		unitData.add(new UnitData("Forge", "Pylon","Probe", 150, 0, 0, 45));
		unitData.add(new UnitData("Twilight Council", "Cybernetics Core","Probe", 150, 100, 0, 50));
		unitData.add(new UnitData("Templar Archives", "Twilight Council","Probe", 150, 200, 0, 50));
		unitData.add(new UnitData("Dark Shrine", "Twilight Council","Probe", 100, 250, 0, 100));
		unitData.add(new UnitData("Robotics Bay", "Robotics Facility","Probe", 200, 200, 0, 65));
		unitData.add(new UnitData("Fleet Beacon", "Stargate","Probe", 300, 200, 0, 60));
		
		//Units
		unitData.add(new UnitData("Probe", null, "Nexus", 50, 0, 1, 17));
		unitData.add(new UnitData("Zealot", null, "Gateway", 100, 0, 2, 38));
		unitData.add(new UnitData("Stalker", "Cybernetics Core", "Gateway", 125, 50, 2, 42));
		unitData.add(new UnitData("Sentry", null, "Gateway", 50, 100, 1, 37));
		unitData.add(new UnitData("Observer", null, "Robotics Facility", 25, 75, 1, 40));
		unitData.add(new UnitData("Immortal", null, "Robotics Facility", 250, 100, 4, 55));
		unitData.add(new UnitData("Phoenix", null, "Stargate", 150, 100, 2, 35));
		unitData.add(new UnitData("Void Ray", null, "Stargate", 250, 100, 3, 60));
		unitData.add(new UnitData("Oracle", null, "Stargate", 150, 150, 3, 50));
		unitData.add(new UnitData("Warp Prism", null, "Robotics Facility", 200, 0, 2, 50));
		unitData.add(new UnitData("Colossus", "Robotics Bay", "Robotics Facility", 300, 200, 6, 75));
		unitData.add(new UnitData("Tempest", "Fleet Beacon", "Stargate", 300, 200, 4, 60));
		unitData.add(new UnitData("High Templar", "Templar Archives", "Gateway", 50, 150, 2, 55));
		unitData.add(new UnitData("Dark Templar", "Dark Shrine", "Gateway", 125, 125, 2, 55));
		unitData.add(new UnitData("Carrier", "Fleet Beacon", "Stargate", 350, 250, 6, 120));
		unitData.add(new UnitData("Mothership Core", "Cybernetics Core", "Nexus", 100, 100, 2, 30));
		unitData.add(new UnitData("Mothership", "Fleet Beacon", "Mothership Core", 300, 300, 8, 100));

		//Upgrades
		unitData.add(new UnitData("Warp Gate", null, "Cybernetics Core", 50, 50, 0, 160));
		unitData.add(new UnitData("Ground Weapons 1", null, "Forge", 100, 100, 0, 160));
		unitData.add(new UnitData("Ground Weapons 2", "Ground Weapons 1", "Forge", 150, 150, 0, 190));
		unitData.add(new UnitData("Ground Weapons 3", "Ground Weapons 2", "Forge", 200, 200, 0, 220));
		unitData.add(new UnitData("Ground Armor 1", null, "Forge", 100, 100, 0, 160));
		unitData.add(new UnitData("Ground Armor 2", "Ground Armor 1", "Forge", 150, 150, 0, 190));
		unitData.add(new UnitData("Ground Armor 3", "Ground Armor 2", "Forge", 200, 200, 0, 220));
		unitData.add(new UnitData("Shields 1", null, "Forge", 150, 150, 0, 160));
		unitData.add(new UnitData("Shields 2", "Shields 1", "Forge", 225, 225, 0, 190));
		unitData.add(new UnitData("Shields 3","Shields 2", "Forge", 300, 300, 0, 220));
		unitData.add(new UnitData("Air Weapons 1", null, "Cybernetics Core", 100, 100, 0, 160));
		unitData.add(new UnitData("Air Weapons 2", "Air Weapons 1", "Cybernetics Core", 175, 175, 0, 190));
		unitData.add(new UnitData("Air Weapons 3", "Air Weapons 2", "Cybernetics Core", 250, 250, 0, 220));
		unitData.add(new UnitData("Air Armor 1", null, "Cybernetics Core", 150, 150, 0, 160));
		unitData.add(new UnitData("Air Armor 2", "Air Armor 1", "Cybernetics Core", 225, 225, 0, 190));
		unitData.add(new UnitData("Air Armor 3", "Air Armor 2", "Cybernetics Core", 300, 300, 0, 220));
		unitData.add(new UnitData("Charge", null, "Twilight Council", 200, 200, 0, 140));
		unitData.add(new UnitData("Gravitic Boosters", null, "Robotics Bay", 100, 100, 0, 80));
		unitData.add(new UnitData("Gravitic Drive", null,"Robotics Bay", 100, 100, 0, 80));
		unitData.add(new UnitData("Anion Pulse-Crystal", null,"Fleet Beacon", 150, 150, 0, 90));
		unitData.add(new UnitData("Extended Thermal Lance", null, "Robotics Bay", 200, 200, 0, 140));
		unitData.add(new UnitData("Psionic Storm", null,"Templar Archives", 200, 200, 0, 110));
		unitData.add(new UnitData("Blink", null,"Twilight Council", 150, 150, 0, 170));
		unitData.add(new UnitData("Graviton Catapult", null,"Fleet Beacon", 150, 150, 0, 80));
		
	}
	
	public static double getMineralCost(String unitType){
		return findName(unitType).getMineralCost();
	}
	
	public static double getGasCost (String unitType) {
		return findName(unitType).getGasCost();
	}
	
	public static int getSupplyCost(String unitType){
		return findName(unitType).getSupplyCost();
	}

	public static double getBuildTime(String unitType) {
		return findName(unitType).getBuildTime();
	}
	
	public static String getDependancy(String unitType){
		return findName(unitType).getDependancy();
	}
	
	public static String getBuiltFrom(String unitType){
		return findName(unitType).getBuiltFrom();
	}
	
	public static UnitData findName(String name) {
		for (UnitData data : unitData){
			if (data.getName().equals(name)){
				return data;
			}
		}
		return null;
	}

}
