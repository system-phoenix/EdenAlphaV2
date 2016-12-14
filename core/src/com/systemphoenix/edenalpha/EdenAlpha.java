package com.systemphoenix.edenalpha;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class EdenAlpha extends Game {

	private GameScreen gameScreen;
	
	@Override
	public void create () {
		this.gameScreen = new GameScreen(this);
		this.setScreen(gameScreen);
		Gdx.app.log("Verbose", "* * * A P P    S T A R T I N G . . . * * *");
	}
}
