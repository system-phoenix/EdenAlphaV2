package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.systemphoenix.edenalpha.EdenAlpha;

public class AbsoluteHud {
    protected final float screenWidth = Gdx.graphics.getWidth(), screenHeight = Gdx.graphics.getHeight();
    protected float worldWidth = screenWidth, worldHeight = screenHeight;

    protected EdenAlpha game;
    protected SpriteBatch gameGraphics;
    protected BitmapFont font;

    protected Stage stage;
    protected Viewport viewport;

    protected Label message;

    public AbsoluteHud(EdenAlpha game) {
        this.game = game;
        this.gameGraphics = game.getGameGraphics();
        this.font = game.getFont();

        viewport = new FitViewport(worldWidth, worldHeight, new OrthographicCamera());
        stage = new Stage(viewport, gameGraphics);

        message = new Label("No touch event yet!", new Label.LabelStyle(font, Color.BLACK));
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }

    public Stage getStage() {
        return stage;
    }
}
