package com.systemphoenix.edenalpha.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.systemphoenix.edenalpha.Actors.Plant;
import com.systemphoenix.edenalpha.Codex.ButtonCodex;
import com.systemphoenix.edenalpha.Codex.PlantCodex;
import com.systemphoenix.edenalpha.Codex.RegionCodex;
import com.systemphoenix.edenalpha.CollisionBit;
import com.systemphoenix.edenalpha.EdenAlpha;
import com.systemphoenix.edenalpha.EnemyUtils.Wave;
import com.systemphoenix.edenalpha.PlantSquares.PlantSquare;
import com.systemphoenix.edenalpha.PlantSquares.SquareType;
import com.systemphoenix.edenalpha.Region;
import com.systemphoenix.edenalpha.Scenes.ButtonActor;
import com.systemphoenix.edenalpha.Scenes.GameHud;
import com.systemphoenix.edenalpha.Scenes.PlantActor;
import com.systemphoenix.edenalpha.Scenes.TopHud;
import com.systemphoenix.edenalpha.WorldContactListener;

public class GameScreen extends AbsoluteScreen {
    public static final float FPSCAP = 1 / 60f;
    private Region region;

    private MapScreen mapScreen;
    private PlantScreen plantScreen;

    private Viewport viewport;
    private TopHud topHud;
    private GameHud gameHud;

    private Sprite loseEndGame, winEndGame, pausedSprite;
    private Sprite redRangeSprite, greenRangeSprite;

    private Stage gameStage, pauseStage;
    private ButtonActor playButton, homeButton, restartButton;
    private Label pauseStageLabel;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer debugRenderer;

    private Music bgMusic;
    private Sound bulletSound, pulseSound, slashSound;

    private Array<Wave> waves;
    private Array<Plant> plants;
    private Array<InputProcessor> inputProcessors, mainProcessors, pauseProcessors;

    private PlantSquare[][] plantSquares, subSquares;
    private float pastZoomDistance, plantSquareSize, accumulator, seeds = 75, waterRate = 0.5f, sunlight, water = 150, waterUpgradeSeedCost = 50, forestLife;
    private int waveIndex = -1, waveLimit = 10, selectedX = -1, selectedY = -1, displaySquare = -3, pseudoPlantIndex = -1;
    private long timer = 0, newWaveCountdown, timeGap, displaySquareTimer, seedTimer, waveDisplayTimer;
    private boolean preSixty = true, directionSquares[][], newWave = false, ready = false, paused = false, willPause = false, win = false, lose = false, running = false, canDisplaySquare, canPlant = false, firstCall = true;
    private boolean willRestart = false, waveDisplay = true;

