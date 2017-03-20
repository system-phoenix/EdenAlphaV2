package com.systemphoenix.edenalpha.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Codex.PlantCodex;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class Bullet implements Disposable{

    private GameScreen gameScreen;

    private Rectangle hitBox;
    private Sprite sprite;
    private Plant plant;
    private Enemy target;

    private float speed = 480f, angle, velX, velY, projectileSize;
    private boolean canDispose = false;
    private int damage;

    private long timer;

    public Bullet(GameScreen gameScreen, Plant plant, Enemy target, int damage) {
        this.gameScreen = gameScreen;
        this.plant = plant;
        this.target = target;
        this.damage = damage;

        projectileSize = PlantCodex.projectileSize[plant.getPlantIndex()];

        hitBox = new Rectangle(plant.getX() + (plant.getWidth() - projectileSize) / 2, plant.getY() + (plant.getWidth() - projectileSize) / 2, projectileSize, projectileSize);
        angle = (float)Math.atan2((hitBox.getY() - target.getBody().getPosition().y), (hitBox.getX() - target.getBody().getPosition().x));

        sprite = new Sprite(new Texture(Gdx.files.internal("bullets/" + PlantCodex.bulletFile[plant.getPlantIndex()] + "Bullet.png")));
        timer = System.currentTimeMillis();
    }

    public void update(float delta) {

        hitBox.x += speed * delta * (-Math.cos(angle));
        hitBox.y += speed * delta * (-Math.sin(angle));

        if(target.getHitBox().overlaps(hitBox)) {
            hitTarget(target);
        }

        if(target.getLife() <= 0 || System.currentTimeMillis() - timer >= 1000) {
            canDispose = true;
        }
    }

    public void render(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        batch.draw(sprite, hitBox.getX(), hitBox.getY(), projectileSize, projectileSize);
    }

    public void hitTarget(Enemy enemy) {
        enemy.receiveDamage(damage);
        canDispose = true;
    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
    }

    public boolean canDispose() {
        return canDispose;
    }

}
