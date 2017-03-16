package com.systemphoenix.edenalpha.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Codex.PlantCodex;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class Slash implements Disposable {

    private GameScreen gameScreen;

    private Texture texture;
    private TextureRegion blank;

    private Plant plant;
    private Array<Enemy> targets;

    private Animation<TextureRegion> animation;

    private int damage;
    private float stateTime;
    private long timeToLive;
    private boolean canDispose = false, isBlank = false;

    public Slash(GameScreen gameScreen, Plant plant, int damage, Array<Enemy> targets) {
        this.gameScreen = gameScreen;
        this.plant = plant;
        this.damage = damage;
        this.targets = targets;

        texture = new Texture(Gdx.files.internal("bullets/slashAnimation.png"));
        blank = new TextureRegion(new Texture(Gdx.files.internal("bullets/blank.png")));
        TextureRegion[][] temp = TextureRegion.split(texture, 256, 256);
        Array<TextureRegion> anim = new Array<TextureRegion>();
        for(int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length; j++) {
                anim.add(temp[i][j]);
            }
        }
        anim.add(blank);

        animation = new Animation<TextureRegion>(0.05f, anim);
        timeToLive = System.currentTimeMillis();
    }

    public void render(Batch batch, float delta) {
        stateTime += delta;
        if(!canDispose && !isBlank) {
            float actualRange = PlantCodex.range[plant.getPlantIndex()];
            batch.draw(animation.getKeyFrame(stateTime), plant.getX() - (32f * actualRange), plant.getY() - (32f * actualRange), (32f * actualRange * 2) + plant.getWidth(), (32f * actualRange * 2) + plant.getHeight());
        } else {
            batch.draw(animation.getKeyFrame(stateTime), gameScreen.getWorldWidth(), gameScreen.getWorldHeight());
        }
        if(System.currentTimeMillis() - timeToLive >= 800) {
            canDispose = true;
        } else if(!isBlank && System.currentTimeMillis() - timeToLive >= 400) {
            isBlank = true;
            for(int i = 0; i < targets.size; i++) {
                targets.get(i).receiveDamage(damage);
            }
        }
    }

    @Override
    public void dispose() {
        texture.dispose();
        blank.getTexture().dispose();
    }

    public boolean canDispose() {
        return canDispose;
    }

}
