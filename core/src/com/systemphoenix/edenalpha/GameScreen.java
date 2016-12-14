package com.systemphoenix.edenalpha;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen implements Screen {

    private EdenAlpha game;

    private SpriteBatch gameGraphics;

    public GameScreen(EdenAlpha game) {
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
//        update stuff below

//        update stuff above

        gameGraphics.begin();

        gameGraphics.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
