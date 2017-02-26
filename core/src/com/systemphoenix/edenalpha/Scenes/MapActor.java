package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Screens.MapScreen;

public class MapActor extends Actor implements InputProcessor, Disposable {

    private MapScreen mapScreen;
    private MapSelect mapSelect;

    private Vector2 coord;

    private Sprite sprite;
    private boolean canDraw, left;
    private int pastScreenX = -1, pastScreenY = -1;
    private float flingLimit;

    public MapActor(MapScreen mapScreen, MapSelect mapSelect, float worldWidth, float worldHeight, boolean left) {
        this.mapScreen = mapScreen;
        this.mapSelect = mapSelect;
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
            if(flingLimit != 0) {
                mapScreen.fling(flingLimit, 0, 0);
            }
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
        if((pastScreenX == -1 && pastScreenY == -1) || !(pastScreenX == screenX && pastScreenY == screenY)) {
            coord = mapSelect.getStage().screenToStageCoordinates(new Vector2((float)screenX, (float)screenY));
            pastScreenX = screenX;
            pastScreenY = screenY;
        }
        Actor hitActor = mapSelect.getStage().hit(coord.x, coord.y, true);

        if(hitActor == this) {
            flingLimit = left ? 8 : -8;
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        flingLimit = 0;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return true;
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
