package com.systemphoenix.edenalpha.Actors.ObjectActors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class Slash {

    private GameScreen gameScreen;

    private Plant plant;
    private Array<com.systemphoenix.edenalpha.Actors.ObjectActors.Enemy> targets;

    private Animation<TextureRegion> animation;

    private int damage;
    private float stateTime;
    private long timeToLive;
    private boolean disposable = false, isBlank = false;

    public Slash(GameScreen gameScreen, Plant plant, int damage, Array<com.systemphoenix.edenalpha.Actors.ObjectActors.Enemy> targets, Animation<TextureRegion> animation) {
        this.gameScreen = gameScreen;
        this.plant = plant;
        this.damage = damage;
        this.targets = targets;

        this.animation = animation;
        timeToLive = gameScreen.getCentralTimer();
    }

    public void render(Batch batch, float delta) {
        stateTime += delta;
        if(!disposable && !isBlank) {
            float actualRange = plant.getRange();
            batch.draw(animation.getKeyFrame(stateTime), plant.getX() - (32f * actualRange), plant.getY() - (32f * actualRange), (32f * actualRange * 2) + plant.getWidth(), (32f * actualRange * 2) + plant.getHeight());
        } else {
            batch.draw(animation.getKeyFrame(stateTime), gameScreen.getWorldWidth(), gameScreen.getWorldHeight());
        }
        if(gameScreen.getCentralTimer() - timeToLive >= 800) {
            disposable = true;
        } else if(!isBlank && gameScreen.getCentralTimer() - timeToLive >= 400) {
            isBlank = true;
            for(int i = 0; i < targets.size; i++) {
                targets.get(i).receiveDamage(damage);
            }
        }
    }

    public boolean isDisposable() {
        return disposable;
    }

}
