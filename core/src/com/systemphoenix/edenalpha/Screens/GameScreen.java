package com.systemphoenix.edenalpha.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.systemphoenix.edenalpha.Actors.Plant;
import com.systemphoenix.edenalpha.CollisionBit;
import com.systemphoenix.edenalpha.EdenAlpha;
import com.systemphoenix.edenalpha.EnemyUtils.Wave;
import com.systemphoenix.edenalpha.PlantSquares.PlantSquare;
import com.systemphoenix.edenalpha.PlantSquares.SquareType;
import com.systemphoenix.edenalpha.Region;
import com.systemphoenix.edenalpha.Scenes.GameHud;
import com.systemphoenix.edenalpha.Scenes.PlantActor;
import com.systemphoenix.edenalpha.Scenes.TopHud;
import com.systemphoenix.edenalpha.WorldContactListener;

public class GameScreen extends AbsoluteScreen {
    public static final float FPSCAP = 1 / 60f;
    private Region region;

    private MapScreen mapScreen;

    private Viewport viewport;
    private TopHud topHud;
    private GameHud gameHud;

    private Stage gameStage;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer debugRenderer;

    private Array<Body> spawnPoints, endPoints, pathBounds;
    private Array<Wave> waves;
    private Array<Plant> plants;
    private Array<InputProcessor> inputProcessors;

    private PlantSquare[][] plantSquares;
    private float pastZoomDistance, plantSquareSize, accumulator;
    private int enemyLimit = 10, waveIndex = -1, waveLimit = 10, selectedX = -1, selectedY = -1, displaySquare = -3;
    private long timer = 0, newWaveCountdown, timeGap, displaySquareTimer;
    private boolean preSixty = true, directionSquares[][], newWave = false, ready = false, paused = false, win = false, lose = false, running = false, canDisplaySquare, showAll, canPlant = false, firstCall = true;

    public GameScreen(EdenAlpha game, MapScreen mapScreen, Region region) {
        super(game);
        this.region = region;
        this.mapScreen = mapScreen;

        worldHeight = region.getWorldHeight();
        worldWidth = region.getWorldWidth();

        this.viewport = new FitViewport(screenWidth, screenHeight, cam);
        this.gameStage = new Stage(viewport, game.getGameGraphics());

        this.topHud = new TopHud(game, this);
        this.gameHud = new GameHud(game, this);
        Gdx.input.setCatchBackKey(true);
        plants = new Array<Plant>();
        inputProcessors = null;
        timer = System.currentTimeMillis();
        ready = true;
    }

    private void initialize() {
        TmxMapLoader mapLoader;
        try {
            topHud.setLoadingMessage("Loading level...");
            mapLoader = new TmxMapLoader();
            map = mapLoader.load("levels/" + region.getMapIndex() + ".tmx");
            renderer = new OrthogonalTiledMapRenderer(map);
            Gdx.app.log("Verbose", "Successfully loaded level: " + worldWidth + " x " + worldHeight);

            topHud.setLoadingMessage("Loading world...");
            createWorld();
            topHud.setLoadingMessage("Loading enemies...");
            createEnemyWaves();
        } catch(Exception e) {
            Gdx.app.log("Verbose", "level " + e.getMessage());
        }

        cam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        boundCamera();
        topHud.setMessage(region.getCode() + ": " + region.getName());
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

        for(int i = 0; i < waveLimit; i++) {
            if((i + 1) % 5 == 0) {
                limit = enemyLimit * 2;
            } else {
                limit = enemyLimit;
            }
            waves.add(new Wave(this, spawnPoints, limit, 2));
        }
    }

    private void createWorld() {
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new WorldContactListener());
        debugRenderer = new Box2DDebugRenderer();

