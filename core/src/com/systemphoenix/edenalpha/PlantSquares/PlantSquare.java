package com.systemphoenix.edenalpha.PlantSquares;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Screens.GameScreen;
import com.systemphoenix.edenalpha.WindowUtils.CollisionBit;

public class PlantSquare extends Sprite implements Disposable {
    protected float x, y;
    protected int gridX, gridY;
    protected short type;
    protected Body body;
    protected Texture square;


    public PlantSquare(GameScreen screen, float x, float y, float size, int type) {
        this.x = x;
        this.y = y;

        this.gridX = (int) x / (int) size;
        this.gridY = (int) y / (int) size;

        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();

        bodyDef.position.set(x, y);
        body = screen.getWorld().createBody(bodyDef);

        shape.setAsBox(size / 2, size / 2);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = CollisionBit.PLANT_SQUARE;

        body.createFixture(fixtureDef);

        this.type = (short) type;
        try {
            this.square = new Texture(Gdx.files.internal("misc/" + this.type + "_PlantSquare.png"));
            switch (this.type) {
                case 0:
                    this.type = 1;
                    break;
                case 1:
                    this.type = 4;
                    break;
                case 2:
                    this.type = 2;
                    break;
            }
        } catch(Exception e) {
            Gdx.app.log("Verbose", "Plant square: " + e.getMessage());
        }
    }

    @Override
    public void draw(Batch batch) {
        try {
            batch.draw(this.square, x - 16, y - 16, 64f, 64f);
        } catch(Exception e) {
            Gdx.app.log("Verbose", "Error in rendering plantSquare: " + e.getMessage());
        }
    }

    public void preDraw(Batch batch) {
        try {
            batch.draw(this.square, x - 16, y - 16);
        } catch(Exception e) {
            Gdx.app.log("Verbose", "Error in rendering plantSquare: " + e.getMessage());
        }
    }

    @Override
    public void dispose() {
        square.dispose();
    }

    public Body getBody() {
        return this.body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public int getGridX() {
        return this.gridX;
    }

    public int getGridY() {
        return this.gridY;
    }

    public short getType() {
        return this.type;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }
}
