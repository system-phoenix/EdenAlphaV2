package com.systemphoenix.edenalpha.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.systemphoenix.edenalpha.Actors.StageActors.AnimalActor;
import com.systemphoenix.edenalpha.Actors.StageActors.PlantActor;
import com.systemphoenix.edenalpha.Eden;
import com.systemphoenix.edenalpha.Scenes.AbsoluteHud;
import com.systemphoenix.edenalpha.Scenes.GameHud;
import com.systemphoenix.edenalpha.Scenes.TopHud;
import com.systemphoenix.edenalpha.WindowUtils.Region;

public class SubGameScreen extends GameScreen {

    public static final float FPS_CAP = 1 / 60f, PIXELS_PER_METER = 32, OBJECT_SIZE = 32;

    //Created classes
    private AbsoluteScreen mapScreen, plantScreen;                              //screen classes that are needed in case the user will quit or restart.
    private AbsoluteHud topHud, gameHud;                                        //hud
    private Region region;                                                      //identifies the level that the player is on
    private PlantActor plantActors[], animalActor;                              //the plants and animal power up to be dragged on the screen

    //Built-in classes
    private TiledMap map;                                                       //level
    private OrthogonalTiledMapRenderer renderer;                                //level renderer
    private Viewport viewport;                                                  //captures the current part of the screen that a user can see
    private World world;                                                        //contains the physical bodies.

    //Primitive data types
    //Boolean
    private boolean running = false;                                            //game state
    private boolean firstCall = true;                                           //for first time initializations that cannot be done on load.
    //Long
    private long globalTime, centralTimer;                                      //globalTime is used for uniformity, centralTimer acts as the counter when to update the globalTime
    private long timer;                                                         //main timer that dictates the current time.

    public SubGameScreen(Eden game, MapScreen mapScreen, PlantScreen plantScreen, Region region, PlantActor[] plantActors, AnimalActor animalActor) {
        super(game, mapScreen, plantScreen, region, plantActors, animalActor);
        this.mapScreen = mapScreen;
        this.plantScreen = plantScreen;
        this.region = region;
        this.animalActor = animalActor;
        this.plantActors = plantActors;

        initializeGameComponents();

        this.globalTime = this.centralTimer = this.timer = 0;
    }

    /*
    initializeGameComponents
    parameters: none
     */
    private boolean initializeGameComponents() {
        topHud = new TopHud(game, this);
        gameHud = new GameHud(game, this, plantActors, (AnimalActor) animalActor);
        return false;
    }

    private boolean initializeGame() {
        viewport = new FitViewport(worldWidth / PIXELS_PER_METER, worldHeight / PIXELS_PER_METER, cam);
//        viewport = new FitViewport(worldWidth, worldHeight, cam);
        initializeMap();

        running = true;
        return false;
    }

    private boolean initializeMap() {
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("levels/" + region.getMapIndex() + ".tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PIXELS_PER_METER);
//        renderer = new OrthogonalTiledMapRenderer(map);
        cam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        boundCamera();
        return false;
    }

    public void update(float delta) {
        cam.update();
        renderer.setView(cam);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        long limit = 50;
        if(System.currentTimeMillis() - centralTimer >= limit) {
            globalTime += limit;
            centralTimer = System.currentTimeMillis();
        }

        if(running) {
            Gdx.app.log("Verbose", "Rendering world");
            update(delta);
            renderer.render();

            gameGraphics.setProjectionMatrix(cam.combined);
            boundCamera();

            gameGraphics.begin();
            gameGraphics.end();

            gameGraphics.setProjectionMatrix(topHud.getStage().getCamera().combined);
            topHud.getStage().act(delta);
            topHud.getStage().draw();

            gameGraphics.setProjectionMatrix(gameHud.getStage().getCamera().combined);
            gameHud.getStage().act(delta);
            gameHud.getStage().draw();
        } else if(firstCall) {
            Gdx.app.log("Verbose", "Trying to initialize");
            if(globalTime - timer >= 1000) {
                firstCall = false;
                timer = globalTime;
                initializeGame();
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        map.dispose();
        topHud.dispose();
        gameHud.dispose();
    }

    public AbsoluteScreen getMapScreen() {
        return mapScreen;
    }

    public AbsoluteScreen getPlantScreen() {
        return plantScreen;
    }

    public Region getRegion() {
        return region;
    }

    public PlantActor[] getPlantActors() {
        return plantActors;
    }

    public PlantActor getAnimalActor() {
        return animalActor;
    }

    public long getGlobalTime() {
        return globalTime;
    }
}
