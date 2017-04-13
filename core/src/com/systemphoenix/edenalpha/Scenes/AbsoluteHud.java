package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.systemphoenix.edenalpha.Eden;

public class AbsoluteHud implements Disposable {

    protected final float screenWidth = 1280, screenHeight = 720;
    protected float worldWidth = screenWidth, worldHeight = screenHeight;

    protected Eden game;
    protected SpriteBatch gameGraphics;
    protected BitmapFont font;

    protected Stage stage;
    protected Viewport viewport;

    protected Label message;

    public AbsoluteHud(Eden game) {
        this.game = game;
        this.gameGraphics = game.getGameGraphics();
        Color border = Color.BLACK, fontColor = Color.WHITE;
        BitmapFont tempFont = font;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arcon.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.borderColor = border;
        parameter.borderWidth = 3;
        font = generator.generateFont(parameter);

        viewport = new FitViewport(worldWidth, worldHeight, new OrthographicCamera());
        stage = new Stage(viewport, gameGraphics);

        message = new Label("No touch event yet!", new Label.LabelStyle(font, fontColor));

        font = tempFont;
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
