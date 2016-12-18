package com.systemphoenix.edenalpha;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class EdenAlpha extends Game {
    private float screenWidth, screenHeight;

	private SpriteBatch gameGraphics;
	private BitmapFont font;
	private Preferences levelPrefs;

	private MapScreen mapScreen;
	private MainScreen mainScreen;
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
		this.font = new BitmapFont();

		this.mainScreen = new MainScreen(this);
		this.setScreen(mainScreen);

		this.mapScreen = new MapScreen(this);
	}

	public void render() {
		super.render();
	}

    public void boundCamera(OrthographicCamera cam, float worldWidth, float worldHeight) {
        cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, worldWidth/screenWidth);

        float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
        float effectiveViewportHeight = cam.viewportHeight * cam.zoom;

        cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, worldWidth - effectiveViewportWidth / 2f);
        cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, worldHeight  - effectiveViewportHeight / 2f);
    }

	public void dispose() {
		gameGraphics.dispose();
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

	public MainScreen getMainScreen() {
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

	public void setScreenToMapScreen() {
		this.setScreen(mapScreen);
	}

    public void setScreenWidth(float screenWidth) {
        this.screenWidth = screenWidth;
    }

    public void setScreenHeight(float screenHeight) {
        this.screenHeight = screenHeight;
    }

    public float getScreenWidth() {
        return screenWidth;
    }

    public float getScreenHeight() {
        return screenHeight;
    }
}