        plantSquares = new PlantSquare[region.getArraySizeY()][region.getArraySizeX()];
        directionSquares = new boolean[region.getArraySizeY()][region.getArraySizeX()];
        for(int i = 0; i < region.getArraySizeY(); i++) {
            for(int j = 0; j < region.getArraySizeX(); j++) {
                plantSquares[i][j] = null;
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

    public void start() {

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
                }
            }
            if(newWave) {
                if(System.currentTimeMillis() - newWaveCountdown >= 5000) {
                    newWave = false;
                    waveIndex++;
                    if(waveIndex >= waveLimit) {
                        win = true;
                        running = false;
                    }
                }

                topHud.setWaveStatMessage("Wave " + (waveIndex + 1));
            }

            if(waveIndex >= 0 && !newWave) {
                Wave wave = waves.get(waveIndex);
                wave.update(delta);
                if(wave.isCleared()) {
                    newWave = true;
                    newWaveCountdown = System.currentTimeMillis();
                }
            }

            topHud.setTimeStats(currentMinTens + "" + currentMinOnes + ":" + currentSecTens + "" + currentSecOnes);
        }
        topHud.setMessage("Forest Land Percentage: " + region.getLifePercentage());
        renderer.setView(cam);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(running) {
            update(delta);
            renderer.render();
            if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
                game.setScreen(mapScreen);
                this.dispose();
            }
//        debugRenderer.render(world, cam.combined);
        }
        else if(firstCall){
//            try {
                if(System.currentTimeMillis() - timer >= 1000) {
                    firstCall = false;
                    initialize();
                    timer = System.currentTimeMillis();
                }
//            } catch(Exception e) {
//                Gdx.app.log("Verbose", "Call on \"initialize()\": " + e.getMessage());
//            }
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
                }
            } else {
                if(waveIndex >= 0 && !newWave) {
                    waves.get(waveIndex).render(gameGraphics);
                }
            }
        }
        gameGraphics.end();

        try {
            gameGraphics.setProjectionMatrix(gameStage.getCamera().combined);
            gameStage.draw();
        } catch(Exception e) {
            Gdx.app.log("Verbose", "Error rendering gameStage: " + e.getMessage());
        }
        try {
            gameGraphics.setProjectionMatrix(topHud.getStage().getCamera().combined);
            topHud.getStage().draw();
        } catch(Exception e) {
            Gdx.app.log("Verbose", "Error rendering top hud: " + e.getMessage());
        }
        try {
            gameGraphics.setProjectionMatrix(gameHud.getStage().getCamera().combined);
            gameHud.getStage().draw();
        } catch (Exception e) {
            Gdx.app.log("Verbose", "Error rendering game hud: " + e.getMessage());
        }

        if(paused || win || lose) {
            Gdx.gl.glClearColor(0.95f, 0.95f, 0.95f, 0.5f);
        }
    }

    public void bindInput() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        if(inputProcessors == null) {
            inputProcessors = new Array<InputProcessor>();
            PlantActor[] plantActors = gameHud.getPlantActors();
            for(int i = 0; i < plantActors.length; i++) {
                inputProcessors.add(plantActors[i]);
            }
            inputProcessors.add(new GestureDetector(this));
        }
        inputMultiplexer.setProcessors(inputProcessors);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void plant(int plantIndex, TextureRegion sprite) {
        plants.add(new Plant(this, gameStage, sprite, plantIndex, selectedX * 32f, selectedY * 32f));
        inputProcessors.insert(0, plants.peek());
        resetHud();
    }

    public void resetHud() {
        selectedX = selectedY = -1;
        gameHud.setMessage("");
        gameHud.setCanDraw(false);
    }

//  Touch methods
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if(!paused) {
            Vector3 touchPos = new Vector3();
            touchPos.set(x, y, 0);
            cam.unproject(touchPos);
            touchPos.x = touchPos.x - (int)touchPos.x  > 0.5 ? (int) touchPos.x + 1 : (int) touchPos.x;
            touchPos.y = touchPos.y - (int)touchPos.y  > 0.5 ? (int) touchPos.y + 1 : (int) touchPos.y;
            if(canPlant) {
                try {
                    if(selectedX == (int)touchPos.x / (int)plantSquareSize && selectedY == (int)touchPos.y / (int)plantSquareSize) {
                        selectedY = selectedX = -1;
                        gameHud.setMessage("");
                        gameHud.setCanDraw(false);
                    } else if(plantSquares[(int)touchPos.y / (int)plantSquareSize][(int)touchPos.x / (int)plantSquareSize] != null) {
                        selectedX = (int) touchPos.x / (int)plantSquareSize;
                        selectedY = (int) touchPos.y / (int)plantSquareSize;

                        gameHud.setMessage("(" + selectedX + ", " + selectedY +")");
                        gameHud.setCanDraw(true);
                    } else {
                        resetHud();
                    }

                    Plant.nullSelectedPlant();
                } catch (Exception e) {
                    Gdx.app.log("Verbose", "Error: " + e.getMessage());
                }
            }
        } else {
            paused = false;
        }

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
        Vector3 touchPos = new Vector3();
        touchPos.set(x, y, 0);
        cam.unproject(touchPos);

        cam.position.x -= deltaX;
        cam.position.y += deltaY;
        boundCamera();
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        if(pastZoomDistance < distance) {
            cam.zoom -= 0.01f;
        } else if(pastZoomDistance > distance) {
            cam.zoom += 0.01f;
        }
        pastZoomDistance = distance;
        cam.zoom = MathUtils.clamp(cam.zoom, 0.4f, worldWidth/screenWidth);
        boundCamera();
        return true;
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
        paused = true;
        timeGap = System.currentTimeMillis() - timer;
    }

    @Override
    public void resume() {
        timer = System.currentTimeMillis() - timeGap;
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        for(int i = 0; i < waves.size; i++) {
            waves.get(i).dispose();
        }

        for(int i = 0; i < plants.size; i++) {
            plants.get(i).dispose();
        }

        topHud.dispose();
        gameHud.dispose();
        map.dispose();

        world.dispose();
        debugRenderer.dispose();
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

    public boolean[][] getDirectionSquares() {
        return directionSquares;
    }

    public boolean isReady() {
        return ready;
    }

    public Vector2 getSelectedXY() {
        return new Vector2(selectedX, selectedY);
    }

    public float getWorldHeight() {
        return this.worldHeight;
    }

    public float getWorldWidth() {
        return this.worldWidth;
    }
}