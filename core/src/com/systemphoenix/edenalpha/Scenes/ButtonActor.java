package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Actors.Plant;
import com.systemphoenix.edenalpha.Codex.ButtonCodex;
import com.systemphoenix.edenalpha.Screens.GameScreen;
import com.systemphoenix.edenalpha.Screens.PlantScreen;

public class ButtonActor extends Actor implements Disposable {

    private Screen screen;
    private Stage stage;

    private int index;
    private Sprite beforePressSprite, onPressSprite, voidPressSprite;
    private boolean canDraw = false, canPress = true, isPressed, isGameScreen, isPlantScreen;

    public ButtonActor(final int index, Screen screen, Stage stage, float x, float y, int size, final boolean isGameScreen, final boolean isPlantScreen) {
        this.index = index;
        this.stage = stage;
        this.screen = screen;
        this.isGameScreen = isGameScreen;
        this.isPlantScreen = isPlantScreen;
        beforePressSprite = new Sprite(new Texture(Gdx.files.internal("utilities/" + ButtonCodex.beforePress[index])));
        beforePressSprite.setBounds(x, y, size, size);
        onPressSprite = new Sprite(new Texture(Gdx.files.internal("utilities/" + ButtonCodex.onPress[index])));
        onPressSprite.setBounds(x, y, size, size);
        voidPressSprite = new Sprite(new Texture(Gdx.files.internal("utilities/" + ButtonCodex.voidPress[index])));
        voidPressSprite.setBounds(x, y, size, size);

        this.setBounds(beforePressSprite.getX(), beforePressSprite.getY(), beforePressSprite.getWidth(), beforePressSprite.getHeight());
        this.setTouchable(Touchable.enabled);
        this.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return ButtonActor.this.triggerAction();
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                isPressed = false;
            }
        });
    }

    public boolean triggerAction() {
        if(canPress) {
            isPressed = true;
            switch (index) {
                case ButtonCodex.PAUSE:
                    if(isGameScreen) {
                        Gdx.app.log("Verbose", "touched PAUSE button");
                        ((GameScreen)screen).setWillPause(true);
                    }
                    break;
                case ButtonCodex.PLAY:
                    if(isGameScreen) {

                    } else if(isPlantScreen) {
                        ((PlantScreen)screen).createGameScreen();
                    }
                    break;
                case ButtonCodex.FAST_FORWARD:
                    break;
                case ButtonCodex.RESTART:
                    break;
                case ButtonCodex.HOME:
                    if(isGameScreen) {

                    } else if(isPlantScreen) {
                        ((PlantScreen)screen).backToMapScreen();
                    }
                    break;
                case ButtonCodex.CHECK:
                    if(isGameScreen) {
                        PlantActor plantActor = PlantActor.getRecentlySelectedActor();
                        ((GameScreen)screen).plant(plantActor.getPlantIndex(), plantActor.getSprite());
                        Plant.setSelectAllPlants(false);
                    } else if(isPlantScreen) {
                        ((PlantScreen)screen).selectPlant();
                    }
                    break;
                case ButtonCodex.CROSS:
                    if(isGameScreen) {
                        ((GameScreen)screen).unroot(Plant.getSelectedPlant());
                    } else if(isPlantScreen) {
                        ((PlantScreen)screen).selectPlant();
                    }
                    break;
            }
            return true;
        }
        return false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        switch(index) {
            case ButtonCodex.CHECK:
                if(isGameScreen) {
                    canPress = PlantActor.getRecentlySelectedActor() != null;
                }
                break;
            case ButtonCodex.CROSS:
                if(isGameScreen) {
                    canPress = Plant.getSelectedPlant() != null;
                }
                break;
        }
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
