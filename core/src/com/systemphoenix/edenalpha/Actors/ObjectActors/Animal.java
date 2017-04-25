package com.systemphoenix.edenalpha.Actors.ObjectActors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Codex.AnimalCodex;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class Animal extends Actor implements Disposable {

    private GameScreen gameScreen;
    private long effectTimer, effectLimit;
    private boolean disposable = false;

    private Sprite sprite;

    private Pulse pulse = null;
    private Arena arena = null;

    public Animal (GameScreen gameScreen, Sprite sprite, Sprite arenaSprite, float damage, int animalIndex) {
        this.gameScreen = gameScreen;
        this.sprite = new Sprite(sprite);
        this.effectLimit = AnimalCodex.effectLimit[animalIndex];
        this.sprite.setBounds(gameScreen.getSelectedXY().x, gameScreen.getSelectedXY().y, 64, 64);
        switch(animalIndex) {
            case 0:
                Rectangle hitBox = gameScreen.getHitRectangle();
                if(gameScreen.getRegion().getLifePercentage() < 40) {
                    damage *= 0.5f;
                } else if(gameScreen.getRegion().getLifePercentage() < 60) {
                    damage *= 1f;
                } else {
                    damage *= 2f;
                }
                pulse = new Pulse(gameScreen, hitBox, (int)damage, gameScreen.getPulseAnimation());
                break;
            case 1:
                if(gameScreen.getRegion().getLifePercentage() < 40) {
                    damage *= 2f;
                } else if(gameScreen.getRegion().getLifePercentage() < 60) {
                    damage *= 1f;
                } else {
                    damage *= 0.5f;
                }
                arena = new Arena(gameScreen, arenaSprite, gameScreen.getSelectedXY().x, gameScreen.getSelectedXY().y, damage, animalIndex, true);
                break;
            case 2:
                arena = new Arena(gameScreen, arenaSprite, gameScreen.getSelectedXY().x, gameScreen.getSelectedXY().y, damage, animalIndex, false);
                break;
        }
        effectTimer = gameScreen.getCentralTimer();
    }

    @Override
    public void draw(Batch batch, float alpha) {
        sprite.draw(batch);
        if(pulse != null) {
            pulse.render(batch, Gdx.graphics.getDeltaTime());
            if(pulse.isDisposable()) {
                pulse = null;
            }
        } else if(arena != null) {
            arena.render(batch);
        }
        if(gameScreen.getCentralTimer() - effectTimer >= effectLimit) {
            disposable = true;
        }
    }

    @Override
    public void dispose() {
        if(pulse != null) {
            pulse = null;
        } else if(arena != null) {
            arena.dispose();
            arena = null;
        }
    }

    public boolean isDisposable() {
        return disposable;
    }
}
