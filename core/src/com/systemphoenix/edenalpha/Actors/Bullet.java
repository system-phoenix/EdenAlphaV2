package com.systemphoenix.edenalpha.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Codex.PlantCodex;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class Bullet implements Disposable{

    private GameScreen gameScreen;

    private Rectangle hitBox;
    private Sprite sprite, blank;
    private Plant plant;
    private Enemy target;

    private Array<Enemy> targets = null;

    private float projectileSize;
    private boolean canDispose = false, drawBlank;
    private int damage;

    private long timer;

    public Bullet(GameScreen gameScreen, Plant plant, Enemy target, int damage) {
        this.gameScreen = gameScreen;
        this.plant = plant;
        this.target = target;
        this.damage = damage;

        projectileSize = PlantCodex.projectileSize[plant.getPlantIndex()];

        hitBox = new Rectangle(plant.getX() + (plant.getWidth() - projectileSize) / 2, plant.getY() + (plant.getWidth() - projectileSize) / 2, projectileSize, projectileSize);
//        angle = (float)Math.atan2(((hitBox.getY() + projectileSize / 2) - target.getBody().getPosition().y), ((hitBox.getX() + projectileSize / 2) - target.getBody().getPosition().x));

        sprite = new Sprite(new Texture(Gdx.files.internal("bullets/" + PlantCodex.bulletFile[plant.getPlantIndex()] + "Bullet.png")));
//        sprite.setBounds(hitBox.getX(), hitBox.getY(), hitBox.getWidth(), hitBox.getHeight());
        blank = new Sprite(new Texture(Gdx.files.internal("bullets/blank.png")));
        blank.setBounds(gameScreen.getWorldWidth(), gameScreen.getWorldHeight(), 0, 0);
        timer = System.currentTimeMillis();
    }

    public Bullet(GameScreen gameScreen, Plant plant, Enemy target, Array<Enemy> targets, int damage) {
        this.gameScreen = gameScreen;
        this.plant = plant;
        this.target = target;
        this.damage = damage;
        this.targets = targets;

        projectileSize = PlantCodex.projectileSize[plant.getPlantIndex()];

        hitBox = new Rectangle(plant.getX() + (plant.getWidth() - projectileSize) / 2, plant.getY() + (plant.getWidth() - projectileSize) / 2, projectileSize, projectileSize);

        sprite = new Sprite(new Texture(Gdx.files.internal("bullets/" + PlantCodex.bulletFile[plant.getPlantIndex()] + "Bullet.png")));
//        sprite.setBounds(hitBox.getX(), hitBox.getY(), hitBox.getWidth(), hitBox.getHeight());
        blank = new Sprite(new Texture(Gdx.files.internal("bullets/blank.png")));
        blank.setBounds(gameScreen.getWorldWidth(), gameScreen.getWorldHeight(), 0, 0);

//        hitBox.set(hitBox.getX() - hitBox.getWidth() / 2, hitBox.getY() - hitBox.getHeight() / 2, hitBox.getWidth() * 2, hitBox.getHeight() * 2);
        timer = System.currentTimeMillis();
    }

    public void update(float delta) {
        if(!drawBlank) {
            float speed = 512f;
            float angle = (float)Math.atan2(((hitBox.getY() + projectileSize / 2) - target.getBody().getPosition().y), ((hitBox.getX() + projectileSize / 2) - target.getBody().getPosition().x));

            hitBox.x += speed * delta * (-Math.cos(angle));
            hitBox.y += speed * delta * (-Math.sin(angle));

            if(targets == null) {
                if(target.getHitBox().overlaps(hitBox)) {
                    hitTarget(target);
                    if(plant.getPlantIndex() == 10) {
                        target.stackSlow(0.1f);
                    }
                }
            } else {
                boolean overlap = false;
                for(int i = 0; i < targets.size && !overlap; i++) {
                    if(targets.get(i).getHitBox().overlaps(hitBox)) {
                        overlap = true;
                    }
                }
                if(overlap) {
//                    Array<Enemy> subTargets = new Array<Enemy>();
//                    hitBox.set(hitBox.getX() - hitBox.getWidth() / 2, hitBox.getY() - hitBox.getHeight() / 2, hitBox.getWidth() * 2, hitBox.getHeight() * 2);
//                    for(int i = 0; i < targets.size; i++) {
//                        if(targets.get(i).getHitBox().overlaps(hitBox)) {
//                            subTargets.add(targets.get(i));
//                        }
//                    }
                    plant.addPulse(new Pulse(gameScreen, hitBox, damage, gameScreen.getPulseAnimation()));
                    drawBlank = true;
                }
//                canDispose = true;
            }
        }

        if(System.currentTimeMillis() - timer >= 1000 && !drawBlank) {
            drawBlank = true;
        } else if(drawBlank && System.currentTimeMillis() - timer >= 1400) {
            canDispose = true;
        }
    }

    public void render(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        if(!drawBlank) {
//            if(targets != null) {
                batch.draw(sprite, hitBox.getX(), hitBox.getY(), hitBox.getWidth(), hitBox.getHeight());
//            } else {
//                batch.draw(sprite, hitBox.getX() + projectileSize / 2, hitBox.getY() + projectileSize / 2, projectileSize, projectileSize);
//            }
//            sprite.draw(batch);
        } else {
            blank.draw(batch);
        }
    }

    public void hitTarget(Enemy enemy) {
        enemy.receiveDamage(damage);
        drawBlank = true;
    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
        blank.getTexture().dispose();
    }

    public boolean canDispose() {
        return canDispose;
    }

}
