package game;

import java.util.ArrayList;

public class Datasheet {

	public static ArrayList<UnitData> unitData;
	
	
	public static void init(){
		unitData = new ArrayList<>();

		//Buildings
		unitData.add(new UnitData("ExpansionNexus", null,"Probe", 400, 0, 0, 100));
		unitData.add(new UnitData("Pylon", null,"Probe", 100, 0, 0, 25));
		unitData.add(new UnitData("Assimilator", null,"Probe", 75, 0, 0, 30));
		unitData.add(new UnitData("Gateway", "Pylon", "Probe",150, 0, 0, 65));
		unitData.add(new UnitData("CyberneticsCore", "Gateway","Probe", 150, 0, 0, 50));
		unitData.add(new UnitData("RoboticsFacility", "CyberneticsCore","Probe", 200, 100, 0, 65));
		unitData.add(new UnitData("Stargate", "CyberneticsCore","Probe", 150, 150, 0, 60));
		unitData.add(new UnitData("Forge", "Pylon","Probe", 150, 0, 0, 45));
		unitData.add(new UnitData("TwilightCouncil", "CyberneticsCore","Probe", 150, 100, 0, 50));
		unitData.add(new UnitData("TemplarArchives", "TwilightCouncil","Probe", 150, 200, 0, 50));
		unitData.add(new UnitData("DarkShrine", "TwilightCouncil","Probe", 100, 250, 0, 100));
		unitData.add(new UnitData("RoboticsBay", "RoboticsFacility","Probe", 200, 200, 0, 65));
		unitData.add(new UnitData("FleetBeacon", "Stargate","Probe", 300, 200, 0, 60));
		
		//Units
		unitData.add(new UnitData("Probe", null, "Nexus", 50, 0, 1, 17));
		unitData.add(new UnitData("Zealot", null, "Gateway", 100, 0, 2, 38));
		unitData.add(new UnitData("Stalker", "CyberneticsCore", "Gateway", 125, 50, 2, 42));
		unitData.add(new UnitData("Sentry", null/*"CyberneticsCore"*/, "Gateway", 50, 100, 1, 37));
		unitData.add(new UnitData("Observer", null, "RoboticsFacility", 25, 75, 1, 40));
		unitData.add(new UnitData("Immortal", null, "RoboticsFacility", 250, 100, 4, 55));
		unitData.add(new UnitData("Phoenix", null, "Stargate", 150, 100, 2, 35));
		unitData.add(new UnitData("VoidRay", null, "Stargate", 250, 100, 3, 60));
		unitData.add(new UnitData("Oracle", null, "Stargate", 150, 150, 3, 50));
		unitData.add(new UnitData("WarpPrism", null, "RoboticsFacility", 200, 0, 2, 50));
		unitData.add(new UnitData("Colossus", "RoboticsBay", "RoboticsFacility", 300, 200, 6, 75));
		unitData.add(new UnitData("Tempest", "FleetBeacon", "Stargate", 300, 200, 4, 60));
		unitData.add(new UnitData("HighTemplar", "TemplarArchives", "Gateway", 50, 150, 2, 55));
		unitData.add(new UnitData("DarkTemplar", "DarkShrine", "Gateway", 125, 125, 2, 55));
		unitData.add(new UnitData("Carrier", "FleetBeacon", "Stargate", 350, 250, 6, 120));
		unitData.add(new UnitData("MothershipCore", "CyberneticsCore", "Nexus", 100, 100, 2, 30));
		unitData.add(new UnitData("Mothership", "FleetBeacon", "MothershipCore", 300, 300, 8, 100));

		//Upgrades
		unitData.add(new UnitData("WarpGate", null, "CyberneticsCore", 50, 50, 0, 160));
		unitData.add(new UnitData("GroundWeapons1", null, "Forge", 100, 100, 0, 160));
		unitData.add(new UnitData("GroundWeapons2", "GroundWeapons1", "Forge", 150, 150, 0, 190));
		unitData.add(new UnitData("GroundWeapons3", "GroundWeapons2", "Forge", 200, 200, 0, 220));
		unitData.add(new UnitData("GroundArmor1", null, "Forge", 100, 100, 0, 160));
		unitData.add(new UnitData("GroundArmor2", "GroundArmor1", "Forge", 150, 150, 0, 190));
		unitData.add(new UnitData("GroundArmor3", "GroundArmor2", "Forge", 200, 200, 0, 220));
		unitData.add(new UnitData("Shields1", null, "Forge", 150, 150, 0, 160));
		unitData.add(new UnitData("Shields2", "Shields1", "Forge", 225, 225, 0, 190));
		unitData.add(new UnitData("Shields3","Shields2", "Forge", 300, 300, 0, 220));
		unitData.add(new UnitData("AirWeapons1", null, "CyberneticsCore", 100, 100, 0, 160));
		unitData.add(new UnitData("AirWeapons2", "AirWeapons1", "CyberneticsCore", 175, 175, 0, 190));
		unitData.add(new UnitData("AirWeapons3", "AirWeapons2", "CyberneticsCore", 250, 250, 0, 220));
		unitData.add(new UnitData("AirArmor1", null, "CyberneticsCore", 150, 150, 0, 160));
		unitData.add(new UnitData("AirArmor2", "AirArmor1", "CyberneticsCore", 225, 225, 0, 190));
		unitData.add(new UnitData("AirArmor3", "AirArmor2", "CyberneticsCore", 300, 300, 0, 220));
		unitData.add(new UnitData("Charge", null, "TwilightCouncil", 200, 200, 0, 140));
		unitData.add(new UnitData("GraviticBoosters", null, "RoboticsBay", 100, 100, 0, 80));
		unitData.add(new UnitData("GraviticDrive", null,"RoboticsBay", 100, 100, 0, 80));
		unitData.add(new UnitData("AnionPulse-Crystal", null,"FleetBeacon", 150, 150, 0, 90));
		unitData.add(new UnitData("ExtendedThermalLance", null, "RoboticsBay", 200, 200, 0, 140));
		unitData.add(new UnitData("PsionicStorm", null,"TemplarArchives", 200, 200, 0, 110));
		unitData.add(new UnitData("Blink", null,"TwilightCouncil", 150, 150, 0, 170));
		unitData.add(new UnitData("GravitonCatapult", null,"FleetBeacon", 150, 150, 0, 80));
		
	}
	
	public static int getMineralCost(String unitType){
		return findName(unitType).getMineralCost();
	}
	
	public static int getGasCost (String unitType) {
		return findName(unitType).getGasCost();
	}
	
	public static int getSupplyCost(String unitType){
		return findName(unitType).getSupplyCost();
	}

	public static int getBuildTime(String unitType) {
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
