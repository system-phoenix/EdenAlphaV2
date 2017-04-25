package com.systemphoenix.edenalpha.Actors.StageActors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.systemphoenix.edenalpha.Actors.ObjectActors.Plant;
import com.systemphoenix.edenalpha.Codex.PlantCodex;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class PlantActor extends Actor {

    protected static PlantActor recentlySelectedActor = null;
    protected long clickTimer;
    protected GameScreen gameScreen;
    protected boolean drawable, drawRectangle = false, draggable, animal;
    protected static boolean dragging;
    protected Sprite sprite, rectangleSprite, maskSprite, dragSprite;

    private int plantIndex;
    private float size, plantCost;

    public PlantActor(Sprite textureRegion, Sprite rectangleSprite, int plantIndex, int index, float size) {
        this.gameScreen = null;
        this.sprite = new Sprite(textureRegion);
        this.dragSprite = new Sprite(textureRegion);
        this.plantIndex = plantIndex;
        this.rectangleSprite = rectangleSprite;
        this.plantCost = PlantCodex.cost[plantIndex];
        this.animal = false;

        this.maskSprite = new Sprite(new Texture(Gdx.files.internal("misc/lock.png")));
        this.maskSprite.setBounds(PlantCodex.plantSelectorIndex[index], 32f, size, size);
        this.setBounds(PlantCodex.plantSelectorIndex[index], 32f, size, size);

        this.size = size;
        this.drawable = false;

        this.setTouchable(Touchable.enabled);
        this.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return PlantActor.this.triggerAction();
            }
            public void touchDragged (InputEvent event, float x, float y, int pointer) {
                dragPlant();
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                action();
            }
        });
    }

    public PlantActor(Sprite textureRegion, Sprite rectangleSprite, int plantIndex, float size) {
        this.gameScreen = null;
        this.sprite = new Sprite(textureRegion);
        this.dragSprite = new Sprite(textureRegion);
        this.plantIndex = plantIndex;
        this.rectangleSprite = rectangleSprite;
        this.animal = true;

        this.maskSprite = new Sprite(new Texture(Gdx.files.internal("misc/lock.png")));
        this.maskSprite.setBounds(PlantCodex.plantSelectorIndex[6], 32f, size, size);
        this.setBounds(PlantCodex.plantSelectorIndex[6], 32f, size, size);

        this.size = size;
        this.drawable = false;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public boolean triggerAction() {
        if(drawable) {
            setRectangleSpriteBounds();
            if(recentlySelectedActor != null) recentlySelectedActor.setDrawRectangle(false);
            drawRectangle = true;
            recentlySelectedActor = this;
            gameScreen.getGameHud().setData();
            gameScreen.setPseudoPlant(plantIndex);
            clickTimer = gameScreen.getCentralTimer();
            return true;
        }
        return false;
    }

    public void dragPlant() {
        if(draggable && gameScreen.getCentralTimer() - clickTimer >= 200) {
            dragging = true;
            float x = 32, y = 32;
//            dragSprite.setBounds(Gdx.input.getX() - x, gameScreen.getWorldHeight() - Gdx.input.getY() + y, size / gameScreen.getCamera().zoom, size / gameScreen.getCamera().zoom);
            gameScreen.tap(Gdx.input.getX() - x, Gdx.input.getY() - y, this);
            gameScreen.getGameHud().setDrawable(false);
            Plant.setSelectAllPlants(true);
        }
    }

    public void action() {
        if(draggable) {
            dragging = false;
            gameScreen.plant(plantIndex, sprite);
            gameScreen.tap(-1, -1, null);
            gameScreen.getGameHud().setDrawable(true);
            Plant.nullSelectedPlant();
        }
    }

    public void setRectangleSpriteBounds() {
        rectangleSprite.setBounds(this.getX() - 5, this.getY() - 5, this.getWidth() + 10, this.getHeight() + 10);
        drawRectangle = true;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        if(drawable) {
            batch.draw(sprite, this.getX(), this.getY(), size, size);

            if(drawRectangle) {
                rectangleSprite.draw(batch);
            }

            if(plantCost / 2 > gameScreen.getSeeds() || plantCost > gameScreen.getWater()) {
                maskSprite.draw(batch);
            }
            draggable = !(plantCost / 2 > gameScreen.getSeeds() || plantCost > gameScreen.getWater());
        }
//        if(dragging) {
        if(dragging && recentlySelectedActor.equals(this)) {
            dragSprite.draw(batch);
        }
    }

    public void setDrawable(boolean drawable) {
        this.drawable = drawable;
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

    public boolean isAnimal() {
        return animal;
    }

    public static boolean isDragging() {
        return dragging;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setPlantCost(float plantCost) {
        this.plantCost = plantCost;
    }

    public void setDragSpriteBounds(float x, float y) {
        dragSprite.setBounds(x, gameScreen.getWorldHeight() - y, size / gameScreen.getCamera().zoom, size / gameScreen.getCamera().zoom);
    }

    public static void setDragging(boolean dragging) {
        PlantActor.dragging = dragging;
    }

    public static PlantActor getRecentlySelectedActor() {
        return recentlySelectedActor;
    }

    public static void setRecentlySelectedActor(PlantActor plantActor) {
        recentlySelectedActor = plantActor;
    }
}
