package com.systemphoenix.edenalpha.Actors.StageActors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.systemphoenix.edenalpha.Screens.PlantScreen;

public class AnimalButton extends PlantButton {

    public AnimalButton(Sprite sprite, Stage stage, final PlantScreen plantScreen, final int index) {
        super(sprite, stage, plantScreen, index);

        this.clearListeners();
        this.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Verbose", "animal index: " + index);
                plantScreen.setAnimalData(index);
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }
        });
    }
}
