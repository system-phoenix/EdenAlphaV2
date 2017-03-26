package com.systemphoenix.edenalpha.Actors;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.CollisionBit;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class PlantCollision implements Disposable{

    private GameScreen gameScreen;
    private Plant plant;

    private Body body;

    public PlantCollision(GameScreen gameScreen, Plant plant, float size, float effectiveRange) {
        this.gameScreen = gameScreen;
        this.plant = plant;

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape;

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(plant.getX() + size, plant.getY() + size);

        body = gameScreen.getWorld().createBody(bodyDef);

        circleShape = new CircleShape();
        circleShape.setRadius(size + 32f * effectiveRange);

        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = CollisionBit.EFFECTIVERANGE;
        fixtureDef.filter.maskBits = CollisionBit.PLANT;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef).setUserData(this);
    }

    public void applyImpulse() {
        body.setAwake(true);
    }

    public boolean checkCollision(Plant plant) {
        if(!this.plant.equals(plant)) {
            plant.downGrade(-1);
            return true;
        }
        return false;
    }

    public boolean removeCollision(Plant plant) {
        if(!this.plant.equals(plant)) {
            plant.downGrade(1);
            return true;
        }

        return false;
    }

    public Plant getPlant() {
        return plant;
    }

    @Override
    public void dispose() {
        gameScreen.getWorld().destroyBody(body);
    }

}
