package com.systemphoenix.edenalpha.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.systemphoenix.edenalpha.Actors.StageActors.AnimalActor;
import com.systemphoenix.edenalpha.Actors.StageActors.AnimalButton;
import com.systemphoenix.edenalpha.Actors.StageActors.BlankActor;
import com.systemphoenix.edenalpha.Actors.StageActors.ButtonActor;
import com.systemphoenix.edenalpha.Actors.StageActors.EnemyButton;
import com.systemphoenix.edenalpha.Actors.StageActors.PlantActor;
import com.systemphoenix.edenalpha.Actors.StageActors.PlantButton;
import com.systemphoenix.edenalpha.Codex.AnimalCodex;
import com.systemphoenix.edenalpha.Codex.ButtonCodex;
import com.systemphoenix.edenalpha.Codex.EnemyCodex;
import com.systemphoenix.edenalpha.Codex.PlantCodex;
import com.systemphoenix.edenalpha.Eden;
import com.systemphoenix.edenalpha.WindowUtils.Region;

public class PlantScreen extends AbsoluteScreen {

    private Stage stage, checkStage, crossStage, enemyStage, buttonStage, descriptionStage, plantInfoStage, enemyInfoStage, blankActorStage;
    private Texture trees, plants, animals, enemies;
    private Sprite lowerHud, plantScreenBG, enemyScreenBG, rectangleSprite, plantHP, plantAS, plantDmg, plantRange, plantSeedRate, animalActorSprite;
    private Sprite enemyHP, enemyAS, enemyDmg, enemyMS;
    private Sprite[] sprites, actorSprites, renderedActorSprites;
    private Rectangle actorRectangles[], rectangles[][], enemyRectangles[][];

    private ButtonActor playButton, checkButton, crossButton, homeButton, bookButton;
    private EnemyButton enemyButtons[];

    private SpriteBatch gameGraphics;
    private Label plantName, plantType, plantCost, plantGrowthTime, descriptionLabel[];
    private Label enemyName;

    private MapScreen mapScreen;
    private Region region;

    private int selectedIndices[], currentSelectionIndex, index, enemyIndex = 0, selectedAnimalIndex = -1;
    private boolean selected[], renderingPlantScreen = true, renderingDescStage = true;

    public PlantScreen(Eden game, MapScreen mapScreen, Region region) {
        super(game);
        this.mapScreen = mapScreen;
        this.region = region;

        this.gameGraphics = game.getGameGraphics();
        this.selectedIndices = new int[6];
        this.actorRectangles = new Rectangle[6];
        this.selected = new boolean[20];

        for(int i = 0; i < selectedIndices.length; i++) {
            selectedIndices[i] = -1;
        }

        for(int i = 0; i < selected.length; i++) {
            selected[i] = false;
        }

        currentSelectionIndex = 0;

        this.sprites = new Sprite[20];
        this.actorSprites = new Sprite[20];
        this.renderedActorSprites = new Sprite[6];
        this.enemyButtons = new EnemyButton[8];

        this.rectangles = new Rectangle[4][5];
        this.enemyRectangles = new Rectangle[2][4];
        for(int i = 0; i < rectangles.length - 1; i++) {
            for(int j = 0; j < rectangles[i].length; j++) {
                rectangles[i][j] = new Rectangle((64 + (j * 144)), (720 - (128 + (i * 128))), 96f, 96f);
            }
        }

        for(int i = 0; i < enemyRectangles.length; i++) {
            for(int j = 0; j < enemyRectangles[i].length; j++) {
                enemyRectangles[i][j] = new Rectangle(208 + j * 144, 720 - (128 + ((i + 1) * 128)), 96, 96);
            }
        }

        for(int j = 0; j < rectangles[rectangles.length - 1].length; j++) {
            if(!(j == 0 || j + 1 == rectangles[rectangles.length - 1].length)) {
                rectangles[rectangles.length - 1][j] = new Rectangle((64 + (j * 144)), 176, 96f, 96f);
            }
        }

        initializePlantStage();
        initializeEnemyStage();
        Gdx.input.setCatchBackKey(true);

        setData(0);
    }

