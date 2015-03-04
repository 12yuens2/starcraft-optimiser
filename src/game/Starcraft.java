package game;

import gameobjects.GameObject;

import java.util.ArrayList;

import units.Probe;
import units.nexus.ExpansionNexus;

public class Starcraft {
public static void main(String[] args) {
		
		Game testGame = new Game();
		
		while (testGame.getTime() < 10000000){
			testGame.passTime();
		}
	}
}
