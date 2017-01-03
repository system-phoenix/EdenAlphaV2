package com.systemphoenix.edenalpha;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.systemphoenix.edenalpha.Screens.LoadingScreen;
import com.systemphoenix.edenalpha.Screens.MapScreen;

public class EdenAlpha extends Game {
	private SpriteBatch gameGraphics;
	private FreeTypeFontGenerator generator;
    private BitmapFont font;
	private Preferences levelPrefs;

    private com.systemphoenix.edenalpha.Screens.LoadingScreen loadingScreen;
	private com.systemphoenix.edenalpha.Screens.MapScreen mapScreen;
	private com.systemphoenix.edenalpha.Screens.MainScreen mainScreen;
	private int selectedMapIndex = 0;
	
	@Override
	public void create () {
        this.gameGraphics = new SpriteBatch();
        try {
            generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/neuropol.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 20;
            parameter.color = Color.WHITE;
            this.font = generator.generateFont(parameter);

            font.setColor(Color.WHITE);
        } catch(Exception e) {
            Gdx.app.log("Verbose", "Failed to load custom font.");
            this.font = new BitmapFont();
        }

		this.levelPrefs = Gdx.app.getPreferences("Level");
		try {
			this.selectedMapIndex = this.levelPrefs.getInteger("SelectedMapIndex");
		} catch(Exception e) {
			this.selectedMapIndex = 0;
		}

        this.loadingScreen = new com.systemphoenix.edenalpha.Screens.LoadingScreen(this);
        this.setScreen(loadingScreen);
		this.mainScreen = new com.systemphoenix.edenalpha.Screens.MainScreen(this);
		this.mapScreen = new com.systemphoenix.edenalpha.Screens.MapScreen(this);
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		gameGraphics.dispose();
        font.dispose();
        generator.dispose();

        loadingScreen.dispose();
        mapScreen.dispose();
        mainScreen.dispose();
	}

	public SpriteBatch getGameGraphics() {
		return gameGraphics;
	}

	public BitmapFont getFont() {
		return font;
	}

	public Preferences getLevelPrefs() {
		return levelPrefs;
	}

	public com.systemphoenix.edenalpha.Screens.MainScreen getMainScreen() {
		return mainScreen;
	}

	public int getSelectedMapIndex() {
		return selectedMapIndex;
	}

	public void setSelectedMapIndex(int selectedMapIndex) {
		this.selectedMapIndex = selectedMapIndex;
		levelPrefs.putInteger("SelectedMapIndex", this.selectedMapIndex);
		levelPrefs.flush();
	}

	public MapScreen getMapScreen() {
		return this.mapScreen;
	}

    public LoadingScreen getLoadingScreen() {
        return this.loadingScreen;
    }
}
