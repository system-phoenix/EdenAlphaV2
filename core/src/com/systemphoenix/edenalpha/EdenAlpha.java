package com.systemphoenix.edenalpha;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class EdenAlpha extends Game {

	private MapScreen mapScreen;
	
	@Override
	public void create () {
		this.mapScreen = new MapScreen(this);
		this.setScreen(mapScreen);
		Gdx.app.log("Verbose", "* * * A P P    S T A R T I N G . . . * * *");
	}
}
