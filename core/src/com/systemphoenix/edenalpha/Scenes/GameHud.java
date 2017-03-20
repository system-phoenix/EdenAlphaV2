package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Actors.Plant;
import com.systemphoenix.edenalpha.Codex.ButtonCodex;
import com.systemphoenix.edenalpha.Codex.PlantCodex;
import com.systemphoenix.edenalpha.EdenAlpha;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class GameHud extends AbsoluteHud implements Disposable {

    private GameScreen gameScreen;
    
    private PlantSelector plantSelector;
    private PlantActor[] plantActors;

    private ButtonActor checkButton;

    private boolean canDraw, canDrawCheck;
    private int index;

    private Label plantName, plantType, plantCost, plantGrowthTime;
    private Sprite plantHP, plantAS, plantDmg, rectSprite;

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

        Label tempLabel, spaceFiller;
        Table tempTable = new Table(), infoTable = new Table(), renderTable;
        tempTable.setBounds(0, 30, 111, 140);
        infoTable.setBounds(111, 30, 200, 140);
        renderTable = infoTable;

        rectSprite = new Sprite(new Texture(Gdx.files.internal("misc/0_PlantSquare.png")));
        rectSprite.setBounds(renderTable.getX(), renderTable.getY(), renderTable.getWidth(), renderTable.getHeight());

        Color border = Color.BLACK;
//        Color fontColor = new Color(0.5f, 1f, 0.5f, 1f);
        Color fontColor = Color.WHITE;
        BitmapFont tempFont = font;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/neuropol.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 14;
        parameter.borderColor = border;
        parameter.borderWidth = 1;
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

        float startY = 115, startX = 111;

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

    }

    public void draw(Batch batch) {
        if(canDraw) {
            batch.begin();
            plantHP.draw(batch);
            plantDmg.draw(batch);
            plantAS.draw(batch);
            batch.end();
            stage.draw();
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
        plantHP.setBounds(plantHP.getX(), plantHP.getY(), barSize * (PlantCodex.maxHP[index] / PlantCodex.baseHP), plantHP.getHeight());
        if(index != 13) {
            plantAS.setBounds(plantAS.getX(), plantAS.getY(), barSize * ((float)PlantCodex.baseAS / (float)(PlantCodex.AS[index] * 3f)), plantAS.getHeight());
        } else {
            plantAS.setBounds(plantAS.getX(), plantAS.getY(), 0, plantAS.getHeight());
        }
        plantDmg.setBounds(plantDmg.getX(), plantDmg.getY(), barSize * ((((PlantCodex.DMG[index].x + PlantCodex.DMG[index].y)) / (((PlantCodex.baseDmg.x + PlantCodex.baseDmg.y))))), plantDmg.getHeight());
        plantGrowthTime.setText("" + (int)(PlantCodex.growthTime[index]) + " seconds");
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
        plantSelector.setCanDraw(canDraw);
        if(canDrawCheck) {
            checkButton.setCanDraw(canDraw);
            Plant.setSelectAllPlants(canDraw);
        }
    }

    public void setCanDraw() {
        canDrawCheck = true;
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

    public boolean canDraw() {
        return canDraw;
    }

}
