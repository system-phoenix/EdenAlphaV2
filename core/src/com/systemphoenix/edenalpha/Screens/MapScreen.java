package com.systemphoenix.edenalpha.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.systemphoenix.edenalpha.EdenAlpha;
import com.systemphoenix.edenalpha.FieldSelection;
import com.systemphoenix.edenalpha.Region;
import com.systemphoenix.edenalpha.Scenes.RegionHud;

public class MapScreen extends AbsoluteScreen {
    private boolean flingDisabled = false;

    private InputMultiplexer inputMultiplexer;

    private Sprite mapSprite;
    private FieldSelection fieldSelection;

    private com.systemphoenix.edenalpha.Scenes.MapSelect mapSelect;
    private RegionHud regionHud;

    private PlantScreen plantScreen = null;
    private GameScreen gameScreen = null;
    private Viewport viewport;

    public MapScreen(EdenAlpha game) {
        super(game);
        float sizeWidth = 640, sizeHeight = 360;
        this.inputMultiplexer = new InputMultiplexer();
        this.regionHud = new RegionHud(game, sizeWidth, sizeHeight);
        this.mapSelect = new com.systemphoenix.edenalpha.Scenes.MapSelect(game, this, worldWidth, worldHeight);
        this.game.getMainScreen().setLoadingMessage("Creating field...");
        this.fieldSelection = new FieldSelection(game.getSelectedMapIndex());

        this.game.getMainScreen().setLoadingMessage("Setting up camera...");
        worldHeight = worldWidth = 1403;
        try {
            mapSprite = new Sprite(new Texture(Gdx.files.internal("maps/[PH]map.gif")));
            Gdx.app.log("Verbose", "Successfully loaded first map");
        } catch(Exception e) {
            Gdx.app.log("Verbose", "mapsprite " + e.getMessage());
        }
        mapSprite.setPosition(0, 0);
        mapSprite.setSize(worldWidth, worldHeight);
        cam.setToOrtho(false, sizeWidth, sizeHeight);

        this.viewport = new FitViewport(sizeWidth, sizeHeight, cam);

        fieldSelection.setCameraPosition(cam);
        cam.update();

        this.game.getMainScreen().setLoadingMessage("Creating input...");

        this.game.getMainScreen().setLoadingMessage("Tap to start!");
        this.game.getMainScreen().setCanStart(true);
        this.game.setScreen(game.getMainScreen());
    }

    @Override
    public void render(float delta) {
//        update stuff below
        boundCamera();
        fieldSelection.setCameraPosition(cam);
        cam.update();
        gameGraphics.setProjectionMatrix(cam.combined);
//        update stuff above

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameGraphics.begin();
//        render stuff below
        fieldSelection.render(gameGraphics);
//        render stuff above
        gameGraphics.end();

        Region currentRegion = fieldSelection.getRegion();
        mapSelect.setCanDraw(true, true);
        if(fieldSelection.getIndex() - 1 >= 0) {
            regionHud.setLeftRegionCode(fieldSelection.getRegionByIndex(fieldSelection.getIndex() - 1).getCode());
        } else {
            mapSelect.setCanDraw(false, true);
            regionHud.setLeftRegionCode("            ");
        }
        if(fieldSelection.getIndex() + 1 < fieldSelection.getRegions().length) {
            regionHud.setRightRegionCode(fieldSelection.getRegionByIndex(fieldSelection.getIndex() + 1).getCode());
        } else {
            mapSelect.setCanDraw(true, false);
            regionHud.setRightRegionCode("            ");
        }
        regionHud.setRegionCode(currentRegion.getCode());
        regionHud.setRegionName(currentRegion.getName());
        regionHud.setRegionForestPercentage("" + currentRegion.getLifePercentage());

        gameGraphics.setProjectionMatrix(mapSelect.getStage().getCamera().combined);
        mapSelect.getStage().draw();
        mapSelect.getStage().act();
        gameGraphics.setProjectionMatrix(regionHud.getStage().getCamera().combined);
        regionHud.getStage().draw();
    }

//    Gesture listener methods
    @Override
    public boolean tap(float x, float y, int count, int button) {
//        if(flingDisabled) flingDisabled = false;
//        else flingDisabled = true;
        Vector3 touchPos = new Vector3();
        touchPos.set(x, y, 0);
        cam.unproject(touchPos);

        game.setSelectedMapIndex(fieldSelection.getIndex());
        plantScreen = new PlantScreen(game, this, fieldSelection.getRegion());
        game.setScreen(plantScreen);
//        if(gameScreen != null) gameScreen.dispose();
//        gameScreen = new GameScreen(game, this, fieldSelection.getRegionByIndex(game.getSelectedMapIndex()));
//        game.setScreen(gameScreen);
//        game.getLoadingScreen().createGameScreen(game, fieldSelection.getRegionByIndex(game.getSelectedMapIndex()));
        return true;
    }

    @Override
    public boolean fling(float deltaX, float deltaY, int button) {
        float limit = 5;
        if(Math.abs(deltaX) < limit) {
            deltaX = 0;
        }
        int update = 0;
        if(deltaX > 0) {
            update = -1;
        } else if(deltaX < 0) {
            update = 1;
        }
        if(!flingDisabled) fieldSelection.setIndex(update);
        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if(flingDisabled) super.pan(x, y, deltaX, deltaY);
        return true;
    }

//   Screen Methods
    @Override
    public void dispose() {
        mapSprite.getTexture().dispose();
        fieldSelection.dispose();
        if(plantScreen != null) plantScreen.dispose();
        if(gameScreen != null) gameScreen.dispose();
        regionHud.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        this.inputMultiplexer.addProcessor(mapSelect.getLeft());
        this.inputMultiplexer.addProcessor(mapSelect.getRight());
        this.inputMultiplexer.addProcessor(new GestureDetector(this));
        Gdx.input.setInputProcessor(inputMultiplexer);
    }
}
