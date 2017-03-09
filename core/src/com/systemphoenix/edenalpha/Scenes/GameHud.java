package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Codex.ButtonCodex;
import com.systemphoenix.edenalpha.EdenAlpha;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class GameHud extends AbsoluteHud implements Disposable {
    
    private GameScreen gameScreen;
    
    private PlantSelector plantSelector;
    private PlantActor[] plantActors;

    private ButtonActor checkButton;

    private boolean canDraw;

    public GameHud(EdenAlpha game, GameScreen gameScreen, PlantActor[] plantActors) {
        super(game);
        
        this.gameScreen = gameScreen;
        this.plantActors = plantActors;

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
        if(PlantActor.getRecentlySelectedActor() != null) {
            checkButton.setCanDraw(true);
        }
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
    
    public void setCheckButtonCanDraw(boolean canDraw) {
        if(checkButton == null) {
            checkButton = new ButtonActor(ButtonCodex.CHECK, gameScreen, stage, gameScreen.getWorldWidth() - 160, 32, 128);
            stage.addActor(checkButton);
            gameScreen.addProcessor(checkButton);
        }
        this.checkButton.setCanDraw(canDraw);
    }

    public PlantActor[] getPlantActors() {
        return plantActors;
    }

}
