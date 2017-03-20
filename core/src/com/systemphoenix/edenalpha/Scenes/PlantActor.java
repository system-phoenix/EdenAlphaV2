package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Actors.Plant;
import com.systemphoenix.edenalpha.Codex.PlantCodex;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class PlantActor extends Actor implements InputProcessor, Disposable {

    private static PlantActor recentlySelectedActor = null;

    private GameScreen gameScreen;

    private int plantIndex;
    private float size, plantCost;
    private boolean canDraw, drawRectangle = false;

    private String plantName;

    private Sprite sprite, rectangleSprite, maskSprite;

    private Vector2 coord;
    private int pastScreenX, pastScreenY;

    public PlantActor(Sprite textureRegion, Sprite rectangleSprite, int plantIndex, int index, float size) {
        this.gameScreen = null;
        this.sprite = new Sprite(textureRegion);
        this.plantIndex = plantIndex;
        this.rectangleSprite = rectangleSprite;

        this.plantName = PlantCodex.plantName[plantIndex];
        this.plantCost = PlantCodex.cost[plantIndex];

        this.maskSprite = new Sprite(new Texture(Gdx.files.internal("utilities/mask.png")));
        this.maskSprite.setBounds(PlantCodex.plantSelectorIndex[index], 64f, size, size);
        this.setBounds(PlantCodex.plantSelectorIndex[index], 64f, size, size);

        this.size = size;
        this.canDraw = false;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
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
            coord = gameScreen.getGameHud().getStage().screenToStageCoordinates(new Vector2((float)screenX, (float)screenY));
            pastScreenX = screenX;
            pastScreenY = screenY;
        }
        Actor hitActor = gameScreen.getGameHud().getStage().hit(coord.x, coord.y, true);

        if(hitActor == this && canDraw) {
//            gameScreen.plant(plantIndex, textureRegion);
            rectangleSprite.setBounds(this.getX() - 5, this.getY() - 5, this.getWidth() + 10, this.getHeight() + 10);
            if(recentlySelectedActor != null) recentlySelectedActor.setDrawRectangle(false);
            drawRectangle = true;
            recentlySelectedActor = this;
            gameScreen.getGameHud().setData();
            gameScreen.getGameHud().setCheckButtonCanDraw(true);
            gameScreen.getGameHud().setCanDraw();
            gameScreen.setPseudoPlant(plantIndex);
            Plant.setSelectAllPlants(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if((pastScreenX == -1 && pastScreenY == -1) || !(pastScreenX == screenX && pastScreenY == screenY)) {
            coord = gameScreen.getGameHud().getStage().screenToStageCoordinates(new Vector2((float)screenX, (float)screenY));
            pastScreenX = screenX;
            pastScreenY = screenY;
        }
        if(gameScreen != null) {
            Actor hitActor = gameScreen.getGameHud().getStage().hit(coord.x, coord.y, true);

            if(hitActor == this && canDraw) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
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
}
