package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.systemphoenix.edenalpha.Actors.Pulse;
import com.systemphoenix.edenalpha.Codex.AnimalCodex;

public class AnimalActor extends PlantActor {

    private Sprite anchoredSprite;

    private Pulse pulse = null;

    private boolean anchored;
    private float damage, range, upgradeCostWater, upgradeCostSeeds, size;
    private int upgradeIndex = 0, animalIndex;
    private long effectTimer, effectLimit;


    public AnimalActor(Sprite textureRegion, Sprite rectangleSprite, int animalIndex, float size) {
        super(textureRegion, rectangleSprite, animalIndex, size);
        this.anchoredSprite = new Sprite(textureRegion);
        this.animalIndex = animalIndex - 16;
        this.size = size;
        this.setBounds(976, 32, size, size);

        this.damage = AnimalCodex.DMG[AnimalCodex.dmgStats[this.animalIndex]];
        this.range = AnimalCodex.RANGE[AnimalCodex.rangeStats[this.animalIndex]];
        this.effectLimit = AnimalCodex.effectLimit[this.animalIndex];

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
        if(canDraw) {
            setRectangleSpriteBounds();
            if(recentlySelectedActor != null) recentlySelectedActor.setDrawRectangle(false);
            drawRectangle = true;
            recentlySelectedActor = this;
            gameScreen.getGameHud().setData();
            gameScreen.setPseudoAnimal(animalIndex);
            clickTimer = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    @Override
    public void action() {
        if(draggable && dragging) {
            setDragging(false);
            gameScreen.tap(-1, -1);
            gameScreen.getGameHud().setCanDraw(true);
            switch(animalIndex) {
                case 0:
                    Rectangle hitBox = gameScreen.getHitRectangle();
                    pulse = new Pulse(gameScreen, hitBox, (int)damage, gameScreen.getPulseAnimation());
                    break;
                case 1:
                    break;
                case 2:
                    break;
            }
            effectTimer = System.currentTimeMillis();
        }
    }

    @Override
    public void draw(Batch batch, float alpha) {
        if(canDraw) {
            batch.draw(sprite, this.getX(), this.getY(), size, size);

            if(drawRectangle) {
                rectangleSprite.draw(batch);
            }

            if(getPlantCost() / 2 > gameScreen.getSeeds() || getPlantCost() > gameScreen.getWater()) {
                maskSprite.draw(batch);
            }
            draggable = !(getPlantCost() / 2 > gameScreen.getSeeds() || getPlantCost() > gameScreen.getWater());
        }

        if(pulse != null) {
            pulse.render(batch, Gdx.graphics.getDeltaTime());
            if(pulse.canDispose()) {
                pulse = null;
            }
        }
//        if(dragging) {
        if(dragging && recentlySelectedActor.equals(this)) {
            dragSprite.draw(batch);
        }

        if(anchored) {
            if(System.currentTimeMillis() - effectTimer >= effectLimit) {
                anchored = false;
            } else {
                anchoredSprite.draw(batch);
            }
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

    public float getDamage() {
        return damage;
    }

    public float getRange() {
        return range;
    }
}
