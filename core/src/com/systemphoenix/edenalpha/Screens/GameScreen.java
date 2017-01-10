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
import com.systemphoenix.edenalpha.Actors.Enemy;
import com.systemphoenix.edenalpha.CollisionBit;
import com.systemphoenix.edenalpha.EdenAlpha;
import com.systemphoenix.edenalpha.Region;
import com.systemphoenix.edenalpha.Scenes.TopHud;
import com.systemphoenix.edenalpha.WorldContactListener;

public class GameScreen extends AbsoluteScreen {
    private Region region;

    private String message;

    private Viewport viewport;
    private TopHud topHud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer debugRenderer;

    private Array<Body> spawnPoints, endPoints, pathBounds;
    private Body[][] plantSquares;
    private Array<Enemy> enemies;
    private Enemy enemy;
    private Body pastBody;
    private float pastZoomDistance, plantSquareWidth = -1, plantSquareHeight = -1, pastBodyX = -1, pastBodyY = -1;
    private int enemyCount = 0, enemyLimit = 15;
    private long timer, sixtyMinuteMark = 1, createInterval, toNewWave;
    private boolean preSixty = true, directionSquares[][], newWave = false, preNewWave = false;

    public GameScreen(EdenAlpha game, Region region) {
        super(game);
        this.region = region;

        worldHeight = region.getWorldHeight();
        worldWidth = region.getWorldWidth();

        this.viewport = new FitViewport(screenWidth, screenHeight, cam);
        this.topHud = new TopHud(game);
        initialize();
        timer = System.currentTimeMillis();
    }

    private void initialize() {

        try {
            mapLoader = new TmxMapLoader();
            map = mapLoader.load("levels/CAR.tmx");
            renderer = new OrthogonalTiledMapRenderer(map);
            Gdx.app.log("Verbose", "Successfully loaded level: " + worldWidth + " x " + worldHeight);

            createWorld();
        } catch(Exception e) {
            Gdx.app.log("Verbose", "level " + e.getMessage());
        }

        cam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        message = "Camera | X: -- ,     Y: -- ,     Zoom: -- ";
        topHud.setCamStatMessage(message);
        topHud.setMessage(region.getCode() + ": " + region.getName());

    }

    private void createWorld() {
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new WorldContactListener());
        debugRenderer = new Box2DDebugRenderer();

        plantSquares = new Body[region.getArraySizeY()][region.getArraySizeX()];
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

        enemies = new Array<Enemy>();

        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;
        for(MapObject object : map.getLayers().get("plantSquares").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            if(plantSquareHeight < 0 && plantSquareWidth < 0) {
                plantSquareHeight = rect.getHeight();
                plantSquareWidth = rect.getWidth();
            }

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2 );

            body = world.createBody(bodyDef);

            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = CollisionBit.PLANTSQUARE;
            fixtureDef.filter.maskBits = CollisionBit.ENDPOINT | CollisionBit.SPAWNPOINT | CollisionBit.PLANTSQUARE;
            body.createFixture(fixtureDef);

            plantSquares[(int)rect.getY() / (int)plantSquareHeight][(int)rect.getX() / (int) plantSquareWidth] = body;
        }

        for(MapObject object : map.getLayers().get("directionSquares").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            directionSquares[(int)rect.getY() / (int)plantSquareHeight][(int)rect.getX() / (int) plantSquareWidth] = true;
        }


        createBodies(spawnPoints, "spawnPoints", CollisionBit.SPAWNPOINT, CollisionBit.SPAWNPOINT | CollisionBit.ENDPOINT | CollisionBit.PATHBOUND);
        createBodies(endPoints, "endPoints", CollisionBit.ENDPOINT, CollisionBit.ENEMY | CollisionBit.ENDPOINT | CollisionBit.SPAWNPOINT | CollisionBit.PATHBOUND);
        createBodies(pathBounds, "pathBounds", CollisionBit.PATHBOUND, CollisionBit.ENEMY | CollisionBit.SPAWNPOINT | CollisionBit.ENDPOINT);

