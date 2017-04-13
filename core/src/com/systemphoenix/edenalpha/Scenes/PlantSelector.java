package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Actors.StageActors.PlantActor;

public class PlantSelector extends Actor implements Disposable {

    private Sprite sprite, exSprite;
    private boolean drawable = false;

    private com.systemphoenix.edenalpha.Actors.StageActors.PlantActor[] plantActors;
    private com.systemphoenix.edenalpha.Actors.StageActors.AnimalActor animalActor;

    public PlantSelector(PlantActor[] plantActors, com.systemphoenix.edenalpha.Actors.StageActors.AnimalActor animalActor) {
        exSprite = new Sprite(new Texture(Gdx.files.internal("misc/lowerHudData.png")));
        sprite = new Sprite(new Texture(Gdx.files.internal("misc/lowerHud.png")));
        this.plantActors = plantActors;
        this.animalActor = animalActor;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        if(drawable) {
            batch.draw(sprite, 0f, 0f);
            batch.draw(exSprite, 0f, 0f);
        }
    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
        exSprite.getTexture().dispose();
    }

    public void setDrawable(boolean drawable) {
        this.drawable = drawable;
        for(int i = 0; i < plantActors.length; i++) {
            plantActors[i].setDrawable(drawable);
        }
        if(animalActor != null) {
            animalActor.setDrawable(drawable);
        }
    }

}
