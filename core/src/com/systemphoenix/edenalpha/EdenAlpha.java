package com.systemphoenix.edenalpha;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EdenAlpha extends Game {

	private SpriteBatch gameGraphics;
	private Preferences levelPrefs;

	private MapScreen mapScreen;
	private int selectedMapIndex = 0;
	
	@Override
	public void create () {
		this.levelPrefs = Gdx.app.getPreferences("Level");
		try {
			this.selectedMapIndex = this.levelPrefs.getInteger("SelectedMapIndex");
		} catch(Exception e) {
			this.selectedMapIndex = 0;
		}
		this.gameGraphics = new SpriteBatch();

		this.mapScreen = new MapScreen(this);
		this.setScreen(mapScreen);
		Gdx.app.log("Verbose", "* * * A P P    S T A R T I N G . . . * * *");
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		gameGraphics.dispose();
	}

	public SpriteBatch getGameGraphics() {
		return gameGraphics;
	}

	public Preferences getLevelPrefs() {
		return levelPrefs;
	}

	public int getSelectedMapIndex() {
		return selectedMapIndex;
	}

	public void setSelectedMapIndex(int selectedMapIndex) {
		this.selectedMapIndex = selectedMapIndex;
		levelPrefs.putInteger("SelectedMapIndex", this.selectedMapIndex);
		levelPrefs.flush();
	}
}
