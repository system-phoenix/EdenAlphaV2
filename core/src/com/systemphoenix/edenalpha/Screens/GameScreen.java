package com.systemphoenix.edenalpha.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.systemphoenix.edenalpha.EdenAlpha;
import com.systemphoenix.edenalpha.Region;
import com.systemphoenix.edenalpha.Scenes.TopHud;

public class GameScreen extends AbsoluteScreen {
    private Region region;

    private Texture fieldTexture;
    private Sprite fieldSprite;
    private String message;

    private Viewport viewport;
    private TopHud topHud;

    private float pastZoomDistance, pastZoom;

    public GameScreen(EdenAlpha game, Region region) {
        super(game);
        this.region = region;
        this.viewport = new FitViewport(worldWidth, worldHeight, cam);
        this.topHud = new TopHud(game);
        initialize();
    }

    private void initialize() {
        fieldTexture = new Texture(Gdx.files.internal("levels/sample.png"));
        TextureRegion[][] temp = TextureRegion.split(fieldTexture, 1, 1);
        if(temp.length == temp[0].length) {
            worldHeight = worldWidth;
        }

        pastZoom = cam.zoom;
        fieldSprite = new Sprite(fieldTexture);
        fieldSprite.setPosition(0, 0);
        fieldSprite.setSize(worldWidth, worldHeight);
        message = "No touch event yet!";
        topHud.setMessage(message);
    }

    @Override
    public void render(float delta) {
        cam.update();
        gameGraphics.setProjectionMatrix(cam.combined);
        Gdx.gl.glClearColor(0.95f, 0.95f, 0.95f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameGraphics.begin();
        fieldSprite.draw(gameGraphics);
//        font.draw(gameGraphics, message, (cam.position.x - (cam.viewportWidth * cam.zoom / 2)), (cam.position.y + (cam.viewportHeight * cam.zoom / 2)));
        gameGraphics.end();

        gameGraphics.setProjectionMatrix(topHud.getStage().getCamera().combined);
        topHud.getStage().draw();
    }

//  Touch methods
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
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
//        float limit = 5;
//        if(Math.abs(deltaX) < limit) {
//            deltaX = 0;
//        }
//
//        if(Math.abs(deltaY) < limit) {
//            deltaY = 0;
//        }

        cam.position.x -= deltaX;
        cam.position.y += deltaY;
        boundCamera();
//        message = "Pan! deltaX: " + deltaX + " | deltaY: " + deltaY + "      (x, y): (" + cam.position.x + ", " + cam.position.y + ")";
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        message = "Zoom! Initial distance: " + initialDistance + " | Distance: " + distance;
        if(initialDistance < distance && pastZoomDistance != distance) {
            cam.zoom -= 0.01f;
        } else if(initialDistance > distance && pastZoomDistance != distance) {
            cam.zoom += 0.01f;
        }
        pastZoomDistance = distance;
        boundCamera();
        if(pastZoom < cam.zoom) {
//            font.getData().setScale(font.getScaleX() * cam.zoom, font.getScaleY() * cam.zoom);
        } else if(pastZoom > cam.zoom) {
//            font.getData().setScale(font.getScaleX() / cam.zoom, font.getScaleY() / cam.zoom);
        }

        pastZoom = cam.zoom;
        topHud.setMessage(message);
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }

//  Screen methods
    @Override
    public void show() {
        super.show();
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
        fieldTexture.dispose();
    }
}
