package com.systemphoenix.edenalpha.Actors.StageActors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.systemphoenix.edenalpha.Screens.AbsoluteScreen;
import com.systemphoenix.edenalpha.Screens.PlantScreen;

public class BlankActor extends Actor {

    private AbsoluteScreen screen;

    public BlankActor(AbsoluteScreen screen, float x, float y, float width, float height) {
        this.screen = screen;

        this.setBounds(x, y, width, height);
        this.setTouchable(Touchable.enabled);
        this.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return BlankActor.this.triggerAction();
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }
        });
    }

    public boolean triggerAction() {
        ((PlantScreen) screen).toggleDescStage();
        return true;
    }

}
