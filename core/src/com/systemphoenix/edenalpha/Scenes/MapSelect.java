package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Actors.StageActors.MapActor;
import com.systemphoenix.edenalpha.Codex.ButtonCodex;
import com.systemphoenix.edenalpha.Eden;
import com.systemphoenix.edenalpha.Screens.MapScreen;

public class MapSelect extends AbsoluteHud implements Disposable {

    private com.systemphoenix.edenalpha.Actors.StageActors.MapActor left, right;
    private com.systemphoenix.edenalpha.Actors.StageActors.ButtonActor playButton;

    public MapSelect(Eden game, MapScreen mapScreen) {
        super(game);
        left = new com.systemphoenix.edenalpha.Actors.StageActors.MapActor(mapScreen, worldWidth, worldHeight, true, false);
        right = new com.systemphoenix.edenalpha.Actors.StageActors.MapActor(mapScreen, worldWidth, worldHeight, false, false);

        playButton = new com.systemphoenix.edenalpha.Actors.StageActors.ButtonActor(ButtonCodex.PLAY, mapScreen, worldWidth / 2 - 64, 64, 128, true);
        stage.addActor(left);
        stage.addActor(right);
        stage.addActor(playButton);
        playButton.setDrawable(true);
    }

    public void setDrawable(boolean left, boolean right) {
        this.left.setDrawable(left);
        this.right.setDrawable(right);
    }

    public void setLeftCanDraw(boolean drawable) {
        this.left.setDrawable(drawable);
    }

    public void setRightCanDraw(boolean drawable) {
        this.right.setDrawable(drawable);
    }

    public Stage getStage() {
        return this.stage;
    }

    public com.systemphoenix.edenalpha.Actors.StageActors.MapActor getLeft() {
        return left;
    }

    public MapActor getRight() {
        return right;
    }

    @Override
    public void dispose() {
        stage.dispose();
        left.dispose();
        right.dispose();
        playButton.dispose();
    }

}
