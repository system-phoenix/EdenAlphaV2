package com.systemphoenix.edenalpha.EnemyUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Actors.Enemy;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class Wave implements Disposable {
    private Array<SpawnPoint> spawnPoints;
    private boolean cleared = false;

    private GameScreen gameScreen;

    private long nextSpawn;
    private int enemyLimit, levels[], spawnCounter = 0;

    public Wave(GameScreen screen, Array<Vector2> spawnPoints, int enemyLimit, int... levels) {
        this.enemyLimit = enemyLimit;
        this.gameScreen = screen;
        this.levels = levels;

        createSpawnPoints(spawnPoints);
        createEnemy();
    }

    private void createSpawnPoints(Array<Vector2> points) {
        this.spawnPoints = new Array<SpawnPoint>();
        for(int i = 0; i < points.size; i++) {
            this.spawnPoints.add(new SpawnPoint(points.get(i)));
        }
    }

    public void createEnemy() {
        for(int counter = 0; counter < enemyLimit; counter++) {
            for(int i = 0; i < spawnPoints.size; i++) {
                spawnPoints.get(i).addEnemy(new Enemy(gameScreen, levels[0], spawnPoints.get(i).getVector().x, spawnPoints.get(i).getVector().y, 32, counter));
    //            spawnPoints.get(i).spawnEnemy(i);
            }
        }
    }

    public void update(float delta) {
        if(System.currentTimeMillis() - nextSpawn >= 1500 && spawnCounter < enemyLimit) {
            for(int i = 0; i < spawnPoints.size; i++) {
//                spawnPoints.get(i).addEnemy(new Enemy(gameScreen, levels[0], spawnPoints.get(i).getVector().x, spawnPoints.get(i).getVector().y, 32, spawnCounter));
                spawnPoints.get(i).spawnEnemy(spawnCounter);
            }
            spawnCounter++;
            nextSpawn = System.currentTimeMillis();
        }

        int enemiesSize = 0;
        for(int i = 0; i < spawnPoints.size; i++) {
            spawnPoints.get(i).update(delta);
            enemiesSize += spawnPoints.get(i).getEnemySize();
        }

        if(spawnCounter != 0 && enemiesSize == 0) {
            cleared = true;
        }
    }

    public void render(Batch gameGraphics) {
        for (int i = 0; i < spawnPoints.size; i++) {
            spawnPoints.get(i).render(gameGraphics);
        }
    }

    @Override
    public void dispose() {
        for(int i = 0; i < spawnPoints.size; i++) {
            spawnPoints.get(i).dispose();
        }
    }

    public boolean isCleared() {
        return cleared;
    }
}