    private void initializePlantStage() {
        Viewport viewport = new FitViewport(1280, 720, cam);
        stage            = new Stage(viewport, gameGraphics);
        plantInfoStage   = new Stage(viewport, gameGraphics);
        descriptionStage = new Stage(viewport, gameGraphics);
        blankActorStage  = new Stage(viewport, gameGraphics);

        buttonStage  = new Stage(viewport, gameGraphics);
        checkStage   = new Stage(viewport, gameGraphics);
        crossStage   = new Stage(viewport, gameGraphics);

        lowerHud         = new Sprite(new Texture(Gdx.files.internal("misc/lowerHud.png")));
        plantScreenBG    = new Sprite(new Texture(Gdx.files.internal("bgScreen/plantScreen.png")));

        playButton   = new ButtonActor(ButtonCodex.PLAY, this, worldWidth - 160f, 32f, 128, false, true);
        homeButton   = new ButtonActor(ButtonCodex.HOME, this, 32f, 32f, 128, false, true);
        checkButton  = new ButtonActor(ButtonCodex.CHECK, this, 950, 200, 128, false, true);
        crossButton  = new ButtonActor(ButtonCodex.CROSS, this, 950, 200, 128, false, true);
        bookButton   = new ButtonActor(ButtonCodex.BOOK, this, 160f, 32f, 128, false, true);

        playButton.setDrawable(true);
        homeButton.setDrawable(true);
        checkButton.setDrawable(true);
        crossButton.setDrawable(false);
        bookButton.setDrawable(true);

        playButton.setCanPress(true);
        homeButton.setCanPress(true);
        checkButton.setCanPress(true);
        crossButton.setCanPress(false);
        bookButton.setCanPress(true);

        buttonStage.addActor(playButton);
        buttonStage.addActor(homeButton);
        buttonStage.addActor(bookButton);
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
                int index = i * tempTrees[i].length + j;
                if ((i == 3 && j == 0) || (i == 3 && j == 4)) {
                    continue;
//                } else if(i == 3 && (AnimalCodex.level[j - 1] % 2 == game.getModule() || game.isDevMode()) && (AnimalCodex.level[j - 1] <= mapScreen.getHighLevelBound() && AnimalCodex.level[j - 1] >= mapScreen.getLowLevelBound())) {
                } else if(i == 3 && (AnimalCodex.level[j - 1] % 2 == game.getModule() || game.isDevMode()) && (AnimalCodex.level[j - 1] >= mapScreen.getLowLevelBound())) {
                    sprites[index] = new Sprite(tempAnimals[0][j - 1]);
                    sprites[index].setBounds(rectangles[i][j].getX(), rectangles[i][j].getY(), rectangles[i][j].getWidth(), rectangles[i][j].getHeight());
                    actorSprites[index] = new Sprite(tempAnimals[0][j - 1]);
                    stage.addActor(new AnimalButton(sprites[index], stage, this, index));
//                } else if((index == 0 || index == 1) || index < 15 && (PlantCodex.level[index] % 2 == game.getModule() || game.isDevMode()) && (index <= mapScreen.getHighLevelBound()) || (mapScreen.getHighLevelBound() == 16 && index == 14)) {
                } else if((index == 0 || index == 1) || index < 15 && ((PlantCodex.level[index] % 2 == game.getModule() && index < 14) || game.isDevMode()) || (mapScreen.getHighLevelBound() == 16 && index == 14)) {
                    if((i == 0 && (j == 3) || (i == 1 && (j == 1 || j == 0)))) {
                        sprites[index] = new Sprite(tempPlants[i][j]);
                        actorSprites[index] = new Sprite(tempPlants[i][j]);
                    } else {
                        sprites[index] = new Sprite(tempTrees[i][j]);
                        actorSprites[index] = new Sprite(tempTrees[i][j]);
                    }
                    sprites[index].setBounds(rectangles[i][j].getX(), rectangles[i][j].getY(), rectangles[i][j].getWidth(), rectangles[i][j].getHeight());
                    stage.addActor(new PlantButton(sprites[index], stage, this, index));
                } else {
                    sprites[index] = new Sprite(new Texture(Gdx.files.internal("misc/lock.png")));
                    sprites[index].setBounds(rectangles[i][j].getX(), rectangles[i][j].getY(), rectangles[i][j].getWidth(), rectangles[i][j].getHeight());
                    actorSprites[index] = new Sprite(new Texture(Gdx.files.internal("misc/lock.png")));
                }
                actorSprites[index].setBounds(0, 64, 64, 64);
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
        tempTable.setBounds(815, 300, 111, 390);
        infoTable.setBounds(908, 300, 320, 390);
        descTable.setBounds(793, 350, 431, 250);

        Color border = Color.BLACK;
        Color fontColor = Color.WHITE;
        BitmapFont tempFont = font;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arcon.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
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

        float startY = worldHeight - 120, startX = 938, decrement = 28f;

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
        tempTable.add(tempLabel);
        tempLabel = new Label("", new Label.LabelStyle(font, fontColor));
        infoTable.add(tempLabel);

        descriptionLabel = new Label[10];
        for(int i = 0; i < descriptionLabel.length; i++) {
            descriptionLabel[i] = new Label("--", new Label.LabelStyle(font, fontColor));
            descTable.add(descriptionLabel[i]);
            descTable.row();
        }

        plantInfoStage.addActor(tempTable);
        plantInfoStage.addActor(infoTable);
        descriptionStage.addActor(descTable);
        generator.dispose();
        font = tempFont;

        blankActorStage.addActor(new BlankActor(this, 793, 350, 431, 225));
    }

