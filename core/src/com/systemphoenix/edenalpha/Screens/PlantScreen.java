package com.systemphoenix.edenalpha.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.systemphoenix.edenalpha.Codex.AnimalCodex;
import com.systemphoenix.edenalpha.Codex.ButtonCodex;
import com.systemphoenix.edenalpha.Codex.PlantCodex;
import com.systemphoenix.edenalpha.Eden;
import com.systemphoenix.edenalpha.WindowUtils.Region;
import com.systemphoenix.edenalpha.Actors.StageActors.AnimalActor;
import com.systemphoenix.edenalpha.Actors.StageActors.AnimalButton;
import com.systemphoenix.edenalpha.Actors.StageActors.ButtonActor;
import com.systemphoenix.edenalpha.Actors.StageActors.PlantActor;
import com.systemphoenix.edenalpha.Actors.StageActors.PlantButton;

public class PlantScreen extends AbsoluteScreen {

    private Stage stage, checkStage, crossStage;
    private Texture trees, plants, animals;
    private Sprite lowerHud, plantScreenBG, rectangleSprite, plantHP, plantAS, plantDmg, plantRange, plantSeedRate, animalActorSprite;
    private Sprite[] sprites, actorSprites, renderedActorSprites;
    private Rectangle actorRectangles[], rectangles[][];

    private ButtonActor playButton, checkButton, crossButton, homeButton;

    private SpriteBatch gameGraphics;
    private Label plantName, plantType, plantCost, plantGrowthTime, plantDescription[];

    private MapScreen mapScreen;
    private Region region;

    private int selectedIndeces[], currentSelectionIndex, index, selectedAnimalIndex = -1;
    private boolean selected[];

    public PlantScreen(Eden game, MapScreen mapScreen, Region region) {
        super(game);
        this.mapScreen = mapScreen;
        this.region = region;

        this.gameGraphics = game.getGameGraphics();
        this.selectedIndeces = new int[6];
        this.actorRectangles = new Rectangle[6];
        this.selected = new boolean[20];

        for(int i = 0; i < selectedIndeces.length; i++) {
            selectedIndeces[i] = -1;
        }

        for(int i = 0; i < selected.length; i++) {
            selected[i] = false;
        }

        currentSelectionIndex = 0;

        this.sprites = new Sprite[20];
        this.actorSprites = new Sprite[20];
        this.renderedActorSprites = new Sprite[6];

        this.rectangles = new Rectangle[4][5];
        for(int i = 0; i < rectangles.length - 1; i++) {
            for(int j = 0; j < rectangles[i].length; j++) {
                rectangles[i][j] = new Rectangle((64 + (j * 144)), (720 - (128 + (i * 128))), 96f, 96f);
            }
        }

        for(int j = 0; j < rectangles[rectangles.length - 1].length; j++) {
            if(!(j == 0 || j + 1 == rectangles[rectangles.length - 1].length)) {
                rectangles[rectangles.length - 1][j] = new Rectangle((64 + (j * 144)), 176, 96f, 96f);
            }
        }

        initialize();
        Gdx.input.setCatchBackKey(true);
    }

