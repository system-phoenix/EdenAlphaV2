package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Screens.MapScreen;

public class MapActor extends Actor implements Disposable {

    private MapScreen mapScreen;

    private Sprite sprite;
    private boolean canDraw, left, isPressed;

    public MapActor(MapScreen mapScreen, float worldWidth, float worldHeight, boolean left) {
        this.mapScreen = mapScreen;
        this.left = left;
        float x = 0;
        try {
            if(left) {
                sprite = new Sprite(new Texture(Gdx.files.internal("misc/leftSelect.png")));
                x = worldWidth / 16;
            } else {
                sprite = new Sprite(new Texture(Gdx.files.internal("misc/rightSelect.png")));
                x = worldWidth - (worldWidth / 8);
            }
        } catch(Exception e) {
            Gdx.app.log("Verbose", "Error loading texture in MapActor: " + e.getMessage());
        }

        sprite.setBounds(x, (worldHeight / 2) - (worldHeight / 4), 96, 384);
        this.setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        this.setTouchable(Touchable.enabled);
        this.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return MapActor.this.triggerAction();
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            }
        });
    }

    public boolean triggerAction() {
        isPressed = true;
        return true;
    }

    @Override
    public void draw(Batch batch, float alpha){
        if(canDraw) {
            sprite.draw(batch);
            if(isPressed) {
                mapScreen.getFieldSelection().setIndex(left ? -1 : 1);
            }
        }
    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }
}