//        createEnemies();
        createInterval = System.currentTimeMillis();
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

    public void createEnemies() {
        if(enemyCount < enemyLimit) {
            if(System.currentTimeMillis() - createInterval > 2000) {
                createInterval = System.currentTimeMillis();
                for (int i = 0; i < spawnPoints.size; i++) {
                    spawnEnemy(i);
                }
                enemyCount++;
            }
        } else {
            newWave = false;
        }
    }

    public void spawnEnemy(int index) {
        enemy = new Enemy(this, 0, spawnPoints.get(index).getPosition().x - 16, spawnPoints.get(index).getPosition().y - 16, 32);

        enemies.add(enemy);

    }

    public void update(float delta) {
        cam.update();
        world.step(1/45f, 0, 0);
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
            }
        }

        if(preNewWave) {
            if(System.currentTimeMillis() - toNewWave >= 5000) {
                preNewWave = false;
                newWave = true;
            }
        }

        if(newWave) {
            createEnemies();
        }

        for(int i = 0; i < enemies.size; i++) {
            enemies.get(i).update(delta);
        }

        for(int i = 0; i < enemies.size; i++) {
            Enemy enemy = enemies.get(i);
            if(enemy.canDispose()) {
                enemy.dispose();
                decrementEnemyCount();
                enemies.removeIndex(i);
            }
        }

        topHud.setMessage("Forest Land Percentage: " + region.getLifePercentage());
        topHud.setTimeStats(currentMinTens + "" + currentMinOnes + ":" + currentSecTens + "" + currentSecOnes);
        renderer.setView(cam);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0.95f, 0.95f, 0.95f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();
        debugRenderer.render(world, cam.combined);

        gameGraphics.setProjectionMatrix(cam.combined);
        gameGraphics.begin();
            for (int i = 0; i < enemies.size; i++) {
                enemies.get(i).draw(gameGraphics);
            }
        gameGraphics.end();

        gameGraphics.setProjectionMatrix(topHud.getStage().getCamera().combined);
        topHud.getStage().draw();
    }

//  Touch methods
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 touchPos = new Vector3();
        touchPos.set(x, y, 0);
        cam.unproject(touchPos);
        touchPos.x = touchPos.x - (int)touchPos.x  > 0.5 ? (int) touchPos.x + 1 : (int) touchPos.x;
        touchPos.y = touchPos.y - (int)touchPos.y  > 0.5 ? (int) touchPos.y + 1 : (int) touchPos.y;
        topHud.setTouchStatsMessage("Touch | Projected (x, y): (" + x + ", " + y + ") Un-projected (x, y): (" + touchPos.x + ", " + touchPos.y + ")");

        if(plantSquares[(int)touchPos.y / (int)plantSquareHeight][(int)touchPos.x / (int)plantSquareWidth] != null) {
            Body body = plantSquares[(int)touchPos.y / (int)plantSquareHeight][(int)touchPos.x / (int)plantSquareWidth];
            FixtureDef fixtureDef = new FixtureDef();
            CircleShape shape = new CircleShape();
            shape.setRadius(plantSquareWidth / 2);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = CollisionBit.PLANTSQUARE;
            fixtureDef.filter.maskBits = CollisionBit.ENDPOINT | CollisionBit.SPAWNPOINT | CollisionBit.PLANTSQUARE;
            for(int j = 0; j < body.getFixtureList().size; j++) {
                body.getFixtureList().removeIndex(j);
            }
            body.createFixture(fixtureDef);
            if((pastBodyX >= 0 && pastBodyY >= 0) && (pastBodyX != body.getPosition().x || pastBodyY != body.getPosition().y)) {
                for(int j = 0; j < pastBody.getFixtureList().size; j++) {
                    pastBody.getFixtureList().removeIndex(j);
                }
                PolygonShape rect = new PolygonShape();
                rect.setAsBox(plantSquareWidth / 2, plantSquareHeight / 2);
                fixtureDef.shape = rect;
                fixtureDef.filter.categoryBits = CollisionBit.PLANTSQUARE;
                fixtureDef.filter.maskBits = CollisionBit.ENDPOINT | CollisionBit.SPAWNPOINT | CollisionBit.PLANTSQUARE;
                pastBody.createFixture(fixtureDef);
            }

            pastBodyY = body.getPosition().y;
            pastBodyX = body.getPosition().x;
            pastBody = body;
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
        message = "Camera | (x, y): (" + cam.position.x + ", " + cam.position.y + ")\t\t\tZoom: " + cam.zoom;
        topHud.setCamStatMessage(message);
        topHud.setTouchStatsMessage("Touch | (x, y): (" + touchPos.x + ", " + touchPos.y + ")");
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

        message = "Camera | (x, y): (" + cam.position.x + ", " + cam.position.y + ")\t\t\tZoom: " + cam.zoom;
        topHud.setCamStatMessage(message);
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

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        for(int i = 0; i < enemies.size; i++) {
            enemies.get(i).dispose();
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

    public void decrementEnemyCount() {
        this.enemyCount--;
        Gdx.app.log("Verbose", "EnemyCount: " + enemyCount);
        if(this.enemyCount <= 0) {
            preNewWave = true;
            toNewWave = System.currentTimeMillis();
        }
    }
}
