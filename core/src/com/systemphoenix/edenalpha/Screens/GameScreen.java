package com.systemphoenix.edenalpha.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.systemphoenix.edenalpha.CollisionBit;
import com.systemphoenix.edenalpha.EdenAlpha;
import com.systemphoenix.edenalpha.EnemyUtils.Wave;
import com.systemphoenix.edenalpha.PlantSquares.PlantSquare;
import com.systemphoenix.edenalpha.PlantSquares.SquareType;
import com.systemphoenix.edenalpha.Region;
import com.systemphoenix.edenalpha.Scenes.TopHud;
import com.systemphoenix.edenalpha.WorldContactListener;

public class GameScreen extends AbsoluteScreen {
    public static final float FPSCAP = 1 / 60f;
    private Region region;

    private Viewport viewport;
    private TopHud topHud;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer debugRenderer;

    private Array<Body> spawnPoints, endPoints, pathBounds;
    private Array<Wave> waves;

    private PlantSquare[][] plantSquares;
    private Body pastBody;
    private float pastZoomDistance, plantSquareSize, pastBodyX = -1, pastBodyY = -1, accumulator;
    private int enemyLimit = 10, waveIndex = -1, waveLimit = 10, selectedX = -1, selectedY = -1;
    private long timer, createInterval, newWaveCountdown, timeGap;
    private boolean preSixty = true, directionSquares[][], newWave = false, ready = false, paused = false, win = false, lose = false, running = false, showAll = false;

    public GameScreen(EdenAlpha game, Region region) {
        super(game);
        this.region = region;

        worldHeight = region.getWorldHeight();
        worldWidth = region.getWorldWidth();

        this.viewport = new FitViewport(screenWidth, screenHeight, cam);
        this.topHud = new TopHud(game);
        initialize();
        timer = System.currentTimeMillis();
        ready = true;
    }

    private void initialize() {
        TmxMapLoader mapLoader;
        try {
            mapLoader = new TmxMapLoader();
            map = mapLoader.load("levels/" + region.getMapIndex() + ".tmx");
            renderer = new OrthogonalTiledMapRenderer(map);
            Gdx.app.log("Verbose", "Successfully loaded level: " + worldWidth + " x " + worldHeight);

            createWorld();
            createEnemyWaves();
        } catch(Exception e) {
            Gdx.app.log("Verbose", "level " + e.getMessage());
        }

        cam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        boundCamera();
        topHud.setMessage(region.getCode() + ": " + region.getName());
        running = true;
        Gdx.app.log("Verbose", "PlantSquares.length: " + plantSquares.length);
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
            if(i + 1 % 5 == 0) {
                limit = enemyLimit * 2;
            } else {
                limit = enemyLimit;
            }
            waves.add(new Wave(this, spawnPoints, limit, 0));
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
        createInterval = System.currentTimeMillis();
    }