    private void initializeEnemyStage() {
        Viewport viewport = new FitViewport(worldWidth, worldHeight, new OrthographicCamera());
        enemyStage = new Stage(viewport, gameGraphics);
        enemyInfoStage = new Stage(viewport, gameGraphics);
        enemyScreenBG = new Sprite(new Texture(Gdx.files.internal("bgScreen/enemyScreen.png")));
        enemyScreenBG.setBounds(0, 0, worldWidth, worldHeight);

        Label tempLabel, spaceFiller;
        Table tempTable = new Table(), infoTable = new Table();
        tempTable.setBounds(815, 300, 111, 390);
        infoTable.setBounds(908, 300, 320, 390);

        Color border = Color.BLACK;
        Color fontColor = Color.WHITE;
        BitmapFont tempFont = font;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arcon.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.borderColor = border;
        parameter.borderWidth = 0;
        font = generator.generateFont(parameter);

        tempTable.top().padTop(15);
        infoTable.top().padTop(15);

        tempLabel = new Label("Name:", new Label.LabelStyle(font, fontColor));
        enemyName = new Label("--", new Label.LabelStyle(font, fontColor));

        tempTable.add(tempLabel);
        infoTable.add(enemyName);
        tempTable.row();
        infoTable.row();

        float startY = worldHeight - 62, startX = 938, decrement = 28f;

        tempLabel = new Label("HP:", new Label.LabelStyle(font, fontColor));
        spaceFiller = new Label(" ", new Label.LabelStyle(font, fontColor));
        enemyHP = new Sprite(new Texture(Gdx.files.internal("utilities/greenLife.png")));
        enemyHP.setBounds(startX, startY - decrement, 32, 8);

        tempTable.add(tempLabel);
        infoTable.add(spaceFiller);
        tempTable.row();
        infoTable.row();

        tempLabel = new Label("Atk Spd:", new Label.LabelStyle(font, fontColor));
        enemyAS = new Sprite(new Texture(Gdx.files.internal("utilities/greenLife.png")));
        enemyAS.setBounds(startX, startY - decrement * 2, 32, 8);

        tempTable.add(tempLabel);
        infoTable.add(spaceFiller);
        tempTable.row();
        infoTable.row();

        tempLabel = new Label("Damage:", new Label.LabelStyle(font, fontColor));
        enemyDmg = new Sprite(new Texture(Gdx.files.internal("utilities/greenLife.png")));
        enemyDmg.setBounds(startX, startY - decrement * 3, 32, 8);

        tempTable.add(tempLabel);
        infoTable.add(spaceFiller);
        tempTable.row();
        infoTable.row();

        tempLabel = new Label("Movement:", new Label.LabelStyle(font, fontColor));
        enemyMS = new Sprite(new Texture(Gdx.files.internal("utilities/greenLife.png")));
        enemyMS.setBounds(startX, startY - decrement * 4, 32, 8);

        tempTable.add(tempLabel);
        infoTable.add(spaceFiller);
        tempTable.row();
        infoTable.row();

        tempLabel = new Label("--", new Label.LabelStyle(font, fontColor));
        tempTable.add(tempLabel);
        tempLabel = new Label("", new Label.LabelStyle(font, fontColor));
        infoTable.add(tempLabel);

        enemyInfoStage.addActor(tempTable);
        enemyInfoStage.addActor(infoTable);

        font = tempFont;
        enemies = new Texture(Gdx.files.internal("enemies/baddies0.png"));
        TextureRegion tempTextureRegion[][] = TextureRegion.split(enemies, 32, 32);
        Animation<TextureRegion> southAnimation = null, westAnimation = null, northAnimation = null, eastAnimation = null;
        Array<TextureRegion> animationRegion;
        Sprite icon = null;
        for(int i = 0; i < tempTextureRegion.length; i += 4) {
            for(int j = 0; j < tempTextureRegion[i].length; j += 4) {
                for(int k = i; k < i + 4; k++) {
                    animationRegion = new Array<TextureRegion>();
                    for(int l = j; l < j + 4; l++) {
                        animationRegion.add(tempTextureRegion[k][l]);
                    }
                    float frameDuration = 0.2f;
                    switch(k - i) {
                        case 0:
                            icon = new Sprite(tempTextureRegion[k][j + 1]);
                            southAnimation = new Animation<TextureRegion>(frameDuration, animationRegion);
                            break;
                        case 1:
                            westAnimation = new Animation<TextureRegion>(frameDuration, animationRegion);
                            break;
                        case 2:
                            northAnimation = new Animation<TextureRegion>(frameDuration, animationRegion);
                            break;
                        case 3:
                            eastAnimation = new Animation<TextureRegion>(frameDuration, animationRegion);
                            break;
                    }
                }
                if(((i / 4) * (tempTextureRegion[i].length / 4) + (j / 4)) % 2 == game.getLowLevelBound() || game.isDevMode()) {
                    enemyButtons[(i / 4) * (tempTextureRegion[i].length / 4) + (j / 4)] = new EnemyButton(this, enemyStage, icon,
                            southAnimation, westAnimation, northAnimation, eastAnimation,
                            enemyRectangles[i / 4][j / 4].getX() + 12,enemyRectangles[i / 4][j / 4].getY() + 12, (i / 4) * (tempTextureRegion[i].length / 4) + (j / 4));
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(renderingPlantScreen) {
            if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
                game.setScreen(mapScreen);
                this.dispose();
            }

            gameGraphics.setProjectionMatrix(cam.combined);
            gameGraphics.begin();
            gameGraphics.draw(plantScreenBG, 0, 0);
            for(int i = 0; i < sprites.length; i++) {
                if(sprites[i] != null) {
                    sprites[i].draw(gameGraphics);
                }
            }
            rectangleSprite.draw(gameGraphics);
            if(index < 15 && PlantCodex.level[index] <= mapScreen.getHighLevelBound()) {
                if(!selected[index] ) {
                    checkButton.setDrawable(true); checkButton.setCanPress(true);
                    crossButton.setDrawable(false); crossButton.setCanPress(false);
                } else {
                    checkButton.setDrawable(false); checkButton.setCanPress(false);
                    crossButton.setDrawable(true); crossButton.setCanPress(true);
                }
                if(currentSelectionIndex > 5 && index < 15) {
                    checkButton.setCanPress(false);
                } else {
                    checkButton.setCanPress(true);
                }
            } else if(index > 15 && index < 19 && AnimalCodex.level[index - 16] <= mapScreen.getHighLevelBound()) {
                if(!selected[index]) {
                    checkButton.setDrawable(true); checkButton.setCanPress(true);
                    crossButton.setDrawable(false); crossButton.setCanPress(false);
                } else {
                    checkButton.setDrawable(true); checkButton.setCanPress(false);
                    crossButton.setDrawable(false); crossButton.setCanPress(false);
                }
            } else {
                checkButton.setDrawable(true); checkButton.setCanPress(false);
                crossButton.setDrawable(false); crossButton.setCanPress(false);
            }

            if(currentSelectionIndex == 0) {
                playButton.setCanPress(false);
            } else {
                playButton.setCanPress(true);
            }


            gameGraphics.end();

            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();

            checkStage.draw();
            crossStage.draw();
            gameGraphics.setProjectionMatrix(descriptionStage.getCamera().combined);
        } else {
            checkButton.setCanPress(false);
            crossButton.setCanPress(false);
            if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
                setRenderingPlantScreen(true);
            }
            gameGraphics.begin();
            enemyScreenBG.draw(gameGraphics);
            rectangleSprite.draw(gameGraphics);
            gameGraphics.end();
            gameGraphics.setProjectionMatrix(enemyStage.getCamera().combined);
            enemyStage.draw();
        }

        if(renderingDescStage) {
            gameGraphics.setProjectionMatrix(stage.getCamera().combined);
            descriptionStage.draw();
        } else {
            if(renderingPlantScreen) {
                gameGraphics.begin();
                plantHP.draw(gameGraphics);
                plantAS.draw(gameGraphics);
                plantDmg.draw(gameGraphics);
                plantRange.draw(gameGraphics);
                plantSeedRate.draw(gameGraphics);
                gameGraphics.end();

                gameGraphics.setProjectionMatrix(plantInfoStage.getCamera().combined);
                plantInfoStage.draw();
            } else {
                gameGraphics.begin();
                enemyDmg.draw(gameGraphics);
                enemyAS.draw(gameGraphics);
                enemyHP.draw(gameGraphics);
                enemyMS.draw(gameGraphics);
                gameGraphics.end();
                gameGraphics.setProjectionMatrix(enemyInfoStage.getCamera().combined);
                enemyInfoStage.draw();
            }
        }

        gameGraphics.begin();
        gameGraphics.draw(lowerHud, 0, 32f);
        for(int i = 0; i < currentSelectionIndex; i++) {
            renderedActorSprites[i].draw(gameGraphics);
        }
        if(selectedAnimalIndex != -1) {
            animalActorSprite.draw(gameGraphics);
        }
        gameGraphics.end();
        gameGraphics.setProjectionMatrix(buttonStage.getCamera().combined);
        buttonStage.draw();
    }

    public void selectPlant() {
        if(index < 16) {
            if(currentSelectionIndex <= selectedIndices.length) {
                selected[index] = !selected[index];
                if(selected[index]) {
                    if(currentSelectionIndex < selectedIndices.length) {
                        selectedIndices[currentSelectionIndex] = index;
                        renderedActorSprites[currentSelectionIndex].set(actorSprites[index]);
                        renderedActorSprites[currentSelectionIndex].setBounds(PlantCodex.plantSelectorIndex[currentSelectionIndex], 64, 64, 64);
                        currentSelectionIndex++;
                    } else {
                        selected[index] = !selected[index];
                    }
                } else {
                    for(int i = 0; i < selectedIndices.length; i++) {
                        if(selectedIndices[i] == index) {
                            for(int j = i; j < selectedIndices.length; j++) {
                                if(j + 1 == selectedIndices.length) {
                                    selectedIndices[j] = -1;
                                    renderedActorSprites[j] = new Sprite();
                                } else {
                                    selectedIndices[j] = selectedIndices[j + 1];
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
                plantActor[k] = new PlantActor(renderedActorSprites[k], rectangleSprite, selectedIndices[k], k, 64);
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
        for(i = 0; i < descriptionLabel.length && i < AnimalCodex.description[index].length; i++) {
            descriptionLabel[i].setText(AnimalCodex.description[index][i]);
        }
        renderingDescStage = true;
    }

    public void setData(int index) {
//        if((index >= mapScreen.getLowLevelBound() && index <= mapScreen.getHighLevelBound()) || (index < 15 && PlantCodex.level[index] <= mapScreen.getHighLevelBound())) {
        if((index >= mapScreen.getLowLevelBound()) || (index < 15)) {
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
            for(i = 0; i < descriptionLabel.length && i < PlantCodex.description[index].length; i++) {
                descriptionLabel[i].setText(PlantCodex.description[index][i]);
            }
            renderingDescStage = true;
        }
    }

    public void setEnemyData(int enemyIndex) {
        this.enemyIndex = enemyIndex;
        int i = enemyIndex / 4 + 1, j = enemyIndex % 4 + 1;
        this.enemyName.setText(EnemyCodex.name[enemyIndex]);
        rectangleSprite.setBounds(rectangles[i][j].getX(), rectangles[i][j].getY(), rectangles[i][j].getWidth(), rectangles[i][j].getHeight());
        float barSize = 260;
        this.enemyHP.setBounds(enemyHP.getX(), enemyHP.getY(), barSize * (EnemyCodex.HP[enemyIndex] / EnemyCodex.baseHP), enemyHP.getHeight());
        this.enemyAS.setBounds(enemyAS.getX(), enemyAS.getY(), barSize * ((float)EnemyCodex.baseAS / (float)(EnemyCodex.attackSpeed[enemyIndex] * 2f)), enemyAS.getHeight());
        this.enemyDmg.setBounds(enemyDmg.getX(), enemyDmg.getY(), barSize * (EnemyCodex.damage[enemyIndex] / EnemyCodex.baseDmg), enemyDmg.getHeight());
        this.enemyMS.setBounds(enemyMS.getX(), enemyMS.getY(), barSize * (EnemyCodex.speed[enemyIndex] / EnemyCodex.baseMS), enemyMS.getHeight());
        for(i = 0; i < descriptionLabel.length && i < EnemyCodex.description[enemyIndex].length; i++) {
            descriptionLabel[i].setText(EnemyCodex.description[enemyIndex][i]);
        }
        renderingDescStage = true;
    }

    @Override
    public void show() {
        bindInput();
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

        enemyHP.getTexture().dispose();
        enemyAS.getTexture().dispose();
        enemyDmg.getTexture().dispose();
        enemyMS.getTexture().dispose();
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
        enemies.dispose();
        playButton.dispose();
        homeButton.dispose();
        crossButton.dispose();
        checkButton.dispose();
        crossStage.dispose();
        checkStage.dispose();
        bookButton.dispose();
        stage.dispose();

        enemyScreenBG.getTexture().dispose();
        enemyStage.dispose();

        buttonStage.dispose();
        descriptionStage.dispose();
        plantInfoStage.dispose();
        enemyInfoStage.dispose();
        blankActorStage.dispose();
    }

    private void bindInput() {
        InputMultiplexer input = new InputMultiplexer();
        input.addProcessor(buttonStage);
        if(renderingPlantScreen) {
            input.addProcessor(checkStage);
            input.addProcessor(crossStage);
            input.addProcessor(stage);
        } else {
            input.addProcessor(enemyStage);
        }
        input.addProcessor(blankActorStage);
        Gdx.input.setInputProcessor(input);
    }

    public MapScreen getMapScreen() {
        return mapScreen;
    }

    public Region getRegion() {
        return region;
    }

    public void setRenderingPlantScreen(boolean renderingPlantScreen) {
        this.renderingPlantScreen = renderingPlantScreen;
        for(int i = 0; i < enemyButtons.length; i++) {
            enemyButtons[i].setDrawable(!renderingPlantScreen);
        }
    }

    public void toggleRenderingPlantScreen() {
        if(!game.isDevMode()) {
            for(int i = game.getLowLevelBound(); i < enemyButtons.length; i += 2) {
                enemyButtons[i].setDrawable(renderingPlantScreen);
            }
        } else {
            for(int i = 0; i < enemyButtons.length; i ++) {
                enemyButtons[i].setDrawable(renderingPlantScreen);
            }
        }
        this.renderingPlantScreen = !renderingPlantScreen;
        if(renderingPlantScreen) {
            if(index < 15) setData(index);
            else setAnimalData(index);
        }
        else setEnemyData(enemyIndex);
        bindInput();
    }

    public void toggleDescStage() {
        renderingDescStage = !renderingDescStage;
    }
}