    private void initialize() {
        Viewport viewport = new FitViewport(1280, 720, cam);
        stage = new Stage(viewport, gameGraphics);
        checkStage = new Stage(viewport, gameGraphics);
        crossStage = new Stage(viewport, gameGraphics);

        lowerHud = new Sprite(new Texture(Gdx.files.internal("misc/lowerHud.png")));
        plantScreenBG = new Sprite(new Texture(Gdx.files.internal("bgScreen/plantScreen.png")));

        playButton = new ButtonActor(ButtonCodex.PLAY, this, worldWidth - 160f, 32f, 128, false, true);
        homeButton = new ButtonActor(ButtonCodex.HOME, this, 32f, 32f, 128, false, true);
        checkButton = new ButtonActor(ButtonCodex.CHECK, this, 950, 200, 128, false, true);
        crossButton = new ButtonActor(ButtonCodex.CROSS, this, 950, 200, 128, false, true);

        playButton.setDrawable(true);
        homeButton.setDrawable(true);
        checkButton.setDrawable(true);
        crossButton.setDrawable(false);

        playButton.setCanPress(true);
        homeButton.setCanPress(true);
        checkButton.setCanPress(true);
        crossButton.setCanPress(false);

        stage.addActor(playButton);
        stage.addActor(homeButton);
        checkStage.addActor(checkButton);
        crossStage.addActor(crossButton);

        rectangleSprite = new Sprite(new Texture(Gdx.files.internal("misc/0_PlantSquare.png")));

        TextureRegion tempTrees[][], tempPlants[][], tempAnimals[][];
        trees = new Texture(Gdx.files.internal("trees/trees.png"));
        tempTrees = TextureRegion.split(trees, 64, 64);
        plants = new Texture(Gdx.files.internal("trees/plants.png"));
        tempPlants = TextureRegion.split(plants, 32, 32);
        animals = new Texture(Gdx.files.internal("trees/animals.png"));
        tempAnimals = TextureRegion.split(animals, 256, 256);
        for(int i = 0; i < tempTrees.length; i++) {
            for(int j = 0; j < tempTrees[i].length; j++) {
                if ((i == 3 && j == 0) || (i == 3 && j == 4)) {
                    continue;
                } else if(i == 3 && (AnimalCodex.level[j - 1] <= mapScreen.getHighLevelBound() && AnimalCodex.level[j - 1] >= mapScreen.getLowLevelBound())) {
                    sprites[(i * tempTrees[i].length) + j] = new Sprite(tempAnimals[0][j - 1]);
                    sprites[i * tempTrees[i].length + j].setBounds(rectangles[i][j].getX(), rectangles[i][j].getY(), rectangles[i][j].getWidth(), rectangles[i][j].getHeight());
                    actorSprites[(i * tempTrees[i].length) + j] = new Sprite(tempAnimals[0][j - 1]);
                    stage.addActor(new AnimalButton(sprites[i * tempTrees[i].length + j], stage, this, i * tempTrees[i].length + j));
                } else if((i * tempTrees[i].length + j >= mapScreen.getLowLevelBound() && i * tempTrees[i].length + j <= mapScreen.getHighLevelBound()) || (i * tempTrees[i].length + j < 15 && PlantCodex.level[i * tempTrees[i].length + j] <= mapScreen.getHighLevelBound())) {
                    if((i == 0 && (j == 3 || j == 4)) || (i == 1 && j == 2)) {
                        sprites[(i * tempTrees[i].length) + j] = new Sprite(tempPlants[i][j]);
                        actorSprites[(i * tempTrees[i].length) + j] = new Sprite(tempPlants[i][j]);
                    } else {
                        sprites[(i * tempTrees[i].length) + j] = new Sprite(tempTrees[i][j]);
                        actorSprites[(i * tempTrees[i].length) + j] = new Sprite(tempTrees[i][j]);
                    }
                    sprites[i * tempTrees[i].length + j].setBounds(rectangles[i][j].getX(), rectangles[i][j].getY(), rectangles[i][j].getWidth(), rectangles[i][j].getHeight());
                    stage.addActor(new PlantButton(sprites[i * tempTrees[i].length + j], stage, this, i * tempTrees[i].length + j));
                } else {
                    sprites[i * tempTrees[i].length + j] = new Sprite(new Texture(Gdx.files.internal("misc/lock.png")));
                    sprites[i * tempTrees[i].length + j].setBounds(rectangles[i][j].getX(), rectangles[i][j].getY(), rectangles[i][j].getWidth(), rectangles[i][j].getHeight());
                    actorSprites[(i * tempTrees[i].length) + j] = new Sprite(new Texture(Gdx.files.internal("misc/lock.png")));
                }
                actorSprites[(i * tempTrees[i].length) + j].setBounds(0, 64, 64, 64);
            }
        }
        rectangleSprite.setBounds(rectangles[0][0].getX(), rectangles[0][0].getY(), rectangles[0][0].getWidth(), rectangles[0][0].getHeight());
        animalActorSprite = new Sprite();
        animalActorSprite.setBounds(976, 32, 64, 64);
        for(int i = 0; i < renderedActorSprites.length; i++) {
            renderedActorSprites[i] = new Sprite();
            renderedActorSprites[i].setBounds(PlantCodex.plantSelectorIndex[i], 64, 64, 64);
            actorRectangles[i] = new Rectangle(PlantCodex.plantSelectorIndex[i], 64, 64, 64);
        }


        Label tempLabel, spaceFiller;
        Table tempTable = new Table(), infoTable = new Table(), descTable = new Table();
        tempTable.setBounds(797, 300, 111, 390);
        infoTable.setBounds(908, 300, 320, 390);
        descTable.setBounds(793, 300, 431, 250);

        Color border = Color.BLACK;
        Color fontColor = Color.WHITE;
        BitmapFont tempFont = font;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arcon.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 18;
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

        float startY = worldHeight - 102, startX = 938, decrement = 21f;

        tempLabel = new Label("HP:", new Label.LabelStyle(font, fontColor));
        spaceFiller = new Label(" ", new Label.LabelStyle(font, fontColor));
        plantHP = new Sprite(new Texture(Gdx.files.internal("utilities/greenLife.png")));
        plantHP.setBounds(startX, startY - decrement, 32, 8);

        tempTable.add(tempLabel);
        infoTable.add(spaceFiller);
        tempTable.row();
        infoTable.row();

        tempLabel = new Label("Atk Spd:", new Label.LabelStyle(font, fontColor));
        plantAS = new Sprite(new Texture(Gdx.files.internal("utilities/greenLife.png")));
        plantAS.setBounds(startX, startY - decrement * 2, 32, 8);

        tempTable.add(tempLabel);
        infoTable.add(spaceFiller);
        tempTable.row();
        infoTable.row();

        tempLabel = new Label("Damage:", new Label.LabelStyle(font, fontColor));
        plantDmg = new Sprite(new Texture(Gdx.files.internal("utilities/greenLife.png")));
        plantDmg.setBounds(startX, startY - decrement * 3, 32, 8);

        tempTable.add(tempLabel);
        infoTable.add(spaceFiller);
        tempTable.row();
        infoTable.row();

        tempLabel = new Label("Range:", new Label.LabelStyle(font, fontColor));
        plantRange = new Sprite(new Texture(Gdx.files.internal("utilities/greenLife.png")));
        plantRange.setBounds(startX, startY - decrement * 4, 32, 8);

        tempTable.add(tempLabel);
        infoTable.add(spaceFiller);
        tempTable.row();
        infoTable.row();

        tempLabel = new Label("Seed Rate:", new Label.LabelStyle(font, fontColor));
        plantSeedRate = new Sprite(new Texture(Gdx.files.internal("utilities/greenLife.png")));
        plantSeedRate.setBounds(startX, startY - decrement * 5, 32, 8);

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

        tempLabel = new Label("--", new Label.LabelStyle(font, fontColor));
        plantDescription = new Label[4];

        tempTable.add(tempLabel);
        for(int i = 0; i < plantDescription.length; i++) {
            plantDescription[i] = new Label("--", new Label.LabelStyle(font, fontColor));
            descTable.add(plantDescription[i]);
            descTable.row();
        }

        stage.addActor(tempTable);
        stage.addActor(infoTable);
        stage.addActor(descTable);
        generator.dispose();
        font = tempFont;

        setData(0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            game.setScreen(mapScreen);
            this.dispose();
        }

        gameGraphics.setProjectionMatrix(cam.combined);
        gameGraphics.begin();
        gameGraphics.draw(plantScreenBG, 0, 0);
        gameGraphics.draw(lowerHud, 0, 32f);
        for(int i = 0; i < sprites.length; i++) {
            if(sprites[i] != null) {
                sprites[i].draw(gameGraphics);
            }
        }
        rectangleSprite.draw(gameGraphics);
//        temppSprite.draw(gameGraphics);
        plantHP.draw(gameGraphics);
        plantAS.draw(gameGraphics);
        plantDmg.draw(gameGraphics);
        plantRange.draw(gameGraphics);
        plantSeedRate.draw(gameGraphics);
        if(!selected[index]) {
            checkButton.setDrawable(true);
            checkButton.setCanPress(true);
            crossButton.setDrawable(false);
            crossButton.setCanPress(false);
        } else {
            checkButton.setDrawable(false);
            checkButton.setCanPress(false);
            crossButton.setDrawable(true);
            crossButton.setCanPress(true);
        }

        if(currentSelectionIndex == 0) {
            playButton.setCanPress(false);
        } else {
            playButton.setCanPress(true);
        }

        if(currentSelectionIndex > 5 && index < 15) {
            checkButton.setCanPress(false);
        } else {
            checkButton.setCanPress(true);
        }

        for(int i = 0; i < currentSelectionIndex; i++) {
            renderedActorSprites[i].draw(gameGraphics);
        }
        if(selectedAnimalIndex != -1) {
            animalActorSprite.draw(gameGraphics);
        }
        gameGraphics.end();

        gameGraphics.setProjectionMatrix(stage.getCamera().combined);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        checkStage.draw();
        crossStage.draw();
    }

