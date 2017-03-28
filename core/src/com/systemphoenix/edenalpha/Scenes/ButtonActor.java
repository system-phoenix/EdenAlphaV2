package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Actors.Plant;
import com.systemphoenix.edenalpha.Codex.ButtonCodex;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class ButtonActor extends Actor implements InputProcessor, Disposable {

    private GameScreen gameScreen;
    private Vector2 coord;
    private Stage stage;

    private int index, pastScreenX, pastScreenY;
    private Sprite beforePressSprite, onPressSprite, voidPressSprite;
    private boolean canDraw = false, canPress = true, isPressed;

    public ButtonActor(int index, GameScreen gameScreen, Stage stage, float x, float y, int size) {
        this.index = index;
        this.stage = stage;
        this.gameScreen = gameScreen;
        this.setBounds(x, y, size, size);
        beforePressSprite = new Sprite(new Texture(Gdx.files.internal("utilities/" + ButtonCodex.beforePress[index])));
        beforePressSprite.setBounds(x, y, size, size);
        onPressSprite = new Sprite(new Texture(Gdx.files.internal("utilities/" + ButtonCodex.onPress[index])));
        onPressSprite.setBounds(x, y, size, size);
        voidPressSprite = new Sprite(new Texture(Gdx.files.internal("utilities/" + ButtonCodex.voidPress[index])));
        voidPressSprite.setBounds(x, y, size, size);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        if(canDraw) {
            if(canPress) {
                if(isPressed) {
                    onPressSprite.draw(batch);
                } else {
                    beforePressSprite.draw(batch);
                }
            } else {
                voidPressSprite.draw(batch);
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
            coord = stage.screenToStageCoordinates(new Vector2((float)screenX, (float)screenY));
            pastScreenX = screenX;
            pastScreenY = screenY;
        }
        Actor hitActor = stage.hit(coord.x, coord.y, true);

        if(hitActor == this && canDraw && canPress) {
            isPressed = true;
            switch (index) {
                case ButtonCodex.PAUSE:
                    gameScreen.setWillPause(true);
                    break;
                case ButtonCodex.PLAY:
                    break;
                case ButtonCodex.FAST_FORWARD:
                    break;
                case ButtonCodex.RESTART:
                    break;
                case ButtonCodex.HOME:
                    break;
                case ButtonCodex.CHECK:
                    PlantActor plantActor = PlantActor.getRecentlySelectedActor();
                    gameScreen.plant(plantActor.getPlantIndex(), plantActor.getSprite());
                    Plant.setSelectAllPlants(false);
                    break;
                case ButtonCodex.CROSS:
                    gameScreen.unroot(Plant.getSelectedPlant());
                    break;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if((pastScreenX == -1 && pastScreenY == -1) || !(pastScreenX == screenX && pastScreenY == screenY)) {
            coord = stage.screenToStageCoordinates(new Vector2((float)screenX, (float)screenY));
            pastScreenX = screenX;
            pastScreenY = screenY;
        }
        Actor hitActor = stage.hit(coord.x, coord.y, true);
        if(hitActor == this ) {
            isPressed = false;
        }
        return hitActor == this;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if((pastScreenX == -1 && pastScreenY == -1) || !(pastScreenX == screenX && pastScreenY == screenY)) {
            coord = stage.screenToStageCoordinates(new Vector2((float)screenX, (float)screenY));
            pastScreenX = screenX;
            pastScreenY = screenY;
        }
        Actor hitActor = stage.hit(coord.x, coord.y, true);
        return hitActor == this;
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
        beforePressSprite.getTexture().dispose();
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }

    public void setCanPress(boolean canPress) {
        this.canPress = canPress;
    }
}
