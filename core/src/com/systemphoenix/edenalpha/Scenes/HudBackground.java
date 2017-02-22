package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class HudBackground extends Actor implements Disposable{

    private Sprite sprite;
    private float x, y;

    public HudBackground(float x, float y) {
        this.x = x;
        this.y = y;

        sprite = new Sprite(new Texture(Gdx.files.internal("misc/hudBG.png")));
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.draw(sprite, x, y);
    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
    }
}
