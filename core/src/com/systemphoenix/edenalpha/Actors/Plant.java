package com.systemphoenix.edenalpha.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Codex.PlantCodex;
import com.systemphoenix.edenalpha.CollisionBit;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class Plant extends Actor implements InputProcessor, Disposable {
    private static Plant selectedPlant = null;

    private GameScreen gameScreen;

    private Vector2 coord;
    private int pastScreenX, pastScreenY;

    private Sprite sprite, rangeSprite, effectiveRangeSprite;
    private Stage gameStage;

    private Body body;
    private Array<Enemy> targets;

    private boolean selected;

    private Vector2 damage;
    private long attackSpeed;
    private int plantIndex;

    public Plant(GameScreen gameScreen, Stage gameStage, TextureRegion sprite, int plantIndex, float x, float y) {
        this.plantIndex = plantIndex;
        this.gameScreen = gameScreen;
        this.gameStage = gameStage;
        this.sprite = new Sprite(sprite);

        this.setBounds(x, y, 32f, 32f);

        initialize();

        gameStage.addActor(this);
    }

    public void initialize() {
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(this.getX() + 16f, this.getY() + 16f);

        body = gameScreen.getWorld().createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        float actualRange = PlantCodex.range[plantIndex];
        float computedRange = 16 + 32f * actualRange;
        float effectiveRange = PlantCodex.effectiveRange[plantIndex];
        circleShape.setRadius(computedRange);

        rangeSprite = new Sprite(new Texture(Gdx.files.internal("plantRange/rangeSprite.png")));
        effectiveRangeSprite = new Sprite(new Texture(Gdx.files.internal("plantRange/effectiveRangeSprite.png")));

        sprite.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        rangeSprite.setBounds(this.getX() - (32f * actualRange), this.getY() - (32f * actualRange), (32f * actualRange * 2) + 32f, (32f * actualRange * 2) + 32f);
        effectiveRangeSprite.setBounds(this.getX() - (32f * (effectiveRange)), this.getY() - (32f * (effectiveRange)), (32f * (effectiveRange) * 2) + 32f, (32f * (effectiveRange) * 2) + 32f);

        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = CollisionBit.ENEMYRANGE;
        fixtureDef.filter.maskBits = CollisionBit.ENEMY;

        body.createFixture(fixtureDef).setUserData(this);

    }

    public void acquireTarget(Enemy enemy) {
        targets.add(enemy);
    }

    public void removeTarget(Enemy enemy) {
        targets.removeValue(enemy, true);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        if(selected) {
            float a = 0.5f;
            rangeSprite.draw(batch);
            effectiveRangeSprite.setColor(effectiveRangeSprite.getColor().r, effectiveRangeSprite.getColor().g, effectiveRangeSprite.getColor().b, a);
            effectiveRangeSprite.draw(batch);
        }
        sprite.draw(batch);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if((pastScreenX == -1 && pastScreenY == -1) || !(pastScreenX == screenX && pastScreenY == screenY)) {
            coord = gameStage.screenToStageCoordinates(new Vector2((float)screenX, (float)screenY));
            pastScreenX = screenX;
            pastScreenY = screenY;
        }
        Actor hitActor = gameStage.hit(coord.x, coord.y, true);

        if(hitActor == this) {
            selected = !selected;
            if(selectedPlant != this && selectedPlant != null) {
                selectedPlant.selected = false;
                selectedPlant = this;
            } else {
                selectedPlant = this;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if((pastScreenX == -1 && pastScreenY == -1) || !(pastScreenX == screenX && pastScreenY == screenY)) {
            coord = gameStage.screenToStageCoordinates(new Vector2((float)screenX, (float)screenY));
            pastScreenX = screenX;
            pastScreenY = screenY;
        }
        Actor hitActor = gameStage.hit(coord.x, coord.y, true);
        return hitActor == this;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
        rangeSprite.getTexture().dispose();
        effectiveRangeSprite.getTexture().dispose();
    }

    public static void nullSelectedPlant() {
        if(selectedPlant != null) {
            selectedPlant.selected = false;
            selectedPlant = null;
        }
    }
}
