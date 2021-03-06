package com.systemphoenix.edenalpha;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.systemphoenix.edenalpha.Screens.MapScreen;

public class Eden extends Game {
	private SpriteBatch gameGraphics;
	private FreeTypeFontGenerator generator;
    private BitmapFont font;
	private Preferences levelPrefs;

	private com.systemphoenix.edenalpha.Screens.MapScreen mapScreen;
	private com.systemphoenix.edenalpha.Screens.MainScreen mainScreen;
	private int selectedMapIndex = 0, lowLevelBound = 0, highLevelBound = 0, module;

    private boolean firstTimePlaying = false, devMode = false;
    private float seedCount, waterCount;
	
	@Override
	public void create () {
        devMode = true;
        this.gameGraphics = new SpriteBatch();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arcon.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        parameter.color = Color.WHITE;
        this.font = generator.generateFont(parameter);

        font.setColor(Color.WHITE);

		this.levelPrefs = Gdx.app.getPreferences("Level");

        module = 0;
        if(!levelPrefs.contains("SelectedMapIndex")) {
            this.selectedMapIndex = module;
            levelPrefs.putInteger("SelectedMapIndex", selectedMapIndex);
        } else {
            this.selectedMapIndex = levelPrefs.getInteger("SelectedMapIndex");
        }

        if(!levelPrefs.contains("HighLevelBound")) {
            this.lowLevelBound = this.highLevelBound = module;
            levelPrefs.putInteger("HighLevelBound", highLevelBound);
            levelPrefs.putInteger("LowLevelBound", lowLevelBound);
        } else {
            this.highLevelBound = levelPrefs.getInteger("HighLevelBound");
            this.lowLevelBound = module;
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

        if(devMode) {
            lowLevelBound = 0;
            highLevelBound = 16;
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

    public int getModule() {
        return module;
    }

    public float getSeedCount() {
        return seedCount;
    }

    public float getWaterCount() {
        return waterCount;
    }

    public boolean isDevMode() {
        return devMode;
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