    public void selectPlant() {
        if(index < 16) {
            if(currentSelectionIndex <= selectedIndeces.length) {
                selected[index] = !selected[index];
                if(selected[index]) {
                    if(currentSelectionIndex < selectedIndeces.length) {
                        selectedIndeces[currentSelectionIndex] = index;
                        renderedActorSprites[currentSelectionIndex].set(actorSprites[index]);
                        renderedActorSprites[currentSelectionIndex].setBounds(PlantCodex.plantSelectorIndex[currentSelectionIndex], 64, 64, 64);
                        currentSelectionIndex++;
                    } else {
                        selected[index] = !selected[index];
                    }
                } else {
                    for(int i = 0; i < selectedIndeces.length; i++) {
                        if(selectedIndeces[i] == index) {
                            for(int j = i; j < selectedIndeces.length; j++) {
                                if(j + 1 == selectedIndeces.length) {
                                    selectedIndeces[j] = -1;
                                    renderedActorSprites[j] = new Sprite();
                                } else {
                                    selectedIndeces[j] = selectedIndeces[j + 1];
                                    renderedActorSprites[j] = renderedActorSprites[j + 1];
                                    renderedActorSprites[j].setBounds(PlantCodex.plantSelectorIndex[j], 64, 64, 64);
                                }
                            }
                            break;
                        }
                    }
                    currentSelectionIndex--;
                }
            }
        } else {
            if(selectedAnimalIndex != -1) selected[selectedAnimalIndex] = false;
            selected[index] = !selected[index];
            animalActorSprite.set(actorSprites[index]);
            animalActorSprite.setBounds(976, 64, 64, 64);
            selectedAnimalIndex = index;
        }
    }

