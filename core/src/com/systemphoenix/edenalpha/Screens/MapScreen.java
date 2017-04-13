package com.systemphoenix.edenalpha.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.systemphoenix.edenalpha.Eden;
import com.systemphoenix.edenalpha.WindowUtils.FieldSelection;
import com.systemphoenix.edenalpha.WindowUtils.Region;
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

    private int lowLevelBound, highLevelBound;

    public MapScreen(Eden game) {
        super(game);
        float sizeWidth = 640, sizeHeight = 360;
        this.inputMultiplexer = new InputMultiplexer();
        this.regionHud = new RegionHud(game, sizeWidth, sizeHeight);
        this.mapSelect = new com.systemphoenix.edenalpha.Scenes.MapSelect(game, this);
        this.lowLevelBound = game.getLowLevelBound();
        this.highLevelBound = game.getHighLevelBound();
        this.fieldSelection = new FieldSelection(this, game.getSelectedMapIndex(), lowLevelBound, highLevelBound);

        worldHeight = worldWidth = 1403;
        try {
            mapSprite = new Sprite(new Texture(Gdx.files.internal("maps/[PH]map.gif")));
            Gdx.app.log("Verbose", "Successfully loaded first map");
        } catch(Exception e) {
            Gdx.app.log("Verbose", "map sprite " + e.getMessage());
        }
        mapSprite.setPosition(0, 0);
        mapSprite.setSize(worldWidth, worldHeight);
        cam.setToOrtho(false, sizeWidth, sizeHeight);

        this.viewport = new FitViewport(sizeWidth, sizeHeight, cam);

        fieldSelection.setCameraPosition(cam);
        cam.update();

        this.game.setScreen(game.getMainScreen());
    }

    @Override
    public void render(float delta) {

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            this.game.setScreen(game.getMainScreen());
        }

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
        mapSelect.setDrawable(true, true);
        if(fieldSelection.getIndex() - 1 >= lowLevelBound) {
            regionHud.setLeftRegionCode(fieldSelection.getRegionByIndex(fieldSelection.getIndex() - 1).getCode());
        } else {
            mapSelect.setLeftCanDraw(false);
            regionHud.setLeftRegionCode("            ");
        }
        if(fieldSelection.getIndex() + 1 <= highLevelBound) {
            regionHud.setRightRegionCode(fieldSelection.getRegionByIndex(fieldSelection.getIndex() + 1).getCode());
        } else {
            mapSelect.setRightCanDraw(false);
            regionHud.setRightRegionCode("            ");
        }
        regionHud.setRegionCode(currentRegion.getCode());
        regionHud.setRegionName(currentRegion.getName());
        regionHud.setRegionForestPercentage("" + currentRegion.getLifePercentage());

        highLevelBound = game.getHighLevelBound();

        gameGraphics.setProjectionMatrix(mapSelect.getStage().getCamera().combined);
        mapSelect.getStage().draw();
        mapSelect.getStage().act();
        gameGraphics.setProjectionMatrix(regionHud.getStage().getCamera().combined);
        regionHud.getStage().draw();
    }

    public void createPlantScreen() {
        game.setSelectedMapIndex(fieldSelection.getIndex());
        if(plantScreen != null) plantScreen.dispose();
        plantScreen = new PlantScreen(game, this, fieldSelection.getRegion());
        game.setScreen(plantScreen);
    }

//    Gesture listener methods
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
        this.inputMultiplexer.addProcessor(mapSelect.getStage());
        this.inputMultiplexer.addProcessor(new GestureDetector(this));
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public int getLowLevelBound() {
        return lowLevelBound;
    }

    public int getHighLevelBound() {
        return highLevelBound;
    }

    public FieldSelection getFieldSelection() {
        return fieldSelection;
    }
}
