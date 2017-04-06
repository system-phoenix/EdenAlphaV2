package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Actors.Plant;
import com.systemphoenix.edenalpha.Codex.ButtonCodex;
import com.systemphoenix.edenalpha.Screens.AbsoluteScreen;
import com.systemphoenix.edenalpha.Screens.GameScreen;
import com.systemphoenix.edenalpha.Screens.MapScreen;
import com.systemphoenix.edenalpha.Screens.PlantScreen;
import com.systemphoenix.edenalpha.Screens.TutorialMapScreen;

public class ButtonActor extends Actor implements Disposable {

    private AbsoluteScreen screen;

    private int index;
    private Sprite beforePressSprite, onPressSprite, voidPressSprite;
    private boolean canDraw = false, canPress = true, isPressed, isGameScreen, isPlantScreen, isMapScreen, isTutorialScreen;

    public ButtonActor(final int index, AbsoluteScreen screen, float x, float y, int size, final boolean isGameScreen, final boolean isPlantScreen) {
        this.index = index;
        this.screen = screen;
        this.isGameScreen = isGameScreen;
        this.isPlantScreen = isPlantScreen;
        this.isTutorialScreen = this.isMapScreen = false;
        initialize(x, y, size);
    }

    public ButtonActor(final int index, AbsoluteScreen screen, float x, float y, int size, final boolean isMapScreen) {
        this.index = index;
        this.screen = screen;
        this.isGameScreen = false;
        this.isPlantScreen = false;
        this.isMapScreen = isMapScreen;
        this.isTutorialScreen = !isMapScreen;
        initialize(x, y, size);
    }

    private void initialize(float x, float y, float size) {
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
                actionTriggered();
            }
        });
    }

    public boolean triggerAction() {
        if(canPress) {
            isPressed = true;
            return true;
        }
        return false;
    }

    public void actionTriggered() {
        switch (index) {
            case ButtonCodex.PAUSE:
                if(isGameScreen) {
                    ((GameScreen)screen).setWillPause(true);
                }
                break;
            case ButtonCodex.PLAY:
                if(isGameScreen) {
                    ((GameScreen)screen).tap();
                } else if(isPlantScreen) {
                    ((PlantScreen)screen).createGameScreen();
                } else if(isMapScreen) {
                    ((MapScreen)screen).createPlantScreen();
                } else if(isTutorialScreen) {
                    ((TutorialMapScreen)screen).createMapScreen();
                }
                break;
            case ButtonCodex.FAST_FORWARD:
                break;
            case ButtonCodex.RESTART:
                if(isGameScreen) {
                    ((GameScreen)screen).setWillRestart(true);
                }
                break;
            case ButtonCodex.HOME:
                if(isGameScreen || isTutorialScreen) {
                    (screen).setWillDispose(true);
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
            case ButtonCodex.UPGRADE:
                if(isGameScreen && Plant.getSelectedPlant() != null) {
                    Plant.getSelectedPlant().upgrade();
                }
                break;
            case ButtonCodex.HARVEST:
                if(isGameScreen && Plant.getSelectedPlant() != null) {
                    if(Plant.getSelectedPlant().getSeeds() >= 1) {
                        ((GameScreen)screen).harvestSeeds(Plant.getSelectedPlant());
                    }
                }
                break;
            case ButtonCodex.WATER_UPGRADE:
                if(isGameScreen) {
                    ((GameScreen)screen).upgradeWaterRate();
                }
                break;

        }
        isPressed = false;
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
