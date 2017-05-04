package com.systemphoenix.edenalpha.Actors.ObjectActors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Screens.GameScreen;

import java.util.Random;

public class Root implements Disposable {
    private GameScreen gameScreen;

    private Texture texture;
    private TextureRegion blank;

    private Plant plant;
    private Array<com.systemphoenix.edenalpha.Actors.ObjectActors.Enemy> targets;
    private com.systemphoenix.edenalpha.Actors.ObjectActors.Enemy target;

    private int damage;
    private long timeToLive, stunTime, limit, lastAttackTime;
    private boolean disposable = false, isBlank = false, damageDealt = false, baleteRoot = false;

    public Root(GameScreen gameScreen, Plant plant, int damage, Array<com.systemphoenix.edenalpha.Actors.ObjectActors.Enemy> targets, boolean baleteRoot) {
        this.gameScreen = gameScreen;
        this.plant = plant;
        this.damage = damage;
        this.targets = targets;
        this.baleteRoot = baleteRoot;

        if(baleteRoot) {
            limit = 10000;
            this.target = targets.get(0);
        } else {
            limit = 800;
            this.target = targets.get(new Random().nextInt(targets.size));
        }
        stunTime = 100;

        texture = new Texture(Gdx.files.internal("bullets/root.png"));
        blank = new TextureRegion(new Texture(Gdx.files.internal("bullets/blank.png")));

        timeToLive = gameScreen.getCentralTimer();
    }

    public void render(Batch batch) {
        if(!disposable && !isBlank) {
            batch.draw(texture, target.getX(), target.getY(), 32, 32);
            if(!damageDealt) {
                if(!baleteRoot) {
                    target.slow(1f);
//                    for(int i = 0; i < targets.size; i++) {
//                        targets.get(i).receiveDamage(damage);
//                    }
                    damageDealt = true;
                } else {
                        target.slow(1f);
                        if(gameScreen.getCentralTimer() - lastAttackTime >= 1000) {
                            target.receiveDamage(target.maxLife / 10);
                            lastAttackTime = gameScreen.getCentralTimer();
                            if(target.getLife() <= 0 || target.isDead()) {
                                Gdx.app.log("Verbose", "Target dead!");
                                damageDealt = true;
//                                limit = gameScreen.getCentralTimer();
                                plant.setHasTarget(false);
                            }
                        }
                }
            }
        } else {
            batch.draw(blank, gameScreen.getWorldWidth(), gameScreen.getWorldHeight());
        }
        if(gameScreen.getCentralTimer() - timeToLive >= limit) {
            disposable = true;
        } else if(!isBlank && gameScreen.getCentralTimer() - timeToLive >= limit - 400) {
            isBlank = true;
        }
    }

    @Override
    public void dispose() {
        texture.dispose();
        blank.getTexture().dispose();
    }

    public boolean isDisposable() {
        return disposable;
    }
}