    public void update(float delta) {
        long sixtyMinuteMark = 1;
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
        if(running) update(delta);
        Gdx.gl.glClearColor(0.95f, 0.95f, 0.95f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();
//        debugRenderer.render(world, cam.combined);

        gameGraphics.setProjectionMatrix(cam.combined);
        gameGraphics.begin();
        if(running) {
            if(showAll) {
                for(int i = 0; i < plantSquares.length; i++) {
                    for(int j = 0; j < plantSquares[i].length; j++) {
                        if(plantSquares[i][j] != null) {
                            plantSquares[i][j].draw(gameGraphics);
                        }
                    }
                }
            } else if(selectedX != -1 && selectedY != -1) {
                plantSquares[selectedY][selectedX].draw(gameGraphics);
            }
            if(waveIndex >= 0 && !newWave) {
                waves.get(waveIndex).render(gameGraphics);
            }
        }
        gameGraphics.end();

        gameGraphics.setProjectionMatrix(topHud.getStage().getCamera().combined);
        topHud.getStage().draw();

        if(paused || win || lose) {
            Gdx.gl.glClearColor(0.95f, 0.95f, 0.95f, 0.5f);
        }
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

            try {
                if(selectedX == (int)touchPos.x / (int)plantSquareSize && selectedY == (int)touchPos.y / (int)plantSquareSize) {
                    showAll = false;
                    selectedY = selectedX = -1;
                } else if(plantSquares[(int)touchPos.y / (int)plantSquareSize][(int)touchPos.x / (int)plantSquareSize] != null) {
                    selectedX = (int) touchPos.x / (int)plantSquareSize;
                    selectedY = (int) touchPos.y / (int)plantSquareSize;

                    showAll = false;

                    Gdx.app.log("Verbose", "(" + selectedX + ", " + selectedY +") Status: " + plantSquares[selectedY][selectedX]);
                } else {
                    selectedX = selectedY = -1;
                    showAll = !showAll;
                }
            } catch (Exception e) {
                Gdx.app.log("Verbose", "Error: " + e.getMessage());
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
        cam.zoom = MathUtils.clamp(cam.zoom, 0.3f, worldWidth/screenWidth);
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
        super.show();
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

        topHud.dispose();
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

    public boolean[][] getDirectionSquares() {
        return directionSquares;
    }

    public boolean isReady() {
        return ready;
    }
}


//                PlantSquare plantSquare = plantSquares[(int)touchPos.y / (int)plantSquareSize][(int)touchPos.x / (int)plantSquareSize];
//                Body body = plantSquare.getBody();
//                for(int j = 0; j < body.getFixtureList().size; j++) {
//                    body.getFixtureList().removeIndex(j);
//                }
//                FixtureDef fixtureDef = new FixtureDef();
//                CircleShape shape = new CircleShape();
//                shape.setRadius(plantSquareSize / 2);
//                fixtureDef.shape = shape;
//                fixtureDef.filter.categoryBits = CollisionBit.PLANTSQUARE;
//                fixtureDef.filter.maskBits = CollisionBit.ENDPOINT | CollisionBit.SPAWNPOINT | CollisionBit.PLANTSQUARE;
//                body.createFixture(fixtureDef);
//
//                shape = new CircleShape();
//                switch(plantSquare.getType()) {
//                    case SquareType.LAND:
//                        shape.setRadius(plantSquareSize * 2 + plantSquareSize);
//                        break;
//                    case SquareType.SALT_WATER:
//                        shape.setRadius(plantSquareSize * 2 + plantSquareSize / 8);
//                        break;
//                    case SquareType.WATER:
//                        shape.setRadius(plantSquareSize * 2 + plantSquareSize / 4);
//                        break;
//                }
//                fixtureDef.shape = shape;
//                fixtureDef.filter.categoryBits = CollisionBit.PLANTSQUARE;
//                fixtureDef.filter.maskBits = CollisionBit.ENDPOINT | CollisionBit.SPAWNPOINT | CollisionBit.PLANTSQUARE;
//                body.createFixture(fixtureDef);
//                if((pastBodyX >= 0 && pastBodyY >= 0) && (pastBodyX != body.getPosition().x || pastBodyY != body.getPosition().y)) {
//                    world.destroyBody(pastBody);
//
//                    BodyDef bodyDef = new BodyDef();
//
//                    bodyDef.type = BodyDef.BodyType.StaticBody;
//                    bodyDef.position.set(pastBodyX, pastBodyY);
//
//                    pastBody = world.createBody(bodyDef);
//
//                    PolygonShape rect = new PolygonShape();
//                    rect.setAsBox(plantSquareSize / 2, plantSquareSize / 2);
//                    fixtureDef.shape = rect;
//                    fixtureDef.filter.categoryBits = CollisionBit.PLANTSQUARE;
//                    fixtureDef.filter.maskBits = CollisionBit.ENDPOINT | CollisionBit.SPAWNPOINT | CollisionBit.PLANTSQUARE;
//                    pastBody.createFixture(fixtureDef);
//
//                    plantSquares[(int)pastBodyY / (int)plantSquareSize][(int)pastBodyX / (int) plantSquareSize].setBody(pastBody);
//                }
//
//                pastBodyY = body.getPosition().y;
//                pastBodyX = body.getPosition().x;
//                pastBody = body;