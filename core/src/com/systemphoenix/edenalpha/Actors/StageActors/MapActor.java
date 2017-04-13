package com.systemphoenix.edenalpha.Actors.StageActors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Screens.MapScreen;
import com.systemphoenix.edenalpha.Screens.TutorialMapScreen;

public class MapActor extends Actor implements Disposable {

    private Screen screen;

    private Sprite sprite;
    private boolean drawable, left, isPressed, tutorial;

    public MapActor(Screen screen, float worldWidth, float worldHeight, boolean left, boolean tutorial) {
        this.screen = screen;
        this.left = left;
        this.tutorial = tutorial;
        float x = 0;
        try {
            if(left) {
                sprite = new Sprite(new Texture(Gdx.files.internal("misc/leftSelect.png")));
                x = 16;
            } else {
                sprite = new Sprite(new Texture(Gdx.files.internal("misc/rightSelect.png")));
                x = worldWidth - 112;
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
                isPressed = false;
            }
        });
    }

    public boolean triggerAction() {
        if(drawable) {
            if(!tutorial) {
                isPressed = true;
            } else {
                int update = left ? -1 : 1;
                ((TutorialMapScreen)screen).updateIndex(update);
            }
            return true;
        }
        return false;
    }

    @Override
    public void draw(Batch batch, float alpha){
        if(drawable) {
            sprite.draw(batch);
            if(isPressed && !tutorial) {
                ((MapScreen)screen).getFieldSelection().setIndex(left ? -1 : 1);
            }
        }
    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
    }

    public void setDrawable(boolean drawable) {
        this.drawable = drawable;
    }
}
