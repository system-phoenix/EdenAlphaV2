package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.EdenAlpha;
import com.systemphoenix.edenalpha.Screens.MapScreen;

public class MapSelect extends AbsoluteHud implements Disposable {

    private MapActor left, right;

    public MapSelect(EdenAlpha game, MapScreen mapScreen, float worldWidth, float worldHeight) {
        super(game);
        left = new MapActor(mapScreen, this, worldWidth, worldHeight, true);
        right = new MapActor(mapScreen, this, worldWidth, worldHeight, false);

        stage.addActor(left);
        stage.addActor(right);
    }

    public void setCanDraw(boolean left, boolean right) {
        this.left.setCanDraw(left);
        this.right.setCanDraw(right);
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
    }

}
