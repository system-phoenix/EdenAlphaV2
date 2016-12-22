package com.systemphoenix.edenalpha.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.systemphoenix.edenalpha.EdenAlpha;

public class LoadingScreen extends AbsoluteScreen {

    private Texture loadingSheet;
    private TextureRegion[] frames;
    private Animation<TextureRegion> animation;

    private float stateTime;

    public LoadingScreen(EdenAlpha game) {
        super(game);
        try {
            this.loadingSheet = new Texture(Gdx.files.internal("loaders/loadingV5.png"));
            Gdx.app.log("Verbose", "Successfully loaded loader.");
        } catch (Exception e) {
            Gdx.app.log("Verbose", "loader " + e.getMessage());
        }
        TextureRegion[][] temp = TextureRegion.split(loadingSheet, 16, 16);
        this.frames = new TextureRegion[temp.length * temp[0].length];
        for(int i = 0; i < temp.length; i++) {
            for(int j = 0; j < temp[i].length; j++) {
                frames[temp[i].length * i + j] = new Sprite(temp[i][j]);
            }
        }

        this.animation = new Animation<TextureRegion>(0.05f, frames);
        this.stateTime = 0f;
    }

    @Override
    public void render(float delta) {
        boundCamera();
        cam.update();
        gameGraphics.setProjectionMatrix(cam.combined);

        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);

        gameGraphics.begin();
        gameGraphics.draw(currentFrame, screenWidth / 2 - 50, screenHeight / 2 - 50, 100, 100);
//        gameGraphics.draw(loadingSheet, 0, 0, screenWidth, screenHeight);
        gameGraphics.end();
    }

    @Override
    public void dispose() {
        loadingSheet.dispose();
    }
}
