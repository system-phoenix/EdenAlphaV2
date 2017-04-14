package com.systemphoenix.edenalpha.Actors.StageActors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.systemphoenix.edenalpha.Screens.PlantScreen;

public class EnemyButton extends Actor {

    private int index;
    private boolean selected = false, drawable = false;
    private float delta = 0;

    private static EnemyButton selectedEnemyButton = null;

    private PlantScreen plantScreen;

    private Sprite icon;
    private Animation<TextureRegion> southAnimation, westAnimation, northAnimation, eastAnimation;

    public EnemyButton(PlantScreen plantScreen, Stage stage, Sprite icon,
                       Animation<TextureRegion> southAnimation,
                       Animation<TextureRegion> westAnimation,
                       Animation<TextureRegion> northAnimation,
                       Animation<TextureRegion> eastAnimation,
                       float x, float y, int index) {
        this.plantScreen = plantScreen;
        this.icon = icon;
        this.southAnimation = southAnimation;
        this.westAnimation = westAnimation;
        this.northAnimation = northAnimation;
        this.eastAnimation = eastAnimation;
        this.index = index;
        this.icon.setBounds(x, y, 72, 72);
        this.setBounds(x, y, 96f, 96f);
        this.setTouchable(Touchable.enabled);
        this.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return triggerAction();
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }
        });

        if(selectedEnemyButton == null) {
            this.selected = true;
            selectedEnemyButton = this;
            plantScreen.setEnemyData(index);
        }
        stage.addActor(this);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        icon.draw(batch);
        if(selected) {
            delta += Gdx.graphics.getDeltaTime();
            batch.draw(southAnimation.getKeyFrame(delta, true), 950, 264, 64, 64);
            batch.draw(westAnimation.getKeyFrame(delta, true), 1014, 264,64, 64);
            batch.draw(eastAnimation.getKeyFrame(delta, true), 950, 200, 64, 64);
            batch.draw(northAnimation.getKeyFrame(delta, true), 1014, 200, 64, 64);
        }
    }

    private boolean triggerAction() {
        if(drawable) {
            Gdx.app.log("Verbose", "Enemy index: " + index);
            plantScreen.setEnemyData(index);
            selectedEnemyButton.selected = false;
            selectedEnemyButton = this;
            selected = true;
            return true;
        }
        return false;
    }

    public int getIndex() {
        return index;
    }

    public Sprite getIcon() {
        return icon;
    }

    public Animation<TextureRegion> getSouthAnimation() {
        return southAnimation;
    }

    public Animation<TextureRegion> getWestAnimation() {
        return westAnimation;
    }

    public Animation<TextureRegion> getNorthAnimation() {
        return northAnimation;
    }

    public Animation<TextureRegion> getEastAnimation() {
        return eastAnimation;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setDrawable(boolean drawable) {
        this.drawable = drawable;
    }

    public static EnemyButton getSelectedEnemyButton() {
        return selectedEnemyButton;
    }
}
