package com.systemphoenix.edenalpha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public class MainScreen implements Screen, GestureDetector.GestureListener {

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
        this.game.setScreenWidth(Gdx.graphics.getWidth());
        this.game.setScreenHeight(Gdx.graphics.getHeight());
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
            update();
        }


        glyphLayout.setText(font, loadingMessage);
        float x = (game.getScreenWidth() - glyphLayout.width) / 2;
        gameGraphics.begin();
        gameGraphics.draw(bg, 0, 0, game.getScreenWidth(), game.getScreenHeight());
        if(canStart) {
            if(blinking) {
                font.draw(gameGraphics, loadingMessage, x, game.getScreenHeight() / 4 - game.getScreenHeight() / 16);
            }
        } else {
            font.draw(gameGraphics, loadingMessage, x, game.getScreenHeight() / 4 - game.getScreenHeight() / 16);
        }
        gameGraphics.end();
    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {
        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
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

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if(canStart) game.setScreenToMapScreen();
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
