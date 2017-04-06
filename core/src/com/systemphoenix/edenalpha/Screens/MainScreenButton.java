package com.systemphoenix.edenalpha.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Disposable;

public class MainScreenButton extends Actor implements Disposable {

    private MainScreen mainScreen;

    private Sprite buttonSprite, onPressSprite;

    private boolean startButton, pressed = false;

    public MainScreenButton(MainScreen mainScreen, String fileName, float x, float y, float width, float height, boolean startButton) {
        this.mainScreen = mainScreen;
        this.startButton = startButton;
        buttonSprite = new Sprite(new Texture(Gdx.files.internal("utilities/beforePress_" + fileName)));
        buttonSprite.setBounds(x, y, width, height);

        onPressSprite = new Sprite(new Texture(Gdx.files.internal("utilities/onPress_" + fileName)));
        onPressSprite.setBounds(x, y, width, height);

        this.setBounds(x, y, width, height);
        this.setTouchable(Touchable.enabled);

        this.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                pressed = true;
                return MainScreenButton.this.triggerAction();
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                pressed = false;
            }
        });
    }

    public boolean triggerAction() {
        if(startButton) {
            if(mainScreen.isFirstTimePlaying()) {
                mainScreen.createTutorialScreen();
            } else {
                mainScreen.createMapScreen();
            }
        } else {
            mainScreen.createTutorialScreen();
        }
        return true;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        if(!pressed) {
            buttonSprite.draw(batch);
        } else {
            onPressSprite.draw(batch);
        }
    }

    @Override
    public void dispose() {
        buttonSprite.getTexture().dispose();
    }
}
