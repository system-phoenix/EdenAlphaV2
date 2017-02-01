package com.systemphoenix.edenalpha.PlantSquares;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.systemphoenix.edenalpha.CollisionBit;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class PlantSquare {
    protected float x, y;
    protected int gridX, gridY, type;
    protected Body body;


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
        fixtureDef.filter.categoryBits = CollisionBit.PLANTSQUARE;

        body.createFixture(fixtureDef);

        this.type = type;

    }

    public void collision() {

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

    public int getType() {
        return this.type;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }
}
