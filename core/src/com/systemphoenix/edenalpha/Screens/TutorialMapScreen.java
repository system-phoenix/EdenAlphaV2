package com.systemphoenix.edenalpha.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.systemphoenix.edenalpha.Codex.ButtonCodex;
import com.systemphoenix.edenalpha.Eden;
import com.systemphoenix.edenalpha.Actors.StageActors.ButtonActor;
import com.systemphoenix.edenalpha.Actors.StageActors.MapActor;

public class TutorialMapScreen extends AbsoluteScreen {

    private InputMultiplexer inputMultiplexer;

    private Stage stage;
    private MapActor left, right;
    private ButtonActor homeButton, playButton;

    private Sprite[] slides;

    private Viewport viewport;

    private int index;

    public TutorialMapScreen(Eden game) {
        super(game);
        float sizeWidth = 1280, sizeHeight = 720;
        this.inputMultiplexer = new InputMultiplexer();
        this.left = new MapActor(this, sizeWidth, sizeHeight, true, true);
        this.left.setDrawable(true);
        this.right = new MapActor(this, sizeWidth, sizeHeight, false, true);
        this.right.setDrawable(true);
        this.viewport = new FitViewport(sizeWidth, sizeHeight, cam);

        this.homeButton = new ButtonActor(ButtonCodex.HOME, this, 32f, 32f, 128, false);
        this.playButton = new ButtonActor(ButtonCodex.PLAY, this, worldWidth - 160f, 32f, 128, false);

        slides = new Sprite[9];

        for(int i = 0; i < slides.length; i++) {
            slides[i] = new Sprite(new Texture(Gdx.files.internal("tutorial/" + i + ".png")));
            slides[i].setBounds(0, 0, 1280, 720);
        }

        stage = new Stage(viewport, gameGraphics);

        stage.addActor(left);
        stage.addActor(right);

        stage.addActor(homeButton);
        stage.addActor(playButton);

        cam.update();
    }

    public void updateIndex(int update) {
        index += update;
    }

    public void createMapScreen() {
        this.dispose();
        game.setFirstTimePlaying(false);
        game.setScreen(game.getMapScreen());
    }

    @Override
    public void render(float delta) {
//        update stuff below
        if(willDispose) {
            this.dispose();
            game.setScreen(game.getMainScreen());
        } else {

            boundCamera();
            cam.update();
            gameGraphics.setProjectionMatrix(cam.combined);
            //        update stuff above

            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            gameGraphics.begin();
            slides[index].draw(gameGraphics);
            gameGraphics.end();

            if(index == 0) {
                homeButton.setCanPress(!game.isFirstTimePlaying());
                homeButton.setDrawable(true);
                left.setDrawable(false);
            } else {
                left.setDrawable(true);
                homeButton.setCanPress(false);
                homeButton.setDrawable(false);
            }

            if(index + 1 == slides.length) {
                playButton.setDrawable(true);
                playButton.setCanPress(true);
                right.setDrawable(false);
            } else {
                playButton.setDrawable(false);
                playButton.setCanPress(false);
                right.setDrawable(true);
            }

            gameGraphics.setProjectionMatrix(stage.getCamera().combined);
            stage.draw();
        }
    }

    //   Screen Methods
    @Override
    public void dispose() {
        stage.dispose();
        left.dispose();
        right.dispose();
        homeButton.dispose();
        playButton.dispose();
        for(int i = 0; i < slides.length; i++) {
            slides[i].getTexture().dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        this.inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }
}
