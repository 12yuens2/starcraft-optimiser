package game;

/**
 * Helper class that is used for unit comparisons.
 */
public class UnitIs {
	
	public static boolean Probe(String unitName){
		return unitName.equals("Probe");
	}

	public static boolean Assimilator(String unitName){
		return unitName.equals("Assimilator");
	}
	public static boolean Pylon(String unitName) {
		return unitName.equals("Pylon");
	}

	public static boolean Nexus(String unitName) {
		return unitName.equals("Nexus");
	}
	
	public static boolean Gateway(String unitName) {
		return unitName.equals("Gateway");
	}
	
	public static boolean Archon(String unitName) {
		return unitName.equals("Archon");
	}
	
	public static boolean HighTemplar(String unitName) {
		return unitName.equals("High Templar");
	}
	
	public static boolean Warpgate(String unitName) {
		return unitName.equals("Warp Gate");
	}
	
	public static boolean Unit(String unitName){
		return (
				unitName.equals("Probe") || unitName.equals("Zealot") || unitName.equals("Stalker") ||
				unitName.equals("Observer") || unitName.equals("Sentry") || unitName.equals("High Templar") ||
				unitName.equals("Immortal") || unitName.equals("Phoenix") || unitName.equals("Void Ray") ||
				unitName.equals("Oracle") || unitName.equals("Warp Prism") || unitName.equals("Colossus") ||
				unitName.equals("Tempest") || unitName.equals("Dark Templar") || unitName.equals("Archon") ||
				unitName.equals("Carrier") || unitName.equals("Interceptor") || unitName.equals("Mothership Core") ||
				unitName.equals("Mothership")
				);
	}
	
	public static boolean Builder(String unitName){
		return (
				unitName.equals("Nexus") || unitName.equals("Gateway") || unitName.equals("Robotics Facility") ||
				unitName.equals("Stargate") || unitName.equals("Mothership Core") || unitName.equals("High Templar") ||
				unitName.equals("Probe") || unitName.equals("Carrier") || unitName.equals("Twilight Council") ||
				unitName.equals("Templar Archives") || unitName.equals("Cybernetice Core") || unitName.equals("Forge") ||
				unitName.equals("Fleet Beacon") || unitName.equals("Robotics Bay") || unitName.equals("High Templar") ||
				unitName.equals("Dark Templar")
		);
	}
	
	public static boolean Building(String unitName) {
		return (
				unitName.equals("Nexus") || unitName.equals("Gateway") || unitName.equals("Robotics Facility") ||
				unitName.equals("Stargate") || unitName.equals("Twilight Council") || unitName.equals("Templar Archives") || 
				unitName.equals("Cybernetice Core") || unitName.equals("Forge") ||unitName.equals("Fleet Beacon") || 
				unitName.equals("Robotics Bay")
		);
	}
	
	public static boolean Upgrade(String unitName){
		return (
				unitName.equals("Warp Gate") || unitName.equals("Ground Weapons 1") || unitName.equals("Ground Weapons 2") ||
				unitName.equals("Ground Weapons 3") || unitName.equals("Ground Armor 1") || unitName.equals("Ground Armor 2") ||
				unitName.equals("Ground Armor 3") || unitName.equals("Shields 1") || unitName.equals("Shields 2") ||
				unitName.equals("Shields 3") || unitName.equals("Air Weapons 1") || unitName.equals("Air Weapons 2") ||
				unitName.equals("Air Weapons 3") || unitName.equals("Air Armor 1") || unitName.equals("Air Armor 2") ||
				unitName.equals("Air Armor 3") || unitName.equals("Charge") || unitName.equals("Gravitic Boosters") ||
				unitName.equals("Gravitic Drive") || unitName.equals("Anion Pulse-Crystal") || unitName.equals("Extended Thermal Lance") ||
				unitName.equals("Psionic Storm") || unitName.equals("Blink") || unitName.equals("Graviton Catapult")
		);
	}
	
	public static boolean Dependancy(String unitName) {
		return (
				unitName.equals("Pylon") || unitName.equals("Gateway") || unitName.equals("Cybernetics Core") ||
				unitName.equals("Twilight Council") || unitName.equals("Robotics Facility") ||
				unitName.equals("Stargate") || unitName.equals("Forge") || unitName.equals("Robotics Bay") ||
				unitName.equals("Fleet Beacon") || unitName.equals("Templar Archives") || unitName.equals("Dark Shrine") ||
				unitName.equals("Ground Weapons 1") || unitName.equals("Ground Weapons 2") ||
				unitName.equals("Ground Armor 1") || unitName.equals("Ground Armor 2") ||
				unitName.equals("Shields 1") || unitName.equals("Shields 2") ||
				unitName.equals("Air Weapons 1") || unitName.equals("Air Weapons 2") ||
				unitName.equals("Air Armor 1") || unitName.equals("Air Armor 2") || unitName.equals("High Templar")
		);
	}
	
	public static boolean fromGateway(String unitName) {
		return (
				unitName.equals("Zealot") || unitName.equals("Stalker") || unitName.equals("Sentry") || 
				unitName.equals("High Templar") || unitName.equals("Dark Templar")
				);
	}

	public static boolean fromRoboticsFacility(String unitName) {
		return (
				unitName.equals("Observer") || unitName.equals("Immortal") || unitName.equals("Colossus") || unitName.equals("Warp Prism")
				);
	}
	
	public static boolean fromStargate(String unitName) {
		return (
				unitName.equals("Phoenix") || unitName.equals("Void Ray") || unitName.equals("Tempest") || unitName.equals("Carrier") ||
				unitName.equals("Oracle")
				);
	}
	
	public static boolean fromForge(String unitName) {
		return (
				unitName.equals("Ground Armor 1") || unitName.equals("Ground Armor 2") || unitName.equals("Ground Armor 3") || 
				unitName.equals("Ground Weapons 1") || unitName.equals("Ground Weapons 2") || unitName.equals("Ground Weapons 3") || 
				unitName.equals("Shields 1") || unitName.equals("Shields 2") || unitName.equals("Shields 3")
				);
	}
	
	public static boolean fromCyberCore(String unitName) {
		return (
				unitName.equals("Warp Gate") || unitName.equals("Air Armor 1") || unitName.equals("Air Armor 2") || 
				unitName.equals("Air Armor 3") || unitName.equals("Air Weapons 1") || unitName.equals("Air Weapons 2") || 
				unitName.equals("Air Weapons 3")
				);
	}
	
	public static boolean fromTwilightCouncil(String unitName) {
		return (
				unitName.equals("Blink") || unitName.equals("Charge")
				);
	}
	
	public static boolean fromRoboticsBay(String unitName) {
		return (
				unitName.equals("Gravitic Boosters") || unitName.equals("Gravitic Drive") || 
				unitName.equals("Extended Thermal Lance")
				);
	}
	
	public static boolean fromFleetBeacon(String unitName) {
		return (
				unitName.equals("Anion Pulse-Crystal") || unitName.equals("Graviton Catapult")
				);
	}
	
	public static boolean fromTemplarArchives(String unitName) {
		return unitName.equals("Psionic Storm");
	}
}
