package com.systemphoenix.edenalpha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

public class MainScreen implements Screen {

    private EdenAlpha game;

    private SpriteBatch gameGraphics;
    private BitmapFont font;

    private Texture bg;
    private String loadingMessage = "Loading...";
    private GlyphLayout glyphLayout;

    private boolean canStart = false, blinking = true;
    private long blink;

    public MainScreen(EdenAlpha game) {
        this.game = game;
        this.gameGraphics = game.getGameGraphics();
        this.font = game.getFont();
        this.font.getData().setScale(1.5f);
        this.font.setColor(Color.BLACK);
        this.glyphLayout = new GlyphLayout();
        this.bg = new Texture(Gdx.files.internal("bg.jpg"));
    }

    public void update() {
        if(blinking) {
            if(TimeUtils.timeSinceMillis(blink) >= 800) {
                blinking = false;
                blink = TimeUtils.millis();
            }
        } else {
            if(TimeUtils.timeSinceMillis(blink) >= 500) {
                blink = TimeUtils.millis();
                blinking = true;
            }
        }
    }

    @Override
    public void render(float delta) {
        if(canStart) {
            if (Gdx.input.justTouched()) {
                game.setScreenToMapScreen();
            }
            update();
        }


        glyphLayout.setText(font, loadingMessage);
        float x = (MapScreen.screenWidth - glyphLayout.width) / 2;
        gameGraphics.begin();
        gameGraphics.draw(bg, 0, 0, MapScreen.screenWidth, MapScreen.screenHeight);
        if(canStart) {
            if(blinking) {
                font.draw(gameGraphics, loadingMessage, x, MapScreen.screenHeight / 4 - MapScreen.screenHeight / 16);
            }
        } else {
            font.draw(gameGraphics, loadingMessage, x, MapScreen.screenHeight / 4 - MapScreen.screenHeight / 16);
        }
        gameGraphics.end();
    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        bg.dispose();
    }

    public void setLoadingMessage(String loadingMessage) {
        this.loadingMessage = loadingMessage;
    }

    public void setCanStart(boolean canStart) {
        this.canStart = canStart;
    }
}