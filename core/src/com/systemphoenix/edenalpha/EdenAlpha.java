package com.systemphoenix.edenalpha;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.systemphoenix.edenalpha.Screens.MapScreen;

public class EdenAlpha extends Game {
	private SpriteBatch gameGraphics;
	private FreeTypeFontGenerator generator;
    private BitmapFont font;
	private Preferences levelPrefs;

	private com.systemphoenix.edenalpha.Screens.MapScreen mapScreen;
	private com.systemphoenix.edenalpha.Screens.MainScreen mainScreen;
	private int selectedMapIndex = 0, lowLevelBound = 0, highLevelBound = 0;

    private boolean firstTimePlaying = false;
    private float seedCount, waterCount;
	
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

        if(!levelPrefs.contains("SelectedMapIndex")) {
            this.selectedMapIndex = 0;
            levelPrefs.putInteger("SelectedMapIndex", selectedMapIndex);
        } else {
            this.selectedMapIndex = levelPrefs.getInteger("SelectedMapIndex");
        }

        if(!levelPrefs.contains("LowLevelBound")) {
            this.lowLevelBound = 0;
            levelPrefs.putInteger("LowLevelBound", lowLevelBound);
        } else {
            this.lowLevelBound = levelPrefs.getInteger("LowLevelBound");
        }

        if(!levelPrefs.contains("HighLevelBound")) {
            this.highLevelBound = 0;
            levelPrefs.putInteger("HighLevelBound", highLevelBound);
        } else {
            this.highLevelBound = levelPrefs.getInteger("HighLevelBound");
        }

        if(!levelPrefs.contains("SeedCount")) {
            this.seedCount = 75;
            levelPrefs.putFloat("SeedCount", seedCount);
        } else {
            this.seedCount = levelPrefs.getFloat("SeedCount");
        }

        if(!levelPrefs.contains("WaterCount")) {
            this.waterCount = 150;
            levelPrefs.putFloat("WaterCount", waterCount);
        } else {
            this.waterCount = levelPrefs.getFloat("WaterCount");
        }

        if(!levelPrefs.contains("FirstTimePlaying")) {
            firstTimePlaying = !firstTimePlaying;
            levelPrefs.putBoolean("FirstTimePlaying", firstTimePlaying);
        } else {
            firstTimePlaying = levelPrefs.getBoolean("FirstTimePlaying");
        }

        levelPrefs.flush();

		this.mainScreen = new com.systemphoenix.edenalpha.Screens.MainScreen(this);
		this.mapScreen = new com.systemphoenix.edenalpha.Screens.MapScreen(this);

        Gdx.app.log("Verbose", "LowLevelBound: " + lowLevelBound + ", HighLevelBound: " + highLevelBound);
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		gameGraphics.dispose();
        font.dispose();
        generator.dispose();

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

    public int getLowLevelBound() {
        return lowLevelBound;
    }

    public int getHighLevelBound() {
        return highLevelBound;
    }

    public float getSeedCount() {
        return seedCount;
    }

    public float getWaterCount() {
        return waterCount;
    }

    public boolean isFirstTimePlaying() {
        return firstTimePlaying;
    }

	public void setSelectedMapIndex(int selectedMapIndex) {
		this.selectedMapIndex = selectedMapIndex;
		levelPrefs.putInteger("SelectedMapIndex", this.selectedMapIndex);
		levelPrefs.flush();
	}

    public void setResourceCount(float seedCount, float waterCount) {
        this.seedCount = seedCount;
        levelPrefs.putFloat("SeedCount", this.seedCount);

        this.waterCount = waterCount;
        levelPrefs.putFloat("WaterCount", this.waterCount);
        levelPrefs.flush();
    }

    public void setLevelBounds(int lowLevelBound, int highLevelBound) {
        this.lowLevelBound = lowLevelBound;
        levelPrefs.putInteger("LowLevelBound", lowLevelBound);

        this.highLevelBound = highLevelBound;
        levelPrefs.putInteger("HighLevelBound", this.highLevelBound);

        levelPrefs.flush();
    }

    public void setFirstTimePlaying(boolean firstTimePlaying) {
        this.firstTimePlaying = firstTimePlaying;
        levelPrefs.putBoolean("FirstTimePlaying", firstTimePlaying);

        levelPrefs.flush();
    }

	public MapScreen getMapScreen() {
		return this.mapScreen;
	}
}