    public GameScreen(EdenAlpha game, MapScreen mapScreen, PlantScreen plantScreen, Region region, PlantActor[] plantActors) {
        super(game);
        this.region = region;
        this.mapScreen = mapScreen;
        this.plantScreen = plantScreen;
        this.forestLife = region.getLifePercentage();

//        this.seeds = game.getSeedCount() > 75 ? game.getSeedCount() : 75;
//        this.water = game.getWaterCount() > 150 ? game.getWaterCount() : 150;
        this.sunlight = region.getSunlight();

        worldHeight = region.getWorldHeight();
        worldWidth = region.getWorldWidth();

        this.viewport = new FitViewport(screenWidth, screenHeight, cam);
        this.gameStage = new Stage(viewport, game.getGameGraphics());
        this.pauseStage = new Stage(viewport, game.getGameGraphics());

        playButton = new ButtonActor(ButtonCodex.PLAY, this, 576, screenHeight / 2 - 64, 128, true, false);
        homeButton = new ButtonActor(ButtonCodex.HOME, this, 432, screenHeight / 2 - 64, 128, true, false);
        restartButton = new ButtonActor(ButtonCodex.RESTART, this, 720, screenHeight / 2 - 64, 128, true, false);

        playButton.setCanPress(false);
        homeButton.setCanPress(false);
        restartButton.setCanPress(false);

        pauseStage.addActor(playButton);
        pauseStage.addActor(homeButton);
        pauseStage.addActor(restartButton);

        Table temp = new Table();
        pauseStageLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));

        temp.setBounds((screenWidth / 2) - (screenWidth / 4), screenHeight / 8, screenWidth / 2, 64);
        temp.center().bottom().padBottom(64);
        temp.add(pauseStageLabel).expandX();

        pauseStage.addActor(temp);

        this.gameHud = new GameHud(game, this, plantActors);
        this.topHud = new TopHud(game, this);

        for(int i = 0; i < plantActors.length; i++) {
            plantActors[i].setGameScreen(this);
        }

        this.winEndGame = new Sprite(new Texture(Gdx.files.internal("bgScreen/winEndGame.png")));
        this.winEndGame.setBounds(0f, 0f, worldWidth, worldHeight);
        this.loseEndGame = new Sprite(new Texture(Gdx.files.internal("bgScreen/loseEndGame.png")));
        this.loseEndGame.setBounds(0f, 0f, worldWidth, worldHeight);
        this.pausedSprite = new Sprite(new Texture(Gdx.files.internal("bgScreen/paused.png")));
        this.pausedSprite.setBounds(0f, 0f, worldWidth, worldHeight);

        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sfx/fx/Bullet.mp3"));
        slashSound = Gdx.audio.newSound(Gdx.files.internal("sfx/fx/Slash.mp3"));
        pulseSound = Gdx.audio.newSound(Gdx.files.internal("sfx/fx/Pulse.mp3"));

        Gdx.input.setCatchBackKey(true);
        plants = new Array<Plant>();
        inputProcessors = null;
        timer = System.currentTimeMillis();
        ready = true;
    }

    private void initialize() {
        TmxMapLoader mapLoader;
        topHud.setLoadingMessage("Loading level...");
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("levels/" + region.getMapIndex() + ".tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        topHud.setLoadingMessage("Loading world...");
        createWorld();
        topHud.setLoadingMessage("Loading enemies...");
        redRangeSprite = new Sprite(new Texture(Gdx.files.internal("plantRange/rangeSprite.png")));
        redRangeSprite.setBounds(0, 0, 0, 0);
        greenRangeSprite = new Sprite(new Texture(Gdx.files.internal("plantRange/effectiveRangeSprite.png")));
        greenRangeSprite.setBounds(0, 0, 0, 0);
        createEnemyWaves();

        cam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        boundCamera();
        running = true;
        timer = System.currentTimeMillis();
        topHud.setLoadingMessage("");
    }

    private void createEnemyWaves() {
        Array<Vector2> spawnPoints = new Array<Vector2>();
        waves = new Array<Wave>();

        for(MapObject object : map.getLayers().get("spawnPoints").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            spawnPoints.add(new Vector2(rect.getX(), rect.getY()));
        }

        int limit;

        int[][] temp = region.getWaves();

        int enemyLimit = 10;
        for(int i = 0; i < waveLimit; i++) {
            if((i + 1) % 5 == 0) {
                limit = enemyLimit * 2;
            } else {
                limit = enemyLimit;
            }
            waves.add(new Wave(this, spawnPoints, i, limit, temp[i]));
        }
    }

    private void createWorld() {
        Array<Body> spawnPoints, endPoints, pathBounds;

        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new WorldContactListener());
        debugRenderer = new Box2DDebugRenderer();

        plantSquares = new PlantSquare[region.getArraySizeY()][region.getArraySizeX()];
        subSquares = new PlantSquare[region.getArraySizeY()][region.getArraySizeX()];
        directionSquares = new boolean[region.getArraySizeY()][region.getArraySizeX()];
        for(int i = 0; i < region.getArraySizeY(); i++) {
            for(int j = 0; j < region.getArraySizeX(); j++) {
                plantSquares[i][j] = null;
                subSquares[i][j] = null;
                directionSquares[i][j] = false;
            }
        }

        spawnPoints = new Array<Body>();
        endPoints = new Array<Body>();
        pathBounds = new Array<Body>();
        plantSquareSize = 32;
        for(MapObject object : map.getLayers().get("plantSquares").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            plantSquares[(int)rect.getY() / (int)plantSquareSize][(int)rect.getX() / (int) plantSquareSize] = new PlantSquare(this, rect.getX() + plantSquareSize / 2, rect.getY() + plantSquareSize / 2, plantSquareSize, SquareType.LAND);
        }

        try {
            for(MapObject object : map.getLayers().get("waterPlantSquares").getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                plantSquares[(int)rect.getY() / (int)plantSquareSize][(int)rect.getX() / (int) plantSquareSize] = new PlantSquare(this, rect.getX() + plantSquareSize / 2, rect.getY() + plantSquareSize / 2, plantSquareSize, SquareType.WATER);
            }
        } catch(Exception e) {
            Gdx.app.log("Verbose", "No waterPlantSquares layer");
        }

        try {
            for(MapObject object : map.getLayers().get("saltwaterPlantSquares").getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                plantSquares[(int)rect.getY() / (int)plantSquareSize][(int)rect.getX() / (int) plantSquareSize] = new PlantSquare(this, rect.getX() + plantSquareSize / 2, rect.getY() + plantSquareSize / 2, plantSquareSize, SquareType.SALT_WATER);
            }
        } catch(Exception e) {
            Gdx.app.log("Verbose", "No saltwaterPlantSquares layer");
        }

        for(MapObject object : map.getLayers().get("directionSquares").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            directionSquares[(int)rect.getY() / (int)plantSquareSize][(int)rect.getX() / (int) plantSquareSize] = true;
        }


        createBodies(spawnPoints, "spawnPoints", CollisionBit.SPAWNPOINT, CollisionBit.SPAWNPOINT | CollisionBit.ENDPOINT | CollisionBit.PATHBOUND);
        createBodies(endPoints, "endPoints", CollisionBit.ENDPOINT, CollisionBit.ENEMY | CollisionBit.ENDPOINT | CollisionBit.SPAWNPOINT | CollisionBit.PATHBOUND);
        createBodies(pathBounds, "pathBounds", CollisionBit.PATHBOUND, CollisionBit.ENEMY | CollisionBit.SPAWNPOINT | CollisionBit.ENDPOINT);

//        createEnemies();
//        createInterval = System.currentTimeMillis();
    }

    public void createBodies(Array<Body> bodies, String layer, short categoryBit, int maskBit) {
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;
        for(MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2 );

            body = world.createBody(bodyDef);

            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = categoryBit;
            fixtureDef.filter.maskBits = (short)maskBit;
            body.createFixture(fixtureDef);

            bodies.add(body);
        }
    }

    public void update(float delta) {
        long sixtyMinuteMark = region.getTimeStart();
        cam.update();
        if(region.getLifePercentage() > 0) {
            accumulator+=delta;
            while(accumulator>FPSCAP){
                world.step(FPSCAP, 6, 2);
                accumulator-=FPSCAP;
            }

            long currentSec = (System.currentTimeMillis() - timer) / 1000;
            long currentSecTens = currentSec / 10, currentSecOnes = currentSec % 10;
            long currentMin = currentSecTens / 6;
            currentSecTens = currentSecTens % 6;
            long currentMinTens = currentMin / 10, currentMinOnes = currentMin % 10;

            if(preSixty) {
                currentSec = sixtyMinuteMark - (System.currentTimeMillis() - timer) / 1000;
                currentSecTens = currentSec / 10;
                currentSecOnes = currentSec % 10;
                currentMin = currentSecTens / 6;
                currentSecTens = currentSecTens % 6;
                currentMinTens = currentMin / 10;
                currentMinOnes = currentMin % 10;
                if(currentMin == 0 && currentSec == 0) {
                    preSixty = false;
                    timer = System.currentTimeMillis();
                    newWave = true;
                    newWaveCountdown = 0;
                } else if(currentSec == 63) {
                    displaySquareTimer = System.currentTimeMillis();
                    canDisplaySquare = true;
                } else if(!canDisplaySquare && currentSec < 60) {
                    canPlant = true;
                }
            }

            if(System.currentTimeMillis() - seedTimer >= 1000) {
                seedTimer = System.currentTimeMillis();
                seeds += 0.25f;
                if(seeds > 999) {
                    seeds = 999;
                }
                topHud.setSeedStatMessage("" + (int)seeds);

                water += waterRate;
                if(water > 999) {
                    water = 999;
                }
                topHud.setWaterStatMessage("" + (int)water);
            }
            if(newWave) {
                if(System.currentTimeMillis() - newWaveCountdown >= 15000) {
                    newWave = false;
                    waveIndex++;
                    if(waveIndex >= waveLimit) {
                        win = true;
                        running = false;
                    }
                    if(!win) {
                        topHud.setReadyPlantMessage("WAVE " + (waveIndex + 1));
                        waveDisplayTimer = System.currentTimeMillis();
                        waveDisplay = true;
                    }
                }

                topHud.setWaveStatMessage("" + (waveIndex + 1));
            }

            if(waveDisplay && System.currentTimeMillis() - waveDisplayTimer > 2000) {
                waveDisplay = false;
                topHud.setReadyPlantMessage("");
            }

            if(waveIndex >= 0 && !newWave) {
                if(waveIndex < waveLimit) {
                    Wave wave = waves.get(waveIndex);
                    wave.update(delta);
                    if(wave.isCleared()) {
                        newWave = true;
                        newWaveCountdown = System.currentTimeMillis();
                    }
                } else {
                    win = true;
                    running = false;
                }
            }

            topHud.setTimeStats(currentMinTens + "" + currentMinOnes + ":" + currentSecTens + "" + currentSecOnes);
        } else {
            lose = true;
            running = false;
        }
        topHud.setMessage("" + (int)forestLife);
        renderer.setView(cam);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1);
        if(willDispose) {
            game.setScreen(mapScreen);
            this.dispose();
        } else if(willRestart) {
            restart();
            willRestart = false;
        } else if(willPause || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            pauseGame();
            willPause = false;
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(!paused) {
            if(running) {
                update(delta);
                renderer.render();
//            debugRenderer.render(world, cam.combined);
            }
            else if(firstCall){
                    if(System.currentTimeMillis() - timer >= 1000) {
                        firstCall = false;
                        initialize();
                        timer = System.currentTimeMillis();
                        seedTimer = System.currentTimeMillis();
                        topHud.createLabels();
                        topHud.setPauseButtonCanDraw(true);
                        topHud.setSeedStatMessage("" + (int)seeds);

                        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sfx/bg/" + region.getMusicFX()));
                        bgMusic.setLooping(true);
//                        bgMusic.play();
                    }
            }

            gameGraphics.setProjectionMatrix(cam.combined);
            gameGraphics.begin();
            if(running) {
                if(canPlant) {
                    if(selectedX != -1 && selectedY != -1) {
                        plantSquares[selectedY][selectedX].draw(gameGraphics);
                    }
                }
                if(canDisplaySquare) {
                    if(System.currentTimeMillis() - displaySquareTimer >= 25) {
                        displaySquare++;
                        displaySquareTimer = System.currentTimeMillis();
                    }
                    if(displaySquare >= 0 && displaySquare < region.getArraySizeX()) {
                        for(int i = 0; i < region.getArraySizeY(); i++) {
                            for(int j = displaySquare - 3; j <= displaySquare + 3; j++) {
                                if(j < region.getArraySizeX() && j >= 0 && plantSquares[i][j] != null)
                                    plantSquares[i][j].preDraw(gameGraphics);
                            }
                        }
                        topHud.setReadyPlantMessage("Ready?");
                    } else if(displaySquare >= region.getArraySizeX() + 10) {
                        for(int i = 0; i < region.getArraySizeY(); i++) {
                            for(int j = 0; j < region.getArraySizeX(); j++) {
                                if(plantSquares[i][j] != null) {
                                    plantSquares[i][j].preDraw(gameGraphics);
                                }
                            }
                        }
                        topHud.setReadyPlantMessage("PLANT!");
                    }
                    if(displaySquare > region.getArraySizeX() + 20) {
                        canDisplaySquare = false;
                        canPlant = true;
                        topHud.setReadyPlantMessage("");
                        gameHud.setDrawable(-1);
                        gameHud.setCanDraw(true);
                    }
                } else {
                    if(waveIndex >= 0 && !newWave) {
                        waves.get(waveIndex).render(gameGraphics);
                    }
                }

                if((selectedX != -1 && selectedY != -1) && PlantActor.getRecentlySelectedActor() != null) {
                    redRangeSprite.draw(gameGraphics);
                    float a = 0.5f;
                    greenRangeSprite.setColor(greenRangeSprite.getColor().r, greenRangeSprite.getColor().g, greenRangeSprite.getColor().b, a);
                    greenRangeSprite.draw(gameGraphics);
                }
            }
            gameGraphics.end();

            try {
                for(int i = 0; i < plants.size; i++) {
                    if(plants.get(i).canDispose()) {
                        unroot(plants.get(i));
                    }
                }
                gameGraphics.setProjectionMatrix(gameStage.getCamera().combined);
                gameStage.draw();
            } catch(Exception e) {
                Gdx.app.log("Verbose", "Error rendering gameStage: " + e.getMessage());
            }
            try {
                gameGraphics.setProjectionMatrix(topHud.getStage().getCamera().combined);
                topHud.update();
                topHud.getStage().draw();
            } catch(Exception e) {
                Gdx.app.log("Verbose", "Error rendering top hud: " + e.getMessage());
            }
            try {
                gameGraphics.setProjectionMatrix(gameHud.getStage().getCamera().combined);
                gameHud.getStage().act(Gdx.graphics.getDeltaTime());
                gameHud.draw(gameGraphics);
            } catch (Exception e) {
                Gdx.app.log("Verbose", "Error rendering game hud: " + e.getMessage());
            }

            if(win || lose) {
                cam.zoom = worldWidth/screenWidth;
                boundCamera();
                gameGraphics.begin();
                if(win) {
                    if(mapScreen.getHighLevelBound() == region.getMapIndex()) {
                        if(mapScreen.getHighLevelBound() < 16) {
                            game.setLevelBounds(mapScreen.getLowLevelBound(), mapScreen.getHighLevelBound() + 1);
                        }

                        boolean stringSet = false;
                        for(int i = 0; i < PlantCodex.level.length; i++) {
                            if(PlantCodex.level[i] == game.getHighLevelBound()) {
                                pauseStageLabel.setText("You unlocked " + RegionCodex.names[region.getMapIndex() + 1] + " and " + PlantCodex.plantName[i] + "!");
                                stringSet = true;
                                break;
                            }
                        }
                        if(!stringSet) {
                            pauseStageLabel.setText("You unlocked " + RegionCodex.names[region.getMapIndex() + 1] + "!");
                        }
                    }
                    winEndGame.draw(gameGraphics);
                } else {
                    loseEndGame.draw(gameGraphics);
                }
                gameGraphics.end();

                homeButton.setCanDraw(true);
                homeButton.setCanPress(true);

                restartButton.setCanPress(true);
                restartButton.setCanDraw(true);

                gameGraphics.setProjectionMatrix(pauseStage.getCamera().combined);
                pauseStage.draw();

                game.setResourceCount(seeds, water);
            }

        } else {
            gameGraphics.begin();
            pausedSprite.draw(gameGraphics);
            gameGraphics.end();
            gameGraphics.setProjectionMatrix(pauseStage.getCamera().combined);
            pauseStage.draw();
        }
    }

    public void bindInput() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        if(mainProcessors == null) {
            mainProcessors = new Array<InputProcessor>();
            mainProcessors.add(this.gameStage);
            mainProcessors.add(gameHud.getStage());
            mainProcessors.add(gameHud.getPlantActorStage());
            mainProcessors.add(gameHud.getPlantStage());
            mainProcessors.add(topHud.getStage());
            mainProcessors.add(new GestureDetector(this));
        }
        if(pauseProcessors == null) {
            pauseProcessors = new Array<InputProcessor>();
            pauseProcessors.add(this.pauseStage);
        }
        inputProcessors = mainProcessors;
        inputMultiplexer.setProcessors(inputProcessors);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void bindInput(Array<InputProcessor> processors) {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputProcessors = processors;
        inputMultiplexer.setProcessors(inputProcessors);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void addProcessor(InputProcessor inputProcessor) {
        if(inputProcessors == null) {
            inputProcessors = new Array<InputProcessor>();
            inputProcessors.add(inputProcessor);
            inputProcessors.add(new GestureDetector(this));
        } else {
            inputProcessors.insert(0, inputProcessor);
        }
    }

    public void harvestSeeds(Plant plant) {
        seeds += plant.harvestSeeds();
        topHud.setSeedStatMessage("" + (int)seeds);
    }

    public void plant(int plantIndex, TextureRegion sprite) {
        if(seeds - PlantCodex.cost[plantIndex] / 2 >= 0 && water - PlantCodex.cost[plantIndex] >= 0 && (plantIndex > -1 && plantIndex < 15) && (selectedX > -1 && selectedY > -1) && (plantSquares[selectedY][selectedX] != null)) {
            seeds -= PlantCodex.cost[plantIndex] / 2;
            water -= PlantCodex.cost[plantIndex];
            topHud.setSeedStatMessage("" + (int)seeds);
            topHud.setWaterStatMessage("" + (int)water);
            switch(plantIndex) {
                case 4:
                    plants.add(new Plant(this, gameStage, sprite, plantSquares[selectedY][selectedX], plantIndex, selectedX * 32f, selectedY * 32f, sunlight, pulseSound));
                    break;
                case 5:
                case 11:
                case 13:
                    plants.add(new Plant(this, gameStage, sprite, plantSquares[selectedY][selectedX], plantIndex, selectedX * 32f, selectedY * 32f, sunlight, slashSound));
                    break;
                default:
                    plants.add(new Plant(this, gameStage, sprite, plantSquares[selectedY][selectedX], plantIndex, selectedX * 32f, selectedY * 32f, sunlight, bulletSound));
                    break;
            }

            for(int i = selectedY - 1; i <= selectedY + 1; i++) {
                for(int j = selectedX - 1; j <= selectedX + 1; j++) {
                    subSquares[i][j] = plantSquares[i][j];
                    plantSquares[i][j] = null;
                }
            }

            resetHud();
        }
    }

    public void unroot(Plant plant) {
        if(plant != null) {
            if(plant.isRefundable()) {
                updateSeeds(PlantCodex.cost[plant.getPlantIndex()] / 2);
                updateWater(PlantCodex.cost[plant.getPlantIndex()]);
            } else {
                updateSeeds(PlantCodex.cost[plant.getPlantIndex()] / 6);
                updateWater(PlantCodex.cost[plant.getPlantIndex()] / 3);
            }
            for(int i = 0; i < plants.size; i++) {
                if(plant.equals(plants.get(i))) {
                    plants.removeIndex(i);
                    break;
                }
            }

            int x = (int)plant.getX() / 32, y = (int)plant.getY() / 32;
            for(int i = y - 1; i <= y + 1; i++) {
                for(int j = x - 1; j <= x + 1; j++) {
                    plantSquares[i][j] = subSquares[i][j];
                    subSquares[i][j] = null;
                }
            }
            world.destroyBody(plant.getBody());
            plant.remove();
            plant.dispose();
            resetHud();
            gameHud.setDrawable(1);
        }
    }

    public void resetHud() {
        selectedX = selectedY = -1;
        gameHud.setMessage("");
        gameHud.setCanDraw(false);
    }

    public void tap(float x, float y) {
        if(!paused && !win && !lose &&(x >= 0 && y >= 0)) {
            Vector3 touchPos = new Vector3();
            touchPos.set(x, y, 0);
            cam.unproject(touchPos);
            touchPos.x = touchPos.x - (int)touchPos.x  > 0.5 ? (int) touchPos.x + 1 : (int) touchPos.x;
            touchPos.y = touchPos.y - (int)touchPos.y  > 0.5 ? (int) touchPos.y + 1 : (int) touchPos.y;
            if(canPlant) {
                if(plantSquares[(int)touchPos.y / (int)plantSquareSize][(int)touchPos.x / (int)plantSquareSize] != null) {
                    selectedX = (int) touchPos.x / (int)plantSquareSize;
                    selectedY = (int) touchPos.y / (int)plantSquareSize;

                    gameHud.setCanDraw(true);
                    if(PlantActor.getRecentlySelectedActor() != null) {
                        setPseudoPlant(pseudoPlantIndex);
                    }
                } else {
                    resetHud();
                }
//                    gameHud.setDrawable(-1);
//                    Plant.nullSelectedPlant();
            }
        } else {
            selectedX = selectedY = -1;
        }
    }

    public void tap() {
        if(paused) {
            resumeGame();
        } else if(win || lose) {
            game.setScreen(mapScreen);
            this.dispose();
        }
    }

//  Touch methods
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Plant.nullSelectedPlant();
        gameHud.setCanDraw(true);
        gameHud.setDrawable(-1);
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if(!paused) {
            Vector3 touchPos = new Vector3();
            touchPos.set(x, y, 0);
            cam.unproject(touchPos);

            cam.position.x -= cam.zoom * deltaX;
            cam.position.y += cam.zoom * deltaY;
            boundCamera();
            return true;
        } else return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        if(!paused && canPlant) {
            if(pastZoomDistance < distance) {
                cam.zoom -= 0.01f;
            } else if(pastZoomDistance > distance) {
                cam.zoom += 0.01f;
            }
            pastZoomDistance = distance;
            cam.zoom = MathUtils.clamp(cam.zoom, 0.4f, worldWidth/screenWidth);
            boundCamera();
            return true;
        } else return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }

//  Screen methods
    @Override
    public void show() {
//        super.show();
        bindInput();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        pauseGame();
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        pauseGame();
    }

    @Override
    public void dispose() {

        for(int i = 0; i < waves.size; i++) {
            waves.get(i).dispose();
        }

        for(int i = 0; i < plants.size; i++) {
            plants.get(i).dispose();
        }

        bgMusic.dispose();
        bulletSound.dispose();
        pulseSound.dispose();
        slashSound.dispose();

        topHud.dispose();
        gameHud.dispose();
        map.dispose();

        pausedSprite.getTexture().dispose();

        world.dispose();
        debugRenderer.dispose();
        greenRangeSprite.getTexture().dispose();
        redRangeSprite.getTexture().dispose();

        playButton.dispose();
        homeButton.dispose();
        restartButton.dispose();

        pauseStage.dispose();
    }

    public void restart() {
        plantScreen.createGameScreen();
        this.dispose();
    }

    public void pauseGame() {
        paused = true;
        cam.zoom = worldWidth/screenWidth;
        boundCamera();
        topHud.setPauseButtonCanDraw(false);
        gameHud.setCanPress(false);

        playButton.setCanDraw(true);
        homeButton.setCanDraw(true);
        restartButton.setCanDraw(true);

        playButton.setCanPress(true);
        homeButton.setCanPress(true);
        restartButton.setCanPress(true);

        bindInput(pauseProcessors);

        timeGap = System.currentTimeMillis() - timer;
    }

    public void resumeGame() {
        topHud.setPauseButtonCanDraw(true);
        gameHud.setDrawable(-1);
        gameHud.setCanDraw(true);

        playButton.setCanDraw(false);
        homeButton.setCanDraw(false);
        restartButton.setCanDraw(false);

        playButton.setCanPress(false);
        homeButton.setCanPress(false);
        restartButton.setCanPress(false);

        paused = false;

        bindInput(mainProcessors);

        timer = System.currentTimeMillis() - timeGap;
        willPause = false;
    }

    public World getWorld() {
        return this.world;
    }

    public Region getRegion() {
        return region;
    }

    public PlantSquare[][] getPlantSquares() {
        return this.plantSquares;
    }

    public GameHud getGameHud() {
        return this.gameHud;
    }

    public TopHud getTopHud() {
        return this.topHud;
    }

    public boolean[][] getDirectionSquares() {
        return directionSquares;
    }

    public boolean isReady() {
        return ready;
    }

    public boolean isFirstCall() {
        return firstCall;
    }

    public boolean canPlant() {
        return canPlant;
    }

    public Vector2 getSelectedXY() {
        return new Vector2(selectedX * plantSquareSize, selectedY * plantSquareSize);
    }

    public float getWorldHeight() {
        return this.worldHeight;
    }

    public float getWorldWidth() {
        return this.worldWidth;
    }

    public float getSeeds() {
        return seeds;
    }

    public float getWater() {
        return water;
    }

    public float getWaterUpgradeSeedCost() {
        return waterUpgradeSeedCost;
    }

    public long getTimer() {
        return timer;
    }

    public Array<Plant> getPlants() {
        return plants;
    }

    public OrthographicCamera getCamera() {
        return cam;
    }

    public void upgradeWaterRate() {
        if(waterRate < 16) {
            seeds -= waterUpgradeSeedCost;
            topHud.setSeedStatMessage("" + (int)seeds);
            waterUpgradeSeedCost *= 2;
            waterRate *= 2;
        }
    }

    public void setWillPause(boolean willPause) {
        this.willPause = willPause;
    }

    public void setWillRestart(boolean willRestart) {
        this.willRestart = willRestart;
    }

    public void setPseudoPlant(int plantIndex) {
        this.pseudoPlantIndex = plantIndex;
        if(plantIndex >= 0 && plantIndex < 15) {
            float actualRange = PlantCodex.rangeStats[PlantCodex.range[plantIndex]];
            float effectiveRange = PlantCodex.effectiveRange[plantIndex];
            redRangeSprite.setBounds(this.getSelectedXY().x - (32f * actualRange), this.getSelectedXY().y - (32f * actualRange), (32f * actualRange * 2) + 64, (32f * actualRange * 2) + 64);
            greenRangeSprite.setBounds(this.getSelectedXY().x - (32f * effectiveRange), this.getSelectedXY().y - (32f * effectiveRange), (32f * effectiveRange * 2) + 64, (32f * effectiveRange * 2) + 64);
        }
    }

    public void updateSeeds(float value) {
        this.seeds += value;
        topHud.setSeedStatMessage("" + (int)seeds);
    }

    public void updateWater(float value) {
        this.water += value;
        topHud.setWaterStatMessage("" + (int)water);
    }

    public void damageForest(float damage) {
        this.forestLife -= damage;
    }
}