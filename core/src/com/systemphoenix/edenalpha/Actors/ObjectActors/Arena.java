package com.systemphoenix.edenalpha.Actors.ObjectActors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Codex.AnimalCodex;
import com.systemphoenix.edenalpha.Screens.GameScreen;
import com.systemphoenix.edenalpha.WindowUtils.CollisionBit;

public class Arena implements Disposable {

    private int animalIndex, shrinkIndex = 0;
    private float damage, x, y;
    private long lastAttackTime, shrinkTimer, effectTimer, effectLimit;
    private boolean stun, attackConsumed = false;

    private GameScreen gameScreen;

    private Array<Enemy> targets;

    private Sprite sprite;
    private Body body;

    public Arena(GameScreen gameScreen, Sprite sprite, float x, float y, float damage, int animalIndex, boolean stun) {
        this.sprite = new Sprite(sprite);
        this.animalIndex = animalIndex;
        this.gameScreen = gameScreen;
        this.stun = stun;
        this.targets = new Array<Enemy>();
        this.damage = damage;
        this.effectTimer = this.shrinkTimer = this.lastAttackTime = gameScreen.getCentralTimer();
        this.effectLimit = AnimalCodex.effectLimit[animalIndex];
        this.x = x; this.y = y;
        initialize();
    }

    private void initialize() {
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x + 32, y + 32);

        body = gameScreen.getWorld().createBody(bodyDef);

        float actualRange = AnimalCodex.RANGE[AnimalCodex.rangeStats[animalIndex] + shrinkIndex];
        circleShape.setRadius(32 + 32 * actualRange);

        fixtureDef.shape = circleShape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = CollisionBit.ARENA;
        fixtureDef.filter.maskBits = CollisionBit.ENEMY;

        body.createFixture(fixtureDef).setUserData(this);

        sprite.setBounds(x - (32 * actualRange), y - 32 * actualRange, 64 * actualRange + 64, 64 * actualRange + 64);
    }

    public void acquireTarget(Enemy enemy) {
        targets.add(enemy);
    }

    public void removeTarget(Enemy enemy) {
        targets.removeValue(enemy, true);
    }

    public void update() {
        if(stun) {
            for(int i = 0; i < targets.size; i++) {
                targets.get(i).slow(1);
            }

            if(gameScreen.getCentralTimer() - effectTimer - 100 >= effectLimit  && !attackConsumed) {
                for(int i = 0; i < targets.size; i++) {
                    targets.get(i).receiveDamage((int)damage);
                }
                attackConsumed = true;
            }
        } else {
            if(gameScreen.getCentralTimer() - shrinkTimer >= 750) {
                if(shrinkIndex > -5) {
                    shrinkIndex--;
                    gameScreen.getWorld().destroyBody(body);
                    initialize();
                    shrinkTimer = gameScreen.getCentralTimer();
                }
            }

            for(int i = 0; i < targets.size; i++) {
                targets.get(i).slow(0.5f);
            }
            if (gameScreen.getCentralTimer() - lastAttackTime >= 1000) {
                for(int i = 0; i < targets.size; i++) {
                    targets.get(i).receiveDamage((int)damage);
                }
                lastAttackTime = gameScreen.getCentralTimer();
            }
        }
    }

    public void render(Batch batch) {
        update();
        sprite.draw(batch);
    }

    @Override
    public void dispose() {
        gameScreen.getWorld().destroyBody(body);
    }
}
