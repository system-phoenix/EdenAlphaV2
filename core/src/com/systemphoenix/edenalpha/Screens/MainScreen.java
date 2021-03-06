package com.systemphoenix.edenalpha.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.systemphoenix.edenalpha.Eden;

import java.util.Random;

public class MainScreen extends AbsoluteScreen {

    private Texture bg;
    private Viewport viewport;

    private Stage stage;

    private TutorialMapScreen tutorialMapScreen;

    private MainScreenButton startButton, helpButton;

    public MainScreen(Eden game) {
        super(game);
        this.font.getData().setScale(1.5f);
        int rand = new Random().nextInt(3) + 1;

        this.viewport = new FitViewport(worldWidth, worldHeight, cam);
        this.stage = new Stage(viewport, gameGraphics);
        try {
            this.bg = new Texture(Gdx.files.internal("main/[eden]bg" + rand + ".png"));
            Gdx.app.log("Verbose", "Successfully loaded bg");
        } catch(Exception e) {
            Gdx.app.log("Verbose", "bg " + e.getMessage());
        }

        if(!game.isFirstTimePlaying()) {
            helpButton = new MainScreenButton(this, "helpButton.png", worldWidth / 2 - 128, 128, 256, 64, false);
            stage.addActor(helpButton);
        }
        startButton = new MainScreenButton(this, "startButton.png", worldWidth / 2 - 128, 230, 256, 64, true);
        stage.addActor(startButton);

        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void render(float delta) {
        boundCamera();
        cam.update();
        gameGraphics.setProjectionMatrix(cam.combined);

        gameGraphics.begin();
        gameGraphics.draw(bg, 0, 0, screenWidth, screenHeight);
        if(helpButton != null) helpButton.draw(gameGraphics, 1);
        startButton.draw(gameGraphics, 1);
        gameGraphics.end();
        gameGraphics.setProjectionMatrix(stage.getCamera().combined);
    }

    public void createTutorialScreen() {
        if(tutorialMapScreen != null) tutorialMapScreen.dispose();
        tutorialMapScreen = new TutorialMapScreen(game);
        game.setScreen(tutorialMapScreen);
    }

    public void createMapScreen() {
        game.setScreen(game.getMapScreen());
    }

    @Override
    public void dispose() {
        bg.dispose();
        if(tutorialMapScreen != null) tutorialMapScreen.dispose();
        if(helpButton != null) helpButton.dispose();
        startButton.dispose();
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return true;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    public boolean isFirstTimePlaying() {
        return game.isFirstTimePlaying();
    }

    public Eden getGame() {
        return game;
    }
}
