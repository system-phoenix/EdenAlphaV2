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
import com.systemphoenix.edenalpha.Actors.ObjectActors.Plant;
import com.systemphoenix.edenalpha.Actors.StageActors.AnimalActor;
import com.systemphoenix.edenalpha.Actors.StageActors.ButtonActor;
import com.systemphoenix.edenalpha.Actors.StageActors.PlantActor;
import com.systemphoenix.edenalpha.Codex.AnimalCodex;
import com.systemphoenix.edenalpha.Codex.ButtonCodex;
import com.systemphoenix.edenalpha.Codex.PlantCodex;
import com.systemphoenix.edenalpha.Eden;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class GameHud extends AbsoluteHud implements Disposable {

    private GameScreen gameScreen;
    private Stage plantStage, plantActorStage;

    private PlantSelector plantSelector;
    private PlantActor[] plantActors;

    private ButtonActor uprootButton, upgradeButton, harvestButton, waterUpgradeButton;
    private AnimalActor animalActor;

    private boolean drawable;
    private int index = -1, drawIndex = 1;

    private Label plantName, plantType, plantCost, plantGrowthTime, plantStatsName, plantStatsCost, plantStatsSeedCount;
    private Sprite plantHP, plantAS, plantDmg, plantStatsSprite, plantStatsHP, plantStatsAS, plantStatsDmg, plantStatsRange, plantStatsSeedRate;

    public GameHud(Eden game, GameScreen gameScreen, com.systemphoenix.edenalpha.Actors.StageActors.PlantActor[] plantActors, AnimalActor animalActor) {
        super(game);

        viewport = new FitViewport(worldWidth, worldHeight, new OrthographicCamera());
        plantStage = new Stage(viewport, gameGraphics);
        plantActorStage = new Stage(viewport, gameGraphics);

        this.gameScreen = gameScreen;
        this.plantActors = plantActors;

        plantSelector = new PlantSelector(plantActors, animalActor);

        plantStatsSprite = new Sprite((new Texture(Gdx.files.internal("bgScreen/lowerPlantHud.png"))));

        Table temp = new Table();
        temp.bottom();
        temp.setFillParent(true);
        temp.add(message);

        message.setText("");

        stage.addActor(plantSelector);

        for (int i = 0; i < plantActors.length; i++) {
            plantActorStage.addActor(plantActors[i]);
        }
        if(animalActor != null) {
            this.animalActor = animalActor;
            plantActorStage.addActor(this.animalActor);
        }

        stage.addActor(temp);

        Table tempTable = new Table(), infoTable = new Table(), plantStatsTemp = new Table();
        tempTable.setBounds(0, 0, 111, 140);
        infoTable.setBounds(111, 0, 200, 140);

        populateTable(stage, tempTable, infoTable, false);

        tempTable = new Table();
        tempTable.setBounds(484, 0, 111, 140);
        plantStatsTemp.setBounds(596, 0, 200, 140);

        populateTable(plantStage, tempTable, plantStatsTemp, true);

        tempTable = new Table();
        tempTable.setBounds(worldWidth - 160, 0, 128, 32);

        tempTable.add(plantStatsSeedCount);

        waterUpgradeButton = new com.systemphoenix.edenalpha.Actors.StageActors.ButtonActor(ButtonCodex.WATER_UPGRADE, gameScreen, gameScreen.getWorldWidth() - 160, 0, 128, true, false);
        uprootButton = new com.systemphoenix.edenalpha.Actors.StageActors.ButtonActor(ButtonCodex.UPROOT, gameScreen, 32, 32, 128, true, false);
        upgradeButton = new com.systemphoenix.edenalpha.Actors.StageActors.ButtonActor(ButtonCodex.UPGRADE, gameScreen, worldWidth - 290, 32, 128, true, false);
        harvestButton = new com.systemphoenix.edenalpha.Actors.StageActors.ButtonActor(ButtonCodex.HARVEST, gameScreen, worldWidth - 160, 32, 128, true, false);

        plantStage.addActor(uprootButton);
        plantStage.addActor(upgradeButton);
        plantStage.addActor(harvestButton);
        plantStage.addActor(tempTable);

        uprootButton.setDrawable(drawable);
        stage.addActor(waterUpgradeButton);
    }

    private void populateTable(Stage stage, Table tempTable, Table infoTable, boolean plantStatsTable) {
        Color border = Color.BLACK;
        Color fontColor = Color.WHITE;
        BitmapFont tempFont = font;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arcon.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 15;
        parameter.borderColor = border;
        parameter.borderWidth = 0;
        font = generator.generateFont(parameter);

        tempTable.top().padTop(15);
        infoTable.top().padTop(15);

        Label tempLabel, spaceFiller;

        tempLabel = new Label("Name:", new Label.LabelStyle(font, fontColor));
        tempTable.add(tempLabel);
        tempTable.row();

        if (!plantStatsTable) {
            float startY = 81, startX = 111, decrement = 18f;
            plantName = new Label("--", new Label.LabelStyle(font, fontColor));
            plantType = new Label("--", new Label.LabelStyle(font, fontColor));
            plantCost = new Label("--", new Label.LabelStyle(font, fontColor));
            plantHP = new Sprite(new Texture(Gdx.files.internal("utilities/greenLife.png")));
            plantHP.setBounds(startX, startY - decrement, 32, 8);
            plantAS = new Sprite(new Texture(Gdx.files.internal("utilities/greenLife.png")));
            plantAS.setBounds(startX, startY - decrement * 2, 32, 8);
            plantDmg = new Sprite(new Texture(Gdx.files.internal("utilities/greenLife.png")));
            plantDmg.setBounds(startX, startY - decrement * 3, 32, 8);
            plantGrowthTime = new Label("--", new Label.LabelStyle(font, fontColor));

            tempLabel = new Label("Type:", new Label.LabelStyle(font, fontColor));

            tempTable.add(tempLabel);
            tempTable.row();

            tempLabel = new Label("Cost:", new Label.LabelStyle(font, fontColor));

            tempTable.add(tempLabel);
            tempTable.row();

            infoTable.add(plantName);
            infoTable.row();
            infoTable.add(plantType);
            infoTable.row();
            infoTable.add(plantCost);
            infoTable.row();
        } else {
            float startY = 113, startX = 596, decrement = 18f;
            plantStatsName = new Label("--", new Label.LabelStyle(font, fontColor));
            plantStatsHP = new Sprite(new Texture(Gdx.files.internal("utilities/greenLife.png")));
            plantStatsHP.setBounds(startX, startY - decrement, 32, 8);
            plantStatsAS = new Sprite(new Texture(Gdx.files.internal("utilities/greenLife.png")));
            plantStatsAS.setBounds(startX, startY - decrement * 2, 32, 8);
            plantStatsDmg = new Sprite(new Texture(Gdx.files.internal("utilities/greenLife.png")));
            plantStatsDmg.setBounds(startX, startY - decrement * 3, 32, 8);
            plantStatsSeedRate = new Sprite(new Texture(Gdx.files.internal("utilities/greenLife.png")));
            plantStatsSeedRate.setBounds(startX, startY - decrement * 4, 32, 8);
            plantStatsRange = new Sprite(new Texture(Gdx.files.internal("utilities/greenLife.png")));
            plantStatsRange.setBounds(startX, startY - decrement * 5, 32, 8);


            plantStatsSeedCount = new Label("--", new Label.LabelStyle(font, fontColor));

            infoTable.add(plantStatsName);
            infoTable.row();
        }

        tempLabel = new Label("HP:", new Label.LabelStyle(font, fontColor));
        spaceFiller = new Label(" ", new Label.LabelStyle(font, fontColor));

        tempTable.add(tempLabel);
        infoTable.add(spaceFiller);
        tempTable.row();
        infoTable.row();

        tempLabel = new Label("Atk Spd:", new Label.LabelStyle(font, fontColor));

        tempTable.add(tempLabel);
        infoTable.add(spaceFiller);
        tempTable.row();
        infoTable.row();

        tempLabel = new Label("Damage:", new Label.LabelStyle(font, fontColor));

        tempTable.add(tempLabel);
        infoTable.add(spaceFiller);
        tempTable.row();
        infoTable.row();

        if (!plantStatsTable) {
            tempLabel = new Label("Growth:", new Label.LabelStyle(font, fontColor));

            tempTable.add(tempLabel);
            infoTable.add(plantGrowthTime);
            tempTable.row();
            infoTable.row();
        } else {
            tempLabel = new Label("Seed Rate:", new Label.LabelStyle(font, fontColor));
            tempTable.add(tempLabel);
            infoTable.add(spaceFiller);
            tempTable.row();
            infoTable.row();

            tempLabel = new Label("Range:", new Label.LabelStyle(font, fontColor));
            tempTable.add(tempLabel);
            infoTable.add(spaceFiller);
            tempTable.row();
            infoTable.row();

            tempLabel = new Label("Upgrade:", new Label.LabelStyle(font, fontColor));
            tempTable.add(tempLabel);
            tempTable.row();

            plantStatsCost = new Label("--", new Label.LabelStyle(font, fontColor));
            infoTable.add(plantStatsCost);
            infoTable.row();
        }

        stage.addActor(tempTable);
        stage.addActor(infoTable);
        generator.dispose();
        font = tempFont;
    }

    public void draw(Batch batch) {
        if (drawable) {
            switch (drawIndex) {
                case 0:
                    if (Plant.getSelectedPlant() != null) {
                        Plant plant = Plant.getSelectedPlant();
                        waterUpgradeButton.setCanPress(false);
                        uprootButton.setCanPress(true);
                        if (PlantCodex.seedProduction[plant.getPlantIndex()] != 0) {
                            plantStatsSeedCount.setText("" + (int) plant.getSeeds());
                            harvestButton.setCanPress(true);
                        } else {
                            plantStatsSeedCount.setText("--");
                            harvestButton.setCanPress(false);
                        }
                        if (plant.getUpgradeIndex() < 3 && plant.isUpgradable()) {
                            upgradeButton.setCanPress(true);
                        } else {
                            upgradeButton.setCanPress(false);
                        }
                        batch.setProjectionMatrix(plantStage.getCamera().combined);
                        batch.begin();
                        plantStatsSprite.draw(batch);
                        plantStatsHP.draw(batch);
                        plantStatsAS.draw(batch);
                        plantStatsDmg.draw(batch);
                        plantStatsSeedRate.draw(batch);
                        plantStatsRange.draw(batch);
                        batch.end();
                        plantStage.draw();
                    }
                    break;
                default:
                    waterUpgradeButton.setCanPress(gameScreen.getSeeds() - gameScreen.getWaterUpgradeSeedCost() >= 0);
                    uprootButton.setCanPress(false);
                    harvestButton.setCanPress(false);
                    upgradeButton.setCanPress(false);
                    batch.setProjectionMatrix(stage.getCamera().combined);
                    stage.draw();
                    batch.begin();
                    plantHP.draw(batch);
                    plantDmg.draw(batch);
                    plantAS.draw(batch);
                    batch.end();
            }
        }
        if (drawIndex != 0) plantActorStage.draw();
    }

    @Override
    public void dispose() {
        waterUpgradeButton.dispose();
        uprootButton.dispose();
        upgradeButton.dispose();
        harvestButton.dispose();
        waterUpgradeButton.dispose();
        stage.dispose();
        plantStage.dispose();

        plantSelector.dispose();
        if(animalActor != null) {
            animalActor.dispose();
        }
        plantHP.getTexture().dispose();
        plantAS.getTexture().dispose();
        plantDmg.getTexture().dispose();

        plantStatsSprite.getTexture().dispose();
        plantStatsHP.getTexture().dispose();
        plantStatsAS.getTexture().dispose();
        plantStatsDmg.getTexture().dispose();
        plantStatsSeedRate.getTexture().dispose();
        plantStatsRange.getTexture().dispose();
    }

    public void setData() {
        this.index = com.systemphoenix.edenalpha.Actors.StageActors.PlantActor.getRecentlySelectedActor().getPlantIndex();
        float barSize = 200f;
        if(!com.systemphoenix.edenalpha.Actors.StageActors.PlantActor.getRecentlySelectedActor().isAnimal()) {
            plantName.setText(PlantCodex.plantName[index]);
            String plantType = "";
            switch (PlantCodex.typeBit[index]) {
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
            plantCost.setText("" + (int) (PlantCodex.cost[index] / 2) + " seeds, " + (int) (PlantCodex.cost[index]) + " water");
            plantHP.setBounds(plantHP.getX(), plantHP.getY(), barSize * (PlantCodex.hpStats[PlantCodex.maxHP[index]] / PlantCodex.baseHP), plantHP.getHeight());
            if (index != 13) {
                plantAS.setBounds(plantAS.getX(), plantAS.getY(), barSize * ((float) PlantCodex.baseAS / (float) (PlantCodex.asStats[PlantCodex.AS[index]] * 4f)), plantAS.getHeight());
            } else {
                plantAS.setBounds(plantAS.getX(), plantAS.getY(), 0, plantAS.getHeight());
            }
            plantDmg.setBounds(plantDmg.getX(), plantDmg.getY(), barSize * ((((PlantCodex.dmgStats[PlantCodex.DMG[index]].x + PlantCodex.dmgStats[PlantCodex.DMG[index]].y)) / (((PlantCodex.baseDmg.x + PlantCodex.baseDmg.y))))), plantDmg.getHeight());
            plantGrowthTime.setText("" + (int) (PlantCodex.growthTime[index]) + " seconds");
        }
    }

    public void setPlantStatsData() {
        Plant plant = Plant.getSelectedPlant();
        this.index = plant.getPlantIndex();
        float barSize = 200f;
        plantStatsName.setText(PlantCodex.plantName[index]);
        plantStatsHP.setBounds(plantStatsHP.getX(), plantStatsHP.getY(), barSize * (plant.getTargetHp() / PlantCodex.baseHP), plantStatsHP.getHeight());
        if (index != 13) {
            plantStatsAS.setBounds(plantStatsAS.getX(), plantStatsAS.getY(), barSize * ((float) PlantCodex.baseAS / (float) (plant.getAttackSpeed() * 4f)), plantStatsAS.getHeight());
        } else {
            plantStatsAS.setBounds(plantStatsAS.getX(), plantStatsAS.getY(), 0, plantStatsAS.getHeight());
        }
        plantStatsDmg.setBounds(plantStatsDmg.getX(), plantStatsDmg.getY(), barSize * ((((plant.getDamage().x + plant.getDamage().y)) / (((PlantCodex.baseDmg.x + PlantCodex.baseDmg.y))))), plantStatsDmg.getHeight());
        plantStatsSeedRate.setBounds(plantStatsSeedRate.getX(), plantStatsSeedRate.getY(), barSize * (plant.getSeedRate() / PlantCodex.baseSeedRate), plantStatsSeedRate.getHeight());
        plantStatsRange.setBounds(plantStatsRange.getX(), plantStatsRange.getY(), barSize * (plant.getRange() / PlantCodex.baseRange), plantStatsRange.getHeight());
        if (plant.getUpgradeIndex() < 3) {
            plantStatsCost.setText("" + (int) (plant.getUpgradeCostSunlight()) + " sunlight, " + (int) (plant.getUpgradeCostWater()) + " water");
        } else {
            plantStatsCost.setText("Fully Upgraded!");
        }
    }

    public void setAnimalData() {
        this.index = animalActor.getAnimalIndex();
        float barSize = 200;
        plantName.setText(AnimalCodex.name[index]);
        plantType.setText("");
        plantCost.setText("" + (int) (50) + " seeds, " + 100 + " water");
        plantHP.setBounds(plantHP.getX(), plantHP.getY(), 0, plantHP.getHeight());
        plantAS.setBounds(plantAS.getX(), plantAS.getY(), 0, plantAS.getHeight());
        plantDmg.setBounds(plantDmg.getX(), plantDmg.getY(), barSize * (AnimalCodex.DMG[AnimalCodex.dmgStats[index]] / AnimalCodex.DMG[AnimalCodex.HIGHEST]), plantDmg.getHeight());
        plantGrowthTime.setText("--");
    }

    public void setDrawable(boolean drawable) {
        this.drawable = drawable;
        plantSelector.setDrawable(drawable);
        waterUpgradeButton.setDrawable(drawable);
        uprootButton.setDrawable(drawable);
        upgradeButton.setDrawable(drawable);
        harvestButton.setDrawable(drawable);
//        Plant.setSelectAllPlants(!drawable);
    }

    public void setCanPress(boolean canPress) {
        uprootButton.setCanPress(canPress);
        waterUpgradeButton.setCanPress(canPress);
        upgradeButton.setCanPress(canPress);
        harvestButton.setCanPress(canPress);
    }

    public void setDrawIndex(int drawIndex) {
        this.drawIndex = drawIndex;
    }

    public PlantActor[] getPlantActors() {
        return plantActors;
    }

    public boolean isDrawable() {
        return drawable;
    }

    public int getDrawIndex() {
        return drawIndex;
    }

    public Stage getPlantStage() {
        return plantStage;
    }

    public Stage getPlantActorStage() {
        return plantActorStage;
    }

}
