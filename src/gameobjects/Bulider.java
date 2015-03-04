package gameobjects;

import game.Game;

public abstract class Bulider extends Entity {
	private Game game;
	
	public Bulider(Game game) {
		this.game = game;
	}
	
	public Game getGame(){
		return game;
	}
}
