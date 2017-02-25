package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Screens.MapScreen;

public class MapActor extends Actor implements InputProcessor, Disposable {

    private MapScreen mapScreen;

    private Sprite sprite;
    private boolean canDraw, left;

    public MapActor(MapScreen mapScreen, float worldWidth, float worldHeight, boolean left) {
        this.mapScreen = mapScreen;
        this.left = left;
        try {
            if(left) {
                sprite = new Sprite(new Texture(Gdx.files.internal("misc/leftSelect.png")));
                this.setX(worldHeight / 16);
            } else {
                sprite = new Sprite(new Texture(Gdx.files.internal("misc/rightSelect.png")));
                this.setX(worldWidth - (worldWidth / 16) - 64);
            }
        } catch(Exception e) {
            Gdx.app.log("Verbose", "Error loading texture in MapActor: " + e.getMessage());
        }

        this.setY(worldHeight / 4);

        this.setBounds(this.getX(), this.getY(), sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void draw(Batch batch, float alpha){
        if(canDraw) {
            batch.draw(sprite, this.getX(), this.getY());
        }
    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Rectangle touchRect = new Rectangle((float)screenX, (float)screenY, 1f, 1f);
        Rectangle rect = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        if(touchRect.overlaps(rect) && canDraw) {
            if(left) mapScreen.fling(8, 0, 0);
            else mapScreen.fling(-8, 0, 0);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }
}
