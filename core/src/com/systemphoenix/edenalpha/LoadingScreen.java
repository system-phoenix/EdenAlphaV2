package com.systemphoenix.edenalpha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LoadingScreen extends AbsoluteScreen {

    private Texture loadingSheet;
    private TextureRegion[] frames;
    private TextureRegion currentFrame;
    private Animation<TextureRegion> animation;

    private float stateTime;

    public LoadingScreen(EdenAlpha game) {
        super(game);

        this.loadingSheet = new Texture(Gdx.files.internal("loadingV4.png"));
        TextureRegion[][] temp = TextureRegion.split(loadingSheet, 16, 16);
        this.frames = new TextureRegion[temp.length * temp[0].length];
        for(int i = 0; i < temp.length; i++) {
            for(int j = 0; j < temp[i].length; j++) {
                frames[temp[i].length * i + j] = new Sprite(temp[i][j]);
            }
        }

        this.animation = new Animation(0.2f, frames);
        this.stateTime = 0f;
    }

    @Override
    public void render(float delta) {
        cam.update();
        gameGraphics.setProjectionMatrix(cam.combined);

        Gdx.gl.glClearColor(0.95f, 0.95f, 0.95f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = animation.getKeyFrame(stateTime, true);

        gameGraphics.begin();
        gameGraphics.draw(currentFrame, screenWidth / 2 - 50, screenHeight / 2 - 50, 100, 100);
//        gameGraphics.draw(loadingSheet, 0, 0, screenWidth, screenHeight);
        gameGraphics.end();
    }
}