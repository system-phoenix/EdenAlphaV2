package com.systemphoenix.edenalpha.Actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class GhostEnemy extends Enemy {

    public GhostEnemy(GameScreen screen, int level, float x, float y, float size, int id) {
        super(screen, level, x, y, size, id);
        this.speed *= 2;
    }

    @Override
    public void draw(Batch batch) {
        int radius = 5;
        if(spawned) {
            batch.draw(getFrame(), this.getX(), this.getY());
            for(int i = ((int) this.getY() / (int) size) - radius; i <= ((int) this.getY() / (int) size) + radius; i++) {
                for(int j = ((int) this.getX() / (int) size) - radius; j <= ((int) this.getY() / (int) size) + radius; j++) {
                    if(!(i < 0 || j < 0 || i > 23 || j > 40)) {
                        gameScreen.getPlantSquares()[i][j].draw(batch);
                    }
                }
            }
        }
    }
}
