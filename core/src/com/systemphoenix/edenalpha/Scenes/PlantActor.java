package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Actors.Plant;
import com.systemphoenix.edenalpha.Codex.PlantCodex;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class PlantActor extends Actor {

    private static PlantActor recentlySelectedActor = null;

    private GameScreen gameScreen;

    private int plantIndex;
    private float size, plantCost;
    private boolean canDraw, drawRectangle = false;

    private Sprite sprite, rectangleSprite, maskSprite;

    public PlantActor(Sprite textureRegion, Sprite rectangleSprite, int plantIndex, int index, float size) {
        this.gameScreen = null;
        this.sprite = new Sprite(textureRegion);
        this.plantIndex = plantIndex;
        this.rectangleSprite = rectangleSprite;

        this.plantCost = PlantCodex.cost[plantIndex];

        this.maskSprite = new Sprite(new Texture(Gdx.files.internal("utilities/mask.png")));
        this.maskSprite.setBounds(PlantCodex.plantSelectorIndex[index], 32f, size, size);
        this.setBounds(PlantCodex.plantSelectorIndex[index], 32f, size, size);

        this.size = size;
        this.canDraw = false;

        this.setTouchable(Touchable.enabled);
        this.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return PlantActor.this.triggerAction();
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }
        });
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public boolean triggerAction() {
        if(canDraw) {
            setRectangleSpriteBounds();
            if(recentlySelectedActor != null) recentlySelectedActor.setDrawRectangle(false);
            recentlySelectedActor = this;
            gameScreen.getGameHud().setData();
            gameScreen.setPseudoPlant(plantIndex);
            Plant.setSelectAllPlants(true);
            return true;
        }
        return false;
    }

    public void setRectangleSpriteBounds() {
        rectangleSprite.setBounds(this.getX() - 5, this.getY() - 5, this.getWidth() + 10, this.getHeight() + 10);
        drawRectangle = true;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        if(canDraw) {
            batch.draw(sprite, this.getX(), this.getY(), size, size);

            if(drawRectangle) {
                rectangleSprite.draw(batch);
            }

            if(plantCost > gameScreen.getSeeds()) {
                maskSprite.draw(batch);
            }
        }
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }

    public void setDrawRectangle(boolean drawRectangle) {
        this.drawRectangle = drawRectangle;
    }

    public int getPlantIndex() {
        return plantIndex;
    }

    public float getPlantCost() {
        return plantCost;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public static PlantActor getRecentlySelectedActor() {
        return recentlySelectedActor;
    }

    public static void setRecentlySelectedActor(PlantActor plantActor) {
        recentlySelectedActor = plantActor;
    }
}
