package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.systemphoenix.edenalpha.Actors.Plant;
import com.systemphoenix.edenalpha.Codex.ButtonCodex;
import com.systemphoenix.edenalpha.Codex.PlantCodex;
import com.systemphoenix.edenalpha.EdenAlpha;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class GameHud extends AbsoluteHud implements Disposable {

    private GameScreen gameScreen;
    private Stage plantStage;
    
    private PlantSelector plantSelector;
    private PlantActor[] plantActors;

    private ButtonActor checkButton, crossButton;

    private boolean canDraw;
    private int index = -1, drawable = 1;

    private Label plantName, plantType, plantCost, plantGrowthTime;
    private Sprite plantHP, plantAS, plantDmg;

    public GameHud(EdenAlpha game, GameScreen gameScreen, PlantActor[] plantActors) {
        super(game);

        viewport = new FitViewport(worldWidth, worldHeight, new OrthographicCamera());
        plantStage = new Stage(viewport, gameGraphics);
        
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

        Label tempLabel, spaceFiller;
        Table tempTable = new Table(), infoTable = new Table();
        tempTable.setBounds(0, 0, 111, 140);
        infoTable.setBounds(111, 0, 200, 140);

        Color border = Color.BLACK;
        Color fontColor = Color.WHITE;
        BitmapFont tempFont = font;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/neuropol.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 14;
        parameter.borderColor = border;
        parameter.borderWidth = 0;
        font = generator.generateFont(parameter);

        tempTable.top().padTop(15);
        infoTable.top().padTop(15);

        tempLabel = new Label("Name:", new Label.LabelStyle(font, fontColor));
        plantName = new Label("--", new Label.LabelStyle(font, fontColor));

        tempTable.add(tempLabel);
        infoTable.add(plantName);
        tempTable.row();
        infoTable.row();

        tempLabel = new Label("Type:", new Label.LabelStyle(font, fontColor));
        plantType = new Label("--", new Label.LabelStyle(font, fontColor));

        tempTable.add(tempLabel);
        infoTable.add(plantType);
        tempTable.row();
        infoTable.row();

        tempLabel = new Label("Cost:", new Label.LabelStyle(font, fontColor));
        plantCost = new Label("--", new Label.LabelStyle(font, fontColor));

        tempTable.add(tempLabel);
        infoTable.add(plantCost);
        tempTable.row();
        infoTable.row();

        float startY = 85, startX = 111;

        tempLabel = new Label("HP:", new Label.LabelStyle(font, fontColor));
        spaceFiller = new Label(" ", new Label.LabelStyle(font, fontColor));
        plantHP = new Sprite(new Texture(Gdx.files.internal("utilities/greenLife.png")));
        plantHP.setBounds(startX, startY - 16, 32, 8);

        tempTable.add(tempLabel);
        infoTable.add(spaceFiller);
        tempTable.row();
        infoTable.row();

        tempLabel = new Label("Atk Spd:", new Label.LabelStyle(font, fontColor));
        plantAS = new Sprite(new Texture(Gdx.files.internal("utilities/greenLife.png")));
        plantAS.setBounds(startX, startY - 32, 32, 8);

        tempTable.add(tempLabel);
        infoTable.add(spaceFiller);
        tempTable.row();
        infoTable.row();

        tempLabel = new Label("Damage:", new Label.LabelStyle(font, fontColor));
        plantDmg = new Sprite(new Texture(Gdx.files.internal("utilities/greenLife.png")));
        plantDmg.setBounds(startX, startY - 48, 32, 8);

        tempTable.add(tempLabel);
        infoTable.add(spaceFiller);
        tempTable.row();
        infoTable.row();

        tempLabel = new Label("Growth:", new Label.LabelStyle(font, fontColor));
        plantGrowthTime = new Label("--", new Label.LabelStyle(font, fontColor));

        tempTable.add(tempLabel);
        infoTable.add(plantGrowthTime);
        tempTable.row();
        infoTable.row();

        stage.addActor(tempTable);
        stage.addActor(infoTable);
        generator.dispose();
        font = tempFont;

        checkButton = new ButtonActor(ButtonCodex.CHECK, gameScreen, stage, gameScreen.getWorldWidth() - 160, 32, 128, true, false);
        crossButton = new ButtonActor(ButtonCodex.CROSS, gameScreen, plantStage, 32, 32, 128, true, false);
        plantStage.addActor(crossButton);
        crossButton.setCanDraw(canDraw);
        stage.addActor(checkButton);
    }

    public void draw(Batch batch) {
        if(canDraw) {
            switch(drawable) {
                case 0:
                    if(Plant.getSelectedPlant() != null) {
                        checkButton.setCanPress(false);
                        crossButton.setCanPress(true);
                        batch.setProjectionMatrix(plantStage.getCamera().combined);
                        plantStage.draw();
                    }
                    break;
                default:
                    if(index > -1 && index < 15) {
                        if(PlantCodex.cost[index] < gameScreen.getSeeds()) {
                            checkButton.setCanPress(true);
                        } else {
                            checkButton.setCanPress(false);
                        }
                    }
                    crossButton.setCanPress(false);
                    batch.setProjectionMatrix(stage.getCamera().combined);
                    stage.draw();
                    batch.begin();
                    plantHP.draw(batch);
                    plantDmg.draw(batch);
                    plantAS.draw(batch);
                    batch.end();
            }
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        plantSelector.dispose();
        plantHP.getTexture().dispose();
        plantAS.getTexture().dispose();
        plantDmg.getTexture().dispose();
    }

    public void setData() {
        this.index = PlantActor.getRecentlySelectedActor().getPlantIndex();
        float barSize = 200f;
        plantName.setText(PlantCodex.plantName[index]);
        String plantType = "";
        switch(PlantCodex.typeBit[index]) {
            case 1:
                plantType = "         Land Plant        ";
                break;
            case 2:
                plantType = "      Salt-water plant     ";
                break;
            case 4:
                plantType = "     Fresh-water plant     ";
                break;
            case 1 | 2:
                plantType = "   Land/salt-water plant   ";
                break;
            case 1 | 4:
                plantType = "  Land/fresh-water plant   ";
                break;
            case 2 | 4:
                plantType = "  Salt/fresh-water plant   ";
                break;
            case 1 | 2 | 4:
                plantType = "Land/salt/fresh-water plant";
                break;
        }
        this.plantType.setText(plantType);
        plantCost.setText("" + (int)(PlantCodex.cost[index]));
        plantHP.setBounds(plantHP.getX(), plantHP.getY(), barSize * (PlantCodex.hpStats[PlantCodex.maxHP[index]] / PlantCodex.baseHP), plantHP.getHeight());
        if(index != 13) {
            plantAS.setBounds(plantAS.getX(), plantAS.getY(), barSize * ((float)PlantCodex.baseAS / (float)(PlantCodex.asStats[PlantCodex.AS[index]] * 3f)), plantAS.getHeight());
        } else {
            plantAS.setBounds(plantAS.getX(), plantAS.getY(), 0, plantAS.getHeight());
        }
        plantDmg.setBounds(plantDmg.getX(), plantDmg.getY(), barSize * ((((PlantCodex.dmgStats[PlantCodex.DMG[index]].x + PlantCodex.dmgStats[PlantCodex.DMG[index]].y)) / (((PlantCodex.baseDmg.x + PlantCodex.baseDmg.y))))), plantDmg.getHeight());
        plantGrowthTime.setText("" + (int)(PlantCodex.growthTime[index]) + " seconds");
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
        plantSelector.setCanDraw(canDraw);
        checkButton.setCanDraw(canDraw);
        crossButton.setCanDraw(canDraw);
        Plant.setSelectAllPlants(canDraw);
    }

    public void setCanPress(boolean canPress) {
        crossButton.setCanPress(canPress);
        checkButton.setCanPress(canPress);
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public PlantActor[] getPlantActors() {
        return plantActors;
    }

    public boolean canDraw() {
        return canDraw;
    }

    public int getDrawable() {
        return drawable;
    }

    public Stage getPlantStage() {
        return plantStage;
    }

}
