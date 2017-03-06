package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.EdenAlpha;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class GameHud extends AbsoluteHud implements Disposable {

    private PlantSelector plantSelector;
    private PlantActor[] plantActors;

    private Texture toSplit;

    private boolean canDraw;

    public GameHud(EdenAlpha game, GameScreen gameScreen, PlantActor[] plantActors) {
        super(game);

        toSplit = new Texture(Gdx.files.internal("trees/treesFinal.png"));
        TextureRegion[][] tempRegion = TextureRegion.split(toSplit, 64, 64);

        this.plantActors = plantActors;
//        int counter = 0;
//        plantActors = new PlantActor[6];
//        for(int i = 0; i < tempRegion.length; i++) {
//            for(int j = 0; j < tempRegion[i].length; j++) {
//                if(counter < plantActors.length) {
//                    plantActors[counter] = new PlantActor(gameScreen, tempRegion[i][j], counter, counter, 64);
//                    counter++;
//                }
//            }
//
//        }

        plantSelector = new PlantSelector(plantActors);

        Table temp = new Table();
        temp.bottom();
        temp.setFillParent(true);
        temp.add(message);

        message.setText("");

        stage.addActor(plantSelector);

        for(int i = 0; i < plantActors.length; i++) {
            stage.addActor(plantActors[i]);
        }

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
        toSplit.dispose();
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
        plantSelector.setCanDraw(canDraw);
    }

    public PlantActor[] getPlantActors() {
        return plantActors;
    }

}
