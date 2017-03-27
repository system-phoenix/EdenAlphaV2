package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class PlantSelector extends Actor implements Disposable {

    private Sprite sprite, exSprite;
    private boolean canDraw = false;

    private PlantActor[] plantActors;

    public PlantSelector(PlantActor[] plantActors) {
        exSprite = new Sprite(new Texture(Gdx.files.internal("misc/lowerHudData.png")));
        sprite = new Sprite(new Texture(Gdx.files.internal("misc/lowerHud.png")));
        this.plantActors = plantActors;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        if(canDraw) {
            batch.draw(sprite, 0f, 0f);
            batch.draw(exSprite, 0f, 0f);
        }
    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
        for(int i = 0; i < plantActors.length; i++) {
            plantActors[i].dispose();
        }
        exSprite.getTexture().dispose();
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
        for(int i = 0; i < plantActors.length; i++) {
            plantActors[i].setCanDraw(canDraw);
        }
    }

}
