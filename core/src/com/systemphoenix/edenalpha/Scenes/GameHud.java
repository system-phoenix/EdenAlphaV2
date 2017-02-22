package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.EdenAlpha;

public class GameHud extends AbsoluteHud implements Disposable {

    private PlantSelector plantSelector;
    private boolean canDraw;

    public GameHud(EdenAlpha game) {
        super(game);

        plantSelector = new PlantSelector();

        Table temp = new Table();
        temp.bottom();
        temp.setFillParent(true);
        temp.add(message);

        message.setText("");

        stage.addActor(plantSelector);
        stage.addActor(temp);
    }

    public void draw() {
        if(canDraw) {
            stage.draw();
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        plantSelector.dispose();
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
        plantSelector.setCanDraw(canDraw);
    }

}
