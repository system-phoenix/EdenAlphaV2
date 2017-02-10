package com.systemphoenix.edenalpha.EnemyUtils;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Actors.Enemy;

public class SpawnPoint implements Disposable {
    private Array<Enemy> enemies, sleepingEnemies;
    private Vector2 vector;

    public SpawnPoint(Vector2 vector) {
        this.vector = vector;
        enemies = new Array<Enemy>();
        sleepingEnemies = new Array<Enemy>();
    }

    public void update(float delta) {
        for(int i = 0; i < enemies.size; i++) {
            enemies.get(i).update(delta);
        }

        for(int i = 0; i < enemies.size; i++) {
            Enemy enemy = enemies.get(i);
            if(enemy.canDispose()) {
                enemy.dispose();
                enemies.removeIndex(i);
            }
        }
    }

    public void render(Batch gameGraphics) {
        for(int i = 0; i < enemies.size; i++) {
            enemies.get(i).draw(gameGraphics);
        }
    }

    @Override
    public void dispose() {
        for(int i = 0; i < enemies.size; i++) {
            enemies.get(i).dispose();
        }
    }

    public void spawnEnemy(int index) {
        enemies.add(sleepingEnemies.get(index));
        enemies.get(index).spawn();
    }

    public void addEnemy(Enemy enemy) {
        sleepingEnemies.add(enemy);
    }

    public Enemy getEnemy(int index) {
        return enemies.get(index);
    }

    public Vector2 getVector() {
        return vector;
    }

    public int getEnemySize() {
        return enemies.size;
    }
}
