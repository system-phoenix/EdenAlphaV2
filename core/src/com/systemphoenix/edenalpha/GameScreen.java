package com.systemphoenix.edenalpha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class GameScreen implements Screen, GestureDetector.GestureListener {
    private EdenAlpha game;

    public static final float screenWidth = Gdx.graphics.getWidth(), screenHeight = Gdx.graphics.getHeight();
    public static final float worldWidth = 969, worldHeight = 1403;

    private float size = 480f;
    private OrthographicCamera cam;
    private Sprite mapSprite;

    private SpriteBatch gameGraphics;

    private FieldSelection fieldSelection;

    public GameScreen(EdenAlpha game) {
        this.game = game;

        this.fieldSelection = new FieldSelection();

        mapSprite = new Sprite(new Texture("[PH]map0.jpg"));
        mapSprite.setPosition(0, 0);
        mapSprite.setSize(worldWidth, worldHeight);
        cam = new OrthographicCamera(size, size * screenHeight / screenWidth);
        cam.position.set(465f, 1073.5f, 0);
        cam.update();

        gameGraphics = new SpriteBatch();

        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
    }

    @Override
    public void render(float delta) {
//        update stuff below
        boundCamera();
        fieldSelection.setCameraPosition(cam);
        cam.update();
        gameGraphics.setProjectionMatrix(cam.combined);
//        update stuff above

        gameGraphics.begin();
//        render stuff below
        fieldSelection.render(gameGraphics);
//        render stuff above
        gameGraphics.end();
    }

    public void boundCamera() {
        cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, worldWidth/screenWidth);

        float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
        float effectiveViewportHeight = cam.viewportHeight * cam.zoom;

        cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, worldWidth - effectiveViewportWidth / 2f);
        cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, worldHeight  - effectiveViewportHeight / 2f);
    }

//    Gesture listener methods
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float deltaX, float deltaY, int button) {
        float limit = 5;
        if(Math.abs(deltaX) < limit) {
            deltaX = 0;
        }
        int update = 0;
        if(deltaX > 0) {
            update = -1;
        } else if(deltaX < 0) {
            update = 1;
        }
        fieldSelection.setIndex(update);


        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {

//        cam.translate(-deltaX, 0);
//        cam.translate(0, deltaY);
//
//        cam.update();
//        Gdx.app.log("Verbose", "(" + cam.position.x + ", " + cam.position.y + ")");
        return true;
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

//   Screen Methods

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        cam.viewportWidth = size;
        cam.viewportHeight = size * screenHeight / screenWidth;
        cam.update();
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
        mapSprite.getTexture().dispose();
        gameGraphics.dispose();
    }
}