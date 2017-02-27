package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Codex.PlantCodex;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class PlantActor extends Actor implements InputProcessor, Disposable {

    private GameScreen gameScreen;

    private int index;
    private float size;
    private boolean canDraw;

    private String plantName;

    private Sprite sprite;
    private TextureRegion textureRegion;

    private Vector2 coord;
    private int pastScreenX, pastScreenY;

    public PlantActor(GameScreen gameScreen, TextureRegion textureRegion, int index, float size) {
        this.gameScreen = gameScreen;
        this.textureRegion = textureRegion;
        this.sprite = new Sprite(textureRegion);
        this.index = index;

        this.plantName = PlantCodex.plantNames[index];

        this.setBounds(PlantCodex.plantSelectorIndex[index], 64f, size, size);

        this.size = size;
        this.canDraw = false;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        if(canDraw) {
            batch.draw(sprite, this.getX(), this.getY(), size, size);
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

        if(hitActor == this) {
            gameScreen.plant(index, textureRegion);
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
        Actor hitActor = gameScreen.getGameHud().getStage().hit(coord.x, coord.y, true);

        if(hitActor == this) {
            return true;
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
}
