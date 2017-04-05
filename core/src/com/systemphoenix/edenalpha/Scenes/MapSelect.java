package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Codex.ButtonCodex;
import com.systemphoenix.edenalpha.EdenAlpha;
import com.systemphoenix.edenalpha.Screens.MapScreen;

public class MapSelect extends AbsoluteHud implements Disposable {

    private MapActor left, right;
    private ButtonActor playButton;

    public MapSelect(EdenAlpha game, MapScreen mapScreen) {
        super(game);
        left = new MapActor(mapScreen, worldWidth, worldHeight, true, false);
        right = new MapActor(mapScreen, worldWidth, worldHeight, false, false);

        playButton = new ButtonActor(ButtonCodex.PLAY, mapScreen, worldWidth / 2 - 64, 64, 128, true);
        stage.addActor(left);
        stage.addActor(right);
        stage.addActor(playButton);
        playButton.setCanDraw(true);
    }

    public void setCanDraw(boolean left, boolean right) {
        this.left.setCanDraw(left);
        this.right.setCanDraw(right);
    }

    public void setLeftCanDraw(boolean canDraw) {
        this.left.setCanDraw(canDraw);
    }

    public void setRightCanDraw(boolean canDraw) {
        this.right.setCanDraw(canDraw);
    }

    public Stage getStage() {
        return this.stage;
    }

    public MapActor getLeft() {
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