    public void createGameScreen() {
        if(currentSelectionIndex > 0) {
            PlantActor plantActor[] = new PlantActor[currentSelectionIndex];
            for(int k = 0; k < plantActor.length; k++) {
                plantActor[k] = new PlantActor(renderedActorSprites[k], rectangleSprite, selectedIndeces[k], k, 64);
            }
            if(selectedAnimalIndex != -1) {
                AnimalActor animalActor = new AnimalActor(animalActorSprite, rectangleSprite, selectedAnimalIndex, 64);
                game.setScreen(new GameScreen(game, mapScreen, this, region, plantActor, animalActor));
            } else {
                game.setScreen(new GameScreen(game, mapScreen, this, region, plantActor, null));
            }
        } else {
            Gdx.app.log("verbose", "You cannot start the game without plants!");
        }
    }

    public void backToMapScreen() {
        game.setScreen(mapScreen);
        this.dispose();
    }

    public void setAnimalData(int index) {
        int i = index / 5, j = index % 5;
        this.index = index;
        index -= 16;
        float barSize = 260f;
        plantName.setText(AnimalCodex.name[index]);
        rectangleSprite.setBounds(rectangles[i][j].getX(), rectangles[i][j].getY(), rectangles[i][j].getWidth(), rectangles[i][j].getHeight());
        this.plantType.setText("--");
        plantCost.setText("" + (int)(50) + " seeds, " + (int)(100) + " water");
        plantHP.setBounds(plantHP.getX(), plantHP.getY(), plantHP.getWidth() * 0, plantHP.getHeight());
        plantAS.setBounds(plantAS.getX(), plantAS.getY(), plantAS.getWidth() * 0, plantAS.getHeight());
        plantDmg.setBounds(plantDmg.getX(), plantDmg.getY(), barSize * (AnimalCodex.DMG[AnimalCodex.dmgStats[index]] / AnimalCodex.DMG[AnimalCodex.HIGHEST]), plantDmg.getHeight());
        plantRange.setBounds(plantRange.getX(), plantRange.getY(), barSize * (AnimalCodex.RANGE[AnimalCodex.rangeStats[index]] / AnimalCodex.RANGE[AnimalCodex.HIGHEST]), plantRange.getHeight());
        plantSeedRate.setBounds(plantSeedRate.getX(), plantSeedRate.getY(), plantSeedRate.getWidth() * 0, plantSeedRate.getHeight());
        plantGrowthTime.setText("--");
        for(i = 0; i < plantDescription.length; i++) {
            plantDescription[i].setText(AnimalCodex.description[index][i]);
        }
    }

