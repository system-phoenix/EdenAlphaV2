package com.systemphoenix.edenalpha;

import com.badlogic.gdx.Game;

public class EdenAlpha extends Game {

	private GameScreen gameScreen;
	
	@Override
	public void create () {
		gameScreen = new GameScreen(this);
	}

	public GameScreen getGameScreen() {
		return this.gameScreen;
	}
}
