package com.systemphoenix.edenalpha.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.systemphoenix.edenalpha.Actors.Plant;
import com.systemphoenix.edenalpha.Codex.PlantCodex;
import com.systemphoenix.edenalpha.EdenAlpha;
import com.systemphoenix.edenalpha.Region;
import com.systemphoenix.edenalpha.Scenes.PlantActor;

public class PlantScreen extends AbsoluteScreen {

    private Stage stage;
    private Texture trees, plants;
    private Sprite lowerHud, playButton, homeButton, plantScreenBG, rectangleSprite, plantHP, plantAS, plantDmg, temppSprite, checkButton, xButton;
    private Sprite onPressPlay, onPressHome, onPressCross, onPressCheck;
    private Sprite[] sprites, actorSprites, renderedActorSprites;
    private Rectangle rectangles[][], actorRectangles[], selectRectangle;

    private SpriteBatch gameGraphics;
    private Label plantName, plantType, plantCost, plantGrowthTime;

    private MapScreen mapScreen;
    private Region region;

    private float sizeModifier = 10;

    private int selectedIndeces[], currentSelectionIndex, index;
    private boolean selected[], pressingPlay, pressingHome, pressingCross, pressingCheck;

    public PlantScreen(EdenAlpha game, MapScreen mapScreen, Region region) {
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
                rectangles[i][j] = new Rectangle((64 + (j * 144)) - (sizeModifier / 2), (720 - (128 + (i * 128))) - (sizeModifier / 2), 96f + sizeModifier, 96f + sizeModifier);
            }
        }

        for(int j = 0; j < rectangles[rectangles.length - 1].length; j++) {
            if(j == 0 || j + 1 == rectangles[rectangles.length - 1].length) {
                if(j == 0) {
                    rectangles[rectangles.length - 1][j] = new Rectangle(32f, 32f, 128f, 128f);
                } else {
                    rectangles[rectangles.length - 1][j] = new Rectangle(worldWidth - 160f, 32f, 128f, 128f);
                }
            } else {
                rectangles[rectangles.length - 1][j] = new Rectangle((64 + (j * 144)) - (sizeModifier / 2), 176 - (sizeModifier / 2), 96f + sizeModifier, 96f + sizeModifier);
            }
        }

        initialize();
        Gdx.input.setCatchBackKey(true);
    }

    private void initialize() {
        lowerHud = new Sprite(new Texture(Gdx.files.internal("misc/lowerHud.png")));
        plantScreenBG = new Sprite(new Texture(Gdx.files.internal("utilities/plantScreen.png")));

        playButton = new Sprite(new Texture(Gdx.files.internal("utilities/beforePress_playButton.png")));
        playButton.setBounds(worldWidth - 160f, 32f, 128f, 128f);
        onPressPlay = new Sprite(new Texture(Gdx.files.internal("utilities/onPress_playButton.png")));
        onPressPlay.setBounds(worldWidth - 160f, 32f, 128f, 128f);
        pressingPlay = false;

        homeButton = new Sprite(new Texture(Gdx.files.internal("utilities/beforePress_homeButton.png")));
        homeButton.setBounds(32f, 32f, 128f, 128f);
        onPressHome = new Sprite(new Texture(Gdx.files.internal("utilities/onPress_homeButton.png")));
        onPressHome.setBounds(32f, 32f, 128f, 128f);
        pressingHome = false;

        checkButton = new Sprite(new Texture(Gdx.files.internal("utilities/beforePress_checkButton.png")));
        checkButton.setBounds(950, 200, 128, 128);
        onPressCheck = new Sprite(new Texture(Gdx.files.internal("utilities/onPress_checkButton.png")));
        onPressCheck.setBounds(950, 200, 128, 128);
        pressingCheck = false;

        xButton = new Sprite(new Texture(Gdx.files.internal("utilities/beforePress_crossButton.png")));
        xButton.setBounds(950, 200, 128, 128);
        onPressCross = new Sprite(new Texture(Gdx.files.internal("utilities/beforePress_crossButton.png")));
        onPressCross.setBounds(950, 200, 128, 128);
        pressingCross = false;

        selectRectangle = new Rectangle(950, 200, 128, 128);

        rectangleSprite = new Sprite(new Texture(Gdx.files.internal("misc/0_PlantSquare.png")));
        rectangleSprite.setBounds(rectangles[0][0].getX(), rectangles[0][0].getY(), rectangles[0][0].getWidth(), rectangles[0][0].getHeight());

        TextureRegion tempTrees[][], tempPlants[][];
        trees = new Texture(Gdx.files.internal("trees/trees.png"));
        tempTrees = TextureRegion.split(trees, 64, 64);
        plants = new Texture(Gdx.files.internal("trees/plants.png"));
        tempPlants = TextureRegion.split(plants, 32, 32);
        for(int i = 0; i < rectangles.length - 1; i++) {
            for(int j = 0; j < rectangles[i].length; j++) {
                if((i == 0 && (j == 3 || j == 4)) || (i == 1 && j == 2)) {
                    sprites[(i * tempTrees[i].length) + j] = new Sprite(tempPlants[i][j]);
                    actorSprites[(i * tempTrees[i].length) + j] = new Sprite(tempPlants[i][j]);
                } else {
                    sprites[(i * tempTrees[i].length) + j] = new Sprite(tempTrees[i][j]);
                    actorSprites[(i * tempTrees[i].length) + j] = new Sprite(tempTrees[i][j]);
                }
                sprites[i * tempTrees[i].length + j].setBounds(rectangles[i][j].getX() + (sizeModifier / 2), rectangles[i][j].getY() + (sizeModifier / 2), rectangles[i][j].getWidth() - sizeModifier, rectangles[i][j].getHeight() - sizeModifier);
                actorSprites[(i * tempTrees[i].length) + j].setBounds(0, 64, 64, 64);
            }
        }

        for(int i = 0; i < renderedActorSprites.length; i++) {
            renderedActorSprites[i] = new Sprite();
            renderedActorSprites[i].setBounds(PlantCodex.plantSelectorIndex[i], 64, 64, 64);
            actorRectangles[i] = new Rectangle(PlantCodex.plantSelectorIndex[i], 64, 64, 64);
        }

        Viewport viewport = new FitViewport(1280, 720, cam);
        stage = new Stage(viewport, gameGraphics);

        Label tempLabel, spaceFiller;
        Table tempTable = new Table(), infoTable = new Table(), renderTable;
        tempTable.setBounds(797, 300, 111, 390);
        infoTable.setBounds(908, 300, 320, 390);
        renderTable = infoTable;
        temppSprite = new Sprite(new Texture(Gdx.files.internal("misc/0_PlantSquare.png")));
        temppSprite.setBounds(renderTable.getX(), renderTable.getY(), renderTable.getWidth(), renderTable.getHeight());

        Color border = Color.BLACK;
//        Color fontColor = new Color(0.5f, 1f, 0.5f, 1f);
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

        float startY = worldHeight - 74, startX = 938;

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
        temppSprite.setX(renderTable.getX());
        temppSprite.setY(renderTable.getY());

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
        if(pressingPlay) {
            onPressPlay.draw(gameGraphics);
        } else {
            playButton.draw(gameGraphics);
        }
        if(pressingHome) {
            onPressHome.draw(gameGraphics);
        } else {
            homeButton.draw(gameGraphics);
        }
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
        if(!selected[index]) {
            if(pressingCheck) {
                onPressCheck.draw(gameGraphics);
            } else {
                checkButton.draw(gameGraphics);
            }
        } else {
            if(pressingCross) {
                onPressCross.draw(gameGraphics);
            } else {
                xButton.draw(gameGraphics);
            }
        }
        for(int i = 0; i < currentSelectionIndex; i++) {
            renderedActorSprites[i].draw(gameGraphics);
        }
        gameGraphics.end();

        gameGraphics.setProjectionMatrix(stage.getCamera().combined);
        stage.draw();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Rectangle rect = new Rectangle(x - 16f, 720 - (y + 16f), 32f, 32f);

        if(selectRectangle.overlaps(rect)) {
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
            for(int i = 0; i < actorRectangles.length; i++) {
                if(actorRectangles[i].overlaps(selectRectangle)) {
                    Gdx.app.log("Verbose", "Touched on a plant icon" );
                    setData(selectedIndeces[i]);
                    return true;
                }
            }

            for(int i = 0; i < rectangles.length; i++) {
                for(int j = 0; j < rectangles[i].length; j++) {
                    if(rect.overlaps(rectangles[i][j])) {
                        if(i + 1 == rectangles.length && (j == 0 || j + 1 == rectangles[i].length)) {
                            if(j == 0) {
                                game.setScreen(mapScreen);
                                this.dispose();
                            } else {
                                if(currentSelectionIndex > 0) {
                                    PlantActor plantActor[] = new PlantActor[currentSelectionIndex];
                                    for(int k = 0; k < plantActor.length; k++) {
                                        plantActor[k] = new PlantActor(renderedActorSprites[k], rectangleSprite, selectedIndeces[k], k, 64);
                                    }
                                    game.setScreen(new GameScreen(game, mapScreen, this, region, plantActor));
                                } else {
                                    Gdx.app.log("verbose", "UYou cannot start the game without plants!");
                                }
                            }
                        } else {
                            rectangleSprite.setBounds(rectangles[i][j].getX(), rectangles[i][j].getY(), rectangles[i][j].getWidth(), rectangles[i][j].getHeight());
                            if(i < rectangles.length - 1) {
                                setData(i * rectangles[i].length + j);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
//        Rectangle rect = new Rectangle(x - 16f, 720 - (y + 16f), 32f, 32f);
//
//        if(selectRectangle.overlaps(rect)) {
//            if(currentSelectionIndex <= selectedIndeces.length) {
//                selected[index] = !selected[index];
//                if(selected[index]) {
//                    if(currentSelectionIndex < selectedIndeces.length) {
//                        selectedIndeces[currentSelectionIndex] = index;
//                        renderedActorSprites[currentSelectionIndex].set(actorSprites[index]);
//                        renderedActorSprites[currentSelectionIndex].setBounds(PlantCodex.plantSelectorIndex[currentSelectionIndex], 64, 64, 64);
//                        currentSelectionIndex++;
//                    } else {
//                        selected[index] = !selected[index];
//                    }
//                } else {
//                    for(int i = 0; i < selectedIndeces.length; i++) {
//                        if(selectedIndeces[i] == index) {
//                            for(int j = i; j < selectedIndeces.length; j++) {
//                                if(j + 1 == selectedIndeces.length) {
//                                    selectedIndeces[j] = -1;
//                                    renderedActorSprites[j] = new Sprite();
//                                } else {
//                                    selectedIndeces[j] = selectedIndeces[j + 1];
//                                    renderedActorSprites[j] = renderedActorSprites[j + 1];
//                                    renderedActorSprites[j].setBounds(PlantCodex.plantSelectorIndex[j], 64, 64, 64);
//                                }
//                            }
//                            break;
//                        }
//                    }
//                    currentSelectionIndex--;
//                }
//            }
//        } else {
//            for(int i = 0; i < actorRectangles.length; i++) {
//                if(actorRectangles[i].overlaps(selectRectangle)) {
//                    Gdx.app.log("Verbose", "Touched on a plant icon" );
//                    setData(selectedIndeces[i]);
//                    return true;
//                }
//            }
//
//            for(int i = 0; i < rectangles.length; i++) {
//                for(int j = 0; j < rectangles[i].length; j++) {
//                    if(rect.overlaps(rectangles[i][j])) {
//                        if(i + 1 == rectangles.length && (j == 0 || j + 1 == rectangles[i].length)) {
//                            if(j == 0) {
//                                game.setScreen(mapScreen);
//                                this.dispose();
//                            } else {
//                                if(currentSelectionIndex > 0) {
//                                    PlantActor plantActor[] = new PlantActor[currentSelectionIndex];
//                                    for(int k = 0; k < plantActor.length; k++) {
//                                        plantActor[k] = new PlantActor(renderedActorSprites[k], rectangleSprite, selectedIndeces[k], k, 64);
//                                    }
//                                    game.setScreen(new GameScreen(game, mapScreen, this, region, plantActor));
//                                } else {
//                                    Gdx.app.log("verbose", "UYou cannot start the game without plants!");
//                                }
//                            }
//                        } else {
//                            rectangleSprite.setBounds(rectangles[i][j].getX(), rectangles[i][j].getY(), rectangles[i][j].getWidth(), rectangles[i][j].getHeight());
//                            if(i < rectangles.length - 1) {
//                                setData(i * rectangles[i].length + j);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return true;
        return false;
    }

    private void setData(int index) {
        this.index = index;
        float barSize = 260f;
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

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new GestureDetector(this));
    }

    @Override
    public void dispose() {
        lowerHud.getTexture().dispose();
        playButton.getTexture().dispose();
        homeButton.getTexture().dispose();
        plantScreenBG.getTexture().dispose();
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
        rectangleSprite.getTexture().dispose();
        trees.dispose();
        plants.dispose();
        stage.dispose();
        plantHP.getTexture().dispose();
        plantAS.getTexture().dispose();
        plantDmg.getTexture().dispose();
        checkButton.getTexture().dispose();
        xButton.getTexture().dispose();
        onPressCross.getTexture().dispose();
        onPressPlay.getTexture().dispose();
        onPressCheck.getTexture().dispose();
        onPressHome.getTexture().dispose();
    }

    public MapScreen getMapScreen() {
        return mapScreen;
    }

    public Region getRegion() {
        return region;
    }
}