    public void setData(int index) {
        if((index >= mapScreen.getLowLevelBound() && index <= mapScreen.getHighLevelBound()) || (index < 15 && PlantCodex.level[index] <= mapScreen.getHighLevelBound())) {
            int i = index / 5, j = index % 5;
            this.index = index;
            float barSize = 260f;
            plantName.setText(PlantCodex.plantName[index]);
            rectangleSprite.setBounds(rectangles[i][j].getX(), rectangles[i][j].getY(), rectangles[i][j].getWidth(), rectangles[i][j].getHeight());
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
            plantCost.setText("" + (int)(PlantCodex.cost[index] / 2) + " seeds, " + (int)(PlantCodex.cost[index]) + " water");
            plantHP.setBounds(plantHP.getX(), plantHP.getY(), barSize * (PlantCodex.hpStats[PlantCodex.maxHP[index]] / PlantCodex.baseHP), plantHP.getHeight());
            if(index != 13) {
                plantAS.setBounds(plantAS.getX(), plantAS.getY(), barSize * ((float)PlantCodex.baseAS / (float)(PlantCodex.asStats[PlantCodex.AS[index]] * 4f)), plantAS.getHeight());
            } else {
                plantAS.setBounds(plantAS.getX(), plantAS.getY(), 0, plantAS.getHeight());
            }
            plantDmg.setBounds(plantDmg.getX(), plantDmg.getY(), barSize * ((((PlantCodex.dmgStats[PlantCodex.DMG[index]].x + PlantCodex.dmgStats[PlantCodex.DMG[index]].y)) / (((PlantCodex.baseDmg.x + PlantCodex.baseDmg.y))))), plantDmg.getHeight());
            plantRange.setBounds(plantRange.getX(), plantRange.getY(), barSize * (PlantCodex.rangeStats[PlantCodex.range[index]] / PlantCodex.rangeStats[PlantCodex.ABS_HIGHEST]), plantRange.getHeight());
            plantSeedRate.setBounds(plantSeedRate.getX(), plantSeedRate.getY(), barSize * (PlantCodex.seedRateStats[PlantCodex.seedProduction[index]] / PlantCodex.seedRateStats[PlantCodex.ABS_HIGHEST]), plantSeedRate.getHeight());
            plantGrowthTime.setText("" + (int)(PlantCodex.growthTime[index]) + " seconds");
            for(i = 0; i < plantDescription.length; i++) {
                plantDescription[i].setText(PlantCodex.description[index][i]);
            }
        }
    }

    @Override
    public void show() {
        InputMultiplexer input = new InputMultiplexer();
        input.addProcessor(checkStage);
        input.addProcessor(crossStage);
        input.addProcessor(stage);
        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void dispose() {
        lowerHud.getTexture().dispose();
        plantScreenBG.getTexture().dispose();
        plantHP.getTexture().dispose();
        plantAS.getTexture().dispose();
        plantDmg.getTexture().dispose();
        plantRange.getTexture().dispose();
        plantSeedRate.getTexture().dispose();
        for(int i = 0; i < sprites.length; i++) {
            if(sprites[i] != null) {
                sprites[i].getTexture().dispose();
                actorSprites[i].getTexture().dispose();
            }
        }

        for(int i = 0; i < renderedActorSprites.length; i++) {
            if(renderedActorSprites[i] != null && renderedActorSprites[i].getTexture() != null) {
                renderedActorSprites[i].getTexture().dispose();
            }
        }

        if(selectedAnimalIndex != -1) {
            if(animalActorSprite != null) animalActorSprite.getTexture().dispose();
        }
        rectangleSprite.getTexture().dispose();
        trees.dispose();
        plants.dispose();
        animals.dispose();
        playButton.dispose();
        homeButton.dispose();
        crossButton.dispose();
        checkButton.dispose();
        crossStage.dispose();
        checkStage.dispose();
        stage.dispose();
    }

    public MapScreen getMapScreen() {
        return mapScreen;
    }

    public Region getRegion() {
        return region;
    }
}
