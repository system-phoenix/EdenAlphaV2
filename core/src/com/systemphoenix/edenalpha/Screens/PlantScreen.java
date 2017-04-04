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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.systemphoenix.edenalpha.Codex.ButtonCodex;
import com.systemphoenix.edenalpha.Codex.PlantCodex;
import com.systemphoenix.edenalpha.EdenAlpha;
import com.systemphoenix.edenalpha.Region;
import com.systemphoenix.edenalpha.Scenes.ButtonActor;
import com.systemphoenix.edenalpha.Scenes.PlantActor;
import com.systemphoenix.edenalpha.Scenes.PlantButton;

public class PlantScreen extends AbsoluteScreen {

    private Stage stage;
    private Texture trees, plants;
    private Sprite lowerHud, plantScreenBG, rectangleSprite, plantHP, plantAS, plantDmg;
    private Sprite[] sprites, actorSprites, renderedActorSprites;
    private Rectangle actorRectangles[], rectangles[][];

    private ButtonActor playButton, checkButton, crossButton, homeButton;

    private SpriteBatch gameGraphics;
    private Label plantName, plantType, plantCost, plantGrowthTime;

    private MapScreen mapScreen;
    private Region region;

    private float sizeModifier = 10;

    private int selectedIndeces[], currentSelectionIndex, index;
    private boolean selected[];

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
            if(!(j == 0 || j + 1 == rectangles[rectangles.length - 1].length)) {
                rectangles[rectangles.length - 1][j] = new Rectangle((64 + (j * 144)) - (sizeModifier / 2), 176 - (sizeModifier / 2), 96f + sizeModifier, 96f + sizeModifier);
            }
        }

        initialize();
        Gdx.input.setCatchBackKey(true);
    }

    private void initialize() {
        Viewport viewport = new FitViewport(1280, 720, cam);
        stage = new Stage(viewport, gameGraphics);

        lowerHud = new Sprite(new Texture(Gdx.files.internal("misc/lowerHud.png")));
        plantScreenBG = new Sprite(new Texture(Gdx.files.internal("bgScreen/plantScreen.png")));

        playButton = new ButtonActor(ButtonCodex.PLAY, this, worldWidth - 160f, 32f, 128, false, true);
        homeButton = new ButtonActor(ButtonCodex.HOME, this, 32f, 32f, 128, false, true);
        checkButton = new ButtonActor(ButtonCodex.CHECK, this, 1022, 200, 128, false, true);
        crossButton = new ButtonActor(ButtonCodex.CROSS, this, 878, 200, 128, false, true);

        playButton.setCanDraw(true);
        homeButton.setCanDraw(true);
        checkButton.setCanDraw(true);
        crossButton.setCanDraw(true);

        playButton.setCanPress(true);
        homeButton.setCanPress(true);
        checkButton.setCanPress(true);
        crossButton.setCanPress(false);

        stage.addActor(playButton);
        stage.addActor(homeButton);
        stage.addActor(checkButton);
        stage.addActor(crossButton);

        rectangleSprite = new Sprite(new Texture(Gdx.files.internal("misc/0_PlantSquare.png")));

        TextureRegion tempTrees[][], tempPlants[][];
        trees = new Texture(Gdx.files.internal("trees/trees.png"));
        tempTrees = TextureRegion.split(trees, 64, 64);
        plants = new Texture(Gdx.files.internal("trees/plants.png"));
        tempPlants = TextureRegion.split(plants, 32, 32);
        for(int i = 0; i < tempTrees.length; i++) {
            for(int j = 0; j < tempTrees[i].length; j++) {
                if((i * tempTrees[i].length + j >= mapScreen.getLowLevelBound() && i * tempTrees[i].length + j <= mapScreen.getHighLevelBound()) || (i * tempTrees[i].length + j < 15 && PlantCodex.level[i * tempTrees[i].length + j] <= mapScreen.getHighLevelBound())) {
                    if((i == 0 && (j == 3 || j == 4)) || (i == 1 && j == 2)) {
                        sprites[(i * tempTrees[i].length) + j] = new Sprite(tempPlants[i][j]);
                        actorSprites[(i * tempTrees[i].length) + j] = new Sprite(tempPlants[i][j]);
                    } else if ((i == 3 && j == 0) || (i == 3 && j == 4)) {
                        continue;
                    } else {
                        sprites[(i * tempTrees[i].length) + j] = new Sprite(tempTrees[i][j]);
                        actorSprites[(i * tempTrees[i].length) + j] = new Sprite(tempTrees[i][j]);
                    }
                    sprites[i * tempTrees[i].length + j].setBounds(rectangles[i][j].getX() + (sizeModifier / 2), rectangles[i][j].getY() + (sizeModifier / 2), rectangles[i][j].getWidth() - sizeModifier, rectangles[i][j].getHeight() - sizeModifier);
    //                plantButtons.add(new PlantButton(sprites[(i * tempTrees[i].length)], stage, rectangles[i][j].getX() + (sizeModifier / 2), rectangles[i][j].getY() + (sizeModifier / 2), rectangles[i][j].getWidth() - sizeModifier));
    //                plantButtons.add(new PlantButton(sprites[i * tempTrees[i].length + j], stage, this, i * tempTrees[i].length + j));
                    stage.addActor(new PlantButton(sprites[i * tempTrees[i].length + j], stage, this, i * tempTrees[i].length + j));
                    actorSprites[(i * tempTrees[i].length) + j].setBounds(0, 64, 64, 64);
                }
            }
        }
        rectangleSprite.setBounds(rectangles[0][0].getX(), rectangles[0][0].getY(), rectangles[0][0].getWidth(), rectangles[0][0].getHeight());

        for(int i = 0; i < renderedActorSprites.length; i++) {
            renderedActorSprites[i] = new Sprite();
            renderedActorSprites[i].setBounds(PlantCodex.plantSelectorIndex[i], 64, 64, 64);
            actorRectangles[i] = new Rectangle(PlantCodex.plantSelectorIndex[i], 64, 64, 64);
        }


        Label tempLabel, spaceFiller;
        Table tempTable = new Table(), infoTable = new Table();
        tempTable.setBounds(797, 300, 111, 390);
        infoTable.setBounds(908, 300, 320, 390);

        Color border = Color.BLACK;
        Color fontColor = Color.WHITE;
        BitmapFont tempFont = font;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/neuropol.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 15;
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

        float startY = worldHeight - 89, startX = 938;

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
        if(!selected[index]) {
            checkButton.setCanPress(true);
            crossButton.setCanPress(false);
        } else {
            checkButton.setCanPress(false);
            crossButton.setCanPress(true);
        }
        for(int i = 0; i < currentSelectionIndex; i++) {
            renderedActorSprites[i].draw(gameGraphics);
        }
        gameGraphics.end();

        gameGraphics.setProjectionMatrix(stage.getCamera().combined);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void selectPlant() {
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
    }

    public void createGameScreen() {
        if(currentSelectionIndex > 0) {
            PlantActor plantActor[] = new PlantActor[currentSelectionIndex];
            for(int k = 0; k < plantActor.length; k++) {
                plantActor[k] = new PlantActor(renderedActorSprites[k], rectangleSprite, selectedIndeces[k], k, 64);
            }
            game.setScreen(new GameScreen(game, mapScreen, this, region, plantActor));
        } else {
            Gdx.app.log("verbose", "You cannot start the game without plants!");
        }
    }

    public void backToMapScreen() {
        game.setScreen(mapScreen);
        this.dispose();
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
                plantAS.setBounds(plantAS.getX(), plantAS.getY(), barSize * ((float)PlantCodex.baseAS / (float)(PlantCodex.asStats[PlantCodex.AS[index]] * 3f)), plantAS.getHeight());
            } else {
                plantAS.setBounds(plantAS.getX(), plantAS.getY(), 0, plantAS.getHeight());
            }
            plantDmg.setBounds(plantDmg.getX(), plantDmg.getY(), barSize * ((((PlantCodex.dmgStats[PlantCodex.DMG[index]].x + PlantCodex.dmgStats[PlantCodex.DMG[index]].y)) / (((PlantCodex.baseDmg.x + PlantCodex.baseDmg.y))))), plantDmg.getHeight());
            plantGrowthTime.setText("" + (int)(PlantCodex.growthTime[index]) + " seconds");
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void dispose() {
        lowerHud.getTexture().dispose();
        plantScreenBG.getTexture().dispose();
        plantHP.getTexture().dispose();
        plantAS.getTexture().dispose();
        plantDmg.getTexture().dispose();
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
        playButton.dispose();
        homeButton.dispose();
        crossButton.dispose();
        checkButton.dispose();
        stage.dispose();
    }

    public MapScreen getMapScreen() {
        return mapScreen;
    }

    public Region getRegion() {
        return region;
    }
}
