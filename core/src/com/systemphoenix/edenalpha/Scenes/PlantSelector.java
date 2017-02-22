package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class PlantSelector extends Actor implements Disposable {

    private Sprite sprite;
    private boolean canDraw = false;

    public PlantSelector() {
        sprite = new Sprite(new Texture(Gdx.files.internal("misc/lowerHud.png")));
    }

    @Override
    public void draw(Batch batch, float alpha) {
        if(canDraw) {
            batch.draw(sprite, 0f, 32f);
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
