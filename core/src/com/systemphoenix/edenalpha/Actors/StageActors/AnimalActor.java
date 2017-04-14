package com.systemphoenix.edenalpha.Actors.StageActors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Codex.AnimalCodex;

public class AnimalActor extends PlantActor implements Disposable {

    private Sprite arenaSprite;

    private float damage, range, upgradeCostWater, upgradeCostSeeds, size;
    private int upgradeIndex = 0, animalIndex;


    public AnimalActor(Sprite textureRegion, Sprite rectangleSprite, int animalIndex, float size) {
        super(textureRegion, rectangleSprite, animalIndex, size);
        this.animalIndex = animalIndex - 16;
        this.size = size;
        this.setBounds(976, 32, size, size);

        this.damage = AnimalCodex.DMG[AnimalCodex.dmgStats[this.animalIndex]];
        this.range = AnimalCodex.RANGE[AnimalCodex.rangeStats[this.animalIndex]];

        this.arenaSprite = new Sprite(new Texture(Gdx.files.internal("bullets/arena.png")));

        float cost = 100;
        setPlantCost(cost);

        upgradeCostSeeds = cost / 2;
        upgradeCostSeeds = cost;

        this.clearListeners();

        this.setTouchable(Touchable.enabled);
        this.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return triggerAction();
            }
            public void touchDragged (InputEvent event, float x, float y, int pointer) {
                dragPlant();
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                action();
            }
        });
    }

    @Override
    public boolean triggerAction() {
        if(drawable) {
            setRectangleSpriteBounds();
            if(recentlySelectedActor != null) recentlySelectedActor.setDrawRectangle(false);
            drawRectangle = true;
            recentlySelectedActor = this;
            gameScreen.getGameHud().setAnimalData();
            gameScreen.setPseudoAnimal(animalIndex);
            clickTimer = gameScreen.getCentralTimer();
            return true;
        }
        return false;
    }

    @Override
    public void action() {
        if(draggable && dragging) {
            if(gameScreen.getSelectedXY().x >= 0 && gameScreen.getSelectedXY().y >= 0) {
                gameScreen.createAnimal(sprite, arenaSprite, damage, animalIndex);
            }
            setDragging(false);
            gameScreen.tap(-1, -1);
            gameScreen.getGameHud().setDrawable(true);
        }
    }

    @Override
    public void draw(Batch batch, float alpha) {
        if(drawable) {
            batch.draw(sprite, this.getX(), this.getY(), size, size);

            if(drawRectangle) {
                rectangleSprite.draw(batch);
            }

            if(getPlantCost() / 2 > gameScreen.getSeeds() || getPlantCost() > gameScreen.getWater()) {
                maskSprite.draw(batch);
            }
            draggable = !(getPlantCost() / 2 > gameScreen.getSeeds() || getPlantCost() > gameScreen.getWater());
        }

        if(dragging && recentlySelectedActor.equals(this)) {
            dragSprite.draw(batch);
        }
    }

    public void upgrade() {
        if(gameScreen.getSeeds() - upgradeCostSeeds >= 0 && gameScreen.getWater() - upgradeCostWater >= 0) {
            upgradeIndex++;
            upgradeCostSeeds *= 2; upgradeCostWater *= 2;
            if(AnimalCodex.dmgStats[getPlantIndex()] + upgradeIndex < AnimalCodex.HIGHEST) {
                damage = AnimalCodex.DMG[AnimalCodex.dmgStats[this.animalIndex] + upgradeIndex];
            }
            if(AnimalCodex.rangeStats[getPlantIndex()] + upgradeIndex < AnimalCodex.HIGHEST) {
                range = AnimalCodex.RANGE[AnimalCodex.rangeStats[this.animalIndex] + upgradeIndex];
            }
        }
    }

    @Override
    public void dispose() {
        arenaSprite.getTexture().dispose();
    }

    public float getDamage() {
        return damage;
    }

    public float getRange() {
        return range;
    }

    public int getAnimalIndex() {
        return animalIndex;
    }
}
