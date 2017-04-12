package com.systemphoenix.edenalpha.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.systemphoenix.edenalpha.CollisionBit;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class Pulse {

    private GameScreen gameScreen;

    private Rectangle hitBox;
    private Plant plant;
    private Array<Enemy> targets;

    private Animation<TextureRegion> animation;

    private Body body;

    private int damage;
    private float stateTime;
    private long timeToLive;
    private boolean canDispose = false, isBlank = false;

    public Pulse(GameScreen gameScreen, Plant plant, int damage, Animation<TextureRegion> animation) {
        this.gameScreen = gameScreen;
        this.plant = plant;
        this.damage = damage;
        this.targets = new Array<Enemy>();
        this.animation = animation;
        this.hitBox = null;
        initialize();
    }

    public Pulse(GameScreen gameScreen, Rectangle hitBox, int damage, Animation<TextureRegion> animation) {
        this.gameScreen = gameScreen;
        this.hitBox = hitBox;
        this.damage = damage;
        this.targets = new Array<Enemy>();
        this.animation = animation;
        this.plant = null;
        initialize();
    }

    private void initialize() {
        timeToLive = System.currentTimeMillis();
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        if(plant != null) {
            bodyDef.position.set(plant.getBody().getPosition().x, plant.getBody().getPosition().y);
        } else {
            bodyDef.position.set(hitBox.getX() + hitBox.getWidth() / 2, hitBox.getY() + hitBox.getHeight() / 2);
        }

        body = gameScreen.getWorld().createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        if(plant != null) {
            circleShape.setRadius(32 * plant.getRange() + 64);
        } else {
            circleShape.setRadius(hitBox.getWidth() / 2);
        }

        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = CollisionBit.PULSE;
        fixtureDef.filter.maskBits = CollisionBit.ENEMY;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef).setUserData(this);
    }

    public void render(Batch batch, float delta) {
        stateTime += delta;
        if(!canDispose && !isBlank) {
            body.setAwake(true);
            if(plant != null) {
                float actualRange = plant.getRange();
                batch.draw(animation.getKeyFrame(stateTime), plant.getX() - (32f * actualRange), plant.getY() - (32f * actualRange), (32f * actualRange * 2) + plant.getWidth(), (32f * actualRange * 2) + plant.getHeight());
            } else {
                batch.draw(animation.getKeyFrame(stateTime), hitBox.getX(), hitBox.getY(), hitBox.getWidth(), hitBox.getHeight());
            }
        } else {
            batch.draw(animation.getKeyFrame(stateTime), gameScreen.getWorldWidth(), gameScreen.getWorldHeight());
        }
        if(System.currentTimeMillis() - timeToLive >= 800) {
            canDispose = true;
            gameScreen.getWorld().destroyBody(body);
        } else if(!isBlank && System.currentTimeMillis() - timeToLive >= 400) {
            isBlank = true;
            attack();
        }
    }

    public void acquireTarget(Enemy enemy) {
        if (targets.indexOf(enemy, true) == -1) {
            targets.add(enemy);
        }
    }

    public void removeTarget(Enemy enemy) {
        if(targets.indexOf(enemy, true) >= 0) {
            targets.removeValue(enemy, true);
        }
    }

    public void attack() {
        for(int i = 0; i < targets.size; i++) {
            targets.get(i).receiveDamage(damage);
            if(plant != null) {
                targets.get(i).slow(0.45f);
            }
        }
    }

    public boolean canDispose() {
        return canDispose;
    }
}
