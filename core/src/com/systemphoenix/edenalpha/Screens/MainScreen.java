package com.systemphoenix.edenalpha.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.systemphoenix.edenalpha.EdenAlpha;

import java.util.Random;

public class MainScreen extends AbsoluteScreen {

    private Texture bg;
    private String loadingMessage = "Loading...";
    private GlyphLayout glyphLayout;
    private Viewport viewport;

    private Stage stage;

    private TutorialMapScreen tutorialMapScreen;

    private MainScreenButton startButton, helpButton;

    private boolean canStart = false, blinking = true;
    private long blink;

    public MainScreen(EdenAlpha game) {
        super(game);
        this.font.getData().setScale(1.5f);
        this.glyphLayout = new GlyphLayout();
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
    }

    public void update() {
//        if(blinking) {
//            if(TimeUtils.timeSinceMillis(blink) >= 800) {
//                blinking = false;
//                blink = TimeUtils.millis();
//            }
//        } else {
//            if(TimeUtils.timeSinceMillis(blink) >= 500) {
//                blink = TimeUtils.millis();
//                blinking = true;
//            }
//        }
    }

    @Override
    public void render(float delta) {
        boundCamera();
        cam.update();
        gameGraphics.setProjectionMatrix(cam.combined);
        if(canStart) {
            update();
        }

        glyphLayout.setText(font, loadingMessage);
        float x = (screenWidth - glyphLayout.width) / 2;
        gameGraphics.begin();
        gameGraphics.draw(bg, 0, 0, screenWidth, screenHeight);
//        if(canStart) {
//            if(blinking) {
//                font.draw(gameGraphics, loadingMessage, x, screenHeight / 4 - screenHeight / 16);
//            }
//        } else {
//            font.draw(gameGraphics, loadingMessage, x, screenHeight / 4 - screenHeight / 16);
//        }
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

    public void setLoadingMessage(String loadingMessage) {
        this.loadingMessage = loadingMessage;
    }

    public void setCanStart(boolean canStart) {
        this.canStart = canStart;
    }

    public boolean isFirstTimePlaying() {
        return game.isFirstTimePlaying();
    }

    public EdenAlpha getGame() {
        return game;
    }
}
